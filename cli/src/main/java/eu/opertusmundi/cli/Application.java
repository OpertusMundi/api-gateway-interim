package eu.opertusmundi.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(
    scanBasePackageClasses = {
        eu.opertusmundi.cli._Marker.class,
        eu.opertusmundi.cli.config._Marker.class,
        eu.opertusmundi.cli.command._Marker.class,
    }
)
@EntityScan(
    basePackageClasses = {

    }
)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}