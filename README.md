# 💰 Expense Tracker Application

A **Full Stack Expense Tracker Application** developed using **Java Spring Boot (Backend)** and **Vanilla JavaScript (Frontend)**.  
The application helps users manage their daily expenses, categorize spending, and visualize expense data securely using **JWT authentication**.  
It is deployed on **Render** with **MYSQL / PostgreSQL** as the database.

This project demonstrates **real-world Java Full Stack development skills**, including REST APIs, authentication, database integration, and frontend-backend communication.

---

## 🚀 Features

### 🔐 Authentication & Security
- User Registration
- User Login
- JWT-based Authentication
- Secure access to protected APIs
- Token stored in browser for session handling

### 🗂 Category Management
- Create new categories
- View all categories
- Update category details
- Delete categories

### 🧾 Expense Management
- Add expenses with category, amount, date, and comments
- Edit existing expenses
- Delete expenses
- View all expenses in tabular format
- Auto-calculated total expense

### 📊 Dashboard & Analytics
- Expense summary visualization
- Pie Chart for category-wise expense distribution
- Clean and responsive UI
- Real-time chart updates on expense changes

### ☁ Deployment
- Backend & frontend hosted together
- MYSQL / PostgreSQL database hosted on Render
- Environment variables for secure configuration

---

## 🛠 Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- Hibernate
- RESTful APIs

### Frontend
- HTML5
- CSS3
- JavaScript (Vanilla JS)
- Bootstrap
- Chart.js

### Database
- MYSQL for local / PostgreSQL (Render Cloud Database)

### Tools & Platforms
- Maven
- Git & GitHub
- Postman and Swagger
- Render (Deployment)

---

## 📂 Project Structure

src  
└── main  
                        ├── java  
                        │                   └── com.example.expensetracker  
                        │                                    ├── controller  
                        │                                    ├── service  
                        │                                    ├── repository  
                        │                                    ├── model  
                        │                                    └── security  
                        └── resources  
                                          ├── static  
                                          │                  ├── index.html  
                                          │                  ├── login.html  
                                          │                  ├── register.html  
                                          │                  ├── app.js  
                                          │                  └── styles.css  
                                          └── application.properties  

---

## 🔐 Environment Variables (Render)

These variables are configured in the **Render Dashboard**:

- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- SPRING_JPA_DATABASE_PLATFORM
- SERVER_PORT

---

## ⚙ application.properties

```properties

spring.datasource.url=jdbc:mysql://localhost:3306/expensetracker
spring.datasource.username=root
spring.datasource.password=Swapn@2281

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# -------- SPRING SECURITY LOGIN (DEV ONLY) --------
spring.security.user.password=admin123
spring.security.user.roles=USER

# remove driver-class-name OR set it to MySQL:
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.properties.hibernate.format_sql=true
server.error.include-message=always
logging.level.org.hibernate.SQL=DEBUG

## OR

spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=${SPRING_JPA_DATABASE_PLATFORM}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=${SERVER_PORT:8080}

---

▶ Live Demo

🔗 https://burma-nonslanderous-synthia.ngrok-free.dev

🔗 Swagger UI:
http://localhost:8080/swagger-ui/index.html#/expense-controller



## 📸 Application Screenshots


### 🔐 Login Page
<img width="469" height="484" alt="image" src="https://github.com/user-attachments/assets/bd4b1943-a1bc-4faa-a945-5c5f089fbdb3" />

---

### 📝 Register Page
<img width="443" height="469" alt="image" src="https://github.com/user-attachments/assets/b1f4bc2b-a392-44c4-bbd6-772cf8fc3b05" />
---

### 📊 Dashboard Overview
<img width="1288" height="727" alt="image" src="https://github.com/user-attachments/assets/d69ec3a5-e4b5-4eae-997e-855e8a282db9" />


---

### 📈 Expense Charts
![chart](https://github.com/user-attachments/assets/760f55eb-ca99-41a1-a588-8aa9395ac70a)

---

🧠 Learning Outcomes
Built a real-world Java Full Stack Application
Implemented JWT-based authentication
Designed RESTful APIs
Integrated PostgreSQL with Spring Boot
Deployed application on Render
Handled frontend-backend communication using Fetch API
Worked with charts and data visualization

