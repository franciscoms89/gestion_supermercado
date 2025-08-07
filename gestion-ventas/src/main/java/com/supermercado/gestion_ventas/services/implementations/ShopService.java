package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.exceptions.ServiceException;
import com.supermercado.gestion_ventas.exceptions.ShopNotFoundException;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ShopService implements ShopInterfaz {

    private static final Logger log = LoggerFactory.getLogger(ShopService.class);

    //relaciones con otros servicios
    @Autowired
    ShopRepositoryInterfaz repository;

    @Autowired
    SaleInterfaz saleInterfaz;


    @Override
    public List<ShopDTO> listAll() {          //listar tiendas
        List<Shop> shopList = repository.findAll();

        if (shopList.isEmpty()) {
            log.info("No hay sucursales registradas.");
        }
        return shopList.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ShopDTO create(ShopDTO s) {          //crear tienda
        try {
            Shop shopEntity = this.convertToOBJ(s);
            shopEntity.setSales(Collections.emptyList());
            Shop shopSaved = repository.save(shopEntity);
            log.info("Sucursal creada con ID {} ",shopSaved.getId());
            return convertToDTO(shopSaved);
        } catch (Exception e) {
            log.error("Error al crear la sucursal '{}'", s.getName(), e);
            throw new ServiceException("Error inesperado al crear la sucursal.", e);
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
        log.info("Sucursal con ID {} actualizada.", shopUpdate.getId());
        return convertToDTO(shopUpdate);
    }

    @Override
    public void delete(Long id) {   //eliminar tienda

        if (!repository.existsById(id)) {
            throw new ShopNotFoundException("Sucursal con ID " + id + " no encontrada para eliminar.");
        }
        try {
            repository.deleteById(id);
           log.info("Sucursal con ID {} eliminada.", id);
        } catch (Exception e) {
            log.error("Error al eliminar la sucursal con ID: {}. Causa: {}", id, e);
            throw new ServiceException("Error inesperado al eliminar la sucursal.", e);
        }
    }
    // Mapeos DTO y OBJ

    @Override
    public ShopDTO convertToDTO(Shop s) {      //metodos para mapear OBJ a DTO

        List<SaleDTO> saleDTOS = Collections.emptyList();

        if (s.getSales() != null) {
            saleDTOS = s.getSales().stream()
                    .map(saleInterfaz::convertToDTO)
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
            // Usamos la variable "saleInterfaz" también aquí.
            List<Sale> sales = s.getSales().stream()
                    .map(saleInterfaz::convertToOBJ)
                    .collect(Collectors.toList());
            shop.setSales(sales);
        }
        return shop;
    }
}