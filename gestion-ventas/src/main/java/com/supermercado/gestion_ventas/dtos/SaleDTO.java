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

    // TODO [IMPORTANTE]: Clase interna para los detalles de cada producto en la venta.
    // Esto es crucial para poder enviar la 'cantidad' de cada producto.
    // Corresponde a la información que se mapeará a la nueva entidad SaleProduct.
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaleDetailsDTO{
        private Long productId;
        private int quantity;
    }
}