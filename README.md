# SmartQueue Backend

A Spring Boot-based queue management system that allows businesses to manage customer queues digitally, reducing wait times and improving customer experience.


## 🎥 Project Demo Video
[▶️ Watch Demo Video](https://drive.google.com/file/d/12o9gY6HG8ty95Z8edJWnZJrCbF-uyLwt/view?usp=sharing)

## 🚀 Features

### Core Functionality
- **Digital Queue Management**: Create and manage virtual queues for events/services
- **Ticket Generation**: Generate unique ticket codes for customers
- **Real-time Status Updates**: Track queue position and estimated wait times
- **Multi-role Authentication**: Separate authentication for customers and admins
- **Queue Operations**: Call next customer, skip tickets, mark as complete

### User Roles
- **Customers**: Join queues, check ticket status, receive notifications
- **Admins**: Create events, manage queues, control queue flow
- **Super Admin**: Approve admin registrations

## Technology Stack

- **Framework**: Spring Boot 4.0.0
- **Language**: Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT authentication
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Additional Libraries**: Lombok, JJWT

## API Endpoints

### Customer Endpoints
- `POST /api/customer/signup` - Customer registration
- `POST /api/customer/login` - Customer authentication
- `POST /api/tickets` - Generate ticket for queue
- `POST /api/tickets/book/{eventId}` - Book ticket for specific event
- `GET /api/tickets/{ticketCode}` - Get ticket status
- `GET /api/tickets/{ticketCode}/notification` - Check if it's customer's turn

### Admin Endpoints
- `POST /api/admin/login` - Admin authentication
- `GET /api/admin/events` - Get all active events
- `POST /api/admin/events` - Create new event
- Queue management endpoints for calling next, completing tickets

### Public Endpoints
- `GET /api/display/**` - Public display information (no auth required)

## Setup Instructions

### Prerequisites
- Java 21
- PostgreSQL database
- Maven

### Database Setup
1. Create PostgreSQL database named `smartQueue`
2. Update database credentials in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/smartQueue
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

### Running the Application
1. Clone the repository
2. Navigate to project directory
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```

The application will start on `http://localhost:8081`

## Configuration

- JWT-based authentication with role-based access control
- CORS enabled for frontend integration
- Hibernate DDL auto-update for development

## Key Features Implementation

### Queue Management
- Position-based queue system
- Automatic position updates when customers are called
- Status tracking (WAITING, IN_PROGRESS, DONE, SKIPPED)
- Estimated wait time calculation

## Future Enhancements

- WebSocket integration for real-time updates
- SMS/Email notifications
- Analytics and reporting
- Multi-location support
- Mobile app integration