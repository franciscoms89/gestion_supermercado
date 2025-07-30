package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.response.Response;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class ShopController {

    // IoC

    @Autowired
    ShopInterfaz shopService;

    // GET - Obtener todas las sucursales en forma de lista
    @GetMapping
    public ResponseEntity<?> listAll() {
        List<ShopDTO> list = shopService.listAll();
        return ResponseEntity.ok(list);
    }

    // POST - Crear una nueva sucursal
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ShopDTO shop) {
        ShopDTO shopCreated = shopService.create(shop);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopCreated);
    }

    // PUT - Actualizar una sucursal existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ShopDTO shopToUpdate) {
        Response shopDTO = shopService.update(id, shopToUpdate);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shopDTO);
    }

    // DELETE - Eliminar una sucursal
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Response response = shopService.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}