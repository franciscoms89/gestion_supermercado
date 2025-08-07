package com.supermercado.gestion_ventas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.SaleDTO.SaleDetailsDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.SaleRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class TestSaleController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SaleRepositoryInterfaz saleRepository;

    @Autowired
    private ShopRepositoryInterfaz shopRepository;

    @Autowired
    private ProductRepositoryInterfaz productRepository;

    private final String URL_ENDPOINT = "/api/ventas";
    private Shop testShop;
    private Product testProduct;


    @BeforeEach
    void setup() {
        // Crea una tienda y un producto base para usar en los tests
        testShop = shopRepository.save(new Shop(null, "Tienda de Prueba", "Ciudad Test", "Dirección Test", null));
        testProduct = productRepository.save(new Product(null, "Producto de Prueba", 10.0, "Categoría Test", null));
    }

    @Test
    @DisplayName("=== Listar todas las ventas ===")
    void listAllTest() throws Exception {
        mockMvc.perform(get(URL_ENDPOINT))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("=== Crear una venta ===")
    void createTest() throws Exception {
        // Usa los IDs de los datos creados en el metodo setup.
        SaleDetailsDTO detailsDTO = new SaleDetailsDTO(testProduct.getId(), 10);
        SaleDTO saleDTO = new SaleDTO(null, testShop.getId(), LocalDate.now(), List.of(detailsDTO));

        mockMvc.perform(post(URL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleDTO)))
                .andExpect(status().isCreated()) // Esperamos 201 Created
                .andExpect(jsonPath("$.ventaId").isNumber())
                .andExpect(jsonPath("$.ventaTiendaId").value(testShop.getId()))
                .andExpect(jsonPath("$.ventaDetalles[0].detallesProductoId").value(testProduct.getId()));
    }

    @Test
    @DisplayName("=== Eliminar una venta ===")
    void deleteTest() throws Exception {
        Sale sale = saleRepository.save(new Sale(null, testShop, LocalDate.now(), null, true));
        mockMvc.perform(delete(URL_ENDPOINT + "/" + sale.getId()))
                .andExpect(status().isNoContent());
    }
}