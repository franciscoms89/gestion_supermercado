package com.supermercado.gestion_ventas.repositories;

import com.supermercado.gestion_ventas.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryInterfaz extends JpaRepository<Product, Long> {
}
