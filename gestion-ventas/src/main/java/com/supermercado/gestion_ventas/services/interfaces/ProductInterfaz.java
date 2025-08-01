package com.supermercado.gestion_ventas.services.interfaces;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.models.Product;

import java.util.List;

public interface ProductInterfaz {

    List<ProductDTO> listAll();                //listar productos
    ProductDTO create(ProductDTO p);   //crear producto
    ProductDTO update(Long id, ProductDTO p);   //crear producto
    void delete(Long id);                //eliminar producto

    // Mapeos DTO y OBJ

    ProductDTO convertToDTO(Product p);          //metodos para mapear OBJ a DTO
    Product convertToOBJ(ProductDTO p);              //metodos para mapear DTO a OBJ
}