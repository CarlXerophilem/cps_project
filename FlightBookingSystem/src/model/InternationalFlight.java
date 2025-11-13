package model;

public class InternationalFlight extends Flight {
    private String visaRequired;

    public InternationalFlight(String flightNumber, String origin, String destination, String visaRequired) {
        super(flightNumber, origin, destination);
        this.visaRequired = visaRequired;
    }

    @Override
    public String getDetails() {
        return "International Flight " + flightNumber + " (Visa: " + visaRequired + "): " + origin + " → " + destination;
    }
}

