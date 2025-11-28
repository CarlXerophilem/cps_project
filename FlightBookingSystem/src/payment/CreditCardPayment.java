package payment;

public class CreditCardPayment extends Payment {
    //print price, specify chosen payment approach
    @Override
    public void processPayment(String flightNum) {
        System.out.println("Payment of $" + importPrice(flightNum) + "should be made with Credit Card.");
    }
}
