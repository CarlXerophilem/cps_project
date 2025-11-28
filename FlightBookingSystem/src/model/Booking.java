package model;

import payment.CreditCardPayment;
import payment.Payment;

public class Booking {
    private Flight flight;
    private Passenger passenger;
    private Payment payment;

    public Booking(Flight flight, Passenger passenger, Payment payment) {
        this.flight = flight;
        this.passenger = passenger;
        this.payment = payment;
    }

    public String getSummary() {
        return passenger.getName() + " booked " + flight.getDetails();
    }

    public void confirm() {
        payment.processPayment(flight.getFlightNumber());
        System.out.println("Booking confirmed for " + passenger.getName());
    }

    // Getter methods for database storage
    public Flight getFlight() {
        return flight;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public String getPaymentMethod() {
        return payment instanceof CreditCardPayment? "Credit Card":"Paypal";
    }
}
