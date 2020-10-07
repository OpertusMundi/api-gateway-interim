# Message Service

## Quickstart

### Build

Copy configuration examples from `config-examples/` into `/src/main/resources/config/`, and edit to adjust to your needs.

`cp config-example/* /src/main/resources/config/`

Build the project:

`mvn clean package`

### Run as standalone JAR

Run application (with an embedded Tomcat 8.x server):

`java -jar target/opertus-mundi-message-service-1.0.0.jar`

### Run as WAR on a servlet container

Normally a WAR archive can be deployed at any servlet container. The following is only tested on a Tomcat 8.x.

Open `pom.xml` and change packaging type to `war`, in order to produce a WAR archive.

Ensure that the following section is uncommented (to avoid packaging an embedded server):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>    
```

Rebuild, and deploy generated `target/opertus-mundi-message-service-1.0.0.war` on a Tomcat 8.x servlet container.

### Run service using Docker

```
docker run  --name message-service -d \
            -p 8110:8110 \
            --volume "$(pwd)/.secrets/db-password:/etc/secrets/db-password" \
            --volume "$(pwd)/.secrets/jwt-signing-key:/etc/secrets/jwt-signing-key" \
            -e DB_HOST=db-host -e DB_USERNAME=username \
            -e ZIPKIN_HOST=zipkin-host \
            -e PROFILE=production \
            local/opertus-mundi-message-service:1.0.0
```