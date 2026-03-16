package com.soluciones.ticketgestor.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "CoreDesk API",
                version = "1.0",
                description = "Sistema empresarial para la gestión y seguimiento de incidencias (Issue Tracker). Desarrollado con Spring Boot, arquitectura REST, seguridad basada en tokens JWT y persistencia de datos relacional."
        ),
        servers = {
                // Forzamos a Swagger a usar HTTPS en producción
                @Server(url = "https://coredesk.isierra93.site/api/v1", description = "Servidor de Producción")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
