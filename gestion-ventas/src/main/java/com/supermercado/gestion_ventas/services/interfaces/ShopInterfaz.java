package com.supermercado.gestion_ventas.services.interfaces;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.response.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShopInterfaz {

    List<ShopDTO> listAll();                //listar tiendas

    ResponseEntity<?> create(ShopDTO s);   //crear tienda

    ResponseEntity<?> update(Long id,ShopDTO s);   //actualizar tienda

    Response delete(Long id);                //eliminar tienda







    ///Todo: mapear objectos
    ShopDTO convertToDTO(Shop s);          //metodos para mapear OBJ a DTO

    Shop converToOBJ(ShopDTO s);              //metodos para mapear DTO a OBJ


}
