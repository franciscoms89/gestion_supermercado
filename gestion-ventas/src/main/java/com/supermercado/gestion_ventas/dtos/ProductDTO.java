package com.supermercado.gestion_ventas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private double price;
    private String category;
    // TODO [ACTUALIZACIÓN DTO]: Se eliminó 'Set<SaleDTO> sales'.
    // La relación Product-Sale ahora se gestiona vía SaleProduct para la cantidad.
    // Este DTO de producto ya no contiene la lista completa de ventas para evitar sobrecarga y bucles.
}
