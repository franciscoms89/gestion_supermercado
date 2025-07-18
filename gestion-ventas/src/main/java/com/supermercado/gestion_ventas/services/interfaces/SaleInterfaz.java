package com.supermercado.gestion_ventas.services.interfaces;
import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.models.Sale;

public interface SaleInterfaz {

    SaleDTO registrar(SaleDTO s);         //registrar compra

    ///Todo: mapear
    SaleDTO convertirToDTO(Sale s);          //metodos para mapear OBJ a DTO

    Sale convertirToOBJ(SaleDTO s);              //metodos para mapear DTO a OBJ

}
