# JobLink - Smart Job & Skill Matching Platform

A microservices-based platform connecting job seekers with employers.

## Current Status: Phase 1 - Infrastructure Setup

This is the initial setup with core infrastructure components:

### Backend Services

1. **Eureka Server** (Port 8761)
   - Service discovery and registry
   - All microservices register here

2. **API Gateway** (Port 8765)
   - Single entry point for all services
   - Routes requests to appropriate microservices

### Frontend

- **Angular Application** (Port 4200)
  - Basic Angular project setup
  - Ready for development

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Node.js 18+
- Angular CLI

### Running the Services

1. **Start Eureka Server** (must be first):
   ```bash
   cd backend/eureka-server
   mvn spring-boot:run
   ```
   Access: http://localhost:8761

2. **Start API Gateway**:
   ```bash
   cd backend/api-gateway
   mvn spring-boot:run
   ```
   Access: http://localhost:8765

3. **Start Frontend**:
   ```bash
   cd frontend/joblink-angular
   npm install
   ng serve
   ```
   Access: http://localhost:4200

## Next Steps

- [ ] Add Job Posting Service
- [ ] Add Profile Service
- [ ] Add Matching Service
- [ ] Add Application Service
- [ ] Configure Keycloak Authentication
- [ ] Set up MySQL Database

## Repository

[GitHub Repository](https://github.com/FouzriChayma/JobLink)
