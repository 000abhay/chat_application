# Real-Time Chat Application - Spring Boot

A production-ready real-time one-to-one chat system built with Spring Boot, WebSocket, and MySQL.

## 📋 Project Overview

**Purpose**: Build a secure, scalable chat system where users can:
- Register & login with JWT authentication
- Search other users
- Send/accept/reject chat requests
- Exchange messages in real-time via WebSocket
- Share files (images, videos, documents)
- Access persistent chat history

**Tech Stack**:
- **Backend**: Java 17, Spring Boot 3.2
- **Real-time**: WebSocket with STOMP protocol
- **Database**: MySQL 8
- **Security**: JWT, Spring Security, BCrypt
- **Build**: Maven

## 🏗️ System Architecture

```
┌─────────────────┐
│   Frontend App  │ (HTML/React/Vue)
└────────┬────────┘
         │
    ┌────┴─────────────────┐
    │                      │
 REST API          WebSocket Connection
    │                      │
┌───▼──────────────────────▼─────┐
│      Spring Boot Backend        │
│  ┌──────────────────────────┐   │
│  │  Controllers & Services  │   │
│  ├──────────────────────────┤   │
│  │  JPA Entities & Repos    │   │
│  └──────────────────────────┘   │
└────────┬──────────────────────┘
         │
┌────────▼──────────────────────┐
│       MySQL Database           │
│  ├─ users                      │
│  ├─ chat_requests             │
│  └─ messages                  │
└────────────────────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- MySQL 8
- Maven 3.6+

### Installation

1. **Setup database**:
```bash
mysql -u root -p
CREATE DATABASE chatdb;
```

2. **Configure application**:
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chatdb
    username: root
    password: your_password

jwt:
  secret: your_long_secure_secret_key_min_32_chars
```

3. **Build and run**:
```bash
mvn clean install
mvn spring-boot:run
```

Server runs at: `http://localhost:8080`

## 📚 API Endpoints

### Auth
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login with credentials

### Users
- `GET /api/users/search?query=name` - Search users
- `GET /api/users/{id}` - Get user details

### Chat Requests
- `POST /api/chat-request` - Send chat request
- `PUT /api/chat-request/accept/{id}` - Accept request
- `PUT /api/chat-request/reject/{id}` - Reject request
- `GET /api/chat-request/pending` - Get pending requests
- `GET /api/chat-request/accepted` - Get active chats

### Messages
- `GET /api/messages/history/{userId}/{otherUserId}` - Get chat history
- `POST /api/upload` - Upload file

### WebSocket
- **Endpoint**: `/ws`
- **Send**: `/app/chat` - Send message
- **Subscribe**: `/user/queue/messages` - Receive messages

## 🗄️ Database Schema

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE chat_requests (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  status ENUM('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (sender_id) REFERENCES users(id),
  FOREIGN KEY (receiver_id) REFERENCES users(id)
);

CREATE TABLE messages (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  content LONGTEXT,
  file_url VARCHAR(255),
  type ENUM('TEXT','IMAGE','VIDEO','DOCUMENT') DEFAULT 'TEXT',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (sender_id) REFERENCES users(id),
  FOREIGN KEY (receiver_id) REFERENCES users(id),
  INDEX idx_chat (sender_id, receiver_id),
  INDEX idx_created (created_at)
);
```

## 💻 Project Structure

```
src/main/java/com/chat/
├── ChatApplication.java
├── controller/          # REST endpoints & WebSocket
├── service/            # Business logic
├── model/              # JPA entities
├── repository/         # Data access
├── dto/                # Request/response objects
├── config/             # Spring configuration
├── security/           # JWT & authentication
├── exception/          # Error handling
└── util/               # Utilities
```

## 🔐 Features

✅ JWT Authentication
✅ User Registration & Search
✅ Chat Request System
✅ Real-Time WebSocket Messaging
✅ File Upload (50MB limit)
✅ Message Persistence
✅ Access Control
✅ BCrypt Password Hashing
✅ Global Exception Handling
✅ CORS Support

## 🧪 Quick Test

```bash
# Register user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@test.com","password":"pass123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"pass123"}'

# Search users (use token from login)
curl -X GET "http://localhost:8080/api/users/search?query=jane" \
  -H "Authorization: Bearer <token>"

# Send chat request
curl -X POST http://localhost:8080/api/chat-request \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"receiverId":2}'
```

## 📱 WebSocket Example

```javascript
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
  stompClient.subscribe('/user/queue/messages', function(msg) {
    console.log('Message:', JSON.parse(msg.body));
  });

  stompClient.send('/app/chat', {}, JSON.stringify({
    senderId: 1,
    receiverId: 2,
    content: "Hello!",
    type: "TEXT"
  }));
});
```

## 🎯 Key Design Decisions

1. **WebSocket + STOMP**: Real-time bidirectional communication without polling
2. **Request-based Permissions**: Prevents spam, clear audit trail
3. **JWT Tokens**: Stateless auth, easy horizontal scaling
4. **File URLs in DB**: Lightweight storage, easier cloud migration
5. **Message Persistence**: Enables history, search, analytics

## 🚀 Future Enhancements

- Online/offline status indicators
- Typing indicators
- Message search & filtering
- Cloud storage integration (S3)
- Push notifications
- End-to-end encryption
- Group chat support
- Message reactions
- Read receipts
- API rate limiting

## 💼 For Interviews

**"I built a real-time chat application using Spring Boot 3.2 featuring:**
- **Real-time messaging** via WebSocket with STOMP protocol
- **JWT authentication** for secure stateless auth
- **Request-based permission system** to prevent spam
- **Persistent storage** of messages and user data in MySQL
- **File sharing** with 50MB file size validation
- **Optimized queries** for chat history with proper indexing
- **Spring Security** configuration and access control
- **Global exception handling** for consistent API responses"

## 🛠️ Build & Deploy

```bash
# Build
mvn clean package

# Run
java -jar target/chat-app-1.0.0.jar

# With custom config
java -jar target/chat-app-1.0.0.jar \
  --spring.datasource.password=your_db_password \
  --jwt.secret=your_secret_key
```

## 📄 License

MIT