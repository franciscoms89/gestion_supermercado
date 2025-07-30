package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.response.Response;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class SaleController {

    // IoC

    @Autowired
    SaleInterfaz saleService;

    // GET - Obtener todas las ventas con posibles filtrados
    @GetMapping
    public ResponseEntity<?> listAll(@RequestParam(required = false) Long tiendaId,
                                                 @RequestParam(required = false) LocalDate fechaOferta) {
        // TODO [IMPORTANTE]: variables en español para hacer las busquedas, ya que los endPoints estan en español.
        ResponseEntity<?> list = saleService.listAll(tiendaId, fechaOferta);
        return ResponseEntity.ok(list);
    }

    // POST - Crear una nueva venta
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SaleDTO sale) {
        ResponseEntity<Response> saleCreated = saleService.register(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleCreated);
    }

    // DELETE - Eliminar una venta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Response response = saleService.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
