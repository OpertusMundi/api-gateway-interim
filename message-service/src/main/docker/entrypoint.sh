#!/bin/bash -x 

JAVA_OPTS="-Djava.security.egd=file:///dev/urandom"

if [ ! -f "${DB_PASSWORD_FILE}" ]; then
    echo "A password file (for the database user) must be specified as DB_PASSWORD_FILE" 2>&1
    exit 1;
fi

if [ ! -f "${JWT_SIGNING_KEY_FILE}" ]; then
    echo "A signing key must be specified as JWT_SIGNING_KEY_FILE" 2>&1
    exit 1;
fi

# Generate properties file, overriding specific properties defined inside JAR

profile_config_file="config/application-${PROFILE}.properties"

cat >${profile_config_file} <<EOD
server.address=0.0.0.0
server.port=8110
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=$(cat ${DB_PASSWORD_FILE})

opertus-mundi.security.jwt.secret=$(cat ${JWT_SIGNING_KEY_FILE})

spring.zipkin.base-url=http://${ZIPKIN_HOST}:${ZIPKIN_PORT}
EOD

# Execute

exec java "${JAVA_OPTS}" "-Dspring.profiles.active=${PROFILE}" -jar message-service.jar 