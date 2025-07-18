package com.supermercado.gestion_ventas.repositories;

import com.supermercado.gestion_ventas.models.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepositoryInterfaz extends JpaRepository<Shop, Long> {
}
