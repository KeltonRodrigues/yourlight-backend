\# YourLight - Backend API



Backend REST API for a digital library platform built with Spring Boot.



---



\## Overview



YourLight is a backend application designed to manage users and books in a digital library environment.  

It implements authentication using JWT and follows a layered architecture pattern.



---



\## Technologies



\- Java 17

\- Spring Boot

\- Spring Security

\- JWT Authentication

\- MySQL

\- Maven



---



\## Architecture



The project follows a layered architecture:



\- \*\*Controller\*\* → Handles HTTP requests  

\- \*\*Service\*\* → Business logic  

\- \*\*Repository\*\* → Database access  

\- \*\*Security\*\* → JWT authentication and authorization  



---



\## Features



\- User registration

\- User authentication (JWT)

\- Protected endpoints

\- Database integration with MySQL

\- Exception handling structure



---



\## How to Run



1\. Clone the repository  

2\. Configure your database credentials in `application.properties`  

3\. Run the project using:



```bash

mvn spring-boot:run

```





Author



Kelton Rodrigues

