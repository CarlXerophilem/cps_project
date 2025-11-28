package model;

import payment.CreditCardPayment;
import payment.Payment;

import javax.lang.model.util.Elements;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseManager handles all MySQL database operations for the flight booking
 * system.
 * Manages connections, creates schema, and provides methods for CRUD
 * operations.
 */
public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String DB_USER = "root";
    private String DB_PASSWORD = "54711425xyz";

    private Connection connection;

    /**
     * Constructor establishes database connection and initializes schema
     */
    public DatabaseManager() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Connected to database successfully");

            // Create tables if they don't exist
            createTablePersonal();
            createTableFlightInfo();

        } catch (ClassNotFoundException e1) {
            System.err.println("✗ MySQL JDBC Driver not found!");
            e1.printStackTrace();
        } catch (SQLException e2) {
            System.err.println("✗ Database connection failed!");
            System.err.println("Make sure MySQL is running and the 'flight_booking' database exists.");
            System.err.println("You can create it with: CREATE DATABASE flight_booking;");
            e2.printStackTrace();
        }
    }

    /**
     * Creates database tables (PersonalInfo+FlightInfo) if they don't already exist
     */
    private void createTablePersonal() {
        String createTablePersonalSQL = "CREATE TABLE IF NOT EXISTS Personal_Information (" +
                " Register_id INT AUTO_INCREMENT PRIMARY KEY, " +
                " Passenger_name VARCHAR(255) NOT NULL, " +
                " Passport_number VARCHAR(50) NOT NULL, " +
                " Phone_number VARCHAR(50) " +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTablePersonalSQL);
            System.out.println("Database table(Personal) ready");
        } catch (SQLException e) {
            System.err.println("Failed to create table(Personal)");
            e.printStackTrace();
        }
    }

    private void createTableFlightInfo() {
        String createTableFlightSQL = "CREATE TABLE IF NOT EXISTS Flight_Information (" +
                " Flight_number VARCHAR(50) PRIMARY KEY, " +
                " Departure VARCHAR(255) NOT NULL, " +
                " Arrival VARCHAR(50) NOT NULL, " +
                " Departure_time VARCHAR(50) " +
                " Arrival_time VARCHAR(50) " +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableFlightSQL);
            System.out.println("Database tables(FlightInfo) ready");
        } catch (SQLException e) {
            System.err.println("Failed to create table(FlightInfo)");
            e.printStackTrace();
        }
    }


    /**
     * Insert a new booking into the database
     * 
     * @param booking The booking to save
     * @return true if insertion was successful, false otherwise
     */
    public boolean insertBooking(Booking booking) {
        String insertSQLpersonal = "INSERT INTO Personal_Information (Passenger_name, Passport_number, Phone_number) " +
                "VALUES (?, ?, ?)";

        String insertSQLflight = "INSERT INTO Flight_Information (Flight_number, Flight_type, origin, destination, " +
                "Departure_date, Departure_time, Arrival_time, price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt1 = connection.prepareStatement(insertSQLpersonal);
                PreparedStatement pstmt2 = connection.prepareStatement(insertSQLflight)) {
            Flight flight = booking.getFlight();
            Passenger passenger = booking.getPassenger();

            //personalInfo Table
            pstmt1.setString(1, passenger.getName());
            pstmt1.setString(2, passenger.getPassportNumber());
            pstmt1.setString(3, passenger.getPhoneNumber());

            //flightInfo Table
            pstmt2.setString(1, flight.getFlightNumber());
            pstmt2.setString(2, flight instanceof DomesticFlight ? "Domestic" : "International");
            pstmt2.setString(3, flight.getOrigin());
            pstmt2.setString(4, flight.getDestination());
            pstmt2.setDate(5, Date.valueOf(flight.getDepartureDate()));

            //!!!Missing: departure_time, arrival_time
            //Departure time?
            //Arrival time?
            pstmt2.setDouble(8, new CreditCardPayment().importPrice(flight.getFlightNumber()));

            int rowsAffected1 = pstmt1.executeUpdate();
            int rowsAffected2 = pstmt2.executeUpdate();

            return rowsAffected1 > 0 && rowsAffected2 > 0;

        } catch (SQLException e) {
            System.err.println("✗ Failed to insert booking");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieve all bookings from the database
     * 
     * @return List of booking summaries as strings
     */
    public List<String> getAllBookings() {
        List<String> bookings = new ArrayList<>();
        String selectSQL = "SELECT * FROM bookings ORDER BY booking_date DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                String summary = String.format(
                        "[#%d] %s (Passport: %s) - Flight %s: %s → %s on %s (%s) [Paid via %s]",
                        rs.getInt("id"),
                        rs.getString("passenger_name"),
                        rs.getString("passport_number"),
                        rs.getString("flight_number"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getDate("departure_date"),
                        rs.getString("flight_type"),
                        rs.getString("payment_method"));
                bookings.add(summary);
            }

        } catch (SQLException e) {
            System.err.println("✗ Failed to retrieve bookings");
            e.printStackTrace();
        }

        return bookings;
    }

    // Flight Info Database
    public double passPrice(String flightNum){
        double price = 0; //default value

        String fetchPrice = "SELECT price FROM Flight_Information WHERE Flight_number = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(fetchPrice)){
            pstmt.setString(1,flightNum);
            ResultSet fetchResult = pstmt.executeQuery();

            if(fetchResult.next()){
                price = fetchResult.getDouble("price");
            };

            fetchResult.close();

        }catch (SQLException eSQL){
            System.out.println("Price of flight not found...");
        }finally {
            return price;
        }


    }








    /**
     * Closes the database connection
     * Should be called when the application is shutting down
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error closing database connection");
            e.printStackTrace();
        }
    }

    /**
     * Check if database connection is active
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
