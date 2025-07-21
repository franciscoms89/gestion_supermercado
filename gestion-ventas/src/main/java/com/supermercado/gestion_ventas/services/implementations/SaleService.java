package com.supermercado.gestion_ventas.services.implementations;



import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.SaleRepositoryInterfaz;
import com.supermercado.gestion_ventas.response.Response;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaleService implements SaleInterfaz {

    @Autowired
    SaleRepositoryInterfaz repository;
    @Override
    public SaleDTO register(SaleDTO s) {        //registrar venta

        Sale saleSave;
        try{
            Sale saleRecover = this.convertToOBJ(s);

            saleSave = repository.save(saleRecover);

            new Response("La compra se registro correctamente",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        } catch (Exception e) {
            new Response("No se pudo registrar la compra",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
            throw new RuntimeException(e);
        }

        return convertToDTO(saleSave);
    }

    @Override
    public List<SaleDTO> listAll(Long shopId, LocalDate saleDate) {            //listar compra
        List<Sale> saleList = repository.findAll();

        if(saleList.isEmpty())
        {
            new Response("No tienes compras registradas",
                HttpStatus.NO_CONTENT.value(),
                LocalDate.now());
        }
        return saleList.stream()
                .filter(sale -> shopId == null || sale.getShop().getId().equals(shopId))
                .filter(sale -> saleDate == null || sale.getSaleDate().equals(saleDate))
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public Response delete(Long id) {
        try {
            repository.deleteById(id);
            return  new Response("No se pudo eliminar la Compra" + id,
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        } catch (Exception e) {
            return new Response("No se pudo eliminar la Compra" + id,
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }

    }


    ///Todo: mapear objectos
    @Override
    public SaleDTO convertToDTO(Sale s)          //metodos para mapear OBJ a DTO
    {

        List<SaleDTO.SaleDetailsDTO> saleDetails = s.getProducts() == null ? List.of()
                :s.getProducts().stream().map(product -> new SaleDTO.SaleDetailsDTO(product.getId(), 1)).collect(Collectors.toList());
        Long shop = s.getShop().getId();

        return new SaleDTO(s.getId(),shop,s.getSaleDate(),saleDetails);
    }

    @Override
    public Sale convertToOBJ(SaleDTO s) {  //metodos para mapear DTO a OBJ
    
        Sale sale = new Sale();
        sale.setId(s.getId());

        Shop shop = new Shop();
        shop.setId(s.getId());

        sale.setSaleDate(s.getSaleDate());

        Set<Product> products = s.getSaleDetails() == null ? new HashSet<>()
                :s.getSaleDetails().stream()
                .map(detailDTO -> {
                    Product product = new Product();
                    product.setId(detailDTO.getProductId());
                    return product;
                }).collect(Collectors.toSet());

        sale.setProducts(products);

        return new Sale(s.getId(),shop,s.getSaleDate(),products);
    }
}
