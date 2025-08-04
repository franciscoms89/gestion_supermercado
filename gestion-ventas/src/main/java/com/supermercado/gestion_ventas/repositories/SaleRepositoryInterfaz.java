package com.supermercado.gestion_ventas.repositories;

import com.supermercado.gestion_ventas.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepositoryInterfaz extends JpaRepository<Sale, Long> {

    // Busca todas las ventas activas
    List<Sale> findAllByActiveTrue();

    // Nuevos metodos que filtran por "active = true"
    List<Sale> findByShopIdAndActiveTrue(Long shopId);

    List<Sale> findBySaleDateAndActiveTrue(LocalDate saleDate);

    List<Sale> findByShopIdAndSaleDateAndActiveTrue(Long shopId, LocalDate saleDate);
}