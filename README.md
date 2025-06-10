# Expenses Tracker API

A backend RESTful API for an expenses tracking application, built with Spring Boot.

## Features

- User authentication and authorization using Spring Security with JWT
- User management with JPA and PostgreSQL
- CRUD operations for managing expenses

## Tech Stack

- Spring Boot  
- Spring Security with JWT for authentication  
- Spring Data JPA for ORM and database interactions  
- PostgreSQL as the database  
- Maven for build and dependency management  

Check out the live app here: https://expenses-tracker-client-ten.vercel.app (Initial request may take some time to be processed due to the hosting service taking time to spin up the server)


### Prerequisites

- Java 17+  
- Maven  
- PostgreSQL database  

### Configuration

The application expects the following environment variables for the database connection:

```bash
DB_URL=jdbc:postgresql://your-host:5432/your-db?sslmode=require
DB_USERNAME=your-username
DB_PASSWORD=your-password
