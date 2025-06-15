package com.perfulandia.clientes_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Clientes - Perfulandia",
        version = "1.0",
        description = "Microservicio para la gestión de clientes en Perfulandia"
    )
)
public class SwaggerConfig {
}
