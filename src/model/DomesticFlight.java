package model;

public class DomesticFlight extends Flight {
    private String airline;

    public DomesticFlight(String flightNumber, String origin, String destination, String departureDate,
            String airline) {
        super(flightNumber, origin, destination, departureDate);
        this.airline = airline;
    }

    @Override
    public String getDetails() {
        return "Domestic Flight " + flightNumber + " (" + airline + ") on " + departureDate + ": " + origin + " → "
                + destination;
    }
}
