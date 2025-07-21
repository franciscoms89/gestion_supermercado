package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService implements ShopInterfaz {

    @Autowired
    ShopRepositoryInterfaz repository;

    @Autowired
    SaleInterfaz SI;


    @Override
    public List<ShopDTO> listAll() {
        List<Shop> shopList = repository.findAll();
        return shopList.stream().map(this::convertToDTO)
                .toList();
    }

    @Override
    public ShopDTO create(ShopDTO s) {
        Shop shopRecover = this.converToOBJ(s);
        Shop shopSave = repository.save(shopRecover);
        return convertToDTO(shopSave);
    }

    @Override
    public ShopDTO update(Long id, ShopDTO s) {
        Optional<Shop> exist = repository.findById(id);
        if(exist.isPresent()){
            Shop shop = new Shop();
            shop.setId(id);
            shop.setName(s.getName());
            shop.setCity(s.getCity());
            Shop SaleObj =this.converToOBJ(s);
            shop.setSales(SaleObj.getSales());


            //actualizar
            Shop shopUpdate = repository.save(shop);
            return this.convertToDTO(shopUpdate);
        }
        else
        {
            System.err.println("No se pudo actualizar");
            return new ShopDTO();
        }

    }

    @Override
    public List<ShopDTO> delete(Long id) {

        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("la tienda con id "+id+" no pudo ser eliminado");
        }
        return List.of();
    }



    ///Todo: mapear objectos
    @Override
    public ShopDTO convertToDTO(Shop s)          //metodos para mapear OBJ a DTO
    {
        List<SaleDTO> sales = s.getSales()== null || s.getSales().isEmpty() ? new ArrayList<>() : s.getSales().stream().map(SI::convertToDTO).toList();
        return new ShopDTO(s.getId(),s.getName(),s.getCity(),s.getAddress(), sales);
    }

    @Override
    public Shop converToOBJ(ShopDTO s) {

        List<Sale> sales = s.getSales()== null || s.getSales().isEmpty() ? new ArrayList<>() : s.getSales().stream().map(SI::convertToOBJ).toList();
        return new Shop(s.getId(),s.getName(),s.getCity(),s.getAddress(), sales);
    }
}
