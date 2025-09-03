# LMS Backend (Spring Boot)

This repository contains the backend application for the University Course Management System, developed using Spring Boot. It provides a robust and secure RESTful API to manage academic courses, student registrations, grades, and user authentication with role-based access control.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup and Local Execution](#setup-and-local-execution)
  - [MySQL Database Setup (Docker)](#mysql-database-setup-docker)
  - [Application Configuration](#application-configuration)
  - [Running the Backend](#running-the-backend)
- [API Endpoints](#api-endpoints)
- [Authentication and Authorization](#authentication-and-authorization)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

## Features

-   **User Authentication & Authorization**: Secure login and registration with JWT (JSON Web Token) and Role-Based Access Control (RBAC) for `STUDENT`, `INSTRUCTOR`, and `ADMIN` roles.
-   **Course Management**: CRUD operations for academic courses (create, retrieve, update, delete).
-   **Student Management**: CRUD operations for student profiles.
-   **Instructor Management**: CRUD operations for instructor profiles.
-   **Enrollment Management**: Functionality to enroll students in courses and manage enrollments.
-   **Grade Management**: Features to assign, view, and update grades for enrolled courses.
-   **Data Validation**: Robust input validation using `jakarta.validation.constraints` on DTOs.
-   **Exception Handling**: Custom exception handling for resources not found or invalid operations.

## Technology Stack

-   **Framework**: Spring Boot 3.x
-   **Language**: Java 17
-   **Build Tool**: Maven
-   **ORM**: Spring Data JPA with Hibernate
-   **Database**: MySQL
-   **Security**: Spring Security, JJWT (Java JWT)
-   **Utilities**: Lombok (for boilerplate code reduction)
-   **Testing**: Spring Boot Starter Test, Postman/Insomnia

## Project Structure

The project follows a standard Spring Boot application structure, with logical grouping of components:
