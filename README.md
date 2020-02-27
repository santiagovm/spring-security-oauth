# ps-spring-07

This repo contains some sample projects to demonstrate OAuth2 using Spring

There are four components:

- **Authorization Server:** Spring Boot application that issues access tokens. Normally you would not
implement such an application. Instead, you would use a service like Pivotal SSO, Okta, Auth0, etc.

- **Resource Server:** Simple REST API using Spring Boot and accessible only if valid access token provided. 
This app checks the access token provided by the caller by issuing a call to Authorization Server and validate the token.

- **Client Password Flow:** Angular application that captures username and password from user 
and sends request to Authorization Server to obtain access token. The application then uses access token to make
calls to Resource Server.

- **Client Implicit Flow:** Angular application that redirects user to a login page at the Authorization Server, 
where user enters credentials. Authorization Server then redirects to client app with issued access token. 
At that point the client makes calls to the Resource Server. In this flow the client application has not knowledge of
the user credentials, while the Password flow exposes the client credentials to the client app.

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

## Resource Server

run resource server jar file

```bash
java -jar resource-server/target/resource-server.jar
```

## Client App (password flow)

run client app

```bash
cd client-password-flow/src/main/resources
yarn start
```

## Client App (implicit flow)

run client app

```bash
cd client-implicit-flow/src/main/resources
yarn start
```

## Client Endpoints and Credentials

http://localhost:8085 (password flow)
http://localhost:8086 (implicit flow)

credentials: 

- john / 123
- tom / 111

## Tokens Endpoint

List tokens

GET http://localhost:8081/spring-security-oauth-server/tokens

Create a token

authorization header is base64(fooClientIdPassword, secret)

```bash
curl --location --request POST 'http://localhost:8081/spring-security-oauth-server/oauth/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--header 'Authorization: Basic Zm9vQ2xpZW50SWRQYXNzd29yZDpzZWNyZXQ=' \
--data-urlencode 'grant_type=password' \
--data-urlencode 'client_id=fooClientIdPassword' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=nimda'
```

### next steps

- pivotal spring patterns (https://github.com/pivotal/spring-patterns)
- testing (https://www.baeldung.com/oauth-api-testing-with-spring-mvc)
- convert to jwt (https://www.baeldung.com/spring-security-oauth-jwt)
- verify claims (https://www.baeldung.com/spring-security-oauth-2-verify-claims)
- openid connect (https://www.baeldung.com/spring-security-openid-connect)
- refresh tokens (https://www.baeldung.com/spring-security-oauth2-remember-me)
- logout (https://www.baeldung.com/logout-spring-security-oauth)

### reference

- https://www.baeldung.com/rest-api-spring-oauth2-angular
- https://github.com/Baeldung/spring-security-oauth
- https://www.baeldung.com/spring-security-oauth2-enable-resource-server-vs-enable-oauth2-sso
