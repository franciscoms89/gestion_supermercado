package com.supermercado.gestion_ventas.services.interfaces;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.response.Response;

import java.util.List;

public interface ProductInterfaz {

    List<ProductDTO> listAll();                //listar productos

     ProductDTO create(ProductDTO p);   //crear producto

    Response update(Long id, ProductDTO p);   //crear producto

    Response delete(Long id);                //eliminar producto






     ///Todo: mapear objectos
    ProductDTO convertToDTO(Product p);          //metodos para mapear OBJ a DTO

    Product convertToOBJ(ProductDTO p);              //metodos para mapear DTO a OBJ


}
