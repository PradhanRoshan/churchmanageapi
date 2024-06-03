# Church Management System

A comprehensive application designed to manage various aspects of church operations, including user authentication, member management, event scheduling, volunteer activities, financial tracking, communication, and reporting. Built with Spring Boot, Angular, and MySQL.

## Table of Contents

- [Features](#features)
- [Technologies](#technologies)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Authentication**: Register, login, and manage user profiles.
- **Member Management**: Add, view, update, and delete church members.
- **Role Management**: Assign roles to users for appropriate access control.
- **Event Management**: Schedule and manage church events.
- **Volunteer Management**: Organize and track volunteer activities.
- **Financial Management**: Track tithes, offerings, and budgets.
- **Communication**: Send and receive messages within the system.
- **Reporting**: Generate and view various reports.
- **Address Management**: Manage member and church addresses.
- **Logging and Auditing**: Monitor user actions and system changes.

## Technologies

- **Backend**: Spring Boot
- **Frontend**: Angular
- **Database**: MySQL

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- Node.js and npm
- MySQL

### Installation

#### Backend

1. **Clone the repository**
    ```bash
    git clone https://github.com/your-username/ChurchManageAPI.git
    cd ChurchManageAPI
    ```

2. **Configure the database**
    - Create a MySQL database named `church_management_db`.
    - Update the `src/main/resources/application.properties` file with your MySQL configuration:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/church_management_db
      spring.datasource.username=your_username
      spring.datasource.password=your_password
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
      ```

3. **Build and run the application**
    ```bash
    ./mvnw spring-boot:run
    ```

4. **Access the application**
    - The API will be available at `http://localhost:8080`.

#### Frontend

1. **Clone the repository**
    ```bash
    git clone https://github.com/your-username/ChurchManageUI.git
    cd ChurchManageUI
    ```

2. **Install dependencies**
    ```bash
    npm install
    ```

3. **Run the application**
    ```bash
    ng serve
    ```

4. **Access the application**
    - The frontend will be available at `http://localhost:4200`.

## Usage

### API Endpoints

- **User Registration**
  - `POST /api/register`
- **User Login**
  - `POST /api/login`
- **Profile Management**
  - `GET /api/users/{id}`
  - `PUT /api/users/{id}`
- **Member Management**
  - `GET /api/members`
  - `POST /api/members`
  - `PUT /api/members/{id}`
  - `DELETE /api/members/{id}`
- **Event Management**
  - `GET /api/events`
  - `POST /api/events`
  - `PUT /api/events/{id}`
  - `DELETE /api/events/{id}`
- **Tithes and Offerings Management**
  - `GET /api/tithes`
  - `POST /api/tithes`
  - `PUT /api/tithes/{id}`
  - `DELETE /api/tithes/{id}`
- **Communication**
  - `GET /api/communications`
  - `POST /api/communications`
  - `DELETE /api/communications/{id}`
- **Report Generation**
  - `GET /api/reports`
  - `POST /api/reports`
  - `DELETE /api/reports/{id}`
- **Address Management**
  - `GET /api/addresses`
  - `POST /api/addresses`
  - `PUT /api/addresses/{id}`
  - `DELETE /api/addresses/{id}`

## Contributing

We welcome contributions! Please read our [Contributing Guide](CONTRIBUTING.md) to learn how you can help.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.



# ChurchManageAPI
API services for Church Management System, handling backend operations such as user management, event scheduling, and donation tracking.

Church Management System Business Plan
Executive Summary

The Church Management System (ChMS) is a comprehensive software solution designed to facilitate and streamline the day-to-day operations of churches. This system aims to assist church staff and volunteers in managing administrative tasks, enhancing communication, and fostering community engagement. The ChMS will include features such as member management, event scheduling, donation tracking, communication tools, and reporting.
Objectives

    Develop a user-friendly and scalable ChMS.
    Improve administrative efficiency and reduce manual work.
    Enhance communication within the church community.
    Provide robust tools for financial management and reporting.
    Ensure data security and privacy.

Target Market

    Churches of all denominations and sizes.
    Non-profit religious organizations.

Key Features

    Member Management: Track member information, attendance, and involvement.
    Event Management: Schedule and manage church events, including services, meetings, and special events.
    Donation and Financial Management: Track donations, manage budgets, and generate financial reports.
    Communication Tools: Email, SMS, and push notifications to communicate with members.
    Volunteer Management: Schedule and track volunteer activities.
    Reporting and Analytics: Generate reports on various aspects of church operations.

Revenue Model

    Subscription-based pricing model with different tiers (basic, standard, premium).
    Customization and consulting services for large churches.
    Integration services with other software solutions.


    https://trello.com/b/c7QJNBjA/church-management-software
    
