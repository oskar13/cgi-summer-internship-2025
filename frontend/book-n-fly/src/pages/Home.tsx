import { useState } from "react";
import SearchForm from "../components/SearchForm";
import FlightList from "../components/FlightList";
import { fetchFlights } from "../api/flights";
import { FlightSearchRequest, FlightSearchResponse } from "../types";

const Home = () => {
    const [flights, setFlights] = useState<FlightSearchResponse | null>(null);
    const [searchParams, setSearchParams] = useState<FlightSearchRequest | null>(null); // Store full search params
    const [error, setError] = useState<string | null>(null);


    const handleSearch = async (params: FlightSearchRequest) => {
        setError(null); 
        try {
            setSearchParams(params); // Save full search request
            const data = await fetchFlights(params);
            setFlights(data);
        } catch (error) {
            setError(error instanceof Error ? error.message : "Something went wrong");
            setFlights(null);
        }
    };

    return (
        <div>
            
            <div className="bg-[url(/background.jpg)] bg-no-repeat bg-cover">
                 
                <div className="container mx-auto p-4 xl:max-w-1/2 lg:max-w-3/4 ">
                <header><img src="/logo.svg" alt="Book n Fly" className="sm:max-w-80 max-w-40" /></header>
                    <SearchForm onSearch={handleSearch} />
                </div>
            </div>
            <div className="container mx-auto p-4 xl:max-w-1/2 lg:max-w-3/4">
                {error && <div className="text-red-500 bg-red-100 p-4 rounded">{error}</div>} {/* ✅ Show error */}
                {flights && <FlightList searchResponse={flights} searchParams={searchParams} />}
            </div>
        </div>

    );
};

export default Home;


