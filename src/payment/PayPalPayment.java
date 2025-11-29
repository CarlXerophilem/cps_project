package payment;

public class PayPalPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Payment of $" + amount + " made via PayPal.");
    }
}
