package com.supermercado.gestion_ventas.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.models.Shop;
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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class TestShopController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShopRepositoryInterfaz shopRepository;

    private final String URL_ENDPOINT = "/api/sucursales";
    private final ShopDTO shopDTO = new ShopDTO(null, "Supermercado Central", "Springfield", "Av. Siempre Viva 123", new ArrayList<>());

    @Test
    @DisplayName("=== Listar todas las sucursales ===")
    public void listAllTest() throws Exception {
        mockMvc.perform(get(URL_ENDPOINT))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("=== Crear una sucursal ===")
    public void createTest() throws Exception {
        // Hacemos un POST de la ruta deseada y verificamos si los datos del objeto creado coinciden
        mockMvc.perform(post(URL_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopDTO)))
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.tiendaId").isNumber())
                .andExpect(jsonPath("$.tiendaNombre").value(shopDTO.getName()))
                .andExpect(jsonPath("$.tiendaDireccion").value(shopDTO.getAddress()))
                .andExpect(jsonPath("$.tiendaCiudad").value(shopDTO.getCity()))
                .andExpect(jsonPath("$.tiendaListaVentas").isArray())
                .andExpect(jsonPath("$.tiendaListaVentas").isEmpty());
    }

    @Test
    @DisplayName("=== Actualizar una sucursal ===")
    public void updateTest() throws Exception {
        // Obtenemos un id de una surucsal existente o creamos una sucursal base si no hay
        Long id = getOrCreateShopId();

        // Preparamos los datos actualizados
        ShopDTO updatedDTO = new ShopDTO(null, "Supermercado Madrid", "Madrid", "Av. Madrid 123", new ArrayList<>());

        // Ejecutamos update y verificamos
        mockMvc.perform(put(URL_ENDPOINT + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDTO)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.tiendaId").value(id))
                .andExpect(jsonPath("$.tiendaNombre").value(updatedDTO.getName()))
                .andExpect(jsonPath("$.tiendaDireccion").value(updatedDTO.getAddress()))
                .andExpect(jsonPath("$.tiendaCiudad").value(updatedDTO.getCity()))
                .andExpect(jsonPath("$.tiendaListaVentas").isArray());
    }

    @Test
    @DisplayName("=== Eliminar una sucursal ===")
    public void deleteTest() throws Exception {
        Long id = getOrCreateShopId();

        mockMvc.perform(delete(URL_ENDPOINT + "/" + id))
                .andExpect(status().isNoContent());
    }

    // Función auxiliar para obtener o crear una sucursal base y devolver su ID
    private Long getOrCreateShopId() {
        var all = shopRepository.findAll();
        if (all.isEmpty()) {
            Shop newShop = new Shop();
            newShop.setName("Supermercado Central");
            newShop.setCity("Springfield");
            newShop.setAddress("Av. Siempre Viva 123");
            newShop.setSales(new ArrayList<>());
            return shopRepository.save(newShop).getId();
        } else {
            return all.get(0).getId();
        }
    }
}