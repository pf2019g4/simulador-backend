# Simulador-backend 
Por ahora utiliza una BD en memoria.
  Para Conectarse: http://localhost:8080/h2-console
  Cambiar donde dice JDBC URL y poner -> jdbc:h2:mem:testdb
  USER: sa
  PASS: password

## Requerimientos
- Maven > 3.1
- Java 11


## Links útiles

## Para compilar 
mvn clean install

## Para levantar
mvn spring-boot:run

## autenticacion

## Url
GET /api/estado/actual
POST /api/estado  

## Pasaje a Prod
Quitar annotation @crossOrigin del Controller
