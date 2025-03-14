
 ## **Functional Requirements**  
 
 ### **1. Flight Selection**  
 #### **1.1 Search Available Flights**  
 - ✅ The user must see a list of available flights.
 - ❌ When the user scrolls near the bottom, the next bach of flights must be automatically loaded. **Problems Implementing**: The API used doesn't provide any pagination, what you query is what you get, the number of results is quite small. 
 - ✅ Flight data must be retrieved from a **real flight API** or a **mock API** for testing.  
 
 #### **1.2 Search Flights**  
 - Users can search flights using the following criteria:  
   - ✅ **Destination** (e.g., "New York"), the API used supports only IATA airport codes. Note: I implemented a suggestion system that provides related IATA codes. 
   - ✅ **Date** (e.g., "28/02/2025")  
   - ✅ **Flight Duration** (e.g., "1 hour - 24 hours")  
   - ✅ **Max Price** (e.g., "$500")
 
 #### **1.3 Select a Flight**  
 - ✅ Users can select a flight to view flight details and **seat recommendations**.  
 - If the flight has layovers, display the **full itinerary** and display generated seating plans **for each flight**.  
 
 ---
 
 ### **2. Seat Recommendation**  
 #### **2.1 Display Airplane Seat Map**  
 - ✅ A pre-made map of the seats will be made with each seat categorized by its properties (window seat, extra legroom, close to exit, etc).
 - ✅ Show a **graphical seat map** of the airplane.  
 - ✅ Randomly mark **occupied seats**.  
 
 #### **2.2 Recommend Seats Based on Preferences**  
 - ✅ Users can specify seat preferences:  
   - **Window Seat**  
   - **Extra Legroom**  
   - **Close to Exit**  
   - **Side-by-Side Seats** (for group bookings)  
 - 🥲 The system must suggest **best available seats** based on these preferences.Note: doesn't work quite as I intended. Must implement a weight system to calculate a seat score to satisfy client needs the best.

 #### **2.3 Select & Confirm Seat(s)**  
 - ✅ Users can manually override recommendations and select their own seat.   
 - On confirmation the flight will be added to the shopping cart and the flight data with seat layout will be **saved in browser local store**.
 #### **2.4 Checkout**
 - On the shopping cart page, the user will be able to review flight data and press the checkout button.
 - After pressing the checkout button, user can view the data in "My Flights" tab. 
 
 ---
 
 ### **3. API Integration**  
 ✅ The application must support toggle to select between real and mock API.

 #### **3.1 Use Real Flight Data**  
 - ✅ Fetch flights from an external **real flight API**.  
 - ✅ The API key must be configurable via **environment variables**.  
 - Handle API errors gracefully (e.g., fallback to mock data).  
 
 #### **3.2 Mock API for Testing (default mode)**  
 - ✅ Provide a **mock API** to simulate flight data.   
 - ✅ The mock API must return **consistent test data**.  
 
 ---
 
 ### **4. Testing & Quality Assurance**  
 #### **4.1 Unit Testing**  
 - ❌ Test core functionality (e.g., filtering, seat recommendation).  
 - ❌ Validate API response handling (real & mock API).  
 
 #### **4.2 Integration Testing**  
 - ❌ Ensure the application behaves correctly in **both real and mock API modes**.  
 
 #### **4.3 Error Handling & Edge Cases**  
 - ❌ Test cases must cover:  
   - **API failures** (e.g., invalid API key, downtime)  
   - **No available flights**  
   - **All seats occupied**  
   - **Invalid user input**  
 
 ---
 
 ### **5. Deployment & Documentation**  
 #### **5.1 Dockerized Application**  
 - ✅ The application must run in a **Docker container**.  
 - ✅ Users must be able to provide their **own API key** via environment variables.  
 - ✅ Default mode must use **mock data** to simplify testing.  
 
 #### **5.2 Documentation**  
 - ✅ Provide instructions for:  
   - Running the app with real API  
   - Running the app in mock mode  
   - ❌ Running tests  
 - ✅ Include API documentation.  
 
 ---
