package com.supermercado.gestion_ventas.models;

import com.supermercado.gestion_ventas.models.keys.SaleProduct;
import jakarta.persistence.*;
import lombok.*;

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

    //La relación es a través de la entidad intermedia SaleProduct
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SaleProduct> saleProducts = new HashSet<>();
}