package eu.opertusmundi.bpm.server.config;

import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;
import org.camunda.bpm.engine.rest.security.auth.impl.HttpBasicAuthenticationProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure security aspects for BPM server.
 *
 * Note: Camunda uses its own security layer (independently of Spring-Security) with is based on
 * servlet filters.
 *
 * @see <a href="https://camunda.com/best-practices/securing-camunda/">Securing Camunda</a>
 * @see <a href="https://github.com/camunda-consulting/code/tree/master/snippets/springboot-security-sso">Integrating Spring Security with Camunda</a>
 */
@Configuration
public class CamundaSecurityConfiguration {

    @Bean
    public FilterRegistrationBean<ProcessEngineAuthenticationFilter> registerProcessEngineAuthenticationFilter() {
        final FilterRegistrationBean<ProcessEngineAuthenticationFilter> registration = new FilterRegistrationBean<>();

        registration.setName("camunda-auth");
        registration.setFilter(new ProcessEngineAuthenticationFilter());
        registration.addInitParameter("authentication-provider", HttpBasicAuthenticationProvider.class.getName());
        registration.addUrlPatterns("/engine-rest/*");

        return registration;
    }

}