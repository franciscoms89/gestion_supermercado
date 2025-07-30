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
    public SaleDTO register(SaleDTO s) {        //registrar venta

        try{
            Sale saleRecover = this.convertToOBJ(s);

             Sale saleSave = saleRepository.save(saleRecover);

            new Response("La compra se registro correctamente",
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
            return convertToDTO(saleSave);
        } catch (Exception e) {
            new Response("No se pudo registrar la compra",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<SaleDTO> listAll(Long shopId, LocalDate saleDate) {            //listar compra

        List<Sale> saleList = saleRepository.findAll();

        if(saleList.isEmpty())
        {
            new Response("No tienes ninguna compra",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }

        return saleList.stream().map(this::convertToDTO)
                .collect(Collectors.toList());
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
