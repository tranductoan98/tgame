# TGame – Scalable Backend Game Service

## 1. Overview

**TGame** là một backend service được xây dựng bằng **Spring Boot**, mô phỏng hệ thống quản lý và xử lý logic cho một game server.

Mục tiêu của project không chỉ dừng ở CRUD, mà tập trung vào:

- Thiết kế backend theo hướng **scalable & maintainable**
- Tách biệt rõ **business logic** và **infrastructure**
- Áp dụng các pattern phổ biến trong hệ thống production

---

## 2. Design Goals

Project được xây dựng với các mục tiêu chính:

- **Separation of Concerns**  
  Tách biệt rõ từng layer (API – Business – Data)

- **Maintainability**  
  Dễ đọc, dễ sửa, dễ mở rộng

- **Extensibility**  
  Có thể thêm feature mới mà không ảnh hưởng code cũ

- **Security-first mindset**  
  Tích hợp xác thực & phân quyền ngay từ đầu

---

## 3. Architecture

### High-level Architecture

```
Client → Controller → Service → Repository → Database
                      ↓
                     DTO
```


### Key Design Decisions

#### 1. Layered Architecture
- Controller chỉ xử lý HTTP
- Service xử lý business logic
- Repository truy cập dữ liệu

👉 Giúp code rõ ràng, dễ test và dễ mở rộng

---

#### 2. DTO Pattern
- Không expose trực tiếp Entity ra ngoài
- DTO dùng để:
  - Validate input
  - Control response structure

👉 Tránh lộ internal structure & tăng tính bảo mật

---

#### 3. Interface + Implementation
- Service được define qua interface
- Logic nằm trong `serviceimpl`

👉 Hỗ trợ:
- Unit test (mock dễ)
- Thay đổi implementation mà không ảnh hưởng layer khác

---

#### 4. Centralized Exception Handling
- Sử dụng global handler

👉 Giúp:
- Response thống nhất
- Dễ debug & logging

---

#### 5. Enum-driven Design
- Trạng thái và logic được enum hóa

👉 Tránh hard-code string, giảm bug runtime

---

## 4. Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Security
- Maven

---

## 5. Security

Hệ thống có tích hợp security layer:

- Authentication
- Authorization (role-based)
- Request filtering

### Design Approach

- Security xử lý ở filter layer → không ảnh hưởng business logic
- Có thể mở rộng:
  - JWT
  - OAuth2

---

## 6. Core Features

- RESTful API cho các entity trong game
- Tách biệt rõ DTO / Entity
- Xử lý lỗi tập trung
- Clean architecture theo chuẩn backend

---

## 7. Project Structure
```
com.example
├── controller       # API layer
├── service          # Business interface
├── serviceimpl      # Business implementation
├── repository       # Data access
├── entity           # ORM mapping
├── dto              # Data transfer
├── security         # Auth & filter
├── config           # App configuration
├── handler          # Global exception handler
└── enums            # Domain constants
```

---

## 8. Trade-offs & Considerations

### 1. Layered Architecture vs Simplicity
- Nhược điểm: code dài hơn  
- Ưu điểm:
  - Dễ maintain
  - Dễ mở rộng

---

### 2. DTO Overhead
- Nhược điểm: tăng số lượng class  
- Ưu điểm:
  - Tăng security
  - Linh hoạt response format

---

### 3. Monolith First
- Hiện tại là monolith  
- Nhưng có thể:
  - Tách microservice khi cần

---

## 9. Scalability Strategy

### Horizontal Scaling
- Stateless service → dễ scale nhiều instance

### Performance Optimization
- Cache layer (Redis)
- Query optimization

### Future Extensions
- WebSocket (real-time gameplay)
- Event-driven architecture (Kafka/RabbitMQ)
- Microservices decomposition

---

## 10. How to Run
```
git clone https://github.com/tranductoan98/tgame.git
cd tgame
./mvnw spring-boot:run
```
## 11. Future Improvements
Unit test & integration test
API documentation (Swagger)
Dockerize & CI/CD
Monitoring (Prometheus / Grafana)
## 12. Author

Toàn Trần
Backend Developer
