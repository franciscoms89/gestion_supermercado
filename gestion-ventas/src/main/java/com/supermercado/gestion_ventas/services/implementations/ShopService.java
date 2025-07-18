package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService implements ShopInterfaz {
    @Override
    public List<ShopDTO> listAll() {
        return List.of();
    }

    @Override
    public ShopDTO create(ShopDTO s) {
        return null;
    }

    @Override
    public ShopDTO update(Long id, ShopDTO s) {
        return null;
    }

    @Override
    public List<ShopDTO> deleteProduct(Long id) {
        return List.of();
    }

    @Override
    public SaleDTO convertToDTO(Shop s)          //metodos para mapear OBJ a DTO
    {
        return null;
    }

    @Override
    public Sale converToOBJ(ShopDTO s) {
        return null;
    }
}
