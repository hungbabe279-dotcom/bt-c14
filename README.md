# Email List Application

A simple JPA-based Email List management application with a web interface.

## Features

- **Add Users** - Add new users with email, first name, and last name
- **View All Users** - Display all users in a table format
- **Update Users** - Edit existing user information
- **Delete Users** - Remove users from the list
- **Web Interface** - Clean and responsive HTML UI
- **In-Memory Storage** - Data stored in memory (no database required)

## Requirements

- Java 11 or higher
- Maven 3.6+

## Local Development

### Build the project:
```bash
mvn clean package
```

### Run the application:
```bash
java -jar target/email-list.jar
```

Then open your browser and navigate to `http://localhost:8080`

## Deployment

### Deploy to Render

1. Push the project to GitHub
2. Connect your GitHub repository to Render
3. Set the build command: `mvn clean package`
4. Set the start command: `java -jar target/email-list.jar`
5. The application will be available at your Render URL

## Project Structure

```
ch13_ex1_email/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/
│       │       ├── User.java
│       │       ├── DBUtil.java
│       │       ├── UserDB.java
│       │       ├── Main.java
│       │       └── web/
│       │           └── WebServer.java
│       └── resources/
│           └── META-INF/
│               └── persistence.xml
├── pom.xml
├── Procfile
├── .gitignore
└── README.md
```

## Usage

### Web Interface
- Navigate to home page: `http://localhost:8080/`
- View users: `http://localhost:8080/users`
- Add user: `http://localhost:8080/add`
- Update user: `http://localhost:8080/update?id=<userId>`
- Delete user: `http://localhost:8080/delete?id=<userId>`

### Console Interface
Run `java -cp target/classes com.example.Main` to use the console version.

## License

This project is for educational purposes.
