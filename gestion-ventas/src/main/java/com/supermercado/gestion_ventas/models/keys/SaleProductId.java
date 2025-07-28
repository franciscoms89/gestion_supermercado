package com.supermercado.gestion_ventas.models.keys;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleProductId implements Serializable {
    private Long saleId;
    private Long productId;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        SaleProductId that = (SaleProductId) o;
        return Objects.equals(saleId, that.saleId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(saleId, productId);
    }
}
