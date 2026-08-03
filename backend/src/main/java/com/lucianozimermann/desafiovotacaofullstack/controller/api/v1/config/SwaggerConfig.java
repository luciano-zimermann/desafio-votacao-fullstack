package com.lucianozimermann.desafiovotacaofullstack.controller.api.v1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                                          .title("Desafio Votação Fullstack")
                                          .version("1.0.0")
                                          .description("Documentação automática da API - Desafio Votação Fullstack")
                                          .contact(new Contact().name("Luciano Zimermann")
                                                                .email("lucianozn16@gmail.com")));
    }
}