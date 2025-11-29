package crawler;

import com.microsoft.playwright.*;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FlightCrawler {

    // Configuration
    private static final int THREAD_COUNT = 4; // Adjust based on CPU/RAM. Too high = IP Ban.
    private static final String CSV_FILE = "flight_data_database.csv";

    public static void main(String[] args) {
        // 1. Generate URLs internally (No need for external text files)
        List<String> urls = FlightRouteGenerator.generateUrls();

        // 2. Initialize CSV Header
        initCsv();

        // 3. Setup Playwright (One Playwright instance, multiple contexts)
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)); // Ctrip detects headless easily. Keep false for stability.

            // 4. Create Thread Pool
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

            for (String url : urls) {
                executor.submit(() -> processUrl(browser, url));
            }

            // 5. Shutdown Logic
            executor.shutdown();
            try {
                executor.awaitTermination(24, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            browser.close();
        }
        System.out.println("Crawling complete.");
    }

    private static void processUrl(Browser browser, String url) {
        // Create a new isolated context for this thread (cookies/cache separated)
        // Set viewport to mimic a real laptop
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setUserAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .setViewportSize(1920, 1080));

        Page page = context.newPage();

        try {
            System.out.println("Processing: " + url);
            page.navigate(url);

            // Handle "Agree to Terms" popup if it exists
            handlePopups(page);

            // Scroll to load dynamic flights (Replaces the Python While loop)
            autoScroll(page);

            // Scrape Data
            List<ElementHandle> flightBoxes = page.querySelectorAll(".flight-box");

            if (flightBoxes.isEmpty()) {
                System.out.println("No flights found for: " + url);
            }

            for (ElementHandle box : flightBoxes) {
                FlightInfo info = extractFlightData(box);
                if (info != null) {
                    saveToCsv(info);
                }
            }

        } catch (Exception e) {
            System.err.println("Error processing " + url + ": " + e.getMessage());
        } finally {
            context.close(); // Important: Close context to free RAM
        }
    }

    private static void handlePopups(Page page) {
        // Check for Service Agreement popup
        try {
            Locator closeBtn = page.locator("text=阅读并同意携程的服务协议");
            // Note: In Playwright, you usually target the button to close it, not the text
            // itself.
            // You might need to inspect the specific 'X' or 'Agree' button class.
            // This is a placeholder for the logic in your Python `if page.ele(...) != None`
            if (closeBtn.isVisible()) {
                // Logic to click the confirm button associated with this text
                page.keyboard().press("Escape"); // Try generic escape first
            }
        } catch (Exception ignored) {
        }
    }

    private static void autoScroll(Page page) {
        // Scroll down until no new content loads
        long previousHeight = 0;
        long currentHeight = 0;
        int attempts = 0;

        while (attempts < 5) { // Max scroll attempts
            previousHeight = (long) page.evaluate("document.body.scrollHeight");
            page.mouse().wheel(0, 1000);
            page.waitForTimeout(1000); // Wait 1 sec for load

            currentHeight = (long) page.evaluate("document.body.scrollHeight");

            if (currentHeight == previousHeight) {
                attempts++;
            } else {
                attempts = 0; // Reset if we successfully scrolled
            }
        }
    }

    private static FlightInfo extractFlightData(ElementHandle box) {
        try {
            // Using CSS selectors which are faster than XPath
            String airline = safeText(box.querySelector(".airline-name"));
            String depAirport = safeText(box.querySelector(".depart-box .airport"));
            String arrAirport = safeText(box.querySelector(".arrive-box .airport"));
            String depTime = safeText(box.querySelector(".depart-box .time"));
            String arrTime = safeText(box.querySelector(".arrive-box .time"));
            String price = safeText(box.querySelector(".price"));

            return new FlightInfo(airline, depAirport, arrAirport, depTime, arrTime, price);
        } catch (Exception e) {
            return null;
        }
    }

    private static String safeText(ElementHandle element) {
        return element == null ? "N/A" : element.innerText().trim();
    }

    // Synchronized to prevent file corruption from multiple threads
    private static synchronized void saveToCsv(FlightInfo info) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE, true))) {
            String[] entry = {
                    info.getAirline(),
                    info.getDepartureAirport(),
                    info.getArrivalAirport(),
                    info.getDepartureTime(),
                    info.getArrivalTime(),
                    info.getPrice()
            };
            writer.writeNext(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initCsv() {
        if (!Files.exists(Paths.get(CSV_FILE))) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE))) {
                String[] header = { "Airline", "Dep Airport", "Arr Airport", "Dep Time", "Arr Time", "Price" };
                writer.writeNext(header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class FlightInfo {
        private String airline;
        private String departureAirport;
        private String arrivalAirport;
        private String departureTime;
        private String arrivalTime;
        private String price;

        public FlightInfo(String airline, String departureAirport, String arrivalAirport, String departureTime,
                String arrivalTime, String price) {
            this.airline = airline;
            this.departureAirport = departureAirport;
            this.arrivalAirport = arrivalAirport;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.price = price;
        }

        public String getAirline() {
            return airline;
        }

        public String getDepartureAirport() {
            return departureAirport;
        }

        public String getArrivalAirport() {
            return arrivalAirport;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public String getPrice() {
            return price;
        }
    }
}