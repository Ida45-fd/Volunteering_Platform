# Volunteering Platform – JavaFX Application

A desktop-based Volunteering Management System built using **JavaFX**, **MySQL**, and **Maven**.  
The platform allows NGOs to post volunteer events and volunteers to browse, apply, and track their participation.

This project demonstrates **CRUD operations**, **API integration**, **database connectivity**, and **modular Java architecture**.

---

## Features

###  **User Roles**
- **Volunteer**
- **NGO / Organization**

###  **Authentication**
- User Registration (Volunteer/NGO)
- Login with validation
- Auto-fetch location using API

---

##  **NGO Features**
- Add Event  
- View Events  
- Update Event Details  
- Auto-calculated:
  - `max_volunteers`
  - `booked_count`
  - `remaining_slots`
- See list of volunteers who applied  
- Manage event status  

---

##  **Volunteer Features**
- Browse all events
- See *nearby events first* (Location API)
- Apply for events
- See event details
- Prevent duplicate application  
- Logout functionality

---

## **API Integration**

We integrated a **public REST API** to auto-detect the user's city:


### How it works:
- Java sends an HTTP GET request  
- Receives JSON containing city, region, country  
- Parses the city  
- UI uses this to:
  - Auto-fill registration location  
  - Sort events → Nearby events first

This makes the project more **smart, modern, and real-world**.

---

##  **Database Schema (MySQL)**

### ✔ `users` Table
| Field | Type |
|------|------|
| userid | VARCHAR |
| name | VARCHAR |
| email | VARCHAR |
| password | VARCHAR |
| role | ENUM('VOLUNTEER','NGO') |

### ✔ `events` Table
| Field | Type |
|------|------|
| id | INT AUTO_INCREMENT |
| event_name | VARCHAR |
| date | DATE |
| location | VARCHAR |
| max_volunteers | INT |
| booked_count | INT |
| remaining_slots | INT |

### ✔ `event_volunteers` Table
(Many-to-many mapping)

| Field | Type |
|------|------|
| id | INT AUTO_INCREMENT |
| event_id | INT |
| volunteer_id | VARCHAR |
| applied_date | TIMESTAMP |

---
app/
├── api/ → External API (LocationAPI)
├── controllers/ → JavaFX Controllers
├── dao/ → DB access layer
├── models/ → Data models (Event, User)
├── services/ → Authentication & business logic
└── MainApp.java → Launcher

### MVC + DAO Pattern Used:
- Controllers → UI logic  
- Services → Business logic  
- DAO → Database operations  
- Models → Data structures  

---

##  **Tech Stack**

###  Frontend
- JavaFX (FXML + CSS)

###  Backend
- Java 17  
- OOP concepts  
- REST API (Location API)

###  Database
- MySQL 8  
- JDBC (MySQL Connector/J)

###  Build Tool
- Maven  
- JavaFX Maven Plugin  

---

## **How to Run the Project**

### 1. Clone the repository
```bash
git clone https://github.com/Ida45-fd/Volunteering_Platform.git




