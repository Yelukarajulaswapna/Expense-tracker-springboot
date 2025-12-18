# Expense Tracker Application 💰

A full-stack Expense Tracker application built using **Spring Boot, JWT Authentication, PostgreSQL, and Vanilla JavaScript**, deployed on **Render**.

---

## 🚀 Features
- User Registration & Login (JWT Authentication)
- Add / Edit / Delete Categories
- Add / Edit / Delete Expenses
- Expense summary chart
- Secure REST APIs
- PostgreSQL database
- Deployed on Render

---

## 🛠 Tech Stack
- **Backend:** Java, Spring Boot, Spring Security, JWT
- **Frontend:** HTML, CSS, JavaScript, Bootstrap
- **Database:** PostgreSQL (Render)
- **Deployment:** Render
- **Build Tool:** Maven

---

## 📂 Project Structure

src └── main ├── java │   └── com.example.expensetracker └── resources ├── static │   ├── index.html │   ├── login.html │   ├── register.html │   ├── app.js │   └── styles.css └── application.properties

---

## 🔐 Environment Variables (Render)

SPRING_DATASOURCE_URL SPRING_DATASOURCE_USERNAME SPRING_DATASOURCE_PASSWORD SPRING_JPA_DATABASE_PLATFORM SERVER_PORT

---

## ⚙ application.properties
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=${SPRING_JPA_DATABASE_PLATFORM}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


---

▶ Live Demo

🔗 https://YOUR-RENDER-URL

🔗 Swagger UI:
https://YOUR-RENDER-URL/swagger-ui/index.html
