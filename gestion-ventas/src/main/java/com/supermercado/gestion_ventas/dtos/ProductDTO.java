package com.supermercado.gestion_ventas.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    // TODO: nombres visibles al cliente en español al estar los endPoints en español
    @JsonProperty("productoId")
    private Long id;
    @JsonProperty("productoNombre")
    private String name;
    @JsonProperty("productoPrecio")
    private Double price;
    @JsonProperty("productoCategoria")
    private String category;
}