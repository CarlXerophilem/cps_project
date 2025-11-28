package model;

public class Passenger {
    private String name;
    private String passportNumber;
    private String phoneNumber;


    public Passenger(String name, String passportNumber, String phoneNumber) {
        this.name = name;
        this.passportNumber = passportNumber;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }
    public String getPassportNumber() {
        return passportNumber;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
}
