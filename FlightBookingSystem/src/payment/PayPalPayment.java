package payment;

public class PayPalPayment extends Payment {
    @Override
    public void processPayment(String flightNum) {
        System.out.println("Payment of $" + importPrice(flightNum) + "should be made with PayPal.");
    }
}
