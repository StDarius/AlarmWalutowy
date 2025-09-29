
package com.example.datagatherer.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("DataGatherer API").version("v1")
                        .description("Polls exchange rate API and publishes messages."))
                .externalDocs(new ExternalDocumentation().description("Project README"));
    }
}
