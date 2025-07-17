package com.supermercado.gestion_ventas.services.interfaces;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.models.Product;

import java.util.List;

public interface ProductInterfaz {

    List<ProductDTO> listAll();                //listar productos

     ProductDTO create(ProductDTO p);   //crear producto

    ProductDTO update(ProductDTO p);   //crear producto

    List<ProductDTO> deleteProduct(Long id);                //eliminar producto


     ///Todo: mapear
    ProductDTO convertirADTO(Product p);          //metodos para mapear OBJ a DTO


    Product convertirAOBJ(ProductDTO p);              //metodos para mapear DTO a OBJ


}
