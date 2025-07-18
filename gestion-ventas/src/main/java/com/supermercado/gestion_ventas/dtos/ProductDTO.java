package com.supermercado.gestion_ventas.dtos;

import com.supermercado.gestion_ventas.models.Sale;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private String category;
    private Set<SaleDTO> sales = new HashSet<>();
}
