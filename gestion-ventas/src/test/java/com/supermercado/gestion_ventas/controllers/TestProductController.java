package com.supermercado.gestion_ventas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermercado.gestion_ventas.dtos.ProductDTO;
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

    private final String URL_ENDPOINT = "/api/productos";

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
        ProductDTO productDTO = new ProductDTO(null, "Pan de Molde", 1.95, "Panadería");
        mockMvc.perform(post(URL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.productoId").isNumber())
                .andExpect(jsonPath("$.productoNombre").value(productDTO.getName()))
                .andExpect(jsonPath("$.productoPrecio").value(productDTO.getPrice()))
                .andExpect(jsonPath("$.productoCategoría").value(productDTO.getCategory()));
    }
}