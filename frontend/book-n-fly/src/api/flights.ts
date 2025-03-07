import { FlightSearchRequest } from "../types";

export const fetchFlights = async (searchParams: FlightSearchRequest) => {
    const response = await fetch("http://localhost:8080/api/mock/flights", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(searchParams),
    });
    if (!response.ok) throw new Error("Failed to fetch flights");
    return response.json();
};