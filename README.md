

## Build

build all projects and run tests

```bash
./mvnw clean install
```

## Authorization Server

run authorization server jar file

```bash
java -jar authorization-server/target/authorization-server.jar
```

### Login Page

- http://localhost:8081/spring-security-oauth-server
- http://localhost:8081/spring-security-oauth-server/login

credentials:

- john / 123 [USER]

### Employee Endpoint

GET http://localhost:8081/spring-security-oauth-server/employee

### Tokens Endpoint

List tokens

GET http://localhost:8081/spring-security-oauth-server/tokens

Create a token

POST http://localhost:8081/spring-security-oauth-server/oauth/token


params.add("grant_type", "password");
params.add("client_id", CLIENT_ID);
params.add("username", username);


                        .params(params)
                        .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                        .accept(CONTENT_TYPE_JSON)
