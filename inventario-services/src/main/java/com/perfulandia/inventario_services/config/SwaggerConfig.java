package com.perfulandia.inventario_services.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Inventario - Perfulandia",
        version = "1.0",
        description = "Microservicio que gestiona productos, stock y sucursales del sistema Perfulandia.",
        contact = @Contact(name = "Equipo de Desarrollo", email = "soporte@perfulandia.com"),
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
    )
)
public class SwaggerConfig {
}