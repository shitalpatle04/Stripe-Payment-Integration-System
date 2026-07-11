# Stripe Payment Integration System

A microservices-based payment integration system built using **Java** and **Spring Boot** that integrates with the **Stripe Payment Gateway** for secure online payments.

The project demonstrates secure payment processing through a modular microservices architecture, incorporating request validation, HMAC authentication, externalized configuration, and business rule validation.

---

## Project Overview

The Stripe Payment Integration System simulates a real-world payment workflow used by merchant applications. Incoming payment requests are validated through a dedicated validation service before being forwarded to Stripe for payment session creation.

The application follows a **Microservices Architecture**, where each service has a single responsibility, making the system scalable, maintainable, and easier to extend.

---

## Key Highlights

- Microservices Architecture
- Stripe Checkout Session Integration
- HMAC SHA-256 Request Authentication
- Business Rule Validation
- Duplicate Transaction Detection
- Payment Threshold Validation
- Spring Security Integration
- Externalized Configuration using Spring Cloud Config
- Flyway Database Migration
- Layered Architecture

---

## System Architecture

![System Architecture](docs/architecture.png)

---

## Payment Flow

1. Customer selects a product or service from the Merchant Application.
2. The Merchant Backend sends a payment request to the Validation Service.
3. The Validation Service verifies the HMAC signature.
4. Business validation rules are executed.
5. Duplicate transaction validation is performed.
6. Payment threshold validation is checked.
7. Valid requests are forwarded to the Create Session Integration Service.
8. The Create Session Integration Service communicates with Stripe APIs.
9. Stripe generates a Checkout Session.
10. The customer is redirected to the Stripe Hosted Checkout page.
11. Stripe returns the payment status to the Merchant Application through a callback/webhook.

---

## Repository Structure

```
Stripe-Payment-Integration-System
│
├── docs
│   └── architecture.png
│
├── create-session-integration
│
├── validation-service-validator
│
├── payment-config-server-integration
│
├── payment-config-properties
│
└── README.md
```

---

## Microservices

### 1. Create Session Integration

Responsible for:

- Creating Stripe Checkout Sessions
- Integrating with Stripe APIs
- Generating Hosted Checkout URLs
- REST API implementation
- Global exception handling

**Technologies Used**

- Spring Boot
- Spring Web
- Stripe Java SDK
- Maven

---

### 2. Validation Service

Responsible for validating incoming payment requests before forwarding them to the Stripe service.

**Features**

- HMAC SHA-256 Signature Validation
- Duplicate Transaction Validation
- Payment Threshold Validation
- Merchant Request Validation
- Spring Security
- Business Rule Validation
- Database-driven Validation Rules

---

### 3. Spring Cloud Config Server

Responsible for centralized configuration management across all microservices.

**Features**

- Centralized Configuration
- Environment-specific Properties
- Spring Cloud Config Server
- External Configuration Management

---

### 4. Configuration Repository

Stores centralized configuration files consumed by the Config Server.

Contains environment-specific property files such as:

- Local
- Development
- QA
- UAT
- Production

---

## Features

- Microservices Architecture
- Stripe Payment Gateway Integration
- Spring Cloud Config Server
- Externalized Configuration
- Secure REST APIs
- Spring Security
- HMAC Authentication
- Business Rule Validation
- Duplicate Transaction Detection
- Flyway Database Migration
- Layered Architecture
- Global Exception Handling
- Maven Build System

---

## Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Web | REST API Development |
| Spring Security | Security |
| Spring Cloud Config | Centralized Configuration |
| Maven | Build Tool |
| MySQL | Relational Database |
| Flyway | Database Migration |
| Stripe Java SDK | Payment Gateway Integration |
| Git | Version Control |
| GitHub | Source Code Management |

---

## Validation Flow

```
Merchant Application
        │
        ▼
Validation Service
        │
        ▼
HMAC Signature Validation
        │
        ▼
Business Rule Validation
        │
        ▼
Duplicate Transaction Validation
        │
        ▼
Payment Threshold Validation
        │
        ▼
Create Session Integration
        │
        ▼
Stripe Checkout Session
        │
        ▼
Hosted Payment Page
```

---

## Security

The project implements multiple security mechanisms including:

- HMAC SHA-256 Authentication
- Request Signature Validation
- Spring Security Filter Chain
- Global Exception Handling

---

## Database

The Validation Service maintains:

- Merchant Payment Requests
- Validation Rules
- Validation Rule Parameters
- Duplicate Transaction Records

Database schema migrations are managed using **Flyway**.

---

## REST API

### Create Stripe Checkout Session

**Endpoint**

```
POST /payment/create-session
```

**Sample Request**

```json
{
  "merchantId": "M1001",
  "transactionId": "TXN001",
  "amount": 1000,
  "currency": "INR"
}
```

**Sample Response**

```json
{
  "status": "SUCCESS",
  "checkoutUrl": "https://checkout.stripe.com/..."
}
```

---

## Prerequisites

Before running the project, install:

- Java 21
- Maven 3.9+
- MySQL 8+
- Git
- Stripe Test Account

---

## Getting Started

### Clone Repository

```bash
git clone https://github.com/shitalpatle04/Stripe-Payment-Integration-System.git
```

### Build

```bash
mvn clean install
```

### Run Individual Microservices

```bash
cd payment-config-server-integration
mvn spring-boot:run
```

```bash
cd validation-service-validator
mvn spring-boot:run
```

```bash
cd create-session-integration
mvn spring-boot:run
```

---

## Configuration

The project uses **Spring Cloud Config Server** for centralized configuration.

Configuration files are maintained inside the **payment-config-properties** module.

Configure the following values before running:

- Database URL
- Database Username
- Database Password
- Stripe Secret Key
- HMAC Secret Key

Example:

```properties
stripe.secret.key=${STRIPE_SECRET_KEY}
hmac.secret.key=${HMAC_SECRET_KEY}
```

---

## Learning Outcomes

Through this project, I gained hands-on experience with:

- Java Backend Development
- Spring Boot
- Microservices Architecture
- REST API Development
- Stripe Payment Gateway Integration
- Spring Security
- HMAC Authentication
- Spring Cloud Config
- Flyway Database Migration
- Git & GitHub

---

## Future Enhancements

- API Gateway
- Service Discovery (Eureka)
- Docker & Docker Compose
- Kubernetes Deployment
- Redis Caching
- Kafka Event Streaming
- CI/CD Pipeline
- Distributed Tracing
- Monitoring using Prometheus & Grafana

---

## Author

**Shital Patle**

Java Backend Developer

**Skills**

- Java
- Spring Boot
- Spring Security
- Spring Cloud
- Microservices
- REST APIs
- MySQL
- Maven
- Git & GitHub

**GitHub**

https://github.com/shitalpatle04
