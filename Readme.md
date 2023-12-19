# Subscription Management System

This is a Spring Boot project for a Subscription Management System. It provides RESTful API endpoints for creating, retrieving, updating, and deleting subscriptions. The system uses MongoDB for data storage.

## Sequence Diagram
![Alt Text](outputViews\images\Subscription Flow Sequence Diagram.png)

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Endpoints](#endpoints)
- [Security](#security)
- [Exception Handling](#exception-handling)
- [Configuration](#configuration)
- [Dependencies](#dependencies)
- [Build and Run](#build-and-run)

## Prerequisites

Make sure you have the following installed on your machine:

- Java 21
- MongoDB
- Maven

## Getting Started

1. **Clone the repository:**

   git clone https://github.com/vishnu-vardhan-raju/Sub.git
   cd Sub
2. **Update MongoDB configuration:**

    Open src/main/resources/application.properties and update the MongoDB connection details if needed.

3. **Build the project:**
    mvn clean install

4. **Run the application:**
    mvn spring-boot:run
    The application will start on http://localhost:8081.



**Endpoints**
    POST /api/subscription: Create new subscriptions.
    GET /api/subscription: Retrieve subscriptions based on filter parameters.
    PATCH /api/subscription: Update existing subscriptions.
    DELETE /api/subscription: Delete subscriptions based on IDs.
    Refer to the Controller for detailed information on the API endpoints.

**Security**
    The project includes basic authentication using Spring Security. Two user roles are available: USER and ADMIN.

    USER credentials:
    Username: vishnu
    Password: vish

    ADMIN credentials:
    Username: vishnuvardhan
    Password: vishnu
    Update the security configuration in EntityAuthentication if needed.

**Exception Handling**
    The project includes a global exception handler that deals with validation errors and other exceptions. Validation errors are returned as a list of error messages. Custom error messages are provided for specific scenarios.

    Refer to the Exception Handler for more details.

**Configuration**
    MongoDB Configuration: Update the MongoDB connection details in src/main/resources/application.properties.

    Security Configuration: Update user details and roles in EntityAuthentication.

    Auditing Configuration: Configure auditing settings in AuditingConfig.

**Dependencies**
    Spring Boot
    Spring Data MongoDB
    Spring Security
    Lombok
    Project Lombok
    Spring Boot DevTools
    Spring Boot Starter Validation
    Spring Boot Starter Web

**Build and Run**
    Build the project using Maven:

     mvn clean install
     
**Run the application:**

    mvn spring-boot:run

    The application will start, and you can access the API at http://localhost:8081.