import { FlightItinerary } from "../types";
import FlightCard from "./FlightCard";

const FlightList = ({ flights }: { flights: FlightItinerary[] }) => {
    if (flights.length === 0) return <p className="text-center">No flights found.</p>;

    return (
        <div className="grid gap-4">
            {flights.map((flight, index) => (
                <FlightCard key={index} flight={flight.flights[0]} />
            ))}
        </div>
    );
};

export default FlightList;
