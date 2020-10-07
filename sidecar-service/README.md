# OpertusMundi Sidecar Service

A simple proxy service using Netflix Zuul.

## Quickstart

Copy configuration example files from `config-example/` into `src/main/resources/`, and edit to adjust to your needs.

`cp -r config-example/* src/main/resources/`


### Database configuration

Set database connection properties for each profile configuration file:

* application-development.properties
* application-production.properties
* application-testing.properties

```properties
#
# Data source
#

spring.datasource.url = jdbc:postgresql://localhost:5432/opertus-mundi
spring.datasource.username = username
spring.datasource.password = password
spring.datasource.driver-class-name = org.postgresql.Driver

spring.datasource.hikari.connectionTimeout = 30000
spring.datasource.hikari.idleTimeout = 600000
spring.datasource.hikari.maxLifetime = 1800000
spring.datasource.hikari.maximumPoolSize = 15
```

### Proxy configuration

Configure one or more Zuul routes. In the next example, all catalogue requests are forwarded to `http://localhost:8085/`. More details on Zuul router configuration can be found [here](https://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html).

```properties
#
# Spring Cloud Zuul Routing
#

zuul.routes.catalogue.path=/api/catalogue/**
zuul.routes.catalogue.url=http://localhost:8085/api/catalogue
```

### Build

Build the project:

`mvn clean package`

### Run as standalone JAR

Run application (with an embedded Tomcat 9.x server) as a standalone application:

`java -jar target/opertus-mundi-sidecar-service-1.0.0.jar`

or using the Spring Boot plugin:

`mvn spring-boot:run`

### Run as WAR on a servlet container

Normally a WAR archive can be deployed at any servlet container. The following is only tested on a Tomcat 9.x.

Open `pom.xml` and change packaging type to `war`, in order to produce a WAR archive.

Ensure that the following section is not commented (to avoid packaging an embedded server):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-tomcat</artifactId>
    <scope>provided</scope>
</dependency>    
```

Rebuild, and deploy generated `target/opertus-mundi-sidecar-service-1.0.0.war` on a Tomcat 9.x servlet container.
