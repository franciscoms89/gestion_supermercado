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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TestProductService {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepositoryInterfaz repository;

    // Pruebas Unitarias
    @Test
    @DisplayName("=== Método para buscar todos los productos de forma exitosa ===")
    void listAll(){
        // Creamos una lista ficticia
        List<Product> productList = List.of(
                new Product(1L, "Ternera", 4.85, "Carnicería", new HashSet<>()),
                new Product(2L, "Leche Sin Lactosa", 1.85, "Lácteos", new HashSet<>())
        );

        // Ejecutamos la función findAll del repositorio para mockear
        when(repository.findAll()).thenReturn(productList);

        // Ejecutamos la función a testear
        List<ProductDTO> productsOfService = service.listAll();

        // Verificamos que los valores sean iguales que los esperados
        assertThat(productsOfService.get(0).getId()).isEqualTo(1L);
        assertThat(productsOfService.get(1).getName()).isEqualTo("Leche Sin Lactosa");
        assertThat(productsOfService.get(0).getPrice()).isEqualTo(4.85);
        assertThat(productsOfService.get(1).getCategory()).isEqualTo("Lácteos");
    }

    @Test
    @DisplayName("=== Método para buscar todos los productos cuando la lista está vacía ===")
    void listAllWhenEmptyList(){
        when(repository.findAll()).thenReturn(List.of());
        List<ProductDTO> result = service.listAll();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("=== Método para crear un producto de forma exitosa ===")
    void create(){
        // Creamos los productos ficticios
        ProductDTO productDTO = new ProductDTO(null, "Pan", 0.75, "Panadería");
        // El objeto que se espera que se construya internamente en el service (id null)
        Product toSave = new Product(null, "Pan", 0.75, "Panadería", new HashSet<>());
        // El que viene "guardado" con ID asignado
        Product productSaved = new Product(3L, "Pan", 0.75, "Panadería", new HashSet<>());

        // Ejecutamos la función save del repositorio para mockear
        when(repository.save(toSave)).thenReturn(productSaved);

        // Ejecutamos la función a testear
        ProductDTO result = service.create(productDTO);

        // Verificamos que los valores sean iguales que los esperados
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Pan");
        assertThat(result.getPrice()).isEqualTo(0.75);
        assertThat(result.getCategory()).isEqualTo("Panadería");
    }

    @Test
    @DisplayName("=== Método para crear un producto pero falla y lanza excepción ===")
    void createAndFail(){
        // Creamos los productos ficticios
        ProductDTO productDTO = new ProductDTO(null, "Queso", 7.95, "Lácteos");
        // El objeto que se espera que se construya internamente en el service (id null)
        Product toSave = new Product(null, "Queso", 7.95, "Lácteos", new HashSet<>());

        // Simulamos que el repositorio lanza una excepción
        when(repository.save(toSave)).thenThrow(new RuntimeException("Error inesperado al registrar el producto."));

        // Verificamos que se lanza una excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.create(productDTO));
        assertThat(exception.getMessage()).contains("Error inesperado al registrar el producto.");
    }
}