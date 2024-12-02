
# AcmePlex Movie Ticket Reservation System

Welcome to the AcmePlex Movie Ticket Reservation System! This project is a system to provide movie theater ticket reservations. The application allows users to search for movies, select theaters and showtimes, choose seats, and complete ticket purchases.

---

## Table of Contents

1. [Project Overview](#project-overview)  
   - [Requirements](#requirements)  
     - [Functional Requirements](#functional-requirements)  
     - [Non-Functional Requirements](#non-functional-requirements)  
   - [Architecture](#architecture)  

2. [Instructions for Running `movieticketreservation.jar`](#instructions-for-running-movieticketreservationjar)  
   - [System Requirements](#system-requirements)  
   - [Setup MySQL Database for `movieticketreservation.jar`](#setup-mysql-database-for-movieticketreservationjar)  
   - [Setup React User Interface for `movieticketreservation.jar`](#setup-react-user-interface-for-movieticketreservationjar)  
   - [Run `movieticketreservation.jar`](#run-movieticketreservationjar)  

3. [Instructions for Setting Up a Development Environment for GitHub Repository](#instructions-for-setting-up-a-development-environment-for-github-repository)  
   - [System Requirements](#system-requirements)  
   - [Clone the Repository](#clone-the-repository)  
   - [Setup MySQL Database for Development](#setup-mysql-database-for-development)  
   - [Setup React Front-End](#setup-react-front-end)  
   - [Setup Maven Project](#setup-maven-project)  

4. [Shutting Down the Application](#shutting-down-the-application)  

5. [Troubleshooting](#troubleshooting)  
   - [Application Won't Start Due to Missing Database Configuration](#application-wont-start-due-to-missing-database-configuration)  
   - [Unable to Stop the Application](#unable-to-stop-the-application)  

---

## Project Overview

### Requirements
The AcmePlex movie theatre ticket reservation application incorporates the following functional, non-functional and technical requirements. 

#### Functional Requirements
- Ordinary users can navigate the application without creating an account. 
- Ordinary users have the capability to search for movies, select a theatre, view available movies and showtimes, view available seats for a selected movie, select seats, make payments with credit card, and receive a copy of ticket and receipt via email. 
- Registered users create an account by providing their name, address, and credit card information. 
- Registered users can log in to their account with a username and password. 
- Registered users must pay a $20.00 annual fee. 
- Registered users receive news about upcoming movies before public announcements. 
- 10% of the seats for new movies are available for registered users to reserve on a first come first serve basis before the public accountment is made. 
- Both ordinary and registered users can cancel tickets up to 72 hours prior to the showtime. 
- After cancelling tickets, ordinary users receive a credit for a future purchase worth 85% the value of the purchase (a 15% administration fee is applied) that is valid for 1 year. 
- After cancelling tickets, registered users receive a credit for a future purchase worth 100% of the value of the purchase (no administration fee) that is valid for 1 year.
  
#### Non-Functional Requirements 
- Scalability/Reliability/Maintainability: The system is designed with a layered architecture, enabling modularity and flexibility. Design patterns are implemented for scalability of the application. 
- Usability: The application user interface is intuitive and responsive. 
- Security: The system includes authentication for secure access to user data. 
Technical Requirements
- Frontend: React framework 
- Backend: Java Spring Boot 
- Database: MySQL 

### Architecture
The system has a layered architecture, consisting of presentation layer, control layer, business layer, and data source layer. The Model View Controller (MVC) Architectural Design Pattern was also used in the system to separate the data management, user interface and application logic.
The backend code in the system includes models for entities such as user, movie, notification, payment, receipt, seat, showtime, theatre, theatre room, credit, and ticket. These models specify the data that is included in the system or entered by the user and the business logic, or rules related to each of the data fields. 

The view elements consist of the components/pages included in the frontend code. These specify how the data is displayed to the user. Examples of the views include the home page, login/register page, showtimes page, available seats page, payment page, confirmation page, cancel tickets page, and the profile page. 

The controller elements are part of the backend code, and they manage inputs from the user and update the models and the views based on these inputs. The system includes controllers for user profile authentication, user profile management, making a payment, browsing/selecting movies and seats, cancelling tickets, and validating a credit. Each controller includes logic to deal with the relevant operations for the system, which include creating, reading, updating, or deleting objects of the model classes. The backend code also includes API routes, which provide the system connection to allow information to flow between the controller and the view.

The MVC design pattern results in modularity and allows each element to be modified independently of the other elements in the system. This enhances the maintainability and scalability of the system.

---

# **Instructions for running `movieticketreservation.jar`**

## System requirements:
- **Java Development Kit (JDK) 21**: [Install JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
    - For Windows environments, [ensure that your PATH points to the JavaJDK directory](https://www.java.com/en/download/help/path.html)
    - For macOS environments, [ensure that your Environment Variables point to the JavaJDK directory](https://mkyong.com/java/how-to-set-java_home-environment-variable-on-mac-os-x/)
- **Node.js v22.11.0**: [Install Node](https://nodejs.org/en/download/package-manager/current)
- **MySQL Community Server 9.1.0 Innovation**: [Install MySQL Server](https://dev.mysql.com/downloads/mysql/8.0.html)
- **MySQL Workbench**: [MySQL Workbench](https://www.mysql.com/products/workbench/)

## **1. Setup MySQL database for `movieticketreservation.jar`**

Before you begin, ensure you have MySQL installed:
1. You can install MySQL from the following link:
  https://dev.mysql.com/downloads/installer/

2. Once you have MySQL Server and MySQL Workbench, go over to your application.properties file. In this file there will be a section regarding MySQL database connection that looks like the below:

```
# MySQL Database Configuration (update the values to your MySQL instance)
spring.datasource.url=jdbc:mysql://localhost:3306/acmeplex_db
spring.datasource.username=root
spring.datasource.password= your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

In this section, change the spring.datasource.password to the password to your MySQL local instance.

3. Next, in MySQL, create the acmeplex_db database with the following SQL command:
```
CREATE DATABASE acmeplex_db;
```

4. Once this is complete, running the springboot application should automatically create tables in your MySQL acmeplex_db database. The DataInitializer java class will also populate the database with initial entries for movie/theatres/showtimes/etc.

5. Alternatively, an SQL dump file has been included in the submission that will be able to populate the database by running it in MySQL. However, this can clash with the DataInitializer class in the java application, so we recommend not using this unless you are able to disable the DataInitializer class.


## **2. Setup React user interface for `movieticketreservation.jar`**

Before you begin, verify that you have the correct version of Node.js installed.
- Verify installation by running the following commands in your terminal:
```bash
node -v
npm -v
```

Steps to run the application:
1. **Download the Group11Web.zip file**  
Unzip the file (Note: Unzipping may take some time, see instructions below to clone the repository if you would prefer)

2. **Naviagate to the project directory:**
```bash
cd frontend
cd acmeplex
```
3. **Install Dependencies**  
Run the following command to install all required dependencies:
```bash
npm install
```

4. **Start the development server**  
To start the application in development mode, run:
```bash
npm run dev
```
NOTE: If you receive the error:
```bash
Error: EPERM: operation not permitted, rmdir 'C:\Users\rhysw\OneDrive\Documents\UofC Masters\Fall\ENSF 614\Final Project\Test\frontend\frontend\AcmePlex\node_modules\.vite\deps'
```
Delete the 'deps' folder from the path shown.

This will launch the application in your default browser at http://localhost:5173/

If the browser does not open automatically, manually navigate to the above URL.

5. **Verify React Router Functionality**  
The application uses React Router for navigation. You should be able to navigate between the different pages. Ensure routing works seamlessly.

### Troubleshooting React Front-End
- If you encounter an error, try clearing the cache and reinstalling dependencies:
```bash
     npm cache clean --force
     npm install
```
2. **API Issues:**  
Ensure the backend API is running and accessible at at http://localhost:8080/.

## **3. Run `movieticketreservation.jar`**

Before running the `.jar`: 
- Confirm that you have JDK21 installed and configured properly. 
- Confirm that the MySQL database is setup and configured properly. Refer to the **Setup MySQL for running `movieticketreservation.jar`**
- Confirm that you have started the React front-end application

1. In your terminal or command prompt, navigate to the directory your `.jar` file is located.
- You can do this by executing the following command, and replacing `/path/to/movieticketreservation.jar` with the correct path.
```bash
cd /path/to/movieticketreservation.jar
````
2. Ensure the `applications.properties` file is located in the same directory as the `.jar` file. 
3. Run the application.
```bash
java -jar movieticketreservation.jar
```

---

# **Instructions for setting up a development environment for Github repository**

## **System Requirements**
The following requirements **must** be installed and configured on your system.

- **Java Development Kit (JDK) 21**: [Install JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
  - For Windows environments, [ensure that your PATH points to the JavaJDK directory](https://www.java.com/en/download/help/path.html)
  - For macOS environments, [ensure that your Environment Variables point to the JavaJDK directory](https://mkyong.com/java/how-to-set-java_home-environment-variable-on-mac-os-x/)
- **Git**: [Install Git](https://git-scm.com/downloads)
- **Maven v3.9.9**: [Install Apache Maven](https://maven.apache.org/install.html)
- **Node.js v22.11.0**: [Install Node](https://nodejs.org/en/download/package-manager/current)
- **MySQL Community Server 9.1.0 Innovation**: [Install MySQL Server](https://dev.mysql.com/downloads/mysql/8.0.html)
- **MySQL Workbench**: [MySQL Workbench](https://www.mysql.com/products/workbench/)

**Integrated Development Environment (IDE) specific settings**
- Depending on your IDE, you may need to specify the JavaJDK version for your project

## **Setup Instructions**

## 1. Clone the repository

Clone the repository using your bash terminal, command prompt, or using the Github GUI application.

```bash
git clone https://github.com/ensf-614-group11/AcmePlex.git
```
---

## Setup MySQL Database for development

Before you begin, ensure you have MySQL installed:
1. You can install MySQL from the following link:
  https://dev.mysql.com/downloads/installer/

2. Once you have MySQL Server and MySQL Workbench, go over to your application.properties file. In this file there will be a section regarding MySQL database connection that looks like the below:

```
# MySQL Database Configuration (update the values to your MySQL instance)
spring.datasource.url=jdbc:mysql://localhost:3306/acmeplex_db
spring.datasource.username=root
spring.datasource.password= your_password_here
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

In this section, change the spring.datasource.password to the password to your MySQL local instance.

---

## 2. Setup React Front-End 

Before you begin, ensure you have the following installed:
1. **Node.js and npm**:
   - Download and install Node.js from [Node.js Official Website](https://nodejs.org/).
   - Verify installation by running the following commands in your terminal:
     ```bash
     node -v
     npm -v
     ```

2. **Git**:
   - Download and install Git from [Git Official Website](https://git-scm.com/).
   - Verify installation by running:
     ```bash
     git --version
     ```

3. **ESLine VSCode Extension**
   - Install the [ESLint VSCode Extension](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint)
  
Steps to run the application:
1. **Clone the Repository**
Use the following command to clone the repository to your local machine:
```bash
git clone https://github.com/ensf-614-group11/frontend.git
```

2. **Naviagate to the project directory:**
```bash
cd frontend
cd acmeplex
```
3. **Install Dependencies**
Run the following command to install all required dependencies:
```bash
npm install
```

4. **Start the development server**
To start the application in development mode, run:
```bash
npm run dev
```
NOTE: If you receive the error:
```bash
Error: EPERM: operation not permitted, rmdir 'C:\Users\rhysw\OneDrive\Documents\UofC Masters\Fall\ENSF 614\Final Project\Test\frontend\frontend\AcmePlex\node_modules\.vite\deps'
```
Delete the 'deps' folder from the path shown.
This will launch the application in your default browser at http://localhost:5173/

If the browser does not open automatically, manually navigate to the above URL.

5. **Verify React Router Functionality**
The application uses React Router for navigation. You should be able to navigate between the different pages. Ensure routing works seamlessly.

## Troubleshooting React Front-End
1. If you encounter an error, try clearing the cache and reinstalling dependencies:
```bash
     npm cache clean --force
     npm install
```
2. **API Issues:**
Ensure the backend API is running and accessible at at http://localhost:8080/.

## 3. Setup Maven Project 

1. Ensure you are in your project directory and that all system requirements have been installed properly.
2. Ensure you have an `application.properties` file in your root directory.
3. Ensure that you have setup your MySQL database, and have an active schema. The name of your database in MySQL **must** match the name of the database in your `application.properties` file.
4. Navigate to your project directory in your development terminal. This will be the directory that you specified when cloning the repository.

```bash
cd /movieticketreservation
```
5. Execute maven command to install dependencies
```bash
mvn clean install
```
6. Execute maven command to perform database migration and run back-end server.
```bash
mvn spring-boot:run
```
- After following the setup instructions, the application should start up at **http://localhost:8080**.
- You’ll see logs showing the application startup.
- Once started, you can access any REST endpoints at `http://localhost:8080`.

## Shutting Down the Application

To stop the application:
- Press **Ctrl + C** in the terminal where it’s running.
  - If this does not work, refer to the **Troubleshooting** section.

# Troubleshooting

### Application Won't Start Due to Missing Database Configuration
If you see errors about missing database configurations, ensure `application.properties` has the following line:
```properties
spring.datasource.url=jdbc:mysql://localhost:####/acmeplex_db
```
- Also ensure to replace `####` with the PORT number where your MySQL server is running.
- Ensure that the name of your database schema in MySQL is `acmeplex_db`

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
