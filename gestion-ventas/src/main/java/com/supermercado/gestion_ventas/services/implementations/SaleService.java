package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.exceptions.ProductNotFoundException;
import com.supermercado.gestion_ventas.exceptions.SaleNotFoundException;
import com.supermercado.gestion_ventas.exceptions.ServiceException;
import com.supermercado.gestion_ventas.exceptions.ShopNotFoundException;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.models.keys.SaleProduct;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.SaleRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;

import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaleService implements SaleInterfaz {

    private static final Logger log = LoggerFactory.getLogger(SaleService.class);

    //relaciones con otros servicios
    @Autowired
    SaleRepositoryInterfaz repository;

    @Autowired
    private ShopRepositoryInterfaz shopRepositoryInterfaz;

    @Autowired
    private ProductRepositoryInterfaz productRepositoryInterfaz;

    @Override
    public SaleDTO register(SaleDTO s) {    //registrar venta
        try {
            Sale saleEntity = this.convertToOBJ(s);
            Sale saleSaved = repository.save(saleEntity);
            log.info("Venta registrada con éxito con ID {}",saleSaved.getId());
            return convertToDTO(saleSaved);
        } catch (ShopNotFoundException | ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al registrar la venta: ", e);
            throw new ServiceException("Error inesperado al registrar la venta.", e);
        }
    }

    @Override
    public List<SaleDTO> listAll(Long shopId, LocalDate saleDate) {            //listar compra
        List<Sale> saleList;

        // Lógica de filtrado
        if (shopId != null && saleDate != null) {
            log.info("Buscando ventas activas por tienda {} y fecha {}", shopId, saleDate);
            saleList = repository.findByShopIdAndSaleDateAndActiveTrue(shopId, saleDate);
        } else if (shopId != null) {
            log.info("Buscando ventas activas por tienda {}", shopId);
            saleList = repository.findByShopIdAndActiveTrue(shopId);
        } else if (saleDate != null) {
            log.info("Buscando ventas activas por fecha {}", saleDate);
            saleList = repository.findBySaleDateAndActiveTrue(saleDate);
        } else {
            log.info("Buscando todas las ventas activas.");
            saleList = repository.findAllByActiveTrue();
        }

        return saleList.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {               //borrar compra
        // 1. Buscamos la venta. Si no existe, la excepción se lanza.
        Sale saleToDelete = repository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Venta con ID " + id + " no encontrada para eliminar."));

        // 2. Cambiamos el estado a inactivo.
        saleToDelete.setActive(false);

        // 3. Guardamos la venta con su nuevo estado.
        repository.save(saleToDelete);

        log.info("INFO: Venta con ID {} marcada como inactiva (borrado lógico).", id);

    }

    // Mapeos DTO y OBJ

    @Override
    public SaleDTO convertToDTO(Sale s)          //metodos para mapear OBJ a DTO
    {
        // Mapea los detalles de la venta (productos y cantidades)
        List<SaleDTO.SaleDetailsDTO> saleDetails = s.getSaleProducts() != null ?
                s.getSaleProducts().stream()
                        .map(saleProduct -> new SaleDTO.SaleDetailsDTO(
                                saleProduct.getProduct().getId(),
                                saleProduct.getQuantity()))
                        .collect(Collectors.toList()) :
                List.of();

        // Manejo seguro del shopId para evitar NullPointerException
        Long shopId = (s.getShop() != null) ? s.getShop().getId() : null;
        return new SaleDTO(s.getId(), shopId, s.getSaleDate(), saleDetails);
    }

    @Override
    public Sale convertToOBJ(SaleDTO s) {  //metodos para mapear DTO a OBJ
        Sale sale = new Sale();
        sale.setId(s.getId());

        Shop shop = shopRepositoryInterfaz.findById(s.getShopId())
                .orElseThrow(() -> new ShopNotFoundException("Sucursal con ID " + s.getShopId() + " no encontrada."));
        sale.setShop(shop);

        // Si la fecha en el DTO es nula, se asigna la fecha actual.
        sale.setSaleDate(s.getSaleDate() == null ? LocalDate.now() : s.getSaleDate());

        Set<SaleProduct> saleProducts = new HashSet<>();
        if (s.getSaleDetails() != null) {
            for (SaleDTO.SaleDetailsDTO detailDTO : s.getSaleDetails()) {
                Product product = productRepositoryInterfaz.findById(detailDTO.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + detailDTO.getProductId() + " no encontrado."));

                // Creamos la entidad que relaciona Venta, Producto y Cantidad
                SaleProduct saleProduct = new SaleProduct(sale, product, detailDTO.getQuantity());
                saleProducts.add(saleProduct);
            }
        }
        sale.setSaleProducts(saleProducts);

        return sale;
    }
}