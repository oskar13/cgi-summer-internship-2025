# Book n' Fly - Frontend

This is the frontend for Book n' Fly, a flight planning and seat recommendation application.

Built with React, Vite, and TailwindCSS.


# API Data Source: Real vs. Mock

The backend handles switching between:
 - Mock API (for local development & testing)
 - SerpAPI (Real API) (for "production" use)

The frontend always calls the same backend endpoints, and the backend determines whether to return mock data or live data based on its internal configuration.


# Installation & Setup

Install dependencies

    npm install

Configure environment variables in .env file (optional)

    API_URL=http://localhost:7878/api

Start dev server

    npm run dev

This will start the frontend at [http://localhost:5173](http://localhost:5173)