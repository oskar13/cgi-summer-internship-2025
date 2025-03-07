import { Flight } from "../types";

const FlightCard = ({ flight }: { flight: Flight }) => (
    <div className="border p-4 rounded shadow">
        <h3 className="font-bold">{flight.airline} - {flight.flight_number}</h3>
        <p>{flight.departure_airport.id} ({flight.departure_airport.time}) → {flight.arrival_airport.id} ({flight.arrival_airport.time})</p>
        <p>Duration: {flight.duration} min</p>
        <p>Legroom: {flight.legroom}</p>
        <p>Travel Class: {flight.travel_class}</p>
        <img src={flight.airline_logo} alt={flight.airline} className="h-8 mt-2" />
    </div>
);

export default FlightCard;
