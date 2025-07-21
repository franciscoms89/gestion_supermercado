package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
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
    public ResponseEntity<List<SaleDTO>> listAll(@RequestParam(required = false) Long shopId,
                                                 @RequestParam(required = false) LocalDate saleDate) {
        List<SaleDTO> list = saleService.listAll(shopId, saleDate);
        return ResponseEntity.ok(list);
    }

    // POST - Crear una nueva venta
    @PostMapping
    public ResponseEntity<SaleDTO> create(@RequestBody SaleDTO sale) {
        SaleDTO saleCreated = saleService.register(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleCreated);
    }

    // DELETE - Eliminar una venta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        saleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}