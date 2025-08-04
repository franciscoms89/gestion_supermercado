package com.supermercado.gestion_ventas.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean  // Esta etiqueta es necesaria para registrar el bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Una cadena de supermercados")
                        .version("0.0.1")
                        .description("Se gestionaran ventas , ofertas y tiendas de la cadena"));
    }
}