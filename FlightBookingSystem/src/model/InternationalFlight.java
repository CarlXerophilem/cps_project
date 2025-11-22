package model;

public class InternationalFlight extends Flight {
    private String visaRequired;

    public InternationalFlight(String flightNumber, String origin, String destination, String departureDate,
            String visaRequired) {
        super(flightNumber, origin, destination, departureDate);
        this.visaRequired = visaRequired;
    }

    @Override
    public String getDetails() {
        return "International Flight " + flightNumber + " (Visa: " + visaRequired + ") on " + departureDate + ": "
                + origin + " → " + destination;
    }
}
