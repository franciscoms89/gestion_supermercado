package com.supermercado.gestion_ventas.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private String message;
    private int estateCode;
    private LocalDate date;
}