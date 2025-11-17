package model;

/**
 * Passenger model used throughout the booking flow.
 */
public class Passenger {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String address;
    private final String country;

    public Passenger(String firstName, String lastName, String email, String phone, String address, String country) {
        this.firstName = firstName == null ? "" : firstName.trim();
        this.lastName  = lastName  == null ? "" : lastName.trim();
        this.email     = email     == null ? "" : email.trim();
        this.phone     = phone     == null ? "" : phone.trim();
        this.address   = address   == null ? "" : address.trim();
        this.country   = country   == null ? "" : country.trim();
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }
    public String getPhone()     { return phone; }
    public String getAddress()   { return address; }
    public String getCountry()   { return country; }
}
