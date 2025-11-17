package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Booking {
    private final Passenger passenger;
    private final Flight flight;
    private final LocalDateTime bookedAt;

    public Booking(Passenger passenger, Flight flight) {
        this.passenger = passenger;
        this.flight = flight;
        this.bookedAt = LocalDateTime.now();
    }

    public void saveToDatabase() throws SQLException {
        // this method assumes direct_flights table for demo purposes; adjust schema if you store bookings separately
        Connection c = Database.getConnection();
        String sql = "INSERT INTO bookings (flight_no, origin, destination, passenger_name, booked_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, flight.getFlightNo());
            ps.setString(2, flight.getOrigin());
            ps.setString(3, flight.getDestination());
            ps.setString(4, passenger.getFirstName() + " " + passenger.getLastName());
            ps.setObject(5, bookedAt);
            ps.executeUpdate();
        }
    }

    // getters if needed
}
