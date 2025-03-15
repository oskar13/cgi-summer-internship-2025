import { useNavigate } from "react-router-dom";
import { FlightItinerary, FlightSearchRequest } from "../types";
import { format } from "date-fns";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";


interface FlightCardProps {
    itinerary: FlightItinerary;
    searchParams: FlightSearchRequest | null;
}

const FlightCard: React.FC<FlightCardProps> = ({ itinerary, searchParams }) => {
    const navigate = useNavigate();
    const firstFlight = itinerary.flights[0];
    const imgBase = searchParams?.useRealApi ? "" :  API_BASE_URL ;


    const formattedDepartureTime = format(new Date(firstFlight.departure_airport.time), "dd MMM yyyy, HH:mm");
    const formattedArrivalTime = format(new Date(firstFlight.arrival_airport.time), "dd MMM yyyy, HH:mm");

    const handleClick = () => {
        navigate(`/flight/${itinerary.booking_token}`, { 
            state: { itinerary, searchParams } // Pass searchParams
        });
    };

    return (
        <div className="border p-4 rounded shadow cursor-pointer hover:bg-gray-100" onClick={handleClick}>
            <header className="items-center align-middle flex mb-4">
                <img src={imgBase + firstFlight.airline_logo} alt={firstFlight.airline} className="h-10 inline-block mr-2" />
                <h3 className="font-bold  inline-block"> {firstFlight.airline} - {firstFlight.flight_number}</h3>
            </header>
            <section className="pl-4">
                <p>
                    {firstFlight.departure_airport.id} ({formattedDepartureTime}) →
                    {firstFlight.arrival_airport.id} ({formattedArrivalTime})
                </p>
                <p>Duration: {itinerary.total_duration} min</p>
                <p>Price: € {itinerary.price}</p>
                {itinerary.carbon_emissions && (
                    <p>CO₂: {itinerary.carbon_emissions.this_flight/1000}kg</p>
                )}
            </section>
            
        </div>
    );
};

export default FlightCard;
