package com.supermercado.gestion_ventas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class TestProductController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepositoryInterfaz repository;

    private final String URL_ENDPOINT = "/api/productos";
    private final ProductDTO productDTO = new ProductDTO(null, "Pan de Molde", 1.95, "Panadería");

    @Test
    @DisplayName("=== Buscar todos los productos ===")
    public void listAllTest() throws Exception {
        // Ruta a testear y qué esperamos del resultado
        mockMvc.perform(get(URL_ENDPOINT))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("=== Crear un producto ===")
    public void createTest() throws Exception {
        // Hacemos un POST de la ruta deseada y verificamos si los datos del objeto creado coinciden
        mockMvc.perform(post(URL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.productoId").isNumber())
                .andExpect(jsonPath("$.productoNombre").value(productDTO.getName()))
                .andExpect(jsonPath("$.productoPrecio").value(productDTO.getPrice()))
                .andExpect(jsonPath("$.productoCategoria").value(productDTO.getCategory()));
    }

    @Test
    @DisplayName("=== Actualizar un producto ===")
    public void updateTest() throws Exception {
        // Obtenemos un id de un producto existente o creamos un producto base si no hay
        Long id = getOrCreateProductId();

        // Preparamos los datos actualizados
        ProductDTO updatedDTO = new ProductDTO(null, "Pan de Molde Integral", 2.50, "Panadería");

        // Ejecutamos update y verificamos
        mockMvc.perform(put(URL_ENDPOINT + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.productoId").value(id))
                .andExpect(jsonPath("$.productoNombre").value(updatedDTO.getName()))
                .andExpect(jsonPath("$.productoPrecio").value(updatedDTO.getPrice()))
                .andExpect(jsonPath("$.productoCategoria").value(updatedDTO.getCategory()));
    }

    @Test
    @DisplayName("=== Eliminar un producto ===")
    public void deleteTest() throws Exception {
        Long id = getOrCreateProductId();

        mockMvc.perform(delete(URL_ENDPOINT + "/" + id))
                .andExpect(status().isNoContent());
    }

    // Función auxiliar para obtener o crear un producto base y devolver su ID
    private Long getOrCreateProductId() {
        var all = repository.findAll();
        if (all.isEmpty()) {
            Product newProduct = new Product();
            newProduct.setName("Pan de Molde");
            newProduct.setPrice(1.95);
            newProduct.setCategory("Panadería");
            return repository.save(newProduct).getId();
        } else {
            return all.get(0).getId();
        }
    }
}