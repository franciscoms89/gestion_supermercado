package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.response.Response;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    // IoC

    @Autowired
    ProductInterfaz productService;

    // GET - Obtener todos los productos en forma de lista
    @GetMapping
    public ResponseEntity<?> listAll() {
        ResponseEntity<?> list = productService.listAll();
        return ResponseEntity.ok(list);
    }

    // POST - Crear un nuevo producto
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDTO product) {
        ResponseEntity<?> productCreated = productService.create(product);
        return ResponseEntity.ok(productCreated);
    }

    // PUT - Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO productToUpdate) {
        ResponseEntity<?> productDTO = productService.update(id, productToUpdate);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productDTO);
    }

    // DELETE - Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Response response = productService.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}