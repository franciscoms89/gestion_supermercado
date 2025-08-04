package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.exceptions.ProductNotFoundException;
import com.supermercado.gestion_ventas.exceptions.SaleNotFoundException;
import com.supermercado.gestion_ventas.exceptions.ShopNotFoundException;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.models.keys.SaleProduct;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.SaleRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;

import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaleService implements SaleInterfaz {

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
            System.out.println("INFO: Venta registrada con éxito con ID: " + saleSaved.getId());
            return convertToDTO(saleSaved);
        } catch (ShopNotFoundException | ProductNotFoundException e) {
            System.err.println("ERROR de negocio al registrar venta: " + e.getMessage());
            // Relanzamos las excepciones para que el GlobalExceptionHandler las capture.
            throw e;
        } catch (Exception e) {
            System.err.println("ERROR inesperado al registrar la venta: " + e.getMessage());
            throw new RuntimeException("Error inesperado al registrar la venta.");
        }
    }

    @Override
    public List<SaleDTO> listAll(Long shopId, LocalDate saleDate) {            //listar compra
        List<Sale> saleList;

        // Lógica de filtrado
        if (shopId != null && saleDate != null) {
            System.out.println("INFO: Buscando ventas activas por tienda " + shopId + " y fecha " + saleDate);
            saleList = repository.findByShopIdAndSaleDateAndActiveTrue(shopId, saleDate);
        } else if (shopId != null) {
            System.out.println("INFO: Buscando ventas activas por tienda " + shopId);
            saleList = repository.findByShopIdAndActiveTrue(shopId);
        } else if (saleDate != null) {
            System.out.println("INFO: Buscando ventas activas por fecha " + saleDate);
            saleList = repository.findBySaleDateAndActiveTrue(saleDate);
        } else {
            System.out.println("INFO: Buscando todas las ventas activas.");
            saleList = repository.findAllByActiveTrue();
        }

        return saleList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

        System.out.println("INFO: Venta con ID " + id + " marcada como inactiva (borrado lógico).");

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