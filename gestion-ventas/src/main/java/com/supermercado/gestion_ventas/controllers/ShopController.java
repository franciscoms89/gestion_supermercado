package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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

    // GET
    @GetMapping
    public ResponseEntity<List<ShopDTO>> listAll() {
        List<ShopDTO> list = shopService.listAll();
        return ResponseEntity.ok(list);
    }

    // POST
    @PostMapping("/{id}")
    public ResponseEntity<ShopDTO> create(@RequestBody ShopDTO shop) {
        ShopDTO shopCreated = shopService.create(shop);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopCreated);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ShopDTO> update(@PathVariable Long id, @RequestBody ShopDTO shopToUpdate) {
        ShopDTO shopDTO = shopService.update(id, shopToUpdate);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shopDTO);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<List<ShopDTO>> delete(@PathVariable Long id) {
        List<ShopDTO> list = shopService.delete(id);
        return ResponseEntity.ok(list);
    }
}
