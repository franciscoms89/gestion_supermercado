package com.supermercado.gestion_ventas.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {

    // TODO: nombres visibles al cliente en español al estar los endPoints en español
    @JsonProperty("ventaId")
    private Long id;
    @JsonProperty("ventaTiendaId")
    private Long shopId;
    @JsonProperty("ventaFecha")
    private LocalDate saleDate;
    @JsonProperty("ventaDetalles")
    private List<SaleDetailsDTO> saleDetails;

    // TODO [IMPORTANTE]: Clase interna para los detalles de cada producto en la venta.
    // Esto es crucial para poder enviar la 'cantidad' de cada producto.
    // Corresponde a la información que se mapeará a la nueva entidad SaleProduct.
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaleDetailsDTO{
        @JsonProperty("detallesProductoId")
        private Long productId;
        @JsonProperty("detallesCantidad")
        private Integer quantity;
    }
}