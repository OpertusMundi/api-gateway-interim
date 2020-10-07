package eu.opertusmundi.rating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(
    scanBasePackageClasses = {
        eu.opertusmundi.rating.config._Marker.class,
        eu.opertusmundi.rating.repository._Marker.class,
        eu.opertusmundi.rating.service._Marker.class,
        eu.opertusmundi.rating.controller._Marker.class,
    }
)
@EntityScan(
    basePackageClasses = {
        eu.opertusmundi.rating.domain._Marker.class,
    }
)
public class Application extends SpringBootServletInitializer {

    /**
     * Used when packaging as a WAR application
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
    {
        return builder.sources(Application.class);
    }

    /**
     * Used when packaging as a standalone JAR (the server is embedded)
     */
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
