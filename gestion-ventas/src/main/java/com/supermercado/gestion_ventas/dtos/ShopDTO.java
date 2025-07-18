package com.supermercado.gestion_ventas.dtos;

import com.supermercado.gestion_ventas.models.Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {
    private Long id;
    private String name;
    private String city;
    private String address;
    private List<Sale> sales;
}
