package crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * FlightCrawler - Scrapes flight data from Ctrip (flights.ctrip.com)
 * 
 * Note: In production, this would use Selenium or JSoup to scrape actual data.
 * For this implementation, we'll simulate the crawling with mock data that
 * demonstrates the functionality.
 */
public class FlightCrawler {

    /**
     * Fetch flight data from Ctrip for the given route and date
     * 
     * @param origin      - origin airport code (3-letter)
     * @param destination - destination airport code (3-letter)
     * @param date        - departure date in yyyy-MM-dd format
     * @return List of FlightData objects, sorted by price (direct flights first)
     */
    public static List<FlightData> fetchFlights(String origin, String destination, String date) {
        List<FlightData> flights = new ArrayList<>();

        // In production, this would:
        // 1. Build the Ctrip URL using FlightRouteGenerator format
        // 2. Use Selenium WebDriver to load the page (JavaScript-rendered)
        // 3. Parse the flight results from the DOM
        // 4. Extract flight details (number, airline, times, price, transfers)

        String url = buildCtripUrl(origin, destination, date);
        System.out.println("Fetching flights from: " + url);

        // Simulate fetching flight data
        flights.addAll(generateMockFlightData(origin, destination, date));

        // Sort flights: direct flights first, then by price
        Collections.sort(flights);

        return flights;
    }

    /**
     * Build the Ctrip URL for the flight search
     */
    private static String buildCtripUrl(String origin, String destination, String date) {
        // Format:
        // https://flights.ctrip.com/online/list/oneway-sha-bjs?depdate=2025-12-01&cabin=y_s_c_f&adult=1&child=0&infant=0
        String formattedDate = date.replace("-", ""); // Remove hyphens for Ctrip format
        return String.format(
                "https://flights.ctrip.com/online/list/oneway-%s-%s?depdate=%s&cabin=y_s_c_f&adult=1&child=0&infant=0",
                origin.toLowerCase(),
                destination.toLowerCase(),
                formattedDate);
    }

    /**
     * Generate mock flight data for demonstration
     * In production, replace this with actual web scraping logic
     */
    private static List<FlightData> generateMockFlightData(String origin, String destination, String date) {
        List<FlightData> flights = new ArrayList<>();
        Random random = new Random();

        String[] airlines = { "China Eastern", "Air China", "China Southern", "Hainan Airlines", "Spring Airlines" };
        String[] times = { "06:00", "08:30", "10:45", "13:20", "15:50", "18:00", "20:30" };

        // Generate 5-8 direct flights
        int numDirectFlights = 5 + random.nextInt(4);
        for (int i = 0; i < numDirectFlights; i++) {
            String airline = airlines[random.nextInt(airlines.length)];
            String flightNum = generateFlightNumber(airline);
            String depTime = times[random.nextInt(times.length)];
            String arrTime = addHours(depTime, 2 + random.nextInt(3));
            double price = 500 + random.nextDouble() * 1500; // ¥500-2000

            flights.add(new FlightData(
                    flightNum, airline, depTime, arrTime, price,
                    origin.toUpperCase(), destination.toUpperCase(),
                    false, 0, date));
        }

        // Generate 2-4 transfer flights (cheaper but with transfers)
        int numTransferFlights = 2 + random.nextInt(3);
        for (int i = 0; i < numTransferFlights; i++) {
            String airline = airlines[random.nextInt(airlines.length)];
            String flightNum = generateFlightNumber(airline);
            String depTime = times[random.nextInt(times.length)];
            String arrTime = addHours(depTime, 5 + random.nextInt(6));
            int transfers = 1 + random.nextInt(2); // 1-2 transfers
            double price = 300 + random.nextDouble() * 800; // ¥300-1100 (cheaper)

            flights.add(new FlightData(
                    flightNum, airline, depTime, arrTime, price,
                    origin.toUpperCase(), destination.toUpperCase(),
                    true, transfers, date));
        }

        return flights;
    }

    /**
     * Generate a flight number based on airline
     */
    private static String generateFlightNumber(String airline) {
        Random random = new Random();
        String prefix;
        switch (airline) {
            case "China Eastern":
                prefix = "MU";
                break;
            case "Air China":
                prefix = "CA";
                break;
            case "China Southern":
                prefix = "CZ";
                break;
            case "Hainan Airlines":
                prefix = "HU";
                break;
            case "Spring Airlines":
                prefix = "9C";
                break;
            default:
                prefix = "XX";
        }
        return prefix + (1000 + random.nextInt(9000));
    }

    /**
     * Add hours to a time string
     */
    private static String addHours(String time, int hours) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        hour = (hour + hours) % 24;

        return String.format("%02d:%02d", hour, minute);
    }

    /**
     * Get the cheapest flight from the list
     */
    public static FlightData getCheapestFlight(List<FlightData> flights) {
        if (flights == null || flights.isEmpty()) {
            return null;
        }

        // Since flights are already sorted (direct first, then by price),
        // we need to find the cheapest direct flight
        FlightData cheapestDirect = null;
        double cheapestPrice = Double.MAX_VALUE;

        for (FlightData flight : flights) {
            if (!flight.hasTransfer() && flight.getPrice() < cheapestPrice) {
                cheapestDirect = flight;
                cheapestPrice = flight.getPrice();
            }
        }

        // If no direct flights, return the overall cheapest (will be a transfer flight)
        return cheapestDirect != null ? cheapestDirect : flights.get(0);
    }
}
