package com.supermercado.gestion_ventas.models;

import com.supermercado.gestion_ventas.models.keys.SaleProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;

    private String category;

    // TODO [IMPORTANTE]: Cambio de relación con Sale.
    // Antes: @ManyToMany(mappedBy = "products") private Set<Sale> sales;
    // Ahora: La relación es a través de la entidad intermedia SaleProduct
    // Esto permite almacenar la 'cantidad' de productos por venta.
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SaleProduct> saleProducts = new HashSet<>();

}
