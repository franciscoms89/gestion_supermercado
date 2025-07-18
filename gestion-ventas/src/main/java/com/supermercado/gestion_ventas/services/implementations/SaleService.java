package com.supermercado.gestion_ventas.services.implementations;


import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.SaleRepositoryInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SaleService implements SaleInterfaz {

    @Autowired
    SaleRepositoryInterfaz repository;
    @Override
    public SaleDTO registrar(SaleDTO s) {
        Sale saleRecover = this.convertirAOBJ(s);
        Sale saleSave = repository.save(saleRecover);
        return convertirADTO(saleSave);
    }

    @Override
    public SaleDTO convertirADTO(Sale s)
    {

        List<SaleDTO.SaleDetailsDTO> saleDetails = s.getProducts() == null ? List.of()
                :s.getProducts().stream().map(product -> new SaleDTO.SaleDetailsDTO(product.getId(), 1)).collect(Collectors.toList());
        Long shop = s.getShop().getId();

        return new SaleDTO(s.getId(),shop,s.getSaleDate(),saleDetails);
    }

    @Override
    public Sale convertirAOBJ(SaleDTO s) {

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
