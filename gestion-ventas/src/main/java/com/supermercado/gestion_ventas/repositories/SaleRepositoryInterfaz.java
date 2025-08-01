package com.supermercado.gestion_ventas.repositories;

import com.supermercado.gestion_ventas.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepositoryInterfaz extends JpaRepository<Sale, Long> {

    // Buscar por ID de tienda Y fecha de venta
    List<Sale> findByShopIdAndSaleDate(Long shopId, LocalDate saleDate);

    // Buscar solo por ID de tienda
    List<Sale> findByShopId(Long shopId);

    // Buscar solo por fecha de venta
    List<Sale> findBySaleDate(LocalDate saleDate);
}