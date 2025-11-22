# Flight Booking System - Quick Setup Guide

## Prerequisites
- MySQL Server installed and running
- Java 17+ installed
- Maven installed

## Database Setup
```powershell
# 1. Start MySQL
net start MySQL

# 2. Create database
mysql -u root -p
CREATE DATABASE flight_booking;
EXIT;
```

## Update Database Password (if needed)
Edit `src/model/DatabaseManager.java`:
```java
private static final String DB_PASSWORD = "0000";
```

## Build & Run
```powershell
# Navigate to project directory
cd c:\Users\Administrator\Desktop\mathss\script\cps_2231\cps_project\FlightBookingSystem

# Compile
mvn clean compile

# Run application
mvn javafx:run
```

## Features
✨ **Step 1**: Flight search with date picker  
✨ **Step 2**: Passenger info and booking  
✨ **URL Generation**: Compare prices across airlines (ANA, JAL, Emirates, United, etc.)  
✨ **SQL Database**: All bookings saved to MySQL

## How to Use
1. **Step 1**: Enter origin (e.g., Tokyo), destination (e.g., Osaka), select date, choose Domestic/International
2. Click "Search Flights"
3. **Step 2**: Enter passenger name, passport, flight number, select payment method
4. Click "Book Flight & Generate Links"
5. **View URLs**: Copy airline URLs to compare prices in browser

## Troubleshooting
- **Database connection failed**: Make sure MySQL is running and `flight_booking` database exists
- **Compile errors**: Ensure Java 17+ is installed (`java -version`)
- **ClassPath issues**: Try `mvn clean` then `mvn compile` again

Enjoy your enhanced flight booking system! 🚀
