# Developer Setup Guide

This guide provides comprehensive instructions for setting up the environment required to develop and run the AcmePlex movie ticket reservation application on both Windows and macOS.

## What is the Spring Framework?

The **Spring Framework** is a powerful and feature-rich framework for building Java applications. It simplifies the development of complex enterprise applications by providing a robust infrastructure and set of tools. Here are some key aspects of Spring:

- **Inversion of Control (IoC)**: Spring promotes loose coupling through IoC, allowing developers to define dependencies externally. This makes the code more flexible and easier to test. In traditional programming, the flow of control is dictated by the program itself. For example, a main method may directly create and invoke instances of classes, tightly coupling the components. In contrast, IoC reverses this control flow. Instead of the application controlling the creation and management of its dependencies, an external entity (like a framework or container) takes over this responsibility.

- **Dependency Injection (DI)**: A specific implementation of IoC, DI enables Spring to manage and inject dependencies into objects, which helps in reducing boilerplate code.

- **Aspect-Oriented Programming (AOP)**: Spring AOP allows for the separation of cross-cutting concerns like logging and security from business logic, leading to cleaner and more maintainable code.

- **Modularity**: The Spring Framework is modular, meaning developers can pick and choose components as needed, such as Spring MVC for web applications, Spring Data for data access, and Spring Security for authentication and authorization.

- **Rich Ecosystem**: Spring includes various projects and tools like Spring Boot (for rapid application development), Spring Cloud (for cloud-native applications), and Spring Data (for simplifying data access), making it a versatile choice for Java developers.

## Prerequisites

### 1. Install Required Software
Ensure the following software is installed on your machine:

