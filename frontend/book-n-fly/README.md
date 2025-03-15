# Book n' Fly - Frontend

This is the frontend for Book n' Fly, a flight planning and seat recommendation application.

Built with **React**, **Vite**, and **TailwindCSS**.

---

# Development: Installation & Setup

Install dependencies

    npm install

Start dev server

    npm run dev

This will start the frontend at [http://localhost:5173](http://localhost:5173)

---

# Deployment

The Dockerfile in the project root directory runs `npm build` which builds a static build of the frontend that will be copied inside the Spring Boot static content folder. Alternatively you can copy and host the build output from `/dist` folder on any webserver.

The backend API base URL is configured in .env file which defines:

```sh
VITE_API_BASE_URL=http://localhost:8080
```

# Flight Data Source Selection: Real vs. Mock

The backend offers 2 sources of data, one is mock data generated based on deterministic random generator that takes the seed from input data. The other data is actual flight data from Google Flights. More info in the project main README.md file.

The frontend can switch between the mock and real data endpoints, this is mainly for testing and making sure that API calls are kept to minimum because they are quite limited in quantity.

## IATA Code Suggestion by Keyword
I'm using a simple API for suggesting airport IATA codes: https://airport-autosuggest.flightright.net.
It's called 1 second after the user stops typing a location name to get airport code suggestions.

**Endpoint:** `GET: /v1/airports/COM`

**Parameters:**

    name - String

**Request Example:**

    curl --location 'https://airport-autosuggest.flightright.net/v1/airports/COM?name=london'

**Response:**
```json
[
    {
        "iata": "ELS",
        "name": "East London (Ben Schoeman)"
    },
    {
        "iata": "GON",
        "name": "Groton/New London"
    },
    {
        "iata": "LCY",
        "name": "London (City Airport)"
    }
]
```


