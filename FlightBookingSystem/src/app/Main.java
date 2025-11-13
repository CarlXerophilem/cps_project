package app;

//--module-path "C:\javafx-sdk-25.0.1\lib" --add-modules javafx.controls,javafx.fxml
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import model.*;
import payment.*;

public class Main extends Application {
    private BookingManager bookingManager = new BookingManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(" Flight Ticket Booking System");

        // Input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Passenger Name");

        TextField passportField = new TextField();
        passportField.setPromptText("Passport Number");

        TextField flightNumField = new TextField();
        flightNumField.setPromptText("Flight Number");

        TextField originField = new TextField();
        originField.setPromptText("Origin");

        TextField destField = new TextField();
        destField.setPromptText("Destination");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Public", "SpaceShip", "Private");
        typeBox.setValue("Public");

        Button bookBtn = new Button("Book Flight");
        TextArea output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(200);

        bookBtn.setOnAction(e -> {
            String name = nameField.getText();
            String passport = passportField.getText();
            String flightNum = flightNumField.getText();
            String origin = originField.getText();
            String dest = destField.getText();
            String type = typeBox.getValue();

            Passenger passenger = new Passenger(name, passport);
            Flight flight;
            if (type.equals(" Public")) {
                flight = new DomesticFlight(flightNum, origin, dest, "ANA");
            } else {
                flight = new InternationalFlight(flightNum, origin, dest, "Required");
            }

            Payment payment = new CreditCardPayment();
            Booking booking = new Booking(flight, passenger, payment);
            booking.confirm(200.0);
            bookingManager.addBooking(booking);

            output.appendText(" " + booking.getSummary() + "\n");
        });

        Button loadBtn = new Button("Load All Bookings");
        loadBtn.setOnAction(e -> {
            output.clear();
            for (String b : bookingManager.readBookingsFromFile()) {
                output.appendText(b + "\n");
            }
        });

        VBox root = new VBox(10,
            new Label("Flight Ticket Booking System"),
            nameField, passportField, flightNumField, originField, destField, typeBox,
            new HBox(10, bookBtn, loadBtn),
            output
        );
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 400, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
