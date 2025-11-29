package model;

public abstract class Flight {
    protected String flightNumber;
    protected String origin;
    protected String destination;
    protected String date;

    public Flight(String flightNumber, String origin, String destination, String date) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
    }

    public abstract String getDetails();

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getDepartureDate() {
        return date;
    }
}
