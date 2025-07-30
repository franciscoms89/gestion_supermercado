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
import com.supermercado.gestion_ventas.response.Response;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public SaleDTO register(SaleDTO s) {        //registrar venta

        try{
            Sale saleRecover = this.convertToOBJ(s);

             Sale saleSave = repository.save(saleRecover);

            new Response("La compra se registro correctamente",
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
            return convertToDTO(saleSave);
        }
        catch (ShopNotFoundException | ProductNotFoundException e) {
            throw e;
        }
        catch (Exception e){
            System.err.println("Error inesperado al registrar la venta: " + e.getMessage());
            throw new RuntimeException("Error al registrar la compra: " + e.getMessage());
        }

    }

    @Override
    public List<SaleDTO> listAll(Long shopId, LocalDate saleDate) {            //listar compra
        List<Sale> saleList = repository.findAll();
        if(saleList.isEmpty())
        {
            new Response("No tienes ninguna compra",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }
        return saleList.stream().map(this::convertToDTO)
                .toList();
    }

    @Override
    public Response delete(Long id) {               //borrar compra
        if (!repository.existsById(id)){
            throw new SaleNotFoundException("Venta con ID " + id + " no encontrado:");
        }
        try {
            repository.deleteById(id);
            return  new Response("Se elimino la compra con ID " + id,
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
        } catch (Exception e) {
            System.err.println("Error al eliminar la compar con ID " + id + ": " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar la compra con ID " + id + ": " + e.getMessage(), e);
        }

    }


    ///Todo: mapear objectos
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
        return new SaleDTO(s.getId(), shopId, s.getSaleDate(),saleDetails);
    }

    @Override
    public Sale convertToOBJ(SaleDTO s) {  //metodos para mapear DTO a OBJ
        Sale sale = new Sale();

        // 1. Setea el ID de la venta SOLO si ya existe (para actualizaciones)
        if (s.getId() != null){
            sale.setId(s.getId());
        }

        // 2. BUSCA LA TIENDA EN LA BASE DE DATOS
        Long shopIdFromDTO = s.getShopId();
        Shop shop = shopRepositoryInterfaz.findById(shopIdFromDTO)
                .orElseThrow(() -> new ShopNotFoundException("Sucursal con ID " + shopIdFromDTO + " no encontrada."));
        sale.setShop(shop);

        // 3. Marca la fecha de la venta
        sale.setSaleDate(s.getSaleDate());

        // 4. Mapea los detalles de la venta (SaleDetailsDTO) a entidades SaleProduct
        Set<SaleProduct> saleProducts = new HashSet<>();
        if (s.getSaleDetails() != null){
            for (SaleDTO.SaleDetailsDTO detailsDTO : s.getSaleDetails()){
                Long productIdFromDTO = detailsDTO.getProductId();
                Product product = productRepositoryInterfaz.findById(productIdFromDTO)
                        .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + productIdFromDTO + " no encontrado."));

                SaleProduct saleProduct = new SaleProduct();
                saleProduct.setSale(sale);  // Asigna la venta actual
                saleProduct.setProduct(product);    // Asigna el producto cargado
                saleProduct.setQuantity(detailsDTO.getQuantity());  // Asigna la cantidad

                saleProducts.add(saleProduct);
            }
        }
        sale.setSaleProducts(saleProducts); // Asigna la colección de SaleProduct a la venta

        return sale;
    }
}
