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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * FlightSearchView - Step 1 of the booking wizard
 * Collects flight search parameters: origin, destination, date, and flight type
 */
public class FlightSearchView {
    private Stage stage;
    private TextField originField;
    private TextField destinationField;
    private DatePicker datePicker;
    private ComboBox<String> flightTypeCombo;
    private Button searchButton;

    public FlightSearchView(Stage stage) {
        this.stage = stage;
    }

    /**
     * Build and return the scene for flight search
     */
    public Scene buildScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label title = new Label("✈️ Flight Search");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.web("#2c3e50"));

        // Subtitle
        Label subtitle = new Label("Find your cheapest flight on Xie Chen");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        // Form container
        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(25));
        formBox.setMaxWidth(500);
        formBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Origin field
        Label originLabel = new Label("Departure From:");
        originLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        originField = new TextField();
        originField.setPromptText("e.g., Tokyo, Narita, HND");
        originField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Destination field
        Label destLabel = new Label("Flying To:");
        destLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        destinationField = new TextField();
        destinationField.setPromptText("e.g., Osaka, Kansai, KIX");
        destinationField.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Date picker
        Label dateLabel = new Label("Departure Date:");
        dateLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        datePicker.setValue(LocalDate.now().plusDays(7)); // Default to 7 days from now
        datePicker.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Flight type combo
        Label typeLabel = new Label("Flight Type:");
        typeLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 13));
        flightTypeCombo = new ComboBox<>();
        flightTypeCombo.getItems().addAll("Domestic", "International");
        flightTypeCombo.setValue("Domestic");
        flightTypeCombo.setMaxWidth(Double.MAX_VALUE);
        flightTypeCombo.setStyle("-fx-padding: 10; -fx-font-size: 13px;");

        // Search button
        searchButton = new Button("Search Flights");
        searchButton.setMaxWidth(Double.MAX_VALUE);
        searchButton.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");
        searchButton.setOnMouseEntered(e -> searchButton.setStyle(
                "-fx-background-color: #2980b9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"));
        // here, add a preview function that shows matched locations with user input.
        // e.g.: type "shangh" shows "shanghai".

        // Note: The search button action is handled in Main.java
        // which navigates to the PassengerInfoView

        // Add all fields to form
        formBox.getChildren().addAll(
                originLabel, originField,
                destLabel, destinationField,
                dateLabel, datePicker,
                typeLabel, flightTypeCombo,
                new Separator(),
                searchButton);

        root.getChildren().addAll(title, subtitle, formBox);

        return new Scene(root, 600, 550);
    }

    /**
     * Get the search button to attach event handlers
     */
    public Button getSearchButton() {
        return searchButton;
    }

    /**
     * Get collected search data
     */
    public SearchData getSearchData() {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        LocalDate date = datePicker.getValue();
        String flightType = flightTypeCombo.getValue();

        // Validate inputs
        if (origin.isEmpty() || destination.isEmpty() || date == null) {
            showAlert("Missing Information", "Please fill in all fields before searching.");
            return null;
        }

        if (date.isBefore(LocalDate.now())) {
            showAlert("Invalid Date", "Please select a future date.");
            return null;
        }

        String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return new SearchData(origin, destination, dateStr, flightType);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Data class to hold flight search parameters
     */
    public static class SearchData {
        public final String origin;
        public final String destination;
        public final String date;
        public final String flightType;

        public SearchData(String origin, String destination, String date, String flightType) {
            this.origin = origin;
            this.destination = destination;
            this.date = date;
            this.flightType = flightType;
        }
    }
}
