import { useNavigate } from "react-router-dom";
import { FlightItinerary, FlightSearchRequest } from "../types";

interface FlightCardProps {
    itinerary: FlightItinerary;
    searchParams: FlightSearchRequest | null;
}

const FlightCard: React.FC<FlightCardProps> = ({ itinerary, searchParams }) => {
    const navigate = useNavigate();
    const firstFlight = itinerary.flights[0];

    const handleClick = () => {
        navigate(`/flight/${itinerary.booking_token}`, { 
            state: { itinerary, searchParams } // Pass searchParams
        });
    };

    return (
        <div className="border p-4 rounded shadow cursor-pointer hover:bg-gray-100" onClick={handleClick}>
            <h3 className="font-bold">{firstFlight.airline} - {firstFlight.flight_number}</h3>
            <p>
                {firstFlight.departure_airport.id} ({firstFlight.departure_airport.time}) → 
                {firstFlight.arrival_airport.id} ({firstFlight.arrival_airport.time})
            </p>
            <p>Duration: {itinerary.total_duration} min</p>
            <p>Price: ${itinerary.price}</p>
            {itinerary.carbon_emissions && (
                <p>CO₂: {itinerary.carbon_emissions.this_flight}g</p>
            )}
            <img src={firstFlight.airline_logo} alt={firstFlight.airline} className="h-8 mt-2" />
        </div>
    );
};

export default FlightCard;
