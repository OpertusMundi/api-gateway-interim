FROM openjdk:8-slim

# Set working directory
RUN mkdir -p /usr/local/opertusmundi/message-service
WORKDIR /usr/local/opertusmundi/message-service

# Expose default service port
EXPOSE 8110

# Spring Boot active profile
ENV PROFILE="development"

# Database configuration
ENV DB_HOST="localhost" DB_PORT="5432" DB_NAME="opertus-mundi" DB_USERNAME="dev" DB_PASSWORD_FILE="/etc/secrets/db-password"

# JWT signing key
ENV JWT_SIGNING_KEY_FILE="/etc/secrets/jwt-signing-key"

# Zipkin server configuration
ENV ZIPKIN_HOST="localhost" ZIPKIN_PORT="9411"

# Persist log files outside of the container
VOLUME ["/usr/local/opertusmundi/message-service/logs"]

# Create configuration file
RUN mkdir config && touch config/application.properties

# Configure executables
COPY message-service.jar entrypoint.sh ./
RUN chmod +x entrypoint.sh

CMD ["./entrypoint.sh"]