package br.com.israelbastos.avaliaai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Avalia Aí API")
                        .version("v1")
                        .description("This is a Restful API with Spring Boot service using springdoc-openapi and OpenAPI 3."));
    }
}
