# LMS Backend (Spring Boot)

This repository hosts the Spring Boot backend for the University Course Management System. It provides a secure, RESTful API for managing courses, users, enrollments, and grades, complete with JWT-based authentication and role-based access control.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Run Application](#run-application)
- [Deployment](#deployment)
- [License](#license)

## Features

-   **User Management**: Secure registration, login, and role-based access for Students, Instructors, and Admins.
-   **Course Management**: Full CRUD operations for academic courses.
-   **Student & Instructor Profiles**: CRUD operations for student and instructor details.
-   **Enrollment & Grade Management**: Functionality for course enrollment and grade assignment/tracking.
-   **Data Integrity**: Robust validation and custom exception handling.

## Technology Stack

-   **Backend**: Spring Boot 3.x, Java 17, Maven, Spring Data JPA (Hibernate), MySQL.
-   **Security**: Spring Security, JJWT (JSON Web Token).
-   **Utilities**: Lombok.
-   **Testing**: Postman/Insomnia.


## Prerequisites

-   Java Development Kit (JDK) 17+
-   Apache Maven 3.6+
-   MySQL Server (or Docker)
-   Git

## Run Application

-   **Build**: mvn clean install
-   **Run**: mvn spring-boot:run
  The API will be available at http://localhost:8080.

## Deployment
he backend is deployed on Railway.

-   **Process**: Integrated with GitHub, main branch pushes automatically trigger build (mvn clean install) and deployment (mvn spring-boot:run).
-   **Production URL**: [mvn spring-boot:run
](https://lms-back-end-production-9099.up.railway.app

## License
This project is licensed under the MIT License.
