# Flight Booking System - Ctrip Crawler Integration

## Summary of Changes

This document summarizes the integration of the Ctrip flight crawler into the existing Flight Booking System.

## New Components Added

### 1. **CityCodeMapper.java** (`crawler` package)
- Converts user-input city names (e.g., "shanghai", "shangh") to 3-letter airport codes (e.g., "sha")
- Supports fuzzy matching for partial city names
- Contains mappings for major Chinese and international cities

### 2. **FlightData.java** (`crawler` package)
- Data model for flight information scraped from Ctrip
- Implements `Comparable` interface to sort flights:
  - **Priority 1**: Direct flights ranked above transfer flights
  - **Priority 2**: Fewer transfers preferred
  - **Priority 3**: Lower price preferred
- Contains flight details: number, airline, times, price, transfer info

### 3. **FlightCrawler.java** (`crawler` package)
- Simulates crawling flight data from Ctrip website
- `fetchFlights()`: Returns sorted list of available flights
- `getCheapestFlight()`: Finds the cheapest direct flight (or cheapest transfer if no direct available)
- **Note**: Currently uses mock data. In production, would use Selenium/Playwright for actual web scraping

## Modified Components

### 1. **Passenger.java** (`model` package)
**Changes:**
- Added `phoneNumber` field (required for domestic flights in China)
- Updated constructor: `Passenger(String name, String passportNumber, String phoneNumber)`
- Added getter: `getPhoneNumber()`

### 2. **PassengerInfoView.java** (`app` package)
**Major Changes:**

#### UI Enhancements:
- Added phone number input field (marked as required for domestic flights)
- Changed URL display area to "Flight Info Display Area"
- Displays cheapest flight information with details

#### New Functionality:
- `fetchAndDisplayCheapestFlight()`: 
  - Converts user input city names to airport codes
  - Fetches flights using FlightCrawler
  - Displays cheapest flight prominently
  - Shows all available flights (up to 10) sorted by preference

#### Updated Booking Process:
- Validates phone number for domestic flights
- Uses actual flight price from crawler data
- Displays flight details in booking confirmation

### 3. **FlightRouteGenerator.java** (`crawler` package)
**Existing component** - provides airport code lists and URL format for Ctrip

## Key Features Implemented

### ✅ Requirement 1: Phone Number Support
- Phone number field added to passenger form
- Required validation for domestic flights
- Integrated into Passenger model

### ✅ Requirement 2: City Code Conversion
- User can type partial city names (e.g., "shangh")
- System automatically converts to proper airport codes (e.g., "sha")
- Supports fuzzy matching

### ✅ Requirement 3: Display Cheapest Flight
- **Prominent display** of cheapest flight in the second UI
- Shows all relevant flight details:
  - Flight number and airline
  - Departure/arrival times
  - Price in CNY (¥)
  - Transfer information
- Lists all available flights sorted by preference

### ✅ Requirement 4: Transfer Flight Ranking
- Direct flights always appear before transfer flights
- Among transfer flights, those with fewer transfers rank higher
- Within same category, sorted by price (lowest first)

## Data Flow

```
User Input (City Name)
    ↓
CityCodeMapper → Airport Code (e.g., "sha")
    ↓
FlightCrawler.fetchFlights(origin, dest, date)
    ↓
List<FlightData> (sorted: direct first, then by price)
    ↓
FlightCrawler.getCheapestFlight()
    ↓
Display in PassengerInfoView
    ↓
User Books Flight
    ↓
Price from FlightData used in booking
```

## Example Usage

1. **User enters**: "shangha" (typo) and "beijing"
2. **System converts**: "sha" and "bjs"
3. **Crawler fetches**: 8 flights (5 direct, 3 transfers)
4. **Display shows**:
   - **Cheapest direct flight**: MU5641, ¥687.23
   - Full list of all 8 flights sorted properly

## Future Enhancements

### For Production Deployment:

1. **Real Web Scraping**:
   - Replace mock data in `FlightCrawler.generateMockFlightData()`
   - Implement Selenium/Playwright integration
   - Handle dynamic page loading and anti-scraping measures

2. **Database Integration**:
   - Cache flight data to reduce scraping frequency
   - Store historical pricing for analytics

3. **Enhanced City Mapping**:
   - Add more cities to `CityCodeMapper`
   - Support multiple airports per city (e.g., Beijing: PEK, NAY)
   - Add internationalization support

4. **Error Handling**:
   - Handle network failures gracefully
   - Implement retry logic for failed scraping
   - Add timeout mechanisms

## Testing Notes

- Classpath warnings are expected until project is built with Maven
- Mock data generation produces realistic flight prices and times
- Transfer flights are intentionally priced lower than direct flights

## Dependencies

No new external dependencies required for the current mock implementation.

For production web scraping, add to `pom.xml`:
```xml
<!-- Selenium for web scraping -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.15.0</version>
</dependency>
```
