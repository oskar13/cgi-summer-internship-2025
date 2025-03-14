# CGI Summer Internship 2025 Test Assignment

This project provides a flight search application using a **Spring Boot backend** and a **React frontend**, packaged together in a **Docker container**. Powered by **Google flights** for real flight data through **SerpAPI**. Featuring a **mock API** mode for offline testing.

## **📂 Project Structure**  
This repository is organized into two main parts:  
- **`/frontend`** – A React application built with Vite and TailwindCSS.  
- **`/backend`** – A Spring Boot application that handles flight data - **mock** and **real API** (SerpAPI - Google Flights).  

For more details, refer to the **README** files inside each folder.

## **Running the Project**  

You can run the project in three ways:  
1. **Using a ready-made Docker image from Docker Hub**  
2. **Building and running the Docker image**  
3. **Running locally without Docker** 

### **Obtaining the API key**

To get real-world data for flights you can create a free account at [https://serpapi.com/](https://serpapi.com/). Free plan has a limit of 100 queries per month and requires email and phone verification.

If your're in a hurry then you can run the project purely on generated mock data.

---

## **Option 1: Run Using Docker Hub (easy way)**  

The easiest way is to pull the prebuilt Docker image from Docker Hub and run it.  

```sh
docker run -p 8080:8080 -e SERPAPI_KEY=your_api_key oskarehaver/booknfly
```

- Replace `your_api_key` with your actual API key.  
- The application will be accessible at `http://localhost:8080`.  

---

## **Option 2: Build and Run the Docker Image Locally**  

If you want to build the image yourself, follow these steps:  

### **Build the Docker Image**  
```sh
docker build -t booknfly .
```

### **Run the Container**  
```sh
docker run -p 8080:8080 --env-file .env.local booknfly
```
**`.env.local`** should contain:  
```
SERPAPI_KEY=your_api_key
```

---

## **Option 3: Run Without Docker (Local Development)**  

If you want to run the backend and frontend manually:  

### **Backend Setup - Using a IDE**  
1. Open the backend folder as a project (Maven).
2. Set your environmental variables for run configuration.
   ```sh
   "SERPAPI_KEY=your_api_key"
   ```
3. Make sure your IDE has Lombok configured.
4. Run the main class: `com.jooseposkarehaver.booknfly.BooknflyApplication`

---

### **Frontend Setup**  
1. Navigate to the frontend directory:  
   ```sh
   cd frontend/book-n-fly
   ```
2. Install dependencies:  
   ```sh
   npm install
   ```
3. Run the frontend:  
   ```sh
   npm run dev
   ```
- The frontend will be available at `http://localhost:5173`.  

---


## Project Planning Stage
I started the planning phase with research about flight data APIs. Then I decided to create a set of functional requirements by analyzing the instructions and setting concrete goals for my desired implementation of this project.

You can read the requirements here : [FUNCTIONAL_REQUIREMENTS.md](FUNCTIONAL_REQUIREMENTS.md)

While I was not able to fill all the requirements it gave me some structure to work with.

## Project Development

During planning phase I overestimated the capability/functionality of SerpAPI and later had some problems matching all the requirements.

One problem was origin/destination search which requires you to input IATA codes and not place names without an option to look them up. SerpAPI doesnt provide a way to find airports by place names. So to work around this problem I added another API on the frontend that suggests IATA codes as you type.

Implementing pagination and lazy loading was pointless because API would always return limited set of results.

Also a change from original API mapping was providing mock and real data on separate URL's instead of switching it with env variable. I made this decision because I wanted to switch between mock and real data from the frontend and not reconfigure the backend server every time. Adding another query parameter or variable in JSON request body would have made things maybe more confusing so splitting it by URL made more sense.

### Challenges
Designing and implementing a mock API besides using real API data wasn't exactly hard but very time consuming, time that could have been used for better UI/UX or writing tests. 

Most of the time was spent on implementing a mock API before I even got to use the real api inside of the project, this was because I wanted to save these limited calls for later.

Frontend work with react was hard because I'm quite new to it. While Java Spring framework is well documented and structured. Working with React is more of a "using a library" experience, its often hard to pass data around.

### Parts that are lacking
The seat recommendation system is bad, this should have been the main focus point in this project but was pushed aside because I decided to put my efforts into the API side.

### Conclusion
In the end I'm happy that I was able to create a project that used a mock and real API source. This way I can have a live demo running from my portfolio site without worrying anyone abusing the API calls against a paid service.

### Future plans
I see myself improving the mock data accuracy to provide more believable results. Probably try to include distance between airports as a way of calculating the price and duration of the flight. Improve the seating plan generator to be more useful.
