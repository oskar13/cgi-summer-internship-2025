import { useNavigate } from "react-router-dom";
import { FlightSearchResponse, FlightSearchRequest, FlightItinerary } from "../types";
import FlightCard from "./FlightCard";

interface FlightListProps {
    searchResponse: FlightSearchResponse;
    searchParams: FlightSearchRequest | null;
}

const FlightList: React.FC<FlightListProps> = ({ searchResponse, searchParams }) => {
    const navigate = useNavigate();

    const handleFlightClick = (itinerary: FlightItinerary) => {
        navigate(`/flight/${itinerary.booking_token}`, {
            state: { itinerary, searchParams }, //  Pass searchParams to details page
        });
    };

    return (
        <div className="grid gap-4">
            {[...searchResponse.best_flights, ...searchResponse.other_flights].map((itinerary, index) => (
                <div key={index} onClick={() => handleFlightClick(itinerary)} className="cursor-pointer">
                    <FlightCard key={index} itinerary={itinerary} searchParams={searchParams} />
                </div>
            ))}
        </div>
    );
};

export default FlightList;