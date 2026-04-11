package edu.dosw.parcial.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DOSW Parcial 2 - API de Ride Hailing")
                        .version("1.0.0")
                        .description("Documentación de los endpoints para el sistema de gestión de viajes (Estilo Uber) solicitado en el Parcial 2 de Desarrollo de Software.")
                        .contact(new Contact()
                                .name("Stiven Pardo")
                                .email("stiven.pardo@mail.escuelaing.edu.co")));
    }
}
