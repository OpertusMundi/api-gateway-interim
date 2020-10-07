package eu.opertusmundi.email.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfiguration {

    @Value("${springdoc.api-docs.server:http://localhost:8105}")
    private String serverUrl;

    /**
     * Provide configuration for OpenAPI auto-generated
     * configuration.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        final Map<String, String> logo = new HashMap<>();
        logo.put("url", this.serverUrl + "/assets/img/logo.png");
        logo.put("backgroundColor", "#FFFFFF");
        logo.put("altText", "OpertusMundi");

        final Map<String, Object> extensions = new HashMap<>();
        extensions.put("x-logo", logo);

        return new OpenAPI()
            .info(new Info()
                .title("Opertus Mundi Email Service")
                .version("1.0.0")
                .description(
                    "Opertus Mundi development instance"
                )
                .termsOfService("https://opertusmundi.eu/terms/")
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://opertusmundi.eu/")
                )
                .extensions(extensions)
            )
            .addServersItem(new Server()
                .url(this.serverUrl)
                .description("OpertusMundi - Email Service Development Instance")
            );
    }


}
