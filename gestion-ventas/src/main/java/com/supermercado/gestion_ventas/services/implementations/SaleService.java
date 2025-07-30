package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.models.keys.SaleProduct;
import com.supermercado.gestion_ventas.models.keys.SaleProductId;
import com.supermercado.gestion_ventas.repositories.SaleProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.SaleRepositoryInterfaz;
import com.supermercado.gestion_ventas.response.Response;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaleService implements SaleInterfaz {

    @Autowired                                     //repositorios utilizados
    SaleRepositoryInterfaz saleRepository;
    @Autowired
    SaleProductRepositoryInterfaz salesRepo;
    @Override
    public ResponseEntity<Response> register(SaleDTO s) {        //registrar venta

        try{
            Sale saleRecover = this.convertToOBJ(s);

             Sale saleSave = saleRepository.save(saleRecover);

            Response response = new Response("La compra se registro correctamente",
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            Response response = new Response("No se pudo registrar la compra",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }


    }

    @Override
    public ResponseEntity<?> listAll(Long shopId, LocalDate saleDate) {            //listar compra
        System.out.println(shopId);
        List<Sale> saleList = saleRepository.findAll();
        List<SaleDTO> salesFilter = List.of();

        if(saleList.isEmpty())
        {
            Response response = new Response("No tienes ninguna compra con esos filtros",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
        else
        {
            salesFilter = saleList.stream()
                    .filter(sale -> shopId == null || sale.getShop().getId().equals(shopId))
                    .filter(sale -> saleDate == null || sale.getSaleDate().equals(saleDate))
                    .map(this::convertToDTO)
                    .toList();

            if(salesFilter.isEmpty())
            {
                Response response = new Response("No tienes ninguna compra con esos filtros",
                        HttpStatus.NO_CONTENT.value(),
                        LocalDate.now());

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }


        }
        return ResponseEntity.ok(salesFilter);
    }

    @Override
    public Response delete(Long id) {                       //borrar compra

        try {
            saleRepository.deleteById(id);
            return  new Response("se elimino la Compra" + id,
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
        } catch (Exception e) {
            return new Response("No se pudo eliminar la Compra" + id,
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }

    }






    ///Todo: mapear objectos
    @Override
    public SaleDTO convertToDTO(Sale s) {                 //metodos para mapear OBJ a DTO

        List<SaleProduct> saleProduct = salesRepo.findAll();     //usamos este repositorio para hacer la relacion

        List<SaleDTO.SaleDetailsDTO> saleDetails = saleProduct.stream()
                .filter(sp->Objects.equals(sp.getSale().getId(),s.getId()))
                .map(sp -> new SaleDTO.SaleDetailsDTO(sp.getId().getProductId(), sp.getQuantity()))
                .collect(Collectors.toList());

        return new SaleDTO(s.getId(), s.getShop().getId(), s.getSaleDate(), saleDetails);
    }

    @Override
    public Sale convertToOBJ(SaleDTO s) {                       //metodos para mapear DTO a OBJ
        Sale sale = new Sale();
        sale.setId(s.getId());
        sale.setSaleDate(s.getSaleDate());

        // Corregimos esto:
        Shop shop = new Shop();
        shop.setId(s.getShopId());
        sale.setShop(shop);

        Set<SaleProduct> products = s.getSaleDetails() == null ? new HashSet<>() :
                s.getSaleDetails().stream()
                        .map(detail -> {
                            Product product = new Product();
                            product.setId(detail.getProductId());

                            SaleProduct saleProduct = new SaleProduct();
                            saleProduct.setSale(sale);
                            saleProduct.setProduct(product);
                            saleProduct.setQuantity(detail.getQuantity());
                            saleProduct.setId(new SaleProductId(sale.getId(), detail.getProductId()));
                            return saleProduct;
                        })
                        .collect(Collectors.toSet());

        sale.setSaleProducts(products);
        return sale;
    }

}
