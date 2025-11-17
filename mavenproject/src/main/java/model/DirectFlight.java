package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;

/**
 * DirectFlight is a concrete Flight representing a direct route.
 * It also provides JavaFX properties for TableView binding.
 */
public class DirectFlight extends Flight {
    private final StringProperty flightNoProp = new SimpleStringProperty();
    private final StringProperty originProp = new SimpleStringProperty();
    private final StringProperty destinationProp = new SimpleStringProperty();

    // optional - scheduled departure time
    private LocalDateTime departTime;

    public DirectFlight(String flightNo, String origin, String destination) {
        super(flightNo, origin, destination);
        this.flightNoProp.set(flightNo);
        this.originProp.set(origin);
        this.destinationProp.set(destination);
    }

    public DirectFlight(String flightNo, String origin, String destination, LocalDateTime departTime) {
        this(flightNo, origin, destination);
        this.departTime = departTime;
    }

    @Override
    public String getSummary() {
        return String.format("%s: %s → %s", flightNo, origin, destination);
    }

    // Getters for TableView binding:
    public String getFlightNo() { return flightNoProp.get(); }
    public String getOrigin() { return originProp.get(); }
    public String getDestination() { return destinationProp.get(); }

    public StringProperty flightNoProperty() { return flightNoProp; }
    public StringProperty originProperty() { return originProp; }
    public StringProperty destinationProperty() { return destinationProp; }

    public LocalDateTime getDepartTime() { return departTime; }
    public void setDepartTime(LocalDateTime departTime) { this.departTime = departTime; }
}
