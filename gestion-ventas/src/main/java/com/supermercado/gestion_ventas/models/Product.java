package com.supermercado.gestion_ventas.models;

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

    // Relación: Un Producto puede estar en muchas Ventas
    @ManyToMany(mappedBy = "products")
    private Set<Sale> sales = new HashSet<>();

}
