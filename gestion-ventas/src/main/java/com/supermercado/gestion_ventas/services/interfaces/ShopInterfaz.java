package com.supermercado.gestion_ventas.services.interfaces;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.models.Sale;

import java.util.List;

public interface ShopInterfaz {

    List<ShopDTO> listAll();                //listar tiendas

    ShopDTO create(ShopDTO s);   //crear tienda

    ShopDTO update(ShopDTO s);   //actualizar tienda

    List<ShopDTO> deleteProduct(Long id);                //eliminar tienda

    ///Todo: mapear
    SaleDTO convertirADTO(Sale s);          //metodos para mapear OBJ a DTO

    Sale convertirAOBJ(SaleDTO s);              //metodos para mapear DTO a OBJ


}
