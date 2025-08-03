package com.supermercado.gestion_ventas.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {

    // TODO: nombres visibles al cliente en español al estar los endPoints en español
    @JsonProperty("tiendaId")
    private Long id;
    @JsonProperty("tiendaNombre")
    private String name;
    @JsonProperty("tiendaCiudad")
    private String city;
    @JsonProperty("tiendaDireccion")
    private String address;
    @JsonProperty("tiendaListaVentas")
    private List<SaleDTO> sales;
}