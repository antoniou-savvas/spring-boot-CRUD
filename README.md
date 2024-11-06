#Project Purpose

This project was created as part of a technical assessment for a job interview, with a three-day window to complete and submit it. The files included in this repository remain exactly as submitted during the interview process, showcasing the unmodified work. This project demonstrates my ability in RESTful API design, database management, and Docker integration, implemented through a Java-based microservice using Spring Boot for managing customer accounts and invoicing. Additional information about the assessment requirements can be found in the `Java Developer Case Study.pdf` included in this repository.

## Getting Started
To review this project, clone the repository to get the files locally.

# Running the Application

## Setting Up the Database
This project uses PostgreSQL as the database. To simplify the setup process, Docker is used to create and initialize the database.

## Prerequisites
- Docker installed on your machine
- Docker Engine running

## Instructions
1. Start the Docker Containers:
Navigate to the root directory of the project, open a terminal, and use the following command to build the Docker image and start the containers for the services:

	```sh
	docker-compose up --build
	```

This command will start the PostgreSQL database and the Spring Boot application. The database will be initialized with the necessary schema using the `init.sql` script.

2. Access the Application:
Once the containers are up and running, you can access the application at `http://localhost:8080`.

3. Access the Database:
Use the following to connect to the database.
- "label": `zalex-invoicing`,
- "host": `localhost`,
- "user": `zalex_user`,
- "port": `5432`,
- "ssl": `false`,
- "database": `zalex_invoicing`,
- "password": `zalex_password`

# Database Schema
The schema includes the following tables:

- `customer`: Stores customer information.
- `product`: Stores product information.
- `invoice`: Stores invoice information linked to a customer.
- `invoice_item`: Stores items within an invoice, linking to both the invoice and product tables.

# Database Initialization Script
The `init.sql` script initializes the database schema and can be found in the root directory of the project. It creates the necessary tables and relationships as described in the schema section.

# API Endpoints
The application provides RESTful APIs to manage customers, products, and invoices.

## Customer Endpoints
- Create Customer: POST /customers
- Get Customer by ID: GET /customers/{customerId}
- Update Customer: PUT /customers/{customerId}
- Delete Customer: DELETE /customers/{customerId}
- Get All Customers: GET /customers
## Product Endpoints
- Create Product: POST /products
- Get Product by ID: GET /products/{productId}
- Update Product: PUT /products/{productId}
- Delete Product: DELETE /products/{productId}
- Get All Products: GET /products
## Invoice Endpoints
- Create Invoice: POST /invoices
- Get Invoice by ID: GET /invoices/{invoiceId}
- Update Invoice: PUT /invoices/{invoiceId}
- Delete Invoice: DELETE /invoices/{invoiceId}
- Get All Invoices: GET /invoices

# Running Tests
To run the unit tests, navigate to the root directory of the project and use the following command:

	```sh
	./mvnw test
	```

This will execute all the unit tests and display the results.

# Docker Compose Configuration
The docker-compose.yml file defines the services and their configurations. It includes the PostgreSQL database and the Spring Boot application.
It handles all the necessary configurations for the services, including environment variables for the PostgreSQL database connection. You do not need to change any environment variables manually.