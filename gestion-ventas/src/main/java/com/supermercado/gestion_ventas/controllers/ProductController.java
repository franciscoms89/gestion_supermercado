package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.response.ApiResponse;
import com.supermercado.gestion_ventas.services.interfaces.ProductInterfaz;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        List<ProductDTO> productList = productService.listAll();
        if (productList.isEmpty()) {
            ApiResponse response = new ApiResponse(
                    "No hay productos registrados",
                    HttpStatus.NO_CONTENT.value(),
                    LocalDate.now()
                    );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(productList);
    }

    // POST - Crear un nuevo producto
    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody ProductDTO product) {
        ProductDTO productCreated = productService.create(product);
        ApiResponse response = new ApiResponse(
                "Producto creado exitosamente",
                HttpStatus.CREATED.value(),
                LocalDate.now()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // PUT - Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody ProductDTO productToUpdate) {
        ProductDTO updateProduct = productService.update(id, productToUpdate);
        ApiResponse response = new ApiResponse(
                "Producto actualizado exitosamente",
                HttpStatus.OK.value(),
                LocalDate.now()
        );
        return ResponseEntity.ok(response);
    }

    // DELETE - Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        productService.delete(id);
        ApiResponse response = new ApiResponse(
                "Producto eliminado exitosamente",
                HttpStatus.NO_CONTENT.value(),
                LocalDate.now()
        );
        return ResponseEntity.ok(response);
    }
}