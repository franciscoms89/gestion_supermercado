package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
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
            new Response("No tienes tiendas registradas",
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

            new Response("Se creo correctamente la  tienda",
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());

            return convertToDTO(shopSave);


        } catch (Exception e) {
            new Response("No se pudo crear la tienda",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
            throw new RuntimeException(e);
        }

    }

    @Override
    public Response update(Long id, ShopDTO s) {             //actualizar tienda
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
            return  new Response("Se actualizo correctamente la tienda" + id,
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());

        }
        else
        {
            System.err.println("No se pudo actualizar");
            new Response("No se pudo actualizar la tienda",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }
       return  null;
    }

    @Override
    public Response delete(Long id) {   //eliminar tienda

        try {
            repository.deleteById(id);
            return new Response("se elimino la tienda" + id,
                    HttpStatus.ACCEPTED.value(),
                    LocalDate.now());
        } catch (Exception e) {
            return new Response("no se pudo eliminar la tienda" + id,
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now());
        }

    }



    ///Todo: mapear objectos
    @Override
    public ShopDTO convertToDTO(Shop s)          //metodos para mapear OBJ a DTO
    {
        List<SaleDTO> sales = new ArrayList<>();
        return new ShopDTO(s.getId(),s.getName(),s.getCity(),s.getAddress(), sales);
    }

    @Override
    public Shop converToOBJ(ShopDTO s) {

        List<Sale> sales = s.getSales()== null || s.getSales().isEmpty() ? new ArrayList<>() : s.getSales().stream().map(SI::convertToOBJ).toList();
        return new Shop(s.getId(),s.getName(),s.getCity(),s.getAddress(), sales);
    }
}
