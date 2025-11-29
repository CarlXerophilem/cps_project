package app;

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
import crawler.FlightRouteGenerator;

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
    private TextField phoneNumber;
    private Button backButton;
    private Button bookButton;
    private TextArea urlDisplayArea;

    // Additional fields for booking
    private String flightNumber = "MU5101"; // Default/Dummy
    private double Price = 1200.00; // Default/Dummy
    private String paymentMethod = "Credit Card"; // Default

    // Booking system
    private BookingManager bookingManager;

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

        // Passenger phone number
        Label phoneNumberLabel = new Label("Phone Number:");
        phoneNumberLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        phoneNumber = new TextField();
        phoneNumber.setPromptText("e.g., +65 12345678");
        phoneNumber.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

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

        formBox.getChildren().addAll(
                nameLabel, nameField,
                passportLabel, passportField,
                phoneNumberLabel, phoneNumber);

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

        Label urlTitle = new Label("🔗 Airline Booking Links (Price Comparison)");
        urlTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        urlTitle.setTextFill(Color.web("#2c3e50"));

        urlDisplayArea = new TextArea();
        urlDisplayArea.setEditable(false);
        urlDisplayArea.setPrefRowCount(8);
        urlDisplayArea.setWrapText(true);
        urlDisplayArea.setStyle(
                "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
                        "-fx-font-size: 11px;" +
                        "-fx-control-inner-background: #f8f9fa;");
        urlDisplayArea.setText("Booking URLs will appear here after you complete the booking...");

        box.getChildren().addAll(urlTitle, urlDisplayArea);
        box.setVisible(false); // Hidden initially
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
     * Process the booking and display airline URLs
     */
    public void processBooking() {
        String name = nameField.getText().trim();
        String passport = passportField.getText().trim();
        String phoneNumber = this.phoneNumber.getText().trim();

        // Validate inputs
        if (name.isEmpty() || passport.isEmpty() || phoneNumber.isEmpty()) {
            showAlert("Missing Information", "Please fill in all passenger fields.");
            return;
        }

        try {
            // Create booking objects
            Passenger passenger = new Passenger(name, passport, phoneNumber);

            Flight flight;
            if (searchData.flightType.equals("Domestic")) {
                flight = new DomesticFlight(flightNumber, searchData.origin, searchData.destination,
                        searchData.date, "ANA"); // Default airline
            } else {
                flight = new InternationalFlight(flightNumber, searchData.origin, searchData.destination,
                        searchData.date, "Required"); // Visa status
            }

            Payment payment = new Payment();
            if (paymentMethod.equals("Credit Card")) {
                payment.CreditCardPayment(Price);
            } else {
                payment.PayPalPayment(Price);
            }

            Booking booking = new Booking(flight, passenger, payment);
            booking.confirm(Price); // Price from crawler
            bookingManager.addBooking(booking);

            // Generate airline URLs
            List<String> urls = FlightRouteGenerator.generateUrls(searchData.origin, searchData.destination,
                    searchData.date);

            // Display URLs
            StringBuilder urlText = new StringBuilder();
            for (String url : urls) {
                urlText.append(url).append("\n");
            }
            urlDisplayArea.setText(urlText.toString());
            urlDisplayArea.getParent().setVisible(true);

            // Show success message
            showSuccessAlert("Booking Confirmed!",
                    "Your flight has been booked successfully!\n\n" +
                            booking.getSummary() + "\n\n" +
                            "Check the airline links below to compare prices.");

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
