package model;

public class DomesticFlight extends Flight {
    private String airline;

    public DomesticFlight(String flightNumber, String origin, String destination, String airline) {
        super(flightNumber, origin, destination);
        this.airline = airline;
    }

    @Override
    public String getDetails() {
        return "Domestic Flight " + flightNumber + " (" + airline + "): " + origin + " → " + destination;
    }
}
