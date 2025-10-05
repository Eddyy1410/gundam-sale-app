package com.huyntd.superapp.gundam_shop.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("Gundam Shop API")
                .version("v1.0.0"));
    }

}
