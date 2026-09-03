# 🐾 Pawdcast — The All-in-One Pet Care Web Application

Pawdcast is a full-stack pet-care platform designed to simplify how pet owners manage their pets’ health, activities, expenses, documents and everyday needs.

Built using **Java Spring Boot, MySQL, HTML, CSS and JavaScript**, the application brings essential pet-care tools together in one convenient ecosystem.

---

## ✨ Project Overview

Pet owners often need separate services for health records, adoption, expense tracking, clinic discovery and pet-care information. Pawdcast combines these requirements into a single web application.

The platform is designed for:

* Pet owners managing one or multiple pets
* People looking to adopt or list pets
* Users maintaining health and vaccination records
* Pet owners tracking expenses and daily activities
* Anyone looking for pet-care information and services

---

## 🚀 Key Features

### 👤 User and Pet Management

* User registration and login
* Multiple pet profiles under one account
* Personal information management
* Digital pet diary for daily updates
* Secure DigiLocker for pet-related documents

### 🩺 Health and Wellness Tracking

* Vaccination and medical-record management
* Pet health history
* Health reminders and important dates
* Food and diet tracking
* Daily habit tracking
* Pet insurance cost estimator

### 🐕 Breed and Care Guidance

* Detailed breed information
* Breed recommendation system
* Pet-care and grooming guidance
* Training tips and educational resources
* Food recommendations and tracking

### 🏡 Adoption and Legal Support

* Pet adoption listings
* Adoption seeker registration
* Pet giver submission form
* Automatic adoption certificate generation
* India-specific pet ownership information
* Pet adoption and ownership guidelines

### 📍 Finder Utilities

* Nearby veterinary clinic finder
* Pet-friendly venue finder
* Information about pet-related services and locations

### 💰 Expense Management

* Record pet-related expenses
* Categorize routine and medical spending
* Review expense information
* Estimate future pet-care costs

### 🛒 Pet E-Commerce Demonstration

* Browse pet-care products
* Add products to a shopping cart
* Place mock orders
* Demonstrates a basic e-commerce workflow

> The e-commerce module is a demonstration feature and does not process real payments.

---

## 🛠️ Technology Stack

| Layer                   | Technologies            |
| ----------------------- | ----------------------- |
| Frontend                | HTML5, CSS3, JavaScript |
| Backend                 | Java, Spring Boot       |
| Database                | MySQL                   |
| Data Access             | JDBC, Spring Data JPA   |
| Build Tool              | Maven                   |
| Server                  | Embedded Apache Tomcat  |
| Development Environment | Eclipse IDE             |
| Containerization        | Docker                  |
| Version Control         | Git and GitHub          |

---

## 🏗️ Application Architecture

```text
User Interface
      │
      ▼
HTML, CSS and JavaScript
      │
      ▼
Spring Boot Controllers
      │
      ▼
Service Layer
      │
      ▼
Repository and DAO Layer
      │
      ▼
MySQL Database
```

The backend follows a layered architecture:

* **Controller layer:** Receives HTTP requests
* **Service layer:** Handles application logic
* **Repository/DAO layer:** Communicates with MySQL
* **Model layer:** Defines application data structures
* **Configuration layer:** Manages security and application settings

---

## 📁 Project Structure

```text
Pawdcast-Pet-Care/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pawdcast/pawdcast/
│   │   │       ├── application/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dao/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── PawdcastApplication.java
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html
│   │       │   ├── adoption.html
│   │       │   ├── clinic.html
│   │       │   ├── digilocker.html
│   │       │   ├── expenses.html
│   │       │   ├── health.html
│   │       │   └── other frontend pages
│   │       │
│   │       └── application.properties
│   │
│   └── test/
│
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── README.md
```

---

## ⚙️ Local Installation

### Prerequisites

Install the following before running the project:

* Java 21 or a compatible version
* MySQL 8
* Maven, or use the included Maven Wrapper
* Git
* Eclipse IDE, IntelliJ IDEA or VS Code

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/Pawdcast-Pet-Care.git
cd Pawdcast-Pet-Care
```

### 2. Create the MySQL database

Open MySQL and run:

```sql
CREATE DATABASE pawdcast;
```

### 3. Configure the application

Open:

```text
src/main/resources/application.properties
```

Configure the required database values using environment variables:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Example local values:

```text
DATABASE_URL=jdbc:mysql://localhost:3306/pawdcast
DATABASE_USERNAME=root
DATABASE_PASSWORD=your_mysql_password
```

> Never commit actual database passwords, email credentials, JWT secrets or API keys to GitHub.

### 4. Run the application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

Alternatively, run `PawdcastApplication.java` as a Java or Spring Boot application from your IDE.

### 5. Open the application

Visit:

```text
http://localhost:8080
```

If port `8080` is already occupied, configure another port:

```properties
server.port=8081
```

Then visit:

```text
http://localhost:8081
```

---

## 🐳 Running with Docker

Build the application package:

```bash
./mvnw clean package -DskipTests
```

Build the Docker image:

```bash
docker build -t pawdcast-pet-care .
```

Run the container:

```bash
docker run -p 8080:8080 pawdcast-pet-care
```

The required database environment variables must also be supplied when running the container.

---

## 📸 Screenshots

### Landing Page

<!-- Add landing-page screenshot here -->

### User and Pet Profiles

<!-- Add user-profile or pet-profile screenshot here -->

### Health and Expense Tracking

<!-- Add health-tracker and expense-tracker screenshots here -->

### Adoption Hub

<!-- Add adoption-hub screenshot here -->

### Vet and Venue Finder

<!-- Add finder-utilities screenshot here -->

### Pet E-Commerce

<!-- Add e-commerce screenshot here -->

Example screenshot format:

```html
<p align="center">
  <img src="SCREENSHOT_URL" alt="Pawdcast landing page" width="900">
</p>
```

---

## 🔮 Future Improvements

* Improve responsive design across mobile devices
* Add automated unit and integration tests
* Introduce role-based authorization
* Add email and in-app reminders
* Integrate live maps for clinics and venues
* Add cloud storage for pet documents
* Integrate a secure payment gateway
* Add an administrative dashboard
* Improve accessibility and performance
* Deploy the complete application publicly

---

## 🎯 Learning Outcomes

This project demonstrates practical experience with:

* Building full-stack Java web applications
* Designing RESTful backend functionality
* Integrating Spring Boot with MySQL
* Structuring controllers, services and repositories
* Implementing authentication and user management
* Connecting a static frontend with backend services
* Containerizing a Spring Boot application with Docker
* Managing code using Git and GitHub

---

## ⚠️ Disclaimer

Pawdcast is an educational project. Health, insurance and legal information provided through the application should not replace professional veterinary, financial or legal advice.

---

## 👨‍💻 Author

**Atharva Padwal**

IT Engineering Student and Full-Stack Developer

* GitHub: [AtharvaPadwal2](https://github.com/AtharvaPadwal2)
* LinkedIn: [Atharva Padwal](https://www.linkedin.com/in/atharva-padwal-11b005397)

---

## ⭐ Support

If you find this project useful, consider giving the repository a star.
