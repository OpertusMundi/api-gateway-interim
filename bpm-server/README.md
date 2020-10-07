# OpertusMundi BPM Server

OpertusMundi Business Process Management Server. This service integrates [Camunda BPM Engine](https://docs.camunda.org/manual/latest/user-guide/spring-boot-integration/) into a Spring Boot Application.

## Quickstart

Copy configuration example files from `config-example/` into `src/main/resources/`, and edit to adjust to your needs.

`cp -r config-example/* src/main/resources/`

### Database configuration

Update database connection properties for each profile configuration file.

* application-development.properties
* application-production.properties

```properties
#
# Data source
#

spring.datasource.url=jdbc:postgresql://localhost:5432/camunda
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
```

* application-testing.properties

```properties
#
# Data source
#

spring.datasource.url=jdbc:postgresql://localhost:5432/camunda-test
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driver-class-name=org.postgresql.Driver
```

### Security configuration

Configure an administrator account for Camunda [Web Applications](https://docs.camunda.org/manual/latest/webapps/) and [REST API](https://docs.camunda.org/manual/latest/reference/rest/).

```properties
camunda.bpm.admin-user.id=
camunda.bpm.admin-user.password=
camunda.bpm.admin-user.firstName=
camunda.bpm.admin-user.lastName=
```

### Build

Build the project:

`mvn clean package`

### Run as standalone JAR

Run application (with an embedded Tomcat 9.x server) as a standalone application:

`java -jar target/opertus-mundi-bpm-server-1.0.0.jar`

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

Rebuild, and deploy generated `target/opertus-mundi-bpm-server-1.0.0.war` on a Tomcat 9.x servlet container.

## Using the Service

Once started, the Web Applications are accessible at `http://localhost:8000/`. By default the new account is granted access only to the [admin](http://localhost:8000/camunda/app/admin/) application. Access to `Cockpit` and `Tasklist` applications must be granted manually.

The REST API is published at the endpoint `http://localhost:8000/engine-rest/`. The endpoint is secured using Basic Authentication.

## Using the API

In order to test the API, at least one BPM worker service must be running.

### View deployed processes

[Docs](https://docs.camunda.org/manual/latest/reference/rest/process-definition/get-query/)

```bash
curl --location --request GET "http://localhost:8000/engine-rest/process-definition?firstResult=0&maxResults=10" \
	 --header "Authorization: Basic $(echo -n username:password | base64)"
```

### Start new process instance

[Docs](https://docs.camunda.org/manual/latest/reference/rest/process-definition/post-start-process-instance/)

Start a new instance for process `Process_Hello_ServiceTask_External_1` with business key equal to `example-1`.

```bash
curl --location --request POST 'http://localhost:8000/engine-rest/process-definition/key/Process_Hello_ServiceTask_External_1/start' \
	 --header "Authorization: Basic $(echo -n username:password | base64)" \
	 --header 'Content-Type: application/json' \
	 --data-raw '{
		"variables": {
        "foo1": {
            "value": "Test",
            "type": "String"
        },
        "name1": {
            "value": "Name",
            "type": "String"
        }
	},
    "businessKey": "example-1"
	}'
```

### Find pending tasks for an instance

[Docs](https://docs.camunda.org/manual/latest/reference/rest/task/post-query/)

```bash
curl --location --request POST 'http://localhost:8000/engine-rest/task' \
	 --header "Authorization: Basic $(echo -n username:password | base64)" \
	 --header 'Content-Type: application/json' \
	 --data-raw '{
		"processInstanceBusinessKey": "example-1"
	 }'
```

### Complete a user task

[Docs](https://docs.camunda.org/manual/latest/reference/rest/task/post-complete/)

```bash
curl --location --request POST 'http://localhost:8000/engine-rest/task/179fb092-03fe-11eb-b319-ba514251716d/complete' \
	 --header "Authorization: Basic $(echo -n username:password | base64)" \
	 --header 'Content-Type: application/json' \
	 --data-raw '{
		"variables": {
			"userResponse": {
				"value": "Accept",
				"type": "String"
			}
		},
		"withVariablesInReturn": true
	}'
```
