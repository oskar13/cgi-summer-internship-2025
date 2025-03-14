import { FlightSearchRequest } from "../types";

export const fetchFlights = async (searchParams: FlightSearchRequest) => {
    const apiUrl = searchParams.useRealApi ?  "http://localhost:8080/api/flights" : "http://localhost:8080/api/mock/flights";
    const response = await fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(searchParams),
    });
    if (!response.ok) throw new Error("Failed to fetch flights");
    return response.json();
};