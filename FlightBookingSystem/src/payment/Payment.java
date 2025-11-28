package payment;

import model.DatabaseManager;

public abstract class Payment {
    DatabaseManager dataTransport1;

    //This method reads from flightInfo Database to import the price of flight
    public double importPrice(String flightNum){
        //Import price from database(flight info from url)
        return dataTransport1.passPrice(flightNum);
    }

    //Price info is stored in a separate table in flightInfo Database
    public abstract void processPayment(String flightNum);
}
