package model;

import java.io.*;
import java.util.*;

public class BookingManager {
    private List<Booking> bookings = new ArrayList<>();
    private File file = new File("data/bookings.txt");

    public void addBooking(Booking booking) {
        bookings.add(booking);
        saveToFile(booking);
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }

    private void saveToFile(Booking booking) {
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(booking.getSummary() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving booking: " + e.getMessage());
        }
    }

    public List<String> readBookingsFromFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null)
                lines.add(line);
        } catch (IOException e) {
            System.out.println("Error reading bookings: " + e.getMessage());
        }
        return lines;
    }
}
