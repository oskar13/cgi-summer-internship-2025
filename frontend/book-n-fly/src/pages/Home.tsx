import { useState } from "react";
import SearchForm from "../components/SearchForm";
import FlightList from "../components/FlightList";
import { fetchFlights } from "../api/flights";
import { FlightSearchRequest, FlightSearchResponse } from "../types";

const Home = () => {
    const [flights, setFlights] = useState<FlightSearchResponse | null>(null);
    const [searchParams, setSearchParams] = useState<FlightSearchRequest | null>(null); // ✅ Store full search params

    const handleSearch = async (params: FlightSearchRequest) => {
        try {
            setSearchParams(params); // ✅ Save full search request
            const data = await fetchFlights(params);
            setFlights(data);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className="container mx-auto p-4">
            <SearchForm onSearch={handleSearch} />
            {flights && <FlightList searchResponse={flights} searchParams={searchParams} />}
        </div>
    );
};

export default Home;


