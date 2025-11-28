# 🏥 Telemedicine Full-Stack Application

A complete telemedicine system built with **Spring Boot (Java 21)** backend and **HTML/CSS/JavaScript** frontend.

## 📋 Features

- **Patient Portal**: Registration, login, view doctors, book appointments, upload/view medical reports
- **Doctor Portal**: View all appointments, update appointment status
- **Admin Panel**: Manage doctors, view all appointments
- **Secure**: Password hashing with BCrypt
- **Responsive**: Mobile-friendly UI
- **RESTful API**: Clean REST endpoints with validation

## 🛠️ Tech Stack

**Backend:** Java 21, Spring Boot 3.2, Spring Web, Spring JDBC, MySQL 9.0, BCrypt, Maven  
**Frontend:** HTML5, CSS3, Vanilla JavaScript

## 🚀 Quick Start

### Prerequisites
- Java 21 JDK
- Maven 3.8+
- MySQL 8.0+ (or XAMPP)

### Setup

1. **Database**
```powershell
# Start MySQL and import schema
mysql -u root -p
CREATE DATABASE telemedicine_db;
USE telemedicine_db;
SOURCE telemedicine_db.sql;
```

2. **Configure** (optional - uses defaults)
```powershell
# Set environment variables or edit application.properties
$env:DB_URL="jdbc:mysql://localhost:3306/telemedicine_db"
$env:DB_USER="root"
$env:DB_PASSWORD="your_password"
```

3. **Build & Run**
```powershell
# Build
mvn clean package

# Run
java -jar target/telemedicine-app-1.0.0.jar

# OR use Maven directly
mvn spring-boot:run
```

4. **Access**
- Frontend: http://localhost:8080
- API: http://localhost:8080/api

## 📡 API Endpoints

**Patients:** `POST /api/patients/register`, `/login`, `GET /{id}`  
**Doctors:** `GET /api/doctors`, `POST /login`, `POST /` (add)  
**Appointments:** `POST /api/appointments`, `GET /patient/{id}`, `PUT /{id}/status`  
**Reports:** `POST /api/reports`, `GET /patient/{id}`

## 🐳 Docker

```powershell
docker build -t telemedicine-app .
docker run -p 8080:8080 -e DB_URL=jdbc:mysql://host.docker.internal:3306/telemedicine_db telemedicine-app
```

## ☁️ Deploy to Render

1. Push to GitHub
2. Create Render Web Service (Docker)
3. Add environment variables: `DB_URL`, `DB_USER`, `DB_PASSWORD`
4. Create Render MySQL database and connect

## 🔐 Default Credentials

**Admin Panel:** `admin123`

## 📝 Development

- Hot reload enabled with Spring Boot DevTools
- CORS enabled for all origins (update for production)
- BCrypt password hashing
- HikariCP connection pooling

## 🛠️ Troubleshooting

**Port in use?** Change in `application.properties`: `server.port=8081`  
**MySQL error?** Verify credentials and MySQL is running  
**Build errors?** Run `mvn clean install -U`
