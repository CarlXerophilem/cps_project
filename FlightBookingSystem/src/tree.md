# FlightBookingSystem - Class-Method Relation Map

## 📋 Legend

- **⬆️ Inheritance (extends)**: Child ⬆️ Parent
- **⬆️ Interface Implementation (implements)**: Concrete ⬆️ Interface
- **🔗 Association/Dependency**: Class → UsedClass
- **【Abstract】**: Abstract method
- **`static`**: Static method
- **〈Constructor〉**: Constructor method
- **(Getter)**: Getter method

---

## 🏗️ Model Layer (`model` package)

### 1️⃣ Flight Hierarchy - Polymorphism & Inheritance

```
┌─────────────────────────────────────┐
│       【Abstract】Flight            │
│  ────────────────────────────────── │
│  Fields:                            │
│    - flightNumber: String           │
│    - origin: String                 │
│    - destination: String            │
│    - departureDate: String          │
│  ────────────────────────────────── │
│  Methods:                           │
│    ○ 〈Flight(...)〉                 │
│    ○ getFlightNumber() (Getter)    │
│    ○ getOrigin() (Getter)          │
│    ○ getDestination() (Getter)     │
│    ○ getDepartureDate() (Getter)   │
│    ○ 【getDetails()】 - Abstract   │
└─────────────────────────────────────┘
               ▲                ▲
               │                │
               │                │
    ┌──────────┘                └──────────┐
    │                                      │
    │ Inheritance                          │ Inheritance
    │                                      │
┌───┴──────────────────┐       ┌──────────┴───────────────┐
│  DomesticFlight      │       │  InternationalFlight     │
│  ──────────────────  │       │  ──────────────────────  │
│  Fields:             │       │  Fields:                 │
│    - airline: String │       │    - visaRequired: String│
│  ──────────────────  │       │  ──────────────────────  │
│  Methods:            │       │  Methods:                │
│    ○ 〈Constructor〉  │       │    ○ 〈Constructor〉      │
│    ○ ▶getDetails()  │       │    ○ ▶getDetails()      │
│      @Override       │       │      @Override           │
└──────────────────────┘       └──────────────────────────┘
```

**Polymorphism**: Both `DomesticFlight` and `InternationalFlight` override the abstract method `getDetails()`.

---

### 2️⃣ Booking - Composition Pattern

```
┌────────────────────────────────────┐
│          Booking                   │
│  ────────────────────────────────  │
│  Fields:                           │
│    - flight: Flight      ◆────┐   │
│    - passenger: Passenger ◆───┼┐  │
│    - payment: Payment    ◆────┼┼┐ │
│  ────────────────────────────  │││ │
│  Methods:                      │││ │
│    ○ 〈Booking(...)〉           │││ │
│    ○ getSummary()              │││ │
│    ○ confirm(amount: double)   │││ │
│    ○ getFlight() (Getter)      │││ │
│    ○ getPassenger() (Getter)   │││ │
│    ○ getPaymentMethod()        │││ │
└────────────────────────────────┘││ │
                                  ││ │
        ┌─────────────────────────┘│ │
        │  ┌───────────────────────┘ │
        │  │  ┌──────────────────────┘
        ▼  ▼  ▼
     (Composition)
```

**Composition**: `Booking` has (HAS-A relationship) with `Flight`, `Passenger`, and `Payment`.

---

### 3️⃣ Passenger - Data Model

```
┌────────────────────────────────┐
│         Passenger              │
│  ────────────────────────────  │
│  Fields:                       │
│    - name: String              │
│    - passportNumber: String    │
│  ────────────────────────────  │
│  Methods:                      │
│    ○ 〈Passenger(...)〉         │
│    ○ getName() (Getter)        │
│    ○ getPassportNumber()       │
└────────────────────────────────┘
```

---

### 4️⃣ BookingManager & DatabaseManager - Business Logic & Persistence

```
┌─────────────────────────────────────┐
│       BookingManager                │
│  ─────────────────────────────────  │
│  Fields:                            │
│    - bookings: List<Booking>        │
│    - dbManager: DatabaseManager 🔗─┐│
│  ─────────────────────────────────  ││
│  Methods:                           ││
│    ○ 〈BookingManager()〉            ││
│    ○ addBooking(booking: Booking)   ││
│    ○ getAllBookings()               ││
│    ○ getAllBookingsFromDB()         ││
│    ○ readBookingsFromFile()         ││
│    ○ shutdown()                     ││
└─────────────────────────────────────┘│
                                       │
                                       │ Uses
                                       ▼
┌─────────────────────────────────────┐
│       DatabaseManager               │
│  ─────────────────────────────────  │
│  Fields:                            │
│    - DB_URL: `static final String` │
│    - DB_USER: `static final String`│
│    - DB_PASSWORD: `static final`   │
│    - connection: Connection         │
│  ─────────────────────────────────  │
│  Methods:                           │
│    ○ 〈DatabaseManager()〉           │
│    ○ createTablesIfNotExist()      │
│      (private helper)               │
│    ○ insertBooking(booking: Booking)│
│    ○ getAllBookings()               │
│    ○ getPersonalBooking(phoneNum)   │
│    ○ updateValueBookings(...)       │
│    ○ deleteValueBooking(id)         │
│    ○ closeConnection()              │
│    ○ isConnected()                  │
└─────────────────────────────────────┘
```

**Association**: `BookingManager` uses `DatabaseManager` for persistence.

---

## 💳 Payment Layer (`payment` package)

### Payment - Concrete Class

```
┌────────────────────────────────┐
│         Payment                │
│  ────────────────────────────  │
│  Methods:                      │
│    ○ processPayment(amount)    │
│    ○ CreditCardPayment(amount) │
│    ○ PayPalPayment(amount)     │
└────────────────────────────────┘
```

**Note**: Payment is now a concrete class providing multiple payment processing methods.

---

## 🕷️ Crawler Layer (`crawler` package) - **NEW**

### 1️⃣ FlightCrawler - Web Scraping Engine

```
┌─────────────────────────────────────────┐
│       FlightCrawler                     │
│  ─────────────────────────────────────  │
│  Fields:                                │
│    - `THREAD_COUNT`: static final int   │
│    - `CSV_FILE`: static final String    │
│  ─────────────────────────────────────  │
│  Methods (All Static):                  │
│    ○ `main(args: String[])` ⭐         │
│    ○ `processUrl(browser, url)` ⭐     │
│    ○ `searchFlights(origin, dest, ...)│
│    ○ `handlePopups(page)` ⭐           │
│    ○ `autoScroll(page)` ⭐             │
│    ○ `extractFlightData(...)` ⭐       │
│    ○ `safeText(element)` ⭐            │
│    ○ `saveToCsv(info)` ⭐              │
│      (synchronized)                     │
│    ○ `initCsv()` ⭐                     │
│  ─────────────────────────────────────  │
│  Inner Class:                           │
│    FlightInfo (Data Transfer Object)    │
└─────────────────────────────────────────┘
               │
               │ Contains
               ▼
┌─────────────────────────────────────────┐
│    FlightCrawler.FlightInfo             │
│  ─────────────────────────────────────  │
│  Fields:                                │
│    - airline: String                    │
│    - flightNumber: String               │
│    - aircraftType: String               │
│    - departureAirport: String           │
│    - arrivalAirport: String             │
│    - departureTime: String              │
│    - arrivalTime: String                │
│    - price: String                      │
│    - originCode: String                 │
│    - destinationCode: String            │
│    - date: String                       │
│  ─────────────────────────────────────  │
│  Methods:                               │
│    ○ 〈FlightInfo(...)〉 - 11 params    │
│    ○ getAirline() (Getter)             │
│    ○ getFlightNumber() (Getter)        │
│    ○ getAircraftType() (Getter)        │
│    ○ getDepartureAirport() (Getter)    │
│    ○ getArrivalAirport() (Getter)      │
│    ○ getDepartureTime() (Getter)       │
│    ○ getArrivalTime() (Getter)         │
│    ○ getPrice() (Getter)               │
│    ○ getOriginCode() (Getter)          │
│    ○ getDestinationCode() (Getter)     │
│    ○ getDate() (Getter)                │
│    ○ toString()                         │
└─────────────────────────────────────────┘
```

---

### 2️⃣ FlightData - Data Model with Comparison

```
┌─────────────────────────────────────────┐
│  FlightData implements Comparable       │
│  ─────────────────────────────────────  │
│  Fields:                                │
│    - flightNumber: String               │
│    - airline: String                    │
│    - departureTime: String              │
│    - arrivalTime: String                │
│    - price: double                      │
│    - origin: String                     │
│    - destination: String                │
│    - hasTransfer: boolean               │
│    - transferCount: int                 │
│    - date: String                       │
│  ─────────────────────────────────────  │
│  Methods:                               │
│    ○ 〈FlightData()〉 - Default          │
│    ○ 〈FlightData(...)〉 - 10 params     │
│    ○ getFlightNumber() (Getter)        │
│    ○ getAirline() (Getter)             │
│    ○ getDepartureTime() (Getter)       │
│    ○ getArrivalTime() (Getter)         │
│    ○ getPrice() (Getter)               │
│    ○ getOrigin() (Getter)              │
│    ○ getDestination() (Getter)         │
│    ○ hasTransfer() (Getter)            │
│    ○ getTransferCount() (Getter)       │
│    ○ getDate() (Getter)                │
│    ○ ▶compareTo(other) @Override      │
│      (Sorts: direct→transfers→price)   │
│    ○ ▶toString() @Override            │
└─────────────────────────────────────────┘
```

**Implements Comparable**: Custom sorting logic prioritizes direct flights over transfers, then by price.

---

### 3️⃣ CityCodeMapper - Static Mapping Utility

```
┌─────────────────────────────────────────┐
│         CityCodeMapper                  │
│  ─────────────────────────────────────  │
│  Fields (All Static Final):             │
│    - `DOMESTIC_MAPPINGS`: Map           │
│    - `INTERNATIONAL_MAPPINGS`: Map      │
│  ─────────────────────────────────────  │
│  Methods (All Static):                  │
│    ○ `getCityName(code)` ⭐            │
│      Returns: String                    │
│    ○ `determineFlightType(origin, ...)│
│      Returns: String ("domestic"/"int")│
│    ○ `getDomesticMappings()` ⭐        │
│      Returns: Map<String, String>       │
│    ○ `getInternationalMappings()` ⭐   │
│      Returns: Map<String, String>       │
│    ○ `getCityCode(name)` ⭐            │
│      Returns: String (3-letter code)    │
└─────────────────────────────────────────┘
```

**Static Utility**: Maps city names to IATA codes and determines flight type (domestic/international).

---

### 4️⃣ FlightRouteGenerator - URL Generator

```
┌──────────────────────────────────────────┐
│       FlightRouteGenerator               │
│  ──────────────────────────────────────  │
│  Fields (All Static Final):              │
│    - `ALL_PLACES`: Set<String>           │
│    - `BASE_URL`: String                  │
│    - `SAMPLE_CITIES`: Set<String>        │
│    - `SAMPLE_DATES`: Set<String>         │
│  ──────────────────────────────────────  │
│  Methods (All Static):                   │
│    ○ `generateUrls(origin, dest, date)` │
│       Returns: List<String>              │
│    ○ `generateUrls()` ⭐                │
│       Returns: List<String>              │
│       (Generates sample URLs)            │
└──────────────────────────────────────────┘
```

**Static Utility**: Generates Ctrip flight search URLs.

---

## 🎨 Application Layer (`app` package)

### 1️⃣ Main Application - JavaFX Entry Point

```
┌─────────────────────────────────────────┐
│  Main extends Application               │
│  ─────────────────────────────────────  │
│  Fields:                                │
│    - bookingManager: BookingManager 🔗 │
│    - primaryStage: Stage                │
│  ─────────────────────────────────────  │
│  Methods:                               │
│    ○ start(stage: Stage) @Override      │
│    ○ showFlightSearchView() (private)   │
│    ○ showPassengerInfoView(...) (priv.) │
│    ○ stop() @Override                   │
│    ○ `main(args: String[])` ⭐         │
└─────────────────────────────────────────┘
              │
              │ Uses
              ▼
    BookingManager (see Model layer)
```

---

### 2️⃣ FlightSearchView - UI Component

```
┌─────────────────────────────────────────┐
│       FlightSearchView                  │
│  ─────────────────────────────────────  │
│  Fields:                                │
│    - stage: Stage                       │
│    - originInput: TextField             │
│    - destinationInput: TextField        │
│    - departureDatePicker: DatePicker    │
│  ─────────────────────────────────────  │
│  Methods:                               │
│    ○ 〈FlightSearchView(stage, callback)│
│    ○ buildScene()                       │
│    ○ getSearchButton()                  │
│  ─────────────────────────────────────  │
│  Inner Class:                           │
│    SearchData (Data Container)          │
└─────────────────────────────────────────┘
```

---

### 3️⃣ PassengerInfoView - UI Component

```
┌─────────────────────────────────────────┐
│       PassengerInfoView                 │
│  ─────────────────────────────────────  │
│  Fields:                                │
│    - stage: Stage                       │
│    - searchData: SearchData             │
│    - bookingManager: BookingManager 🔗  │
│    - passengerNameField: TextField      │
│    - passportField: TextField           │
│    - phoneField: TextField              │
│    - urlTextArea: TextArea              │
│  ─────────────────────────────────────  │
│  Methods:                               │
│    ○ 〈PassengerInfoView(...)〉          │
│    ○ buildScene()                       │
│    ○ buildSummaryBox()                  │
│    ○ buildPassengerForm()               │
│    ○ buildURLDisplayBox()               │
│    ○ buildButtonPanel()                 │
│    ○ processBooking()                   │
│    ○ getBackButton()                    │
│    ○ getBookButton()                    │
│    ○ showAlert(title, message)          │
│    ○ showSuccessAlert(title, message)   │
└─────────────────────────────────────────┘
              │
              │ Uses
              ▼
     FlightCrawler.searchFlights()
     FlightRouteGenerator.generateUrls()
```

---

## 🔄 Complete System Architecture Diagram

```
┌───────────────────────────────────────────────────────────────┐
│                      APP LAYER (app)                          │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │              Main (JavaFX Application)                   │ │
│  │                        │                                 │ │
│  │         ┌──────────────┴───────────────┐                │ │
│  │         │                              │                │ │
│  │         ▼                              ▼                │ │
│  │  FlightSearchView            PassengerInfoView          │ │
│  │   (UI Component)                (UI Component)          │ │
│  │         │                              │                │ │
│  │         │                              │ Uses           │ │
│  │         │                              ▼                │ │
│  │         │                      FlightCrawler            │ │
│  │         │                   FlightRouteGenerator        │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────────────────────────────────────────────┘
                              │
                              │ Uses
                              ▼
┌───────────────────────────────────────────────────────────────┐
│                     MODEL LAYER (model)                       │
│  ┌────────────────────────────────────────────────────────┐   │
│  │           BookingManager                               │   │
│  │                  │                                     │   │
│  │    ┌─────────────┴─────────────┐                      │   │
│  │    │                           │                      │   │
│  │    ▼                           ▼                      │   │
│  │ DatabaseManager            Booking                    │   │
│  │                              │                        │   │
│  │                    ┌─────────┼─────────┐             │   │
│  │                    ▼         ▼         ▼             │   │
│  │                 Flight   Passenger  Payment          │   │
│  │                    │                                 │   │
│  │           ┌────────┴────┐                            │   │
│  │           ▼             ▼                            │   │
│  │    DomesticFlight  InternationalFlight              │   │
│  └────────────────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────────────────┘
                              │
                              │ Provides
                              ▼
┌───────────────────────────────────────────────────────────────┐
│                   PAYMENT LAYER (payment)                     │
│  ┌────────────────────────────────────────────────────────┐   │
│  │                Payment (Concrete Class)                │   │
│  │              - processPayment()                        │   │
│  │              - CreditCardPayment()                     │   │
│  │              - PayPalPayment()                         │   │
│  └────────────────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────────────────┘
                              
┌───────────────────────────────────────────────────────────────┐
│                   CRAWLER LAYER (crawler) - NEW               │
│  ┌────────────────────────────────────────────────────────┐   │
│  │              FlightCrawler (Main Engine)               │   │
│  │                  └─► FlightInfo (Inner)                │   │
│  │                                                        │   │
│  │              FlightData (DTO w/ Compare)               │   │
│  │                                                        │   │
│  │              CityCodeMapper (Static Util)              │   │
│  │                                                        │   │
│  │              FlightRouteGenerator (URL Gen)            │   │
│  └────────────────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────────────────┘
```

