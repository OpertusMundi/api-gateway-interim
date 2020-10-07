package eu.opertusmundi.rating.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${springdoc.api-docs.path}")
    private String openApiSpec;

    @Value("${opertus-mundi.security.basic-auth.username}")
    private String username;

    @Value("${opertus-mundi.security.basic-auth.password}")
    private String password;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // See: https://stackoverflow.com/questions/49654143/spring-security-5-there-is-no-passwordencoder-mapped-for-the-id-null

        auth.inMemoryAuthentication()
            .withUser(this.username)
            .password("{noop}" + this.password)
            .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure request authentication
        http.authorizeRequests()
            // Public
            .antMatchers(
                // Public resources
                "/",
                "/error/**",
                "/assets/**",
                // Swagger UI
                "/swagger-ui/**",
                // ReDoc Open API documentation viewer
                "/docs",
                // Open API specification
                this.openApiSpec,
                this.openApiSpec + "/**"
             ).permitAll()
            // Restrict access to actuator to administrators only
            .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ADMIN")
            // Secure any other path
            .anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.csrf().disable();

        // Support basic authentication
        http.httpBasic().realmName("rating-service");
    }

}