 # CGI Summer Internship 2025 Test Assignment

## **📂 Project Structure**  
This repository is organized into two main parts:  
- **`/frontend`** – A React application built with Vite and TailwindCSS.  
- **`/backend`** – A Spring Boot application that handles flight data - **mock** and **real API** (SerpAPI - Google Flights).  

For more details, refer to the README files inside each folder.

---

 ## Project Planning Stage
I started the planning phase with research about flight data APIs. Then I decided to create a set of functional requirements by analyzing the instructions and setting concrete goals for my desired implementation of this project while making sure I fulfil the requirements set by the assignment. This process took a full day.


 ## **Functional Requirements**  
 
 ### **1. Flight Selection**  
 #### **1.1 View Available Flights**  
 - The user must see a list of available flights.
 - Flights should be loaded dynamically using scroll-triggered pagination to avoid overwhelming the user.
 - When the user scrolls near the bottom, the next bach of flights must be automatically loaded.
 - Flight data must be retrieved from a **real flight API** or a **mock API** for testing by setting a **.env variable**.  
 
 #### **1.2 Filter Flights**  
 - Users can filter flights using the following criteria:  
   - **Destination** (e.g., "New York")  
   - **Date** (e.g., "28/02/2025")  
   - **Flight Duration** (e.g., "1 hour - 24 hours")  
   - **Price Range** (e.g., "$100 - $500")  
 
 #### **1.3 Select a Flight**  
 - Users can select a flight to view flight details and **seat recommendations**.  
 - If the flight has layovers, display the **full itinerary** and display generated seating plans **for each flight**.  
 
 ---
 
 ### **2. Seat Recommendation**  
 #### **2.1 Display Airplane Seat Map**  
 - A pre-made map of the seats will be made with each seat categorized by its properties (window seat, extra legroom, close to exit, etc).
 - Show a **graphical seat map** of the airplane.  
 - Randomly mark **occupied seats**.  
 
 #### **2.2 Recommend Seats Based on Preferences**  
 - Users can specify seat preferences:  
   - **Window Seat**  
   - **Extra Legroom**  
   - **Close to Exit**  
   - **Side-by-Side Seats** (for group bookings)  
 - The system must suggest **best available seats** based on these preferences.  

 #### **2.3 Select & Confirm Seat(s)**  
 - Users can manually override recommendations and select their own seat.  
 - Confirmed seats must be marked as **"reserved"**.  
 - On confirmation the flight will be added to the shopping cart and the flight data with seat layout will be **saved in browser local store**.
 #### **2.4 Checkout**
 - On the shopping cart page, the user will be able to review flight data and press the checkout button.
 - After pressing the checkout button, user can view the data in "My Flights" tab. 
 
 ---
 
 ### **3. API Integration**  
The application must support toggle to select between real and mock API.

 #### **3.1 Use Real Flight Data**  
 - Fetch flights from an external **real flight API**.  
 - The API key must be configurable via **environment variables**.  
 - Handle API errors gracefully (e.g., fallback to mock data).  
 
 #### **3.2 Mock API for Testing (default mode)**  
 - Provide a **mock API** to simulate flight data.   
 - The mock API must return **consistent, predefined test data**.  
 
 ---
 
 ### **4. Testing & Quality Assurance**  
 #### **4.1 Unit Testing**  
 - Test core functionality (e.g., filtering, seat recommendation).  
 - Validate API response handling (real & mock API).  
 
 #### **4.2 Integration Testing**  
 - Test end-to-end flow using the mock API container.  
 - Ensure the application behaves correctly in **both real and mock API modes**.  
 
 #### **4.3 Error Handling & Edge Cases**  
 - Test cases must cover:  
   - **API failures** (e.g., invalid API key, downtime)  
   - **No available flights**  
   - **All seats occupied**  
   - **Invalid user input**  
 
 ---
 
 ### **5. Deployment & Documentation**  
 #### **5.1 Dockerized Application**  
 - The application must run in a **Docker container**.  
 - Users must be able to provide their **own API key** via environment variables.  
 - Default mode must use **mock data** to simplify testing.  
 
 #### **5.2 Documentation**  
 - Provide instructions for:  
   - Running the app with real API  
   - Running the app in mock mode  
   - Running tests  
 - Include API documentation.  
 
 ---
