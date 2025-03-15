import { FlightSearchRequest } from "../types";

export const fetchFlights = async (searchParams: FlightSearchRequest) => {
    console.log(JSON.stringify(searchParams));
    const apiUrl = searchParams.useRealApi 
        ? "http://localhost:8080/api/flights" 
        : "http://localhost:8080/api/mock/flights";

    try {
        const response = await fetch(apiUrl, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(searchParams),
        });

        const responseData = await response.json();

        if (!response.ok) {
            if (responseData.error) {
                throw new Error(`API Error: ${responseData.error}`);
            }
            throw new Error(`HTTP Error ${response.status}: ${responseData.message || "Something went wrong"}`);
        }

        if (responseData.error) {
            throw new Error(`SerpAPI Error: ${responseData.error}`);
        }

        return responseData;
    } catch (error) {
        throw new Error(error instanceof Error ? error.message : "Unknown error occurred");
    }
};
