package eu.opertusmundi.cli.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

    @Bean
    MessageSource messageSource() {
        final ResourceBundleMessageSource source = new ResourceBundleMessageSource();

        source.setBasenames(
                "common-messages",
                "messages");

        source.setAlwaysUseMessageFormat(true);
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("utf-8");

        return source;
    }

}
