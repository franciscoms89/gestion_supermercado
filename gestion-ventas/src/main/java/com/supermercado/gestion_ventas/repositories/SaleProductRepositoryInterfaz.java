package com.supermercado.gestion_ventas.repositories;

import com.supermercado.gestion_ventas.models.keys.SaleProduct;
import com.supermercado.gestion_ventas.models.keys.SaleProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleProductRepositoryInterfaz extends JpaRepository<SaleProduct, SaleProductId> {
}
