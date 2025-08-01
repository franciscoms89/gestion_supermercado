package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.response.ApiResponse;
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
    public ResponseEntity<?> listAll(@RequestParam(name = "tiendaId", required = false) Long tiendaId,
                                                 @RequestParam(name = "fecha", required = false) LocalDate fechaOferta) {
        // TODO [IMPORTANTE]: variables en español para hacer las busquedas, ya que los endPoints estan en español.
        List<SaleDTO> saleList = saleService.listAll(tiendaId, fechaOferta);
        if (saleList.isEmpty()) {
            ApiResponse response = new ApiResponse(
                    "No hay ventas registradas",
                    HttpStatus.OK.value(),
                    LocalDate.now()
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(saleList);
    }

    // POST - Crear una nueva venta
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody SaleDTO sale) {
        SaleDTO saleCreated = saleService.register(sale);
        ApiResponse response = new ApiResponse(
                "Venta registrada exitosamente",
                HttpStatus.CREATED.value(),
                LocalDate.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // DELETE - Eliminar una venta
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        saleService.delete(id);
        ApiResponse response = new ApiResponse(
                "Venta eliminada exitosamente",
                HttpStatus.OK.value(),
                LocalDate.now()
        );
        return ResponseEntity.ok(response);
    }
}