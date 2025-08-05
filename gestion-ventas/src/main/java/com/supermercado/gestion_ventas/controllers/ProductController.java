package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    // IoC

    @Autowired
    ProductInterfaz productService;

    // GET - Obtener todos los productos en forma de lista
    @GetMapping("/productos")
    public ResponseEntity<List<ProductDTO>> listAll() {
        List<ProductDTO> productList = productService.listAll();
        return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    //GET - Obtener producto más vendido
    @GetMapping("/estadisticas/producto-mas-vendido")
    public ResponseEntity<ProductDTO> topSellingProduct() {
        ProductDTO topSellingProduct = productService.productTopSelling();
        return ResponseEntity.status(HttpStatus.OK).body(topSellingProduct);
    }

    // POST - Crear un nuevo producto
    @PostMapping("/productos")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product) {
        ProductDTO productCreated = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreated);
    }

    // PUT - Actualizar un producto existente
    @PutMapping("/productos/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productToUpdate) {
        ProductDTO updateProduct = productService.update(id, productToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    // DELETE - Eliminar un producto
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}