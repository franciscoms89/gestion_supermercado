package com.supermercado.gestion_ventas.services.interfaces;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.models.Sale;

import java.util.List;

public interface ShopInterfaz {

    List<ShopDTO> listAll();                //listar tiendas

    ShopDTO create(ShopDTO s);   //crear tienda

    ShopDTO update(Long id,ShopDTO s);   //actualizar tienda

    List<ShopDTO> deleteProduct(Long id);                //eliminar tienda

    ///Todo: mapear
    SaleDTO convertToDTO(Sale s);          //metodos para mapear OBJ a DTO

    Sale converToOBJ(SaleDTO s);              //metodos para mapear DTO a OBJ


}
