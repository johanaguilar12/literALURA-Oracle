# 📖 LiterAlura

LiterAlura is a console application that allows users to manage a catalog of books. Users can register books in a PostgreSQL database and retrieve information about these books. The application is built using Java and Spring technologies.

## ✨ Features

The application supports five basic functionalities:

### 🔍 Search Book by Title

- Users can search for a book by entering its title. For example, searching for "Pride" will retrieve "Pride and Prejudice" by Jane Austen from the API, and then register it in the database if it's not already there.
- The application fetches book information from the Gutendex API and stores it in the database.
- The user is notified with a message if a book is not found.

### 📋 List All Registered Books

- Users can list all the books that are currently registered in the database.

### 👩‍💻 List All Registered Authors

- Users can list all authors who have books registered in the database.

### 📅 List Authors Alive in a Specific Year

- Users can list authors who were alive in a specific year. For example, listing authors alive in the year 1600 will show Cervantes and Shakespeare.

### 🌐 List Books by Language

- Users can list books by language. The supported languages are Spanish (ES), English (EN), French (FR), and Portuguese (PT).
- Users enter the language code to search for books in that language.

## 🚀 Getting Started

### Prerequisites

- Java 17
- PostgreSQL
- Maven
- Gutendex API

### Setup
```bash
git clone https://github.com/yourusername/literalura.git
cd literatura
```
#### Clone the Repository
- Create a database named literalura.
- Update the database configuration in application.properties:
  ```bash
  spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
  spring.datasource.username=yourusername
  spring.datasource.password=yourpassword
  spring.jpa.hibernate.ddl-auto=update
  ```
#### Build and Run the Application
```bash
mvn clean install
mvn spring-boot:run
```
## 📝 Example Usage

### Search Book by Title

- **Input:** `Pride`
- **Output:**
  ```yaml
  Title: Pride and Prejudice
  Author: Austen, Jane
  Language: EN
  Downloads: 6493
  ```
### List All Registered Books
- **Output:**
  ```yaml
  1. Don Quixote
  2. Emma
  3. A Dictionary of the Portuguese Language
  4. Pride and Prejudice
  5. Romeo and Juliet
  6. A Philosophy Book by Nietzsche
  ```
### List All Registered Authors
- **Output:**
  ```yaml
  1. Austen, Jane
  2. Cervantes, Miguel de
  3. Shakespeare, William
  4. Nietzsche, Friedrich
  ```
