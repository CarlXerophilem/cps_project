package app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * URLGenerator creates booking links to official airline websites with
 * pre-filled search parameters.
 * 
 * HOW IT WORKS:
 * URLs contain query parameters after a '?' character. For example:
 * https://airline.com/booking?origin=Tokyo&destination=Osaka&date=2025-11-25
 * 
 * The airline's website reads these parameters and pre-fills their booking
 * form.
 * This is done using JavaScript on the airline's website, not in this Java
 * application.
 */
public class URLGenerator {

    /**
     * Generate URLs for multiple airlines with pre-filled flight search parameters
     * 
     * @param origin      Departure city/airport
     * @param destination Arrival city/airport
     * @param date        Departure date in yyyy-MM-dd format
     * @param flightType  "Domestic" or "International"
     * @return List of URLs formatted as "Airline Name: URL"
     */
    public static List<String> generateAirlineURLs(String origin, String destination, String date, String flightType) {
        List<String> urls = new ArrayList<>();

        // URL-encode parameters to handle special characters
        String encodedOrigin = encodeParameter(origin);
        String encodedDest = encodeParameter(destination);
        String encodedDate = encodeParameter(date);

        if (flightType.equals("Domestic")) {
            // Domestic airlines (Japan-focused)

            // ANA (All Nippon Airways)
            String anaUrl = String.format(
                    "https://www.ana.co.jp/en/us/book-flights/?origin=%s&destination=%s&date=%s&adults=1",
                    encodedOrigin, encodedDest, encodedDate);
            urls.add("ANA: " + anaUrl);

            // JAL (Japan Airlines)
            String jalUrl = String.format(
                    "https://www.jal.co.jp/en/inter/booking/?origin=%s&destination=%s&date=%s",
                    encodedOrigin, encodedDest, encodedDate);
            urls.add("JAL: " + jalUrl);

            // Skymark Airlines
            String skymarkUrl = String.format(
                    "https://www.skymark.co.jp/en/reservation/?dep=%s&arr=%s&date=%s",
                    encodedOrigin, encodedDest, encodedDate);
            urls.add("Skymark: " + skymarkUrl);

        } else {
            // International airlines

            // Emirates
            String emiratesUrl = String.format(
                    "https://www.emirates.com/us/english/book/flights.aspx?orig=%s&dest=%s&date=%s",
                    encodedOrigin, encodedDest, encodedDate);
            urls.add("Emirates: " + emiratesUrl);

            // United Airlines
            String unitedUrl = String.format(
                    "https://www.united.com/en/us/fsr/choose-flights?f=%s&t=%s&d=%s&tt=1",
                    encodedOrigin, encodedDest, encodedDate);
            urls.add("United: " + unitedUrl);

            // Qatar Airways
            String qatarUrl = String.format(
                    "https://www.qatarairways.com/booking?origin=%s&destination=%s&departDate=%s",
                    encodedOrigin, encodedDest, encodedDate);
            urls.add("Qatar Airways: " + qatarUrl);
        }

        // Add explanatory note
        urls.add("");
        urls.add("═══════════════════════════════════════");
        urls.add("?How to Use These Links:");
        urls.add("1. Copy any URL above");
        urls.add("2. Paste into your web browser");
        urls.add("3. The airline's website will show flights for your search");
        urls.add("4. Compare prices across different airlines!");
        urls.add("═══════════════════════════════════════");

        return urls;
    }

    /**
     * URL-encode a parameter value to handle special characters
     * 
     * For example: "Tokyo Narita" becomes "Tokyo+Narita" or "Tokyo%20Narita"
     * This ensures the URL is properly formatted for web browsers.
     * 
     * @param value The parameter value to encode
     * @return URL-encoded string
     */
    private static String encodeParameter(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            // This should never happen with UTF-8
            System.err.println("Error encoding parameter: " + value);
            return value;
        }
    }

    /**
     * Format a date for airline-specific requirements
     * Most airlines use yyyy-MM-dd format, but this method allows customization
     * 
     * @param date        Date in yyyy-MM-dd format
     * @param airlineCode Airline identifier (for future customization)
     * @return Formatted date string
     */
    public static String formatDateForAirline(String date, String airlineCode) {
        // Currently all airlines use the same format
        // Future: add airline-specific formatting if needed
        return date;
    }
}
