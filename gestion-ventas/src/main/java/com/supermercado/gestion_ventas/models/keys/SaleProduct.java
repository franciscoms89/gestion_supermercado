package com.supermercado.gestion_ventas.models.keys;

import com.supermercado.gestion_ventas.models.Product;
import com.supermercado.gestion_ventas.models.Sale;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sale_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleProduct {

    @EmbeddedId
    private SaleProductId id;

    @ManyToOne
    @MapsId("saleId")
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public SaleProduct(Sale sale, Product product, int quantity){
        this.sale = sale;
        this.product = product;
        this.quantity = quantity;
        this.id = new SaleProductId(sale.getId(), product.getId());
    }
}