---

## 🎯 Design Patterns Used

1. **Inheritance & Polymorphism**
   - `Flight` ⬆️ `DomesticFlight`, `InternationalFlight`
   - Abstract method `getDetails()` overridden in subclasses

2. **Interface Implementation (Comparable)**
   - `FlightData` implements `Comparable<FlightData>`
   - Custom sorting logic for flight prioritization

3. **Composition**
   - `Booking` contains `Flight`, `Passenger`, and `Payment`
   - "Has-A" relationships

4. **Singleton-like Pattern**
   - `DatabaseManager` manages single database connection
   - Created once by `BookingManager`

5. **Static Utility Classes**
   - `CityCodeMapper` - City/airport code mapping
   - `FlightRouteGenerator` - URL generation for flight searches
   - `FlightCrawler` - Web scraping orchestration

6. **MVC-like Architecture**
   - **Model**: Flight, Booking, Passenger, Payment classes
   - **View**: FlightSearchView, PassengerInfoView (JavaFX)
   - **Controller**: Main, BookingManager

7. **Inner Class Pattern**
   - `FlightCrawler.FlightInfo` - Data Transfer Object
   - `FlightSearchView.SearchData` - Data Container

---

## 📝 Key Method Categories

| Shape/Format | Method Type | Example |
|--------------|-------------|---------|
| **【Method】** | Abstract method | `Flight.getDetails()` |
| **▶Method** | Overridden method (@Override) | `DomesticFlight.getDetails()` |
| **`static`** | Static method | `CityCodeMapper.getCityCode()` |
| **〈Method〉** | Constructor | `Flight(...)` |
| **(Getter)** | Getter method | `getFlight()`, `getName()` |
| **○ Method** | Regular instance method | `confirm(double amount)` |

---

## 🔗 Inter-Package Dependencies

```
app (Main, FlightSearchView, PassengerInfoView)
  ├─► model (BookingManager, Booking, Flight, Passenger, DatabaseManager)
  ├─► payment (Payment)
  └─► crawler (FlightCrawler, FlightRouteGenerator, CityCodeMapper, FlightData)

model (Booking)
  └─► payment (Payment)

model (BookingManager)
  └─► model (DatabaseManager, Booking)

model (DatabaseManager)
  └─► crawler (FlightData, FlightRouteGenerator)

crawler (All classes are independent utilities)
  └─► No internal dependencies, used by app and model layers
```

---

## 🆕 Major Changes from Previous Version

### ✨ Added Components:

1. **Crawler Package** (NEW)
   - `FlightCrawler` - Playwright-based web scraper for Ctrip
   - `FlightData` - Data model with sorting capabilities
   - `CityCodeMapper` - Domestic and international city code mapping
   - `FlightRouteGenerator` - Dynamic URL generation

2. **Enhanced DatabaseManager**
   - Added `getPersonalBooking(phoneNum)` 
   - Added `updateValueBookings(...)` 
   - Added `deleteValueBooking(id)`

### 🗑️ Removed Components:

1. **URLGenerator** - Replaced by `FlightRouteGenerator` in crawler package
2. **AddressUtils** - Removed (IP geolocation no longer needed)
3. **Payment Interface Pattern** - Changed to concrete class

### 🔄 Modified Components:

1. **Payment** - Changed from interface to concrete class with direct methods
2. **PassengerInfoView** - Now integrates with FlightCrawler for real-time data
3. **DatabaseManager** - Expanded CRUD operations

---

**Generated**: 2025-11-30  
**Project**: FlightBookingSystem  
**Documentation**: Class-Method Relation Map (Updated)
