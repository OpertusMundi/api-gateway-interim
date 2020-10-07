package eu.opertusmundi.email.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import eu.opertusmundi.email.security.JwtAuthorizationFilter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${springdoc.api-docs.path}")
    private String openApiSpec;

    @Value("${opertus-mundi.security.key-store.path}")
    private String trustedStorePath;

    @Value("${opertus-mundi.security.key-store.password}")
    private String turstedStorePassword;

    /**
     * Returns the authentication manager currently used by Spring. It represents a
     * bean definition with the aim allow wiring from other classes performing the
     * Inversion of Control (IoC).
     *
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // See: https://stackoverflow.com/questions/49654143/spring-security-5-there-is-no-passwordencoder-mapped-for-the-id-null

        auth.inMemoryAuthentication();
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

        // Disable basic authentication
        http.httpBasic().disable();

        // Support JWT authentication
        http.addFilterAfter(
            new JwtAuthorizationFilter(
                this.authenticationManager(), this.trustedStorePath, this.turstedStorePassword
            ),
            BasicAuthenticationFilter.class
        );
    }

}