package com.supermercado.gestion_ventas.services.implementations;


import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;

public class SaleService implements SaleInterfaz {
    @Override
    public SaleDTO create(SaleDTO s) {
        return null;
    }

    @Override
    public SaleDTO convertirADTO(Sale s)
    {
        return null;
    }

    @Override
    public Sale convertirAOBJ(SaleDTO s) {
        return null;
    }
}
