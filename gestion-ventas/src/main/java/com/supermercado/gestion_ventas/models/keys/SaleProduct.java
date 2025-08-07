package com.supermercado.gestion_ventas.models.keys;

import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "sale_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleProduct implements Serializable {

    @EmbeddedId
    private SaleProductId id;

    @ManyToOne
    @MapsId("saleId")
    @JoinColumn(name = "sale_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Sale sale;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    public void setSale(Sale sale) {
        this.sale = sale;
        this.id = new SaleProductId();
        {
        }
        if (sale != null && sale.getId() != null) {
            this.id.setSaleId(sale.getId());
        }
    }

    public SaleProduct(Sale sale, Product product, Integer quantity) {
        this.sale = sale;
        this.product = product;
        this.quantity = quantity;
        this.id = new SaleProductId(sale.getId(), product.getId());
    }
}