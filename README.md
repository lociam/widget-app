# Description

Welcome to `WidgetApp`. This is a simple Spring boot aplication
that allows you to Add / Modify / Get / Delete widgets via a REST
api that will be explained later in this document.

This project was created using the spring boot helper. That created
the basic bootstrap.

Springfox was added as a dependency to add a swagger documentation.

## How to run

You need Java 11 in order to run the web server.

To run this project you need to run:

`./mvnw spring-boot:run`

It will start the web server in the port `8080`.

After running it, you can find the documentation at:

`http://localhost:8080/swagger-ui/
`

## Project structure

```
- widget-app
 - src
  - main
   - java
    - com.miro.widgetapp
     - config
      SwaggerConfig - Swagger generator config
     - controller - Controllers classes and helpers
      ExceptionAdvice - Tranformation from pre defined exceptions to responses
      InputWidgetRequest - External model used for insert and update operations
      WidgetController - Main controller class containing all the defined endpoints
     - exceptions - personalized exceptions
     - models - widget app models
     - repositories - DB related classes
     WidgetApplication - Main app class
  - test - Test classes   
```