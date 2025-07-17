package com.supermercado.gestion_ventas.services.interfaces;
import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.models.Sale;

public interface SaleInterfaz {

    SaleDTO create(SaleDTO s);

    ///Todo: mapear
    SaleDTO convertirADTO(Sale s);          //metodos para mapear OBJ a DTO

    Sale convertirAOBJ(SaleDTO s);              //metodos para mapear DTO a OBJ

}
