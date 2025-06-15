# I Saw, Was I 🎬📺

A Spring Boot REST API for managing entertainment items — movies and series — using inheritance with JPA. Data is stored in a MySQL database.

## 🔍 Overview

"I Saw, Was I" is a backend application that allows users to track and organize the movies and series they’ve watched. Each item is associated with a `username`. The project follows a clean layered architecture (Entity, Repository, Service, Controller).

## 🧱 Tech Stack

- **Spring Boot 3**
- **Spring Data JPA**
- **MySQL**
- **RESTful API**
- JPA Inheritance (`Item` as abstract class, with `Movie` and `Series` as subclasses)
- Strategy: `SINGLE_TABLE` with `@DiscriminatorColumn`

## 📦 Entity Structure

### `Item` (abstract)

- id: Long
- title: String
- genre: String
- year: int
- username: String

### `Movie`
- director: String
- duration: int

### `Series`
- seasons: int
- episodes: int

## 🚀 Main Endpoints

POST /items — Create a new item (movie or series). JSON must include "type": "movie" or "type": "series".

GET /items — List all items.

GET /items/username/{username} — List all items by a specific user. (coming soon)

GET /items/{id} — Get item by ID.

DELETE /items/{id} — Delete item by ID.

## ⚙️ Getting Started

1. Clone the repo

git clone https://github.com/your-username/isawwasi.git
cd isawwasi

2. Configure MySQL connection
Edit your application.properties:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/isawwasi
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3. Run the project
With Maven:

./mvnw spring-boot:run
Or via your IDE (IntelliJ, VS Code, etc.)

## 📋 Future Features
User authentication and registration

Frontend (React or Vanilla JS)

Advanced filtering (by genre, year, type, etc.)

Improved error handling and validations

## 🤝 Contributing
Contributions are welcome! Feel free to open an issue or submit a pull request.

## 📄 License
This project is licensed under the MIT License.
