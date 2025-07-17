package com.supermercado.gestion_ventas.repositories;

import com.supermercado.gestion_ventas.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepositoryInterface extends JpaRepository<Sale, Long> {
}
