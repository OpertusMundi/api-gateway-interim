package eu.opertusmundi.email.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * Authorization filter for custom JWT tokens
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String KEY_ALIAS    = "email-service";

    private final String trustedStorePath;

    private final String turstedStorePassword;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, String trustedStorePath, String turstedStorePassword) {
        super(authenticationManager);

        this.trustedStorePath     = trustedStorePath;
        this.turstedStorePassword = turstedStorePassword;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws IOException, ServletException {
        final UsernamePasswordAuthenticationToken authentication = this.getAuthentication(request);

        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        final String header = request.getHeader(TOKEN_HEADER);

        if (!StringUtils.isBlank(header) && header.startsWith(TOKEN_PREFIX)) {
            try {
                final String token = header.replace("Bearer ", "");

                final PublicKey key = this.getPublicKey(KEY_ALIAS);

                final Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

                final String username = parsedToken
                    .getBody()
                    .getSubject();

                final List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                    .get("roles")).stream()
                    .map(authority -> new SimpleGrantedAuthority((String) authority))
                    .collect(Collectors.toList());

                if (!StringUtils.isBlank(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            } catch (final ExpiredJwtException exception) {
                logger.warn("Request to parse expired JWT : {} failed : {}", header, exception.getMessage());
            } catch (final UnsupportedJwtException exception) {
                logger.warn("Request to parse unsupported JWT : {} failed : {}", header, exception.getMessage());
            } catch (final MalformedJwtException exception) {
                logger.warn("Request to parse invalid JWT : {} failed : {}", header, exception.getMessage());
            } catch (final SignatureException exception) {
                logger.warn("Request to parse JWT with invalid signature : {} failed : {}", header, exception.getMessage());
            } catch (final IllegalArgumentException exception) {
                logger.warn("Request to parse empty or null JWT : {} failed : {}", header, exception.getMessage());
            } catch (final Exception ex) {
                logger.error("Failed to verify JWT token : {} failed : {}", header, ex.getMessage());
            }
        }

        return null;
    }

    private PublicKey getPublicKey(String alias) throws Exception {
        final DefaultResourceLoader loader   = new DefaultResourceLoader();
        final Resource              resource = loader.getResource(this.trustedStorePath);
        final KeyStore              keyStore = KeyStore.getInstance("PKCS12");
        final char[]                password = this.turstedStorePassword.toCharArray();

        try (InputStream in = resource.getInputStream()) {
            keyStore.load(in, password);
        }

        final Certificate emailCert = keyStore.getCertificate(alias);

        return emailCert.getPublicKey();
    }

}