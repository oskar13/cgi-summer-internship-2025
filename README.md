# CGI Summer Internship 2025 Test Assignment

## **📂 Project Structure**  
This repository is organized into two main parts:  
- **`/frontend`** – A React application built with Vite and TailwindCSS.  
- **`/backend`** – A Spring Boot application that handles flight data - **mock** and **real API** (SerpAPI - Google Flights).  

For more details, refer to the **README** files inside each folder.

---

## Project Planning Stage
I started the planning phase with research about flight data APIs. Then I decided to create a set of functional requirements by analyzing the instructions and setting concrete goals for my desired implementation of this project while making sure I fulfil the requirements set by the assignment. This process took a full day.

You can read the requirements here : [FUNCTIONAL_REQUIREMENTS.md](FUNCTIONAL_REQUIREMENTS.md)

## Project Development

During planning phase I overestimated the capability/functionality of SerpAPI and later had some problems matching all the requirements.

Most notable would be origin/destination search which requires you to input IATA codes and not place names. To work around this problem I added a another API on the frontend that suggests IATA codes as you type. The problem with this API is that its publicly available but not officially listed by the company that runs it.

Pagination and lazy loading was pointless because it returned one set of results.

Also a change in backend API design was providing mock and real data on separate URL's instead of switching it with env variable. I made this decision because I wanted to switch between mock and real data from the frontend and not reconfigure the backend server every time. Adding another query parameter or variable in JSON request body would have made things maybe more confusing so splitting it by URL made more sense.

### Challenges
Designing and implementing a mock API besides using real API data wasn't exactly hard but very time consuming, time that could have been used for better UI/UX or writing tests. 

Most of the time was spent on implementing a mock API before I even got to use the real api inside of the project.

Frontend work with react was hard because I'm quite new to it. While Java Spring framework is well documented and structured. Working with React is more of a "using a library" experience, its often hard to pass data around, even while using TypeScript, you still have to watch out for data type mismatch and bugs.

### Parts that are lacking
The seat recommendation system is bad, this should have been the main focus point in this project but was pushed aside because I decided to put my efforts into the API side.

### Conclusion
In the end I'm happy that I was able to create a project that used a mock and real API source. This way I can have a live demo running from my portfolio site without worrying anyone abusing the API calls against a paid service.

### Future plans
I see myself improving the mock data accuracy to provide more believable results. Probably try to include distance between airports as a way of calculating the price and duration of the flight.
