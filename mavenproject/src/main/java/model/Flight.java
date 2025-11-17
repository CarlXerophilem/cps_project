package model;

/**
 * Abstract Flight base class to demonstrate inheritance.
 */
public abstract class Flight {
    protected String flightNo;
    protected String origin;
    protected String destination;

    public Flight(String flightNo, String origin, String destination) {
        this.flightNo = flightNo;
        this.origin = origin;
        this.destination = destination;
    }

    public String getFlightNo() { return flightNo; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }

    /** human readable summary */
    public abstract String getSummary();
}
