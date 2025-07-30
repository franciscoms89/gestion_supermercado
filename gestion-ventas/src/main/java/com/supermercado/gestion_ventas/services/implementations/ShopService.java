package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.exceptions.ShopNotFoundException;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;
import com.supermercado.gestion_ventas.response.Response;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        if(shopList.isEmpty())
        {
            new Response("No tienes sucursales registradas",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }
        return shopList.stream().map(this::convertToDTO)
                .toList();
    }

    @Override
    public ShopDTO create(ShopDTO s) {          //crear tienda
        try{
            Shop shopRecover = this.converToOBJ(s);
            Shop shopSave = repository.save(shopRecover);

            new Response("Se creó correctamente la sucursal",
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());

            return convertToDTO(shopSave);


        } catch (Exception e) {
            System.err.println("Error al crear la sucursal: " + e.getMessage());
            throw new RuntimeException("No se pudo crear la sucursal: " + e.getMessage(), e);
        }

    }

    @Override
    public Response update(Long id, ShopDTO s) {             //actualizar tienda
       Shop existingShop = repository.findById(id)
               .orElseThrow(() -> new ShopNotFoundException("Sucursal con ID " + id + " no encontrado para actualizar"));

       existingShop.setName(s.getName());
       existingShop.setCity(s.getCity());
       existingShop.setAddress(s.getAddress());

       Shop shopUpdate = repository.save(existingShop);
       return new Response("Se actualizó correctamente la sucursal con ID " + id,
               HttpStatus.ACCEPTED.value(),
               LocalDate.now());
    }

    @Override
    public Response delete(Long id) {   //eliminar tienda

        if (!repository.existsById(id)){
            throw new ShopNotFoundException("Sucursal con ID " + id + " no encontrada para eliminar.");
        }
        try {
            repository.deleteById(id);
            return new Response("Se eliminó la sucursal " + id,
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
        }catch (Exception e){
            System.err.println("Error al eliminar la sucursal con ID " + id + ": " + e.getMessage());
            throw new RuntimeException("No se pudo eliminar la sucursal con ID " + id + ": " + e.getMessage(), e);
        }
    }



    ///Todo: mapear objectos
    @Override
    public ShopDTO convertToDTO(Shop s)          //metodos para mapear OBJ a DTO
    {
        List<SaleDTO> sales = new ArrayList<>();

        if (s.getSales() != null){
            sales = s.getSales().stream().map(SI::convertToDTO).toList();
        }
        return new ShopDTO(s.getId(),s.getName(),s.getCity(),s.getAddress(), sales);
    }

    @Override
    public Shop converToOBJ(ShopDTO s) {

        List<Sale> sales = s.getSales()== null || s.getSales().isEmpty() ? new ArrayList<>() : s.getSales().stream().map(SI::convertToOBJ).toList();
        return new Shop(s.getId(),s.getName(),s.getCity(),s.getAddress(), sales);
    }
}
