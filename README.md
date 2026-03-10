# Event Driven Ticket System

## Architecture

Inventory Service → Kafka → Booking Service

## Technologies
Spring Boot
Apache Kafka
H2 Database
JUnit
Mockito
Maven

## Services

Inventory Service
- Manages seats
- Publishes Kafka events

Booking Service
- Consumes Kafka events
- Shows events and seats

## Running the Project

Start Kafka
Run inventory-service
Run booking-service

CIC-5 Seat reservation implementation