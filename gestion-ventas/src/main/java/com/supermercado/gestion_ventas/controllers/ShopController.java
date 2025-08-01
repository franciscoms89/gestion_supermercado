package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.response.ApiResponse;
import com.supermercado.gestion_ventas.services.interfaces.ShopInterfaz;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        if (list.isEmpty()) {
            ApiResponse response = new ApiResponse(
                    "No hay sucursales registradas",
                    HttpStatus.OK.value(),
                    LocalDate.now()
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(list);
    }

    // POST - Crear una nueva sucursal
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody ShopDTO shop) {
        ShopDTO shopCreated = shopService.create(shop);
        ApiResponse response = new ApiResponse(
                "Sucursal creada exitosamente",
                HttpStatus.CREATED.value(),
                LocalDate.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT - Actualizar una sucursal existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody ShopDTO shopToUpdate) {
        ShopDTO updateShop = shopService.update(id, shopToUpdate);
        ApiResponse response = new ApiResponse(
                "Sucursal actualizada exitosamente",
                HttpStatus.OK.value(),
                LocalDate.now()
        );
        return ResponseEntity.ok(response);
    }

    // DELETE - Eliminar una sucursal
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        shopService.delete(id);
        ApiResponse response = new ApiResponse(
                "Sucursal eliminada exitosamente",
                HttpStatus.OK.value(),
                LocalDate.now()
        );
        return ResponseEntity.ok(response);
    }
}