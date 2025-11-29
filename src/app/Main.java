package app;

import java.lang.Thread;
import javafx.application.Application;
import javafx.stage.Stage;
import model.BookingManager;

/**
 * Main Application Entry Point
 * Implements a multi-step booking wizard:
 * - Step 1: Flight search (FlightSearchView)
 * - Step 2: Passenger information and booking (PassengerInfoView)
 */
public class Main extends Application {
    private BookingManager bookingManager;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.bookingManager = new BookingManager();

        primaryStage.setTitle("✈️ Flight Booking System");

        // Show Step 1: Flight Search
        showFlightSearchView();

        primaryStage.show();
    }

    /**
     * Display the flight search view (Step 1)
     */
    private void showFlightSearchView() {
        FlightSearchView searchView = new FlightSearchView(primaryStage);
        primaryStage.setScene(searchView.buildScene());

        // When user clicks "Search Flights", go to passenger info view
        searchView.getSearchButton().setOnAction(e -> {
            FlightSearchView.SearchData searchData = searchView.getSearchData();
            if (searchData != null) { // Validation passed
                showPassengerInfoView(searchData);
            }
        });
    }

    /**
     * Display the passenger information view (Step 2)
     */
    private void showPassengerInfoView(FlightSearchView.SearchData searchData) {
        PassengerInfoView passengerView = new PassengerInfoView(primaryStage, searchData, bookingManager);
        primaryStage.setScene(passengerView.buildScene());

        // Back button returns to flight search
        passengerView.getBackButton().setOnAction(e -> {
            showFlightSearchView();
        });

        // Book button processes the booking
        passengerView.getBookButton().setOnAction(e -> {
            passengerView.processBooking();
        });
    }

    // Display detailed flight info.
    /*
     * DATABASE.fetch(
     * "SELECT * FROM flights WHERE origin = ?  destination = ?  departure_date = ? , price = ?"
     * )
     * 
     */

    @Override
    public void stop() {
        // Clean up database connection when application closes
        System.out.println("\n=== Application Closing ===");
        if (bookingManager != null) {
            bookingManager.shutdown();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
