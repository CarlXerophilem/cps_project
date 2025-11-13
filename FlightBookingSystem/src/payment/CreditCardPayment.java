package payment;

public class CreditCardPayment implements Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Payment of $" + amount + " made using Credit Card.");
    }
}
