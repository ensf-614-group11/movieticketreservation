
# AcmePlex Movie Ticket Reservation System

Welcome to the AcmePlex Movie Ticket Reservation System! This project is a system to provide movie theater ticket reservations. The application allows users to search for movies, select theaters and showtimes, choose seats, and complete ticket purchases online.

## Table of Contents
1. [Project Overview](#project-overview)
2. [Prerequisites](#prerequisites)
3. [Setup Instructions](#setup-instructions)
   - [Windows](#setup-on-windows)
   - [macOS](#setup-on-macos)
4. [Starting the Application](#starting-the-application)
5. [Shutting Down the Application](#shutting-down-the-application)
6. [Introduction to Spring and Maven](#introduction-to-spring-and-maven)
7. [Troubleshooting](#troubleshooting)

---

## Project Overview

This system is developed in **Java** using **Spring Boot** and **Maven** as a build tool. The application is configured to run on **localhost:8000**. It includes basic authentication for secure access and an embedded server (Tomcat) to handle requests.

## Prerequisites

Before starting, ensure you have the following software installed:
- **Java Development Kit (JDK) 17**: [Download JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Git**: [Download Git](https://git-scm.com/downloads)
- **Maven** (optional): We use Maven Wrapper, so installation is not mandatory.

## Setup Instructions

### 1. Clone the Repository

Open a terminal (Command Prompt, PowerShell, or Terminal on macOS) and clone the repository:

```bash
git clone https://github.com/ensf-614-group11/AcmePlex.git
```

### 2. Navigate to the Project Directory

```bash
cd AcmePlex
```

### Setup on Windows

1. **Check Java Installation**:
   - Open Command Prompt or PowerShell.
   - Run:
     ```powershell
     java -version
     ```
   - Ensure it shows JDK 17.

2. **Run the Application with Maven Wrapper**:
   - Run the following command in the project directory:
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```

### Setup on macOS

1. **Check Java Installation**:
   - Open Terminal.
   - Run:
     ```bash
     java -version
     ```
   - Ensure it shows JDK 17.

2. **Run the Application with Maven Wrapper**:
  - Run this command to install dependencies. You should only need to do this once, unless any dependencies change
     ```bash
     ./mvnw install
     ```

   - Run the following command in the project directory:
     ```bash
     ./mvnw spring-boot:run
     ```

## Starting the Application

After following the setup instructions, the application should start up at **http://localhost:8000**.

- You’ll see logs showing the application startup.
- Once started, you can access any REST endpoints at `http://localhost:8000`.

## Shutting Down the Application

To stop the application:
- Press **Ctrl + C** in the terminal where it’s running.
  - If this does not work, refer to the **Troubleshooting** section.

## Logging Configuration

This project uses SLF4J with Logback for logging. Logs are output to both the console and a file (\`logs/app.log\`) in the project root. Here’s a quick setup guide:

### Configure Logging Levels

In \`src/main/resources/application.properties\`:

```properties
logging.level.root=INFO
logging.level.org.springframework=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
logging.level.ensfgroup11.acmeplex=DEBUG
logging.file.name=logs/app.log
```

### Using the Logger

In your Java classes, use the logger as follows:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleService {
private static final Logger logger = LoggerFactory.getLogger(ExampleService.class);

    public void exampleMethod() {
        logger.info("Starting example method");
        try {
            // Business logic here
            logger.debug("Executing logic...");
        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
    }
}
```

## Introduction to Spring and Maven

### Spring Boot
**Spring Boot** is a powerful framework that simplifies building Java-based applications. Spring Boot allows you to create standalone, production-grade applications with minimal configuration. It includes built-in support for embedded web servers like Tomcat, Jetty, and Undertow, which makes running and testing web applications simple and straightforward.

Spring Boot applications rely on a wide range of built-in configurations and allow dependency injection to manage and assemble components, making code modular and easy to maintain. In this project, Spring Boot manages the server and provides REST API capabilities, authentication, and more.

### Maven
**Maven** is a project management and build tool for Java applications. Maven handles dependencies, builds, and other project tasks through a central configuration file called `pom.xml`. This project uses Maven to manage dependencies like Spring Boot and database drivers, to compile code, run tests, and package the application into a deployable `.jar` file.

The **Maven Wrapper** (`mvnw` and `mvnw.cmd` files) is included to simplify setup. Using the wrapper ensures consistency across different environments without requiring Maven to be installed globally. Commands like `./mvnw spring-boot:run` (macOS) or `mvnw.cmd spring-boot:run` (Windows) allow you to build and run the application without additional configuration.

## Troubleshooting

### Application Won't Start Due to Missing Database Configuration
If you see errors about missing database configurations, ensure `application.properties` has the following line:
```properties
spring.datasource.url=none
```

### Unable to Stop the Application
If `Ctrl + C` does not stop the application:
1. **Windows**:
   - Find the process ID:
     ```powershell
     netstat -ano | findstr :8000
     ```
   - Stop the process:
     ```powershell
     taskkill /PID <PID> /F
     ```

2. **macOS**:
   - Find the process ID:
     ```bash
     lsof -i :8000
     ```
   - Kill the process:
     ```bash
     kill -9 <PID>
     ```

---

We hope this guide helps you get started with AcmePlex. If you run into issues, please reach out to the team for support!
