import { useState } from "react";
import SearchForm from "../components/SearchForm";
import FlightList from "../components/FlightList";
import { fetchFlights } from "../api/flights";
import { FlightItinerary, FlightSearchRequest } from "../types"; // Importing the correct type

const Home = () => {
    const [flights, setFlights] = useState<FlightItinerary[]>([]); // Using the correct type

    const handleSearch = async (params: FlightSearchRequest) => {
        try {
            const data = await fetchFlights(params);
            setFlights(data.best_flights || data.other_flights || []);
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className="container mx-auto p-4">
            <SearchForm onSearch={handleSearch} />
            <FlightList flights={flights} /> {/* Fixed typo here */}
        </div>
    );
};

export default Home;
