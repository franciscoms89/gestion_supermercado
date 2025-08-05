package com.supermercado.gestion_ventas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.dtos.SaleDTO.SaleDetailsDTO;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.SaleRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;
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
public class TestSaleController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SaleRepositoryInterfaz saleRepository;

    @Autowired
    private ShopRepositoryInterfaz shopRepository;

    private final String URL_ENDPOINT = "/api/ventas";
    private final SaleDetailsDTO detailsDTO = new SaleDetailsDTO(1L, 10);
    private final SaleDTO saleDTO = new SaleDTO(null, 101L, LocalDate.now(), List.of(detailsDTO));

    @Test
    @DisplayName("=== Listar todas las ventas ===")
    public void listAllTest() throws Exception {
        mockMvc.perform(get(URL_ENDPOINT))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("=== Crear una venta ===")
    public void createTest() throws Exception {
        mockMvc.perform(post(URL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.ventaId").isNumber())
                .andExpect(jsonPath("$.ventaTiendaId").value(101L))
                .andExpect(jsonPath("$.ventaDetalles[0].detallesProductoId").value(1L))
                .andExpect(jsonPath("$.ventaDetalles[0].detallesCantidad").value(10));
    }

    @Test
    @DisplayName("=== Eliminar una venta ===")
    public void deleteTest() throws Exception {
        Long saleId = getOrCreateSaleId();
        mockMvc.perform(delete(URL_ENDPOINT + "/" + saleId))
                .andExpect(status().isNoContent());

    }

    // Función auxiliar para obtener o crear una venta base y devolver su ID
    private Long getOrCreateSaleId() {
        var all = saleRepository.findAll();
        if (all.isEmpty()) {
            Shop shop = shopRepository.findAll().stream().findFirst().orElseGet(() -> {
                Shop newShop = new Shop();
                newShop.setName("Sucursal Base");  // pon aquí los datos mínimos que requieras
                return shopRepository.save(newShop);
            });
            Sale newSale = new Sale();
            newSale.setShop(shop);
            newSale.setSaleDate(LocalDate.now());
            newSale.setSaleProducts(new HashSet<>());
            return saleRepository.save(newSale).getId();
        } else {
            return all.get(0).getId();
        }
    }
}