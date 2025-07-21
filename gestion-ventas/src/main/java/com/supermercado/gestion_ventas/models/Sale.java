package com.supermercado.gestion_ventas.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Relación: Muchas Ventas pertenecen a una Sucursal
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    private LocalDate saleDate;

    //Relación: Una Venta contiene muchos Productos
    //Una Venta puede tener muchos Productos y un Producto puede estar en muchas Ventas
    @ManyToMany
    @JoinTable(
            name = "sale_products",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
            )
    private Set<Product> products = new HashSet<>();
}
