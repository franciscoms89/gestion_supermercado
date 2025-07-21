package com.supermercado.gestion_ventas.services.interfaces;
import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.models.Sale;

import java.util.List;

public interface SaleInterfaz {

    SaleDTO registrar(SaleDTO s);         //registrar compra

    List<SaleDTO> listAll();                //listar compra

    SaleDTO update(Long id, SaleDTO s);   //actualizar compra

    List<SaleDTO> delete(Long id);                //eliminar compra

    
    
    
    
    ///Todo: mapear objectos
    SaleDTO convertToDTO(Sale s);          //metodos para mapear OBJ a DTO

    Sale convertToOBJ(SaleDTO s);              //metodos para mapear DTO a OBJ

   
}
