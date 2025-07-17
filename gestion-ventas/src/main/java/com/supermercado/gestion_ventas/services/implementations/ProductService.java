package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;

import java.util.List;

public class ProductService  implements ProductInterfaz {
    @Override
    public List<ProductDTO> listAll() {
        return List.of();
    }

    @Override
    public ProductDTO create(ProductDTO p) {
        return null;
    }

    @Override
    public ProductDTO update(ProductDTO p) {
        return null;
    }

    @Override
    public List<ProductDTO> deleteProduct(Long id) {
        return List.of();
    }

    @Override
    public ProductDTO convertirADTO(Product p)
    {
        return null;
    }

    @Override
    public Product convertirAOBJ(ProductDTO p) {
        return null;
    }
}
