package model;

public abstract class Flight {
    protected String flightNumber;
    protected String origin;
    protected String destination;

    public Flight(String flightNumber, String origin, String destination) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
    }

    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }

    public abstract String getDetails();
}
