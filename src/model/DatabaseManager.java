package model;

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
    private static final String DB_URL = "jdbc:mysql://localhost:3306/flight_booking";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0000"; // Update with your MySQL password

    private Connection connection;

    /*
     * Constructor establishes database connection and initializes schema
     */
    public DatabaseManager() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println(" > Connected to database successfully <");

            // Create tables if they don't exist
            createTablesIfNotExist();

        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException es) {
            System.err.println("✗ Database connection failed!");
            System.err.println("Make sure MySQL is running and the 'flight_booking' database exists.");
            System.err.println("You can create it with: CREATE DATABASE flight_booking;");
            es.printStackTrace();
        }
    }

    /*
     * Creates database tables if they don't already exist
     */
    private void createTablesIfNotExist() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS bookings (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  passenger_name VARCHAR(255) NOT NULL," +
                "  passport_number VARCHAR(50) NOT NULL," +
                "  flight_number VARCHAR(20) NOT NULL," +
                "  origin VARCHAR(100) NOT NULL," +
                "  destination VARCHAR(100) NOT NULL," +
                "  departure_date DATE NOT NULL," +
                "  flight_type VARCHAR(50) NOT NULL," +
                "  payment_method VARCHAR(50) NOT NULL," +
                "  booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("[Info] Database tables ready");
        } catch (SQLException e) {
            System.err.println("[Error] Failed to create tables");
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
        String insertSQL = "INSERT INTO bookings (passenger_name, passport_number, flight_number, " +
                "origin, destination, departure_date, flight_type, payment_method) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            Flight flight = booking.getFlight();
            Passenger passenger = booking.getPassenger();

            pstmt.setString(1, passenger.getName());
            pstmt.setString(2, passenger.getPassportNumber());
            pstmt.setString(3, flight.getFlightNumber());
            pstmt.setString(4, flight.getOrigin());
            pstmt.setString(5, flight.getDestination());
            pstmt.setDate(6, Date.valueOf(flight.getDepartureDate()));
            pstmt.setString(7, flight instanceof DomesticFlight ? "Domestic" : "International");
            pstmt.setString(8, booking.getPaymentMethod());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[Error] Failed to insert booking");
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
            System.err.println("[Error] Failed to retrieve bookings");
            e.printStackTrace();
        }

        return bookings;
    }

    /**
     * Closes the database connection
     * Should be called when the application is shutting down
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("[Info] Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("[Error] Error closing database connection");
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
