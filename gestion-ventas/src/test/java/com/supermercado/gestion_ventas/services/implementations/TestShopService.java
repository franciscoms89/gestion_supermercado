package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.ShopDTO;
import com.supermercado.gestion_ventas.exceptions.ShopNotFoundException;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestShopService {

    @InjectMocks
    private ShopService shopService;

    @Mock
    private ShopRepositoryInterfaz shopRepository;

    @Mock
    private SaleInterfaz saleService;

    @Test
    @DisplayName("=== Listar todas las sucursales con datos ===")
    void listAllSuccess() {
        Shop shop1 = new Shop(1L, "Sucursal A", "Madrid", "Calle 123", List.of());
        Shop shop2 = new Shop(2L, "Sucursal B", "Barcelona", "Avenida 456", List.of());

        when(shopRepository.findAll()).thenReturn(List.of(shop1, shop2));

        List<ShopDTO> result = shopService.listAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Sucursal A");
        assertThat(result.get(1).getCity()).isEqualTo("Barcelona");
        assertThat(result.get(1).getAddress()).isEqualTo("Avenida 456");
    }

    @Test
    @DisplayName("=== Listar todas las sucursales pero no hay ninguna ===")
    void listAllEmpty() {
        when(shopRepository.findAll()).thenReturn(Collections.emptyList());

        List<ShopDTO> result = shopService.listAll();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("=== Crear sucursal correctamente ===")
    void createSuccess() {
        ShopDTO shopDTO = new ShopDTO(null, "Sucursal Nueva", "Valencia", "Calle Nueva", List.of());
        Shop shopToSave = new Shop(null, "Sucursal Nueva", "Valencia", "Calle Nueva", List.of());
        Shop savedShop = new Shop(3L, "Sucursal Nueva", "Valencia", "Calle Nueva", List.of());

        when(shopRepository.save(shopToSave)).thenReturn(savedShop);

        ShopDTO result = shopService.create(shopDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Sucursal Nueva");
        assertThat(result.getCity()).isEqualTo("Valencia");
    }

    @Test
    @DisplayName("=== Crear sucursal lanza excepción inesperada ===")
    void createThrowsException() {
        ShopDTO shopDTO = new ShopDTO(null, "Sucursal Fallida", "Ciudad", "Dirección", Collections.emptyList());

        when(shopRepository.save(any(Shop.class)))
                .thenThrow(new RuntimeException("Error al guardar en BD"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> shopService.create(shopDTO));

        assertThat(exception.getMessage()).isEqualTo("Error inesperado al crear la sucursal.");
    }

    @Test
    @DisplayName("=== Actualizar sucursal existente correctamente ===")
    void updateSuccess() {
        Long id = 5L;
        Shop existingShop = new Shop(id, "Sucursal Antigua", "Sevilla", "Calle Antigua", Collections.emptyList());
        ShopDTO updateDTO = new ShopDTO(null, "Sucursal Actualizada", "Sevilla", "Calle Nueva", Collections.emptyList());
        Shop updatedShop = new Shop(id, "Sucursal Actualizada", "Sevilla", "Calle Nueva", Collections.emptyList());

        when(shopRepository.findById(id)).thenReturn(Optional.of(existingShop));
        when(shopRepository.save(existingShop)).thenReturn(updatedShop);

        ShopDTO result = shopService.update(id, updateDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Sucursal Actualizada");
        assertThat(result.getAddress()).isEqualTo("Calle Nueva");
    }

    @Test
    @DisplayName("=== Actualizar sucursal inexistente y lanza excepción ===")
    void updateNotFound() {
        Long missingId = 100L;
        ShopDTO updateDTO = new ShopDTO(null, "Sucursal X", "Ciudad", "Dirección", Collections.emptyList());

        when(shopRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(ShopNotFoundException.class, () -> shopService.update(missingId, updateDTO));
    }

    @Test
    @DisplayName("=== Eliminar sucursal existente correctamente ===")
    void deleteSuccess() {
        Long id = 10L;

        when(shopRepository.existsById(id)).thenReturn(true);
        doNothing().when(shopRepository).deleteById(id);

        shopService.delete(id);

        verify(shopRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("=== Eliminar sucursal inexistente y lanza excepción ===")
    void deleteNotFound() {
        Long missingId = 200L;

        when(shopRepository.existsById(missingId)).thenReturn(false);

        assertThrows(ShopNotFoundException.class, () -> shopService.delete(missingId));
    }
}