package com.supermercado.gestion_ventas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Long id;
    private Long shopId;
    private LocalDate saleDate;
    private List<SaleDetailsDTO> saleDetails;


    // DTO interno para el desglose de cada producto en la venta
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaleDetailsDTO{
        private Long productId;
        private int quantity;
    }
}
