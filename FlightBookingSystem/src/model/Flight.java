package model;

public abstract class Flight {
    protected String flightNumber;
    protected String origin;
    protected String destination;
    protected String departureDate; // Format: yyyy-MM-dd

    public Flight(String flightNumber, String origin, String destination, String departureDate) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public abstract String getDetails();
}
