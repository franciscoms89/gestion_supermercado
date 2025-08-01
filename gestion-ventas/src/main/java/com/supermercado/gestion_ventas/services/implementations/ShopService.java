package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.exceptions.ShopNotFoundException;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ShopService implements ShopInterfaz {

    //relaciones con otros servicios
    @Autowired
    ShopRepositoryInterfaz repository;

    @Autowired
    SaleInterfaz SI;


    @Override
    public List<ShopDTO> listAll() {          //listar tiendas
        List<Shop> shopList = repository.findAll();

        if (shopList.isEmpty()) {
            System.out.println("INFO: No hay sucursales registradas.");
        }
        return shopList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ShopDTO create(ShopDTO s) {          //crear tienda
        try {
            Shop shopEntity = this.convertToOBJ(s);
            shopEntity.setSales(Collections.emptyList());
            Shop shopSaved = repository.save(shopEntity);
            System.out.println("INFO: Sucursal creada con ID: " + shopSaved.getId());
            return convertToDTO(shopSaved);
        } catch (Exception e) {
            System.err.println("ERROR: Error al crear la sucursal '" + s.getName() + "'. Causa: " + e.getMessage());
            throw new RuntimeException("Error inesperado al crear la sucursal.");
        }

    }

    @Override
    public ShopDTO update(Long id, ShopDTO s) {             //actualizar tienda
        Shop existingShop = repository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException("Sucursal con ID " + id + " no encontrado para actualizar"));

        existingShop.setName(s.getName());
        existingShop.setCity(s.getCity());
        existingShop.setAddress(s.getAddress());

        Shop shopUpdate = repository.save(existingShop);
        System.out.println("INFO: Sucursal con ID " + shopUpdate.getId() + " actualizada.");
        return convertToDTO(shopUpdate);
    }

    @Override
    public void delete(Long id) {   //eliminar tienda

        if (!repository.existsById(id)) {
            throw new ShopNotFoundException("Sucursal con ID " + id + " no encontrada para eliminar.");
        }
        try {
            repository.deleteById(id);
            System.out.println("INFO: Sucursal con ID " + id + " eliminada.");
        } catch (Exception e) {
            System.err.println("ERROR: Error al eliminar la sucursal con ID: " + id + ". Causa: " + e.getMessage());
            throw new RuntimeException("Error inesperado al eliminar la sucursal.");
        }
    }
    // Mapeos DTO y OBJ

    @Override
    public ShopDTO convertToDTO(Shop s) {      //metodos para mapear OBJ a DTO

        List<SaleDTO> saleDTOS = Collections.emptyList();

        if (s.getSales() != null) {
            saleDTOS = s.getSales().stream()
                    .map(SI::convertToDTO)
                    .collect(Collectors.toList());
        }
        return new ShopDTO(s.getId(), s.getName(), s.getCity(), s.getAddress(), saleDTOS);
    }

    @Override
    public Shop convertToOBJ(ShopDTO s) {
        Shop shop = new Shop();
        shop.setId(s.getId());
        shop.setName(s.getName());
        shop.setCity(s.getCity());
        shop.setAddress(s.getAddress());

        if (s.getSales() != null && !s.getSales().isEmpty()) {
            // Usamos la variable "SI" también aquí.
            List<Sale> sales = s.getSales().stream()
                    .map(SI::convertToOBJ)
                    .collect(Collectors.toList());
            shop.setSales(sales);
        }
        return shop;
    }
}