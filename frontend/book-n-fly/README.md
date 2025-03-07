# Book n' Fly - Frontend

This is the frontend for Book n' Fly, a flight planning and seat recommendation application.

Built with React, Vite, and TailwindCSS.


# Flight Data Source: Real vs. Mock

The backend offers 2 sources of data, one is mock data generated based on deterministic random generator that takes the seed from input data. The other data is actual flight data from Google Flights. More info in the project main README.md file.

The frontend can switch between the mock and real data endpoints, this is mainly for testing and making sure that API calls are kept to minimum because they are quite limited in quantity.

## IATA code suggestion 
I found a easy to use API for finding airport IATA codes: https://airport-autosuggest.flightright.net/v1/airports/COM?name=london
Its called 1 second after the user stops typing a location name to get airport code suggestions.


# Installation & Setup

Install dependencies

    npm install

Configure environment variables in .env file (optional)

    API_URL=http://localhost:7878/api

Start dev server

    npm run dev

This will start the frontend at [http://localhost:5173](http://localhost:5173)