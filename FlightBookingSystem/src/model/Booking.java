package model;

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

    public void confirm(double amount) {
        payment.processPayment(amount);
        System.out.println("Booking confirmed for " + passenger.getName());
    }
}
