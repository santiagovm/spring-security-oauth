
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

## Client App

run client app

```bash
cd client-password-flow/src/main/resources
yarn start
```

## Run Test

http://localhost:4200

credentials: john / 123 [USER]

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
