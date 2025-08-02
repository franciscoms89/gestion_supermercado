package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.ProductDTO;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepositoryInterfaz repository;

    // Pruebas Unitarias
    @Test
    @DisplayName("============= Método para buscar todos los productos =============")
    void listAll(){
        // Creamos una lista ficticia
        List<Product> productList = List.of(
                new Product(1L, "Ternera", 4.85, "Carnicería", new HashSet<>()),
                new Product(2L, "Leche Sin Lactosa", 1.85, "Lácteos", new HashSet<>())
        );
        when(repository.findAll()).thenReturn(productList);
        List<ProductDTO> productsOfService = service.listAll();

        // Verificamos que los valores sean iguales que los esperados
        assertThat(productsOfService.get(0).getId()).isEqualTo(1L);
        assertThat(productsOfService.get(1).getName()).isEqualTo("Leche Sin Lactosa");
        assertThat(productsOfService.get(0).getPrice()).isEqualTo(4.85);
        assertThat(productsOfService.get(1).getCategory()).isEqualTo("Lácteos");
    }

    @Test
    @DisplayName("============= Método para buscar todos los productos cuando la lista está vacía =============")
    void listAllWhenEmptyList(){
        when(repository.findAll()).thenReturn(List.of());
        List<ProductDTO> result = service.listAll();
        assertThat(result).isEmpty();
    }
}