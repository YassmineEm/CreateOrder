# Create Order Microservice 🛒

A distributed order management system using **Spring Boot**, **gRPC**, and **Kafka** to handle order creation with product availability checks.

![Architecture Create Order](CreateOrder.PNG)

## Features ✨
- **gRPC Communication**: Fetch product details from Product Service
- **Kafka Events**: Async order/product state updates
- **Transaction Workflow**:
  1. Order creation with `CREATED` state
  2. Product availability check via Kafka
  3. Order state update (`PROCESSING`/`FAILED`)
- HATEOAS-enabled REST API
- Swagger Documentation
- MySQL persistence

## Tech Stack 🛠️
- **Java 17** • **Spring Boot 3.4.3** • **Maven**
- **gRPC** • **Kafka** • **MySQL**
- **Spring Data JPA** • **HATEOAS** • **Lombok**
- **Spring Security** (Disabled for development)

## Installation ⚙️

### Prerequisites
- Java 17 JDK
- MySQL 8+
- Kafka 3.6+
- Maven 3.9+

### Setup
1. **Clone Repository**
   ```bash
   git clone https://github.com/YassmineEm/CreateOrder.git

## Database Setup 🗃️

Créer la base de données MySQL :

```sql
CREATE DATABASE createOrder1;
```

**💡 Configuration importante**  
Assurez-vous que les identifiants MySQL dans `application.properties` correspondent à votre installation :
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## Kafka Setup �

### 1. Démarrer Zookeeper (service de coordination)
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```

### 2. Démarrer Kafka (dans une nouvelle fenêtre/terminal)
```bash
bin/kafka-server-start.sh config/server.properties
```
