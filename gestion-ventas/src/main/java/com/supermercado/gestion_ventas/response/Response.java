package com.supermercado.gestion_ventas.response;

import com.supermercado.gestion_ventas.dtos.SaleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private String message;
    private int estateCode;
    private LocalDate date;
}
