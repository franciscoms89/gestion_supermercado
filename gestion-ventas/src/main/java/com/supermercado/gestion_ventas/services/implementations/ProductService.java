package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService  implements ProductInterfaz {

    @Autowired
    ProductRepositoryInterfaz repository;

    //relaciones con otros servicios
    SaleInterfaz sai;
    ShopInterfaz soi;

    @Override
    public List<ProductDTO> listAll() {                         //listar producto
        List<Product> productList = repository.findAll();
        return productList.stream().map(this::convertToDTO)
                .toList();
    }

    @Override
    public ProductDTO create(ProductDTO p) {               //crear producto
        Product productRecover = this.convertToOBJ(p);
        Product productSave = repository.save(productRecover);
        return convertToDTO(productSave);
    }

    @Override
    public ProductDTO update(Long id, ProductDTO p) {      // actualizar Produto
        Optional<Product> exist = repository.findById(id);
        if(exist.isPresent()) {
            Product product = new Product();
            product.setId(id);
            product.setName(p.getName());
            product.setPrice(p.getPrice());
            product.setCategory(p.getCategory());

            Product productUpdate = repository.save(product);
            return this.convertToDTO(productUpdate);
        }
        else {
            System.err.println("No se pudo actualizar");
            return new ProductDTO();
        }
    }

    @Override
    public List<ProductDTO> delete(Long id) {

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("El producto con id "+id+" no pudo ser eliminado");
        }
        return List.of();
    }

    @Override
    public ProductDTO convertToDTO(Product p)
    {
        Set<SaleDTO> sales = p.getSales() == null || p.getSales().isEmpty() ? new HashSet<>() : p.getSales().stream().map(sai::convertirADTO).collect(Collectors.toSet());
        return new ProductDTO(p.getId(),p.getName(),p.getPrice(), p.getCategory(),sales);
    }

    @Override
    public Product convertToOBJ(ProductDTO p)
    {
        Set<Sale> sales = p.getSales() == null || p.getSales().isEmpty() ? new HashSet<>() : p.getSales().stream().map(sai::convertirAOBJ).collect(Collectors.toSet());
        return new Product(p.getId(), p.getName(), p.getPrice(), p.getCategory(), sales);
    }
}
