package com.supermercado.gestion_ventas.models;
import com.supermercado.gestion_ventas.models.keys.SaleProduct;
import jakarta.persistence.*;
import lombok.*;

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
    @ToString.Exclude
    private Shop shop;

    private LocalDate saleDate;

    // TODO [IMPORTANTE]: Cambio de relación con Product.
    // Antes: @ManyToMany y @JoinTable directamente aquí.
    // Ahora: La relación es a través de la entidad intermedia SaleProduct.
    // Esto es NECESARIO para manejar la 'cantidad' de productos en cada venta.
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SaleProduct> saleProducts = new HashSet<>();
}
