package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Passenger;

public class BookingFormController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField address1Field;
    @FXML private ComboBox<String> countryBox;

    public void initialize() {
        countryBox.getItems().addAll("China", "USA", "Canada", "Japan", "Singapore");
    }

    @FXML
    private void goToFlightSelector() {
        Passenger p = new Passenger(
                firstNameField.getText(),
                lastNameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                address1Field.getText(),
                countryBox.getValue()
        );

        PassengerSession.setCurrentPassenger(p);
        SceneNavigator.switchTo("fxml/FlightSelector.fxml");
    }
}

