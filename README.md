# CodePracticeBE

A comprehensive backend system for an online coding platform built with Spring Boot framework and microservice architecture.

## 🚀 Features

### Core Services

- **Auth Service**: Handles user authentication, authorization, and JWT token management
- **Forum Service**: Manages blog posts and comments
- **Problem Service**: Maintains coding problems, test cases, and difficulty levels
- **Submission Service**: Processes code submissions and manages submission history, Executes and evaluates submitted code against test cases

## 🛠️ Technology Stack

- **Framework**: Spring Boot
- **Architecture**: Microservices
- **Language**: Java
- **Build Tool**: Maven
- **Database**: Mysql, MongoDB
- **Message Queue**: RabbitMQ
- **API Gateway**: Spring Cloud Gateway
- **Service Discovery**: Eureka

## 🚀 Getting Started

### Clone the Repository

```bash
git clone https://github.com/Tuavsn/CodePracticeBE.git
cd CodePracticeBE
```

### Configuration

1. **Database Configuration**
   ```properties
   # Update application.yml or application.properties for each service
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

2. **Service Configuration**
   - Configure port numbers for each microservice
   - Set up service discovery endpoints
   - Configure message queue connections (if applicable)

### Running the Services

#### Option 1: Using Maven (Development)

```bash
# Start each service individually
cd config-server
mvn spring-boot:run

cd ../eureka-server
mvn spring-boot:run

cd ../api-gateway
mvn spring-boot:run

cd ../auth-service
mvn spring-boot:run

cd ../user-service
mvn spring-boot:run

cd ../forum-service
mvn spring-boot:run

cd ../problem-service
mvn spring-boot:run

cd ../submission-service
mvn spring-boot:run

# Repeat for other services...
```

#### Option 2: Using Docker (Recommended)

```bash
# Build and run all services
docker-compose up --build

# Run in detached mode
docker-compose up -d
```

#### Option 3: Using Docker individually

```bash
# Build each service
mvn clean package -DskipTests

# Run with Docker
docker build -t codepractice-auth-service ./auth-service
docker run -p 8081:8081 codepractice-auth-service

# Repeat for other services...
```

## 📚 API Documentation

### Service Endpoints

| Service | Default Port | Health Check |
|---------|-------------|--------------|
| Api Gateway | 8080 | `/actuator/health` |
| Eureka Server | 8761 | `/actuator/health` |
| Config Server | 8888 | `/actuator/health` |
| Auth Service | 9000 | `/actuator/health` |
| User Service | 9001 | `/actuator/health` |
| Forum Service | 9002 | `/actuator/health` |
| Problem Service | 9003 | `/actuator/health` |
| Submission Service | 9004 | `/actuator/health` |

### API Gateway

If using an API Gateway, all services are accessible through:
```
http://localhost:8080/api/{service-name}/{endpoint}
```

## 🔧 Development

### Project Structure

```
CodePracticeBE/
├── api-gateway/             # Gateway
├── eureka-server/           # Eureka Server
├── config-server/           # Central Config Server
├── auth-service/
├── user-service/
├── forum-service/
├── problem-service/
├── submission-service/
├── common-lib/              # Shared utilities and models
├── docker-compose.yml
├── pom.xml                  # Parent POM
└── README.md
```

### Adding New Services

1. Create a new Spring Boot module
2. Add service registration to discovery service
3. Update API Gateway routing (if applicable)
4. Add service to docker-compose.yml
5. Update documentation

## 📊 Monitoring and Logging

- **Health Checks**: Available at `/actuator/health` for each service
- **Metrics**: Accessible via `/actuator/metrics`
- **Logging**: Centralized logging with appropriate log levels

## 🚀 Deployment

### Production Deployment

1. **Build Production Images**
   ```bash
   mvn clean package -DskipTests
   docker-compose build
   ```

2. **Deploy to Production**
   ```bash
   docker-compose up -d
   ```

### Environment Variables

Configure the following environment variables for production:

```bash
# Database
DB_HOST=your-database-host
DB_PORT=5432
DB_NAME=codepractice
DB_USERNAME=your-username
DB_PASSWORD=your-password

# JWT
JWT_SECRET=your-jwt-secret-key
JWT_EXPIRATION=86400

# Other service-specific configurations...
```

## 🔒 Security

- JWT-based authentication
- Role-based access control (RBAC)
- Input validation and sanitization
- Rate limiting on API endpoints
- CORS configuration for frontend integration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🌐 Demo

Visit the live demo: 
- **Backend**: [https://codejudge.trinhhoctuan.io.vn](https://codejudge.trinhhoctuan.io.vn)
- **Frontend**: [https://code-practice-fe.vercel.app](https://code-practice-fe.vercel.app)

## 📞 Contact

- **Email**: hoctuavsn@gmail.com
- **GitHub**: [https://github.com/Tuavsn](https://github.com/Tuavsn)

## 🙏 Acknowledgments

- Spring Boot community for excellent documentation
- Contributors and testers who helped improve this project
- Open source libraries that made this project possible

---

**Note**: This README assumes a standard microservice setup. Please update the configuration details, database information, and deployment instructions according to your specific implementation.