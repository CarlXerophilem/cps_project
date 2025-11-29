package app;

import crawler.CityCodeMapper;
import crawler.FlightCrawler;
import crawler.FlightData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.*;
import payment.*;

import java.util.List;

/**
 * PassengerInfoView - Step 2 of the booking wizard
 * Displays flight search summary and collects passenger information
 */
public class PassengerInfoView {
    private Stage stage;
    private FlightSearchView.SearchData searchData;

    // UI Components
    private TextField nameField;
    private TextField passportField;
    private TextField flightNumberField;
    private TextField phoneNumberField; // for domestic flight needs
    private ComboBox<String> paymentCombo;
    private Button backButton;
    private Button bookButton;
    private TextArea flightInfoArea; // Changed from urlDisplayArea to show flight info

    // Booking system
    private BookingManager bookingManager;

    // Flight data
    private FlightData cheapestFlight;

    public PassengerInfoView(Stage stage, FlightSearchView.SearchData searchData, BookingManager bookingManager) {
        this.stage = stage;
        this.searchData = searchData;
        this.bookingManager = bookingManager;
    }

    /**
     * Build and return the scene for passenger information
     */
    public Scene buildScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label title = new Label("✈️ Passenger Information");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));

        // Flight search summary box
        VBox summaryBox = buildSummaryBox();

        // Passenger info form
        VBox formBox = buildPassengerForm();

        // URL display area
        VBox urlBox = buildURLDisplayBox();

        // Button panel
        HBox buttonPanel = buildButtonPanel();

        root.getChildren().addAll(title, summaryBox, formBox, buttonPanel, urlBox);

        return new Scene(root, 700, 700);
    }

    private VBox buildSummaryBox() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(15));
        box.setMaxWidth(650);
        box.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea 0%, #764ba2 100%);" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

        Label summaryTitle = new Label("Your Flight Search");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        summaryTitle.setTextFill(Color.WHITE);

        String summaryText = String.format(
                "%s → %s  |  %s  |  %s",
                searchData.origin,
                searchData.destination,
                searchData.date,
                searchData.flightType);
        Label summaryLabel = new Label(summaryText);
        summaryLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        summaryLabel.setTextFill(Color.WHITE);

        box.getChildren().addAll(summaryTitle, summaryLabel);
        return box;
    }

    private VBox buildPassengerForm() {
        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(25));
        formBox.setMaxWidth(650);
        formBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Passenger name
        Label nameLabel = new Label("Passenger Name:");
        nameLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        nameField = new TextField();
        nameField.setPromptText("Full name as on passport");
        nameField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Passport number
        Label passportLabel = new Label("Passport Number:");
        passportLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        passportField = new TextField();
        passportField.setPromptText("e.g., AB1234567");
        passportField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Phone number (required for domestic flights)
        Label phoneLabel = new Label("Phone Number:" + (searchData.flightType.equals("Domestic") ? " *" : ""));
        phoneLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        phoneNumberField = new TextField();
        phoneNumberField.setPromptText("e.g., +86 138-0000-0000");
        phoneNumberField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Flight number
        Label flightNumLabel = new Label("Flight Number:");
        flightNumLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        flightNumberField = new TextField();
        flightNumberField.setPromptText("e.g., NH123, JL456");
        flightNumberField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Payment method
        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        paymentCombo = new ComboBox<>();
        paymentCombo.getItems().addAll("Credit Card", "PayPal");
        paymentCombo.setValue("Credit Card");
        paymentCombo.setMaxWidth(Double.MAX_VALUE);
        paymentCombo.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        formBox.getChildren().addAll(
                nameLabel, nameField,
                passportLabel, passportField,
                phoneLabel, phoneNumberField,
                flightNumLabel, flightNumberField,
                paymentLabel, paymentCombo);

        return formBox;
    }

    private VBox buildURLDisplayBox() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(15));
        box.setMaxWidth(650);
        box.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label flightInfoTitle = new Label("✈️ Cheapest Flight Found (from Ctrip)");
        flightInfoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        flightInfoTitle.setTextFill(Color.web("#2c3e50"));

        flightInfoArea = new TextArea();
        flightInfoArea.setEditable(false);
        flightInfoArea.setPrefRowCount(10);
        flightInfoArea.setWrapText(true);
        flightInfoArea.setStyle(
                "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
                        "-fx-font-size: 12px;" +
                        "-fx-control-inner-background: #f8f9fa;");
        flightInfoArea.setText("Loading flight information from Ctrip...");

        box.getChildren().addAll(flightInfoTitle, flightInfoArea);

        // Fetch and display cheapest flight
        fetchAndDisplayCheapestFlight();

        return box;
    }

    private HBox buildButtonPanel() {
        HBox panel = new HBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setMaxWidth(650);
        panel.setPadding(new Insets(10, 0, 10, 0));

        backButton = new Button("← Back to Search");
        backButton.setStyle(
                "-fx-background-color: #95a5a6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");

        bookButton = new Button("Book Flight & Generate Links");
        bookButton.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");

        // Hover effects
        backButton.setOnMouseEntered(e -> backButton.setStyle(
                "-fx-background-color: #7f8c8d;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        backButton.setOnMouseExited(e -> backButton.setStyle(
                "-fx-background-color: #95a5a6;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));

        bookButton.setOnMouseEntered(e -> bookButton.setStyle(
                "-fx-background-color: #229954;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        bookButton.setOnMouseExited(e -> bookButton.setStyle(
                "-fx-background-color: #27ae60;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 20;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));

        panel.getChildren().addAll(backButton, bookButton);
        return panel;
    }

    /**
     * Fetch and display the cheapest flight from Ctrip
     */
    private void fetchAndDisplayCheapestFlight() {
        // Convert city names to airport codes
        String originCode = CityCodeMapper.getCityCode(searchData.origin);
        String destCode = CityCodeMapper.getCityCode(searchData.destination);

        System.out.println("Converting: " + searchData.origin + " -> " + originCode);
        System.out.println("Converting: " + searchData.destination + " -> " + destCode);

        // Fetch flights from Ctrip (simulated)
        List<FlightData> flights = FlightCrawler.fetchFlights(originCode, destCode, searchData.date);

        if (flights.isEmpty()) {
            flightInfoArea.setText("No flights found for this route.\n\n" +
                    "Route: " + originCode.toUpperCase() + " → " + destCode.toUpperCase() + "\n" +
                    "Date: " + searchData.date);
            return;
        }

        // Get the cheapest flight
        cheapestFlight = FlightCrawler.getCheapestFlight(flights);

        // Build display text
        StringBuilder info = new StringBuilder();
        info.append("=== CHEAPEST FLIGHT ===").append("\n\n");
        info.append("Flight: ").append(cheapestFlight.getFlightNumber()).append("\n");
        info.append("Airline: ").append(cheapestFlight.getAirline()).append("\n");
        info.append("Route: ").append(cheapestFlight.getOrigin()).append(" → ");
        info.append(cheapestFlight.getDestination()).append("\n");
        info.append("Date: ").append(searchData.date).append("\n");
        info.append("Departure: ").append(cheapestFlight.getDepartureTime()).append("\n");
        info.append("Arrival: ").append(cheapestFlight.getArrivalTime()).append("\n");
        info.append("Type: ").append(
                cheapestFlight.hasTransfer() ? cheapestFlight.getTransferCount() + " transfer(s)" : "Direct flight")
                .append("\n");
        info.append("Price: ¥").append(String.format("%.2f", cheapestFlight.getPrice())).append("\n\n");

        info.append("=== ALL AVAILABLE FLIGHTS ===").append("\n");
        info.append("(Sorted: Direct flights first, then by price)\n\n");

        for (int i = 0; i < Math.min(flights.size(), 10); i++) {
            FlightData flight = flights.get(i);
            info.append(String.format("%d. %s\n", i + 1, flight.toString()));
        }

        if (flights.size() > 10) {
            info.append("\n... and ").append(flights.size() - 10).append(" more flights");
        }

        flightInfoArea.setText(info.toString());
    }

    /**
     * Process the booking and display airline URLs
     */
    public void processBooking() {
        String name = nameField.getText().trim();
        String passport = passportField.getText().trim();
        String phone = phoneNumberField.getText().trim();
        String flightNumber = flightNumberField.getText().trim();
        String paymentMethod = paymentCombo.getValue();

        // Validate inputs
        if (name.isEmpty() || passport.isEmpty() || flightNumber.isEmpty()) {
            showAlert("Missing Information", "Please fill in all passenger fields.");
            return;
        }

        // Validate phone number for domestic flights
        if (searchData.flightType.equals("Domestic") && phone.isEmpty()) {
            showAlert("Missing Phone Number", "Phone number is required for domestic flights.");
            return;
        }

        try {
            // Create booking objects
            Passenger passenger = new Passenger(name, passport, phone);

            Flight flight;
            if (searchData.flightType.equals("Domestic")) {
                flight = new DomesticFlight(flightNumber, searchData.origin, searchData.destination,
                        searchData.date, "ANA"); // Default airline
            } else {
                flight = new InternationalFlight(flightNumber, searchData.origin, searchData.destination,
                        searchData.date, "Required"); // Visa status
            }

            Payment payment = paymentMethod.equals("Credit Card") ? new CreditCardPayment() : new PayPalPayment();

            Booking booking = new Booking(flight, passenger, payment);

            // Get price from cheapest flight if available
            double price = (cheapestFlight != null) ? cheapestFlight.getPrice() : 1000.0;
            booking.confirm(price); // Process payment
            bookingManager.addBooking(booking);

            // Show success message with flight details
            String successMessage = "Your flight has been booked successfully!\n\n" +
                    booking.getSummary() + "\n\n";

            if (cheapestFlight != null) {
                successMessage += "Booked Flight Details:\n" +
                        "Flight: " + cheapestFlight.getFlightNumber() + "\n" +
                        "Airline: " + cheapestFlight.getAirline() + "\n" +
                        "Price: ¥" + String.format("%.2f", cheapestFlight.getPrice()) + "\n" +
                        (cheapestFlight.hasTransfer() ? "(" + cheapestFlight.getTransferCount() + " transfer(s))\n"
                                : "(Direct flight)\n");
            }

            showSuccessAlert("Booking Confirmed!", successMessage);

        } catch (Exception e) {
            showAlert("Booking Error", "Failed to process booking: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getBookButton() {
        return bookButton;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