- **Java Development Kit (JDK) 17**
    - [Download JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
    - Follow the installation instructions provided by Oracle.
    - **Setting JAVA_HOME**:
        - **Windows**: Go to System Properties > Environment Variables and add a new system variable `JAVA_HOME` pointing to the JDK installation path (e.g., `C:\Program Files\Java\jdk-17`).
        - **macOS**: Open Terminal and add `export JAVA_HOME=$(/usr/libexec/java_home -v 17)` to your shell profile file (`~/.bash_profile` or `~/.zshrc`). Reload the profile with `source ~/.bash_profile` or `source ~/.zshrc`.

- **Git**
    - [Download Git](https://git-scm.com/downloads)
    - After installation, verify by running `git --version` in your terminal.

- **MySQL**
    - CONFIGURATION DETAILS WILL BE PROVIDED LATER

- **H2 Database**: By default, the project is configured to use H2, an in-memory database ideal for development and testing.

    - **What is H2?**
        - The **H2 Database** is a lightweight, open-source relational database system that runs entirely in memory. It’s quick and easy to set up, making it ideal for development and testing.
        - **Key Features**:
            - **In-Memory Mode**: Stores data in memory and resets it upon application restart, providing a clean database state for each test.
            - **Embedded Mode**: Runs directly within the application, requiring no external setup.
            - **SQL Compatibility**: Supports SQL syntax, allowing easy transition to other databases later.
        - **Accessing the H2 Console**:
            - H2 includes a web console for SQL command execution and data inspection. When enabled in `application.properties`, access it at `http://localhost:8000/h2-console`.

- **Maven** (optional): The project uses Maven Wrapper (`mvnw`), so Maven installation is not mandatory.

### 2. Clone the Repository
Open a terminal (Command Prompt/PowerShell on Windows or Terminal on macOS) and clone the repository:

```bash
git clone https://github.com/ensf-614-group11/AcmePlex.git
```

Navigate to the project directory:

```bash
cd AcmePlex
```

## Setting Up Environment

### Windows Setup

1. **Check Java Installation**
    - Open Command Prompt or PowerShell.
    - Verify the Java version with:
      ```powershell
      java -version
      ```
    - Ensure it shows JDK 17. If not, verify the `JAVA_HOME` variable in Environment Variables points to JDK 17.

2. **Build and Run the Application**
    - Use the Maven Wrapper to build and run the application:
      ```powershell
      .\mvnw.cmd clean install
      .\mvnw.cmd spring-boot:run
      ```

3. **Verify Environment Variables**
    - Run the following to confirm `JAVA_HOME` is set correctly:
      ```powershell
      echo %JAVA_HOME%
      ```
    - It should output the path to your JDK 17 directory.

### macOS Setup

1. **Check Java Installation**
    - Open Terminal and run:
      ```bash
      java -version
      ```
    - Ensure it shows JDK 17. If it’s not set, follow the instructions in Prerequisites to configure `JAVA_HOME`.

2. **Build and Run the Application**
    - Use the Maven Wrapper to build and run the application:
      ```bash
      ./mvnw clean install
      ./mvnw spring-boot:run
      ```

      When to Use `./mvnw clean install`:
        - **First Build**: When setting up the project for the first time, running `./mvnw clean install` will compile the code, run tests, and package the application into a JAR file. This step ensures that all dependencies are resolved and the application is built correctly.
        - **Code Changes**: If you have made changes to the code (especially in your Java files), it is a good practice to run this command to ensure that the latest changes are compiled and included in the build.
        - **Dependencies Update**: If there have been changes to the `pom.xml` file (like adding or updating dependencies), running `clean install` will download the necessary dependencies and compile the application accordingly.
        - **Clean State**: If you are encountering issues and suspect that there are stale classes or resources, running `clean install` will delete the `target` directory and start fresh, ensuring that only the latest changes are built.

      When You Can Skip It:
        - **Just Starting the Server**: If no code changes have been made since the last successful build, you can simply run `./mvnw spring-boot:run` to start the application. This command will use the existing compiled classes in the `target` directory.
        - **Incremental Changes**: If you are making small changes and are confident that they don’t require a full recompilation (like changes to static files, configurations, or non-Java resources), you can skip the `clean install`.

3. **Verify Environment Variables**
    - Confirm `JAVA_HOME` is set correctly with:
      ```bash
      echo $JAVA_HOME
      ```
    - If not, add `export JAVA_HOME=$(/usr/libexec/java_home -v 17)` to your profile and reload.

## Testing Environment Setup

To ensure the environment is set up correctly:

1. **Run Tests**
    - Execute unit and integration tests with:
      ```bash
      ./mvnw test
      ```
    - All tests should pass. If any tests fail, review the error logs for troubleshooting.

2. **Verify Application on `localhost:8000`**
    - After running the application, open a browser and go to [http://localhost:8000](http://localhost:8000).
    - The home page should load, confirming the application is working as expected.

3. **Database Configuration (IGNORE FOR NOW)**
    - By default, the project uses an H2 in-memory database. **I WILL CONFIGURE SQL LATER, IGNORE THE INSTRUCTIONS ON HOW TO CONNECT TO MYSQL FOR NOW....** To connect to MySQL, edit `src/main/resources/application.properties` and add:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/acmeplex
    spring.datasource.username=<your_username>
    spring.datasource.password=<your_password>
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    ```

   Replace `<your_username>` and `<your_password>` with your MySQL credentials. Create a database in MySQL named `acmeplex`:

    ```sql
    CREATE DATABASE acmeplex;
    ```

## Troubleshooting

- **Java Version Issues**: If you see an error about Java, confirm that `JAVA_HOME` points to JDK 17 and restart your terminal.
- **Maven Wrapper Issues**: Always use `./mvnw` (macOS) or `.\mvnw.cmd` (Windows) instead of `mvn` directly.
- **Port Conflicts**: If port `8000` is in use, either stop the conflicting service or modify the port in `application.properties`:
  ```properties
  server.port=8080
  ```

## Summary

- Clone the repository and install required software.
- Verify environment variables and Java installation.
- Build and run the project with Maven Wrapper.
- Access the application at [http://localhost:8000](http://localhost:8000) and verify functionality.
