package br.com.solutis.squad1.orderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for SpringDoc OpenAPI.
 * This class sets up the necessary configurations for the OpenAPI documentation of the Order Microservice.
 */
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order")
                        .description("Order Microservice")
                        .version("1.0.0"));
    }
}