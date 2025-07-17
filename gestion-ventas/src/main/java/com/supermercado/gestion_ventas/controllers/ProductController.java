package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    // Todo: Todos los nombres de las funciones están sujetos a cambios

    // IoC

    @Autowired
    ProductInterfaz productService;

    // GET
    @GetMapping("/productos")
    public ResponseEntity<List<ProductDTO>> listAll() {
        List<ProductDTO> list = productService.listAll();
        return ResponseEntity.ok(list);
    }

    // POST
    @PostMapping("/{id}")
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO product) {
        ProductDTO productoCreated = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoCreated);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productToUpdate) {
        ProductDTO productDTO = productService.update(id, productToUpdate);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(productDTO);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<List<ProductDTO>> delete(@PathVariable Long id) {
        List<ProductDTO> list = productService.deleteProduct(id);
        return ResponseEntity.ok(list);
    }
}
