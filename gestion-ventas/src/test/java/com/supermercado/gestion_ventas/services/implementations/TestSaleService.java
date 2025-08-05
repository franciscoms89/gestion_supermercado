package com.supermercado.gestion_ventas.services.implementations;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import com.supermercado.gestion_ventas.exceptions.ProductNotFoundException;
import com.supermercado.gestion_ventas.exceptions.SaleNotFoundException;
import com.supermercado.gestion_ventas.exceptions.ShopNotFoundException;
import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import com.supermercado.gestion_ventas.models.Shop;
import com.supermercado.gestion_ventas.repositories.ProductRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.SaleRepositoryInterfaz;
import com.supermercado.gestion_ventas.repositories.ShopRepositoryInterfaz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestSaleService {

    @InjectMocks
    private SaleService service;

    @Mock
    private SaleRepositoryInterfaz repository;

    @Mock
    private ShopRepositoryInterfaz shopRepositoryInterfaz;

    @Mock
    private ProductRepositoryInterfaz productRepositoryInterfaz;

    @Test
    @DisplayName("=== Listar ventas sin filtros ===")
    void listAllNoFilters() {
        Shop shop = new Shop(1L, "Sucursal A", null, null, List.of());
        Sale sale = new Sale(10L, shop, LocalDate.now(), new HashSet<>(), true);

        when(repository.findAllByActiveTrue()).thenReturn(List.of(sale));

        List<SaleDTO> result = service.listAll(null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(10L);
        assertThat(result.get(0).getShopId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("=== Listar ventas con filtro por tienda y fecha ===")
    void listAllFilterShopAndDate() {
        Long shopId = 1L;
        LocalDate saleDate = LocalDate.of(2025, 8, 5);
        Shop shop = new Shop(shopId, "Sucursal A", null, null, List.of());
        Sale sale = new Sale(20L, shop, saleDate, new HashSet<>(), true);

        when(repository.findByShopIdAndSaleDateAndActiveTrue(shopId, saleDate)).thenReturn(List.of(sale));

        List<SaleDTO> result = service.listAll(shopId, saleDate);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(20L);
        assertThat(result.get(0).getShopId()).isEqualTo(shopId);
        assertThat(result.get(0).getSaleDate()).isEqualTo(saleDate);
    }

    @Test
    @DisplayName("=== Registrar venta exitosa ===")
    void registerSuccess() {
        Long shopId = 1L;
        Long productId = 10L;
        LocalDate saleDate = LocalDate.now();

        Shop shop = new Shop(shopId, "Sucursal A", null, null, List.of());
        Product product = new Product(productId, "Producto A", 1.0, "Categoría", new HashSet<>());

        SaleDTO.SaleDetailsDTO detailDTO = new SaleDTO.SaleDetailsDTO(productId, 5);
        SaleDTO saleDTO = new SaleDTO(null, shopId, saleDate, List.of(detailDTO));

        // Mock para shop y product encontrados
        when(shopRepositoryInterfaz.findById(shopId)).thenReturn(Optional.of(shop));
        when(productRepositoryInterfaz.findById(productId)).thenReturn(Optional.of(product));

        // Para guardar la venta
        Sale saleToSave = service.convertToOBJ(saleDTO);
        Sale saleSaved = new Sale();
        saleSaved.setId(100L);
        saleSaved.setShop(shop);
        saleSaved.setSaleDate(saleDate);
        saleSaved.setSaleProducts(saleToSave.getSaleProducts());

        when(repository.save(saleToSave)).thenReturn(saleSaved);

        SaleDTO result = service.register(saleDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getShopId()).isEqualTo(shopId);
        assertThat(result.getSaleDate()).isEqualTo(saleDate);
        assertThat(result.getSaleDetails()).hasSize(1);
        assertThat(result.getSaleDetails().get(0).getProductId()).isEqualTo(productId);
        assertThat(result.getSaleDetails().get(0).getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("=== Registrar venta pero falla por tienda no encontrada ===")
    void registerFailShopNotFound() {
        Long shopId = 99L;
        SaleDTO saleDTO = new SaleDTO(null, shopId, LocalDate.now(), List.of());

        when(shopRepositoryInterfaz.findById(shopId)).thenReturn(Optional.empty());

        assertThrows(ShopNotFoundException.class, () -> service.register(saleDTO));
    }

    @Test
    @DisplayName("=== Registrar venta pero falla por producto no encontrado ===")
    void registerFailProductNotFound() {
        Long shopId = 1L;
        Long productId = 55L;

        Shop shop = new Shop(shopId, "Sucursal A", null, null, List.of());
        SaleDTO.SaleDetailsDTO detailDTO = new SaleDTO.SaleDetailsDTO(productId, 3);
        SaleDTO saleDTO = new SaleDTO(null, shopId, LocalDate.now(), List.of(detailDTO));

        when(shopRepositoryInterfaz.findById(shopId)).thenReturn(Optional.of(shop));
        when(productRepositoryInterfaz.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.register(saleDTO));
    }

    @Test
    @DisplayName("=== Borrar venta exitosa ===")
    void deleteSuccess() {
        Long saleId = 100L;
        Shop shop = new Shop(1L, "Sucursal", null, null, List.of());
        Sale sale = new Sale(saleId, shop, LocalDate.now(), new HashSet<>(), true);

        when(repository.findById(saleId)).thenReturn(Optional.of(sale));
        when(repository.save(sale)).thenReturn(sale);

        service.delete(saleId);

        assertThat(sale.isActive()).isFalse();
    }

    @Test
    @DisplayName("=== Borrar venta pero falla por no existir ===")
    void deleteFailSaleNotFound() {
        Long missingId = 999L;

        when(repository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(SaleNotFoundException.class, () -> service.delete(missingId));
    }
}