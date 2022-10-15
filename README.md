# Challenge
IONIX Challenge

## environment & requirements

### requirements

* **Java JDK 17**.

Internet connection for downloading dependencies

## Build

### Maven

In order to generate artifact and run unit tests, just run:

```text
./mvn clean package
```

to deploy in local mode:

```text
./mvn spring-boot:run
```

Open-API & Swagger-ui are configured in this project. You can check it out from the root route:

```text
localhost:8080
```

Some methods are secured with basic auth. you can check for credentials on application.properties.

