import { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { FlightItinerary, FlightSearchRequest, Seat, SeatingMessage } from "../types";

const FlightDetails = () => {
	const { booking_token } = useParams<{ booking_token: string }>();
	const location = useLocation();
	const { itinerary, searchParams }: { itinerary: FlightItinerary; searchParams?: FlightSearchRequest } = location.state;

	const imgBase = searchParams?.useRealApi ? "" :  "http://localhost:8080" ;

	const [seats, setSeats] = useState<Seat[]>([]);
	const [selectedSeats, setSelectedSeats] = useState<string[]>([]);
	const [message, setMessage] = useState<SeatingMessage | null>(null);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState<string | null>(null);
	const [preferences, setPreferences] = useState({
		extraLegroom: searchParams?.extra_legroom ?? false,
		windowSeats: searchParams?.window_seats ?? false,
		groupSeating: searchParams?.group_seating ?? false,
		closeToExit: searchParams?.close_to_exit ?? false,
	});

	const travelClassMap: Record<number, string> = {
		1: "Economy",
		2: "Premium Economy",
		3: "Business",
		4: "First Class",
	};

	const numTickets = (Number(searchParams?.adults) || 1) + (Number(searchParams?.children) || 0);


	// Fetch seat suggestions from API
	const fetchSeats = async () => {
		setLoading(true);
		try {
			const queryParams = new URLSearchParams({
				numTickets: numTickets.toString(),
				seatingClass: travelClassMap[searchParams?.travel_class ?? 1],
				extraLegroom: preferences.extraLegroom.toString(),
				windowSeats: preferences.windowSeats.toString(),
				groupSeating: preferences.groupSeating.toString(),
				closeToExit: preferences.closeToExit.toString(),
				booking_id: booking_token || "",
			});

			const response = await fetch(`http://localhost:8080/api/mock/seating/map?${queryParams.toString()}`);
			if (!response.ok) throw new Error("Failed to fetch seat data.");
			const data = await response.json();

			setSeats(data.seats);
			setMessage(data.message);
		} catch (err) {
			setError("Could not load seat map." + JSON.stringify(err));
		} finally {
			setLoading(false);
		}
	};

	// Fetch seats when component mounts or preferences change
	useEffect(() => {
		fetchSeats();
	}, [booking_token, searchParams, preferences]);

	// Handle user clicking a seat preference
	const togglePreference = (key: keyof typeof preferences) => {
		setPreferences((prev) => ({
			...prev,
			[key]: !prev[key],
		}));
	};

	const handleSelectSeat = (seatNumber: string) => {
		if (selectedSeats.includes(seatNumber)) {
			// Deselect seat
			setSelectedSeats((prevSelected) => prevSelected.filter((seat) => seat !== seatNumber));
		} else if (selectedSeats.length < numTickets) {
			// Select seat if under limit
			setSelectedSeats((prevSelected) => [...prevSelected, seatNumber]);
		}
	};

	// Group seats by row
	const groupedSeats: { [key: number]: Seat[] } = {};
	seats.forEach((seat) => {
		if (!groupedSeats[seat.row]) {
			groupedSeats[seat.row] = [];
		}
		groupedSeats[seat.row].push(seat);
	});

	return (
		<div className="p-4 container mx-auto">
			<section className="md:max-w-2/3 mx-auto mb-4">
				{JSON.stringify(itinerary)}

				<h2 className="text-3xl font-bold mb-2">Flight Details</h2>
				{itinerary.flights.map((flight, index) => (
					<div key={index} className="border-l-gray-300 border-l-2 rounded-lg p-4 mb-4 bg-gray-100">
						<h3 className="mb-2 text-gray-500 font-semibold">Flight #{index+1}</h3>
						<div className="flex items-center space-x-4">
						
							<img src={imgBase +flight.airline_logo} alt={`${flight.airline} Logo`} className="w-12 h-12" />
							<div>
								<h4 className="text-xl font-semibold">{flight.airline} ({flight.flight_number})</h4>
								<p className="text-sm text-gray-600">{flight.airplane} - {travelClassMap[Number(flight.travel_class)]}</p>
							</div>
						</div>

						<div className="mt-2">
							<p><strong>Departure:</strong> {flight.departure_airport.name} ({flight.departure_airport.id}) at {flight.departure_airport.time}</p>
							<p><strong>Arrival:</strong> {flight.arrival_airport.name} ({flight.arrival_airport.id}) at {flight.arrival_airport.time}</p>
							<p><strong>Duration:</strong> {flight.duration} min</p>
							<p><strong>Legroom:</strong> {flight.legroom}</p>
							{flight.extensions && flight.extensions.length > 0 && (
							<p><strong>Extensions:</strong> {flight.extensions.join(", ")}</p>
							)}
							<p><strong>Overnight Flight:</strong> {flight.overnight ? "Yes" : "No"}</p>
						</div>
					</div>
				))}

				<div className="border-t pt-4 mt-4">
					<h3 className="text-xl font-semibold">Total Trip Details</h3>
					<p><strong>Total Duration:</strong> {itinerary.total_duration} min</p>
					{itinerary.extensions && itinerary.extensions.length > 0 && (
							<p><strong>Extensions:</strong> {itinerary.extensions.join(", ")}</p>
							)}
					<p><strong>Price:</strong> ${itinerary.price}</p>
					{/*itinerary.carbon_emissions && ( <p><strong>Carbon Emissions:</strong> {itinerary.carbon_emissions} kg CO₂</p>)*/}
				</div>

				<h3 className="text-2xl font-bold mt-4">Seat Selection</h3>
				<p className="mb-2 ">
					Seats selected: {selectedSeats.join(", ")} ({selectedSeats.length}/{numTickets})
				</p>
				<div className="mb-2">
					<h3 className="text-2xl font-semibold mb-2">Seat suggestion preferences:</h3>
					<button
						className={`mx-1 px-2 py-1 rounded-lg ${preferences.groupSeating ? "bg-sky-200" : "bg-gray-200"}`}
						onClick={() => togglePreference("groupSeating")}
					>
						👨‍👩‍👦 Group Seating {preferences.groupSeating ? "✅" : ""}
					</button>
					<button
						className={`mx-1 px-2 py-1 rounded-lg ${preferences.extraLegroom ? "bg-sky-200" : "bg-gray-200"}`}
						onClick={() => togglePreference("extraLegroom")}
					>
						🦵 Extra Legroom {preferences.extraLegroom ? "✅" : ""}
					</button>
					<button
						className={`mx-1 px-2 py-1 rounded-lg ${preferences.windowSeats ? "bg-sky-200" : "bg-gray-200"}`}
						onClick={() => togglePreference("windowSeats")}
					>
						🪟 Window seats {preferences.windowSeats ? "✅" : ""}
					</button>
					<button
						className={`mx-1 px-2 py-1 rounded-lg ${preferences.closeToExit ? "bg-sky-200" : "bg-gray-200"}`}
						onClick={() => togglePreference("closeToExit")}
					>
						🏃‍♀️ Close to Exit {preferences.closeToExit ? "✅" : ""}
					</button>
					<p className="my-2">Suggested seats are highlighted <span className="bg-sky-300 inline-block rounded px-2">blue</span></p>
				</div>
			</section>

			{loading ? (
				<p>Loading seat map...</p>
			) : error ? (
				<p className="text-red-500">{error}</p>
			) : (
				<div className="md:w-1/2 mx-auto">
					{message && (
						<div
							className={`mt-4 p-2 md:max-w-1/2 mx-auto rounded text-white ${message.status === "error"
								? "bg-red-500"
								: message.status === "warning"
									? "bg-yellow-500"
									: "bg-blue-500"
								}`}
						>
							{message.text}
						</div>
					)}
					<div className="mt-4 space-y-2">
						{Object.entries(groupedSeats).map(([row, rowSeats]) => (
							<div key={row} className="flex space-x-2">
								{rowSeats.map((seat) => (
									<div
										key={seat.seat_number}
										className={`p-2 border-b-2  rounded text-center cursor-pointer w-48
											${seat.suggested ? "bg-blue-300 border-b-blue-500" : ""}
											${seat.available && !seat.suggested ? "bg-green-200 border-b-emerald-300" : ""} 
											${!seat.available ? "bg-gray-300 border-b-gray-400" : ""}
											${selectedSeats.includes(seat.seat_number) ? "border-4 border-black !border-b-black !border-b-4" : ""}
										`}
										onClick={() => seat.available && handleSelectSeat(seat.seat_number)}
									>
										{seat.seat_number} {/*selectedSeats.includes(seat.seat_number) ? "✅" : ""*/}
										{/*JSON.stringify(seat.features)*/}
										{seat.features.includes("window_seat") ? "🪟" : ""}
										{seat.features.includes("extra_legroom") ? "🦵" : ""}
										{seat.features.includes("close_to_exit") ? "🏃" : ""}
									</div>
								))}
							</div>
						))}
					</div>
				</div>
			)}
		</div>
	);
};

export default FlightDetails;
