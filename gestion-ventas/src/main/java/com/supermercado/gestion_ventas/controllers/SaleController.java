package com.supermercado.gestion_ventas.controllers;

import com.supermercado.gestion_ventas.services.interfaces.SaleInterfaz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ventas")
public class SaleController {

    // IoC

    @Autowired
    SaleInterfaz saleService;


}
