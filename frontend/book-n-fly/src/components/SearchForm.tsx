import { useState } from "react";
import { FlightSearchRequest } from "../types";

const API_URL = "https://airport-autosuggest.flightright.net/v1/airports/COM?name=";

const SearchForm = ({ onSearch }: { onSearch: (params: FlightSearchRequest) => void }) => {
	const [formData, setFormData] = useState<FlightSearchRequest>({
		origin: "",
		destination: "",
		outbound_date: "",
		travel_class: "1",
		adults: 1,
		children: 0,
		stops: 0,
		max_price: undefined,
		max_duration: undefined,
		extra_legroom: false,
		window_seats: false,
		group_seating: false,
		close_to_exit: false,
		seed: "", // Seed will be generated dynamically
		useRealApi: false
	});

	const [suggestions, setSuggestions] = useState<{ iata: string; name: string }[]>([]);
	const [activeField, setActiveField] = useState<"origin" | "destination" | null>(null);
	const [typingTimeout, setTypingTimeout] = useState<number| null>(null);


	// Generate the seed based on selected fields
	const generateSeed = () => {
		return `${formData.origin}${formData.destination}${formData.outbound_date}${formData.max_price}${formData.max_duration}`;
	};

	const fetchAirportSuggestions = async (query: string) => {
		if (!query) {
			setSuggestions([]);
			return;
		}

		try {
			const response = await fetch(API_URL + encodeURIComponent(query));
			if (!response.ok) throw new Error("API error");
			const data = await response.json();
			setSuggestions(data || []);
		} catch (error) {
			console.warn("Airport autocomplete API failed", error);
			setSuggestions([]); // Silent fallback
		}
	};

	const handleSelectAirport = (iata: string) => {
		if (!activeField) return;
		setFormData((prev) => ({ ...prev, [activeField]: iata }));
		setSuggestions([]);
		setActiveField(null);
	};

	const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
		const { name, value, type } = e.target;

		const isCheckbox = e.target instanceof HTMLInputElement && type === "checkbox";

		setFormData((prevData) => ({
			...prevData,
			[name]: isCheckbox ? (e.target as HTMLInputElement).checked : value,
		}));

		if (name === "origin" || name === "destination") {
			setActiveField(name);

			// Debounce API call (waits 1 second after user stops typing)
			if (typingTimeout) clearTimeout(typingTimeout);
			const newTimeout = setTimeout(() => fetchAirportSuggestions(value), 500);
			setTypingTimeout(newTimeout);
		}
	};

	const handleSubmit = (e: React.FormEvent) => {
		e.preventDefault();
		const finalData = {
			...formData,
			seed: generateSeed(),
			max_duration: formData.max_duration ? formData.max_duration * 60 : undefined, // Convert hours to minutes
		};
		onSearch(finalData);
	};

	return (
		<form onSubmit={handleSubmit} className="bg-white p-4 shadow-2xl rounded-lg">
			<div className="grid grid-cols-2 gap-4">
				<div className="relative">
					<label htmlFor="origin">Flying from</label>
					<input
						name="origin"
						placeholder="Origin"
						value={formData.origin}
						onChange={handleChange}
						required
						className="border p-2 rounded w-full"
						autoComplete="off"
					/>
					{activeField === "origin" && suggestions.length > 0 && (
						<ul className="absolute bg-white border rounded w-full mt-1 max-h-40 overflow-auto z-10 shadow-xl">
							{suggestions.map((airport) => (
								<li
									key={airport.iata}
									onClick={() => handleSelectAirport(airport.iata)}
									className="p-2 cursor-pointer hover:bg-gray-200"
								>
									{airport.name} ({airport.iata})
								</li>
							))}
						</ul>
					)}

				</div>
				<div className="relative">
					<label htmlFor="destination">Flying to</label>
					<input
						name="destination"
						placeholder="Destination"
						value={formData.destination}
						onChange={handleChange}
						required
						className="border p-2 rounded w-full"
						autoComplete="off"
					/>
					{activeField === "destination" && suggestions.length > 0 && (
						<ul className="absolute bg-white border rounded w-full mt-1 max-h-40 overflow-auto z-10 shadow-xl">
							{suggestions.map((airport) => (
								<li
									key={airport.iata}
									onClick={() => handleSelectAirport(airport.iata)}
									className="p-2 cursor-pointer hover:bg-gray-200"
								>
									{airport.name} ({airport.iata})
								</li>
							))}
						</ul>
					)}
				</div>
				<div className="flex flex-col">
					<label htmlFor="outbound_date">Outbound date</label>
					<input type="date" name="outbound_date" onChange={handleChange} required className="border p-2 rounded" />
				</div>

				<div className="flex flex-col">
					<label htmlFor="travel_class">Travel Class</label>
					<select name="travel_class" onChange={handleChange} className="border p-2 rounded">
						<option value="1">Economy</option>
						<option value="2">Premium Economy</option>
						<option value="3">Business</option>
						<option value="4">First</option>
					</select>
				</div>

				<div className="flex flex-col">
					<label>Adults</label>
					<input type="number" name="adults" min="1" value={formData.adults} onChange={handleChange} className="border p-2 rounded" />
				</div>

				<div className="flex flex-col">
					<label>Children</label>
					<input type="number" name="children" min="0" value={formData.children} onChange={handleChange} className="border p-2 rounded" />
				</div>

				<div className="flex flex-col">
					<label>Max Price (€)</label>
					<input type="number" name="max_price" min="0" value={formData.max_price || ""} onChange={handleChange} className="border p-2 rounded" />
				</div>

				<div className="flex flex-col">
					<label>Max Duration (hours)</label>
					<input type="number" name="max_duration" min="1" value={formData.max_duration || ""} onChange={handleChange} className="border p-2 rounded" />
				</div>

				<h3 className="font-light text-xl">Seating Options:</h3>
				<div className="col-span-2 grid sm:grid-cols-2 grid-cols-1 gap-2">
					<label className="flex items-center">
						<input type="checkbox" name="extra_legroom" checked={formData.extra_legroom} onChange={handleChange} className="mr-2" />
						Extra Legroom
					</label>
					<label className="flex items-center">
						<input type="checkbox" name="window_seats" checked={formData.window_seats} onChange={handleChange} className="mr-2" />
						Window Seats
					</label>
					<label className="flex items-center">
						<input type="checkbox" name="group_seating" checked={formData.group_seating} onChange={handleChange} className="mr-2" />
						Group Seating
					</label>
					<label className="flex items-center">
						<input type="checkbox" name="close_to_exit" checked={formData.close_to_exit} onChange={handleChange} className="mr-2" />
						Close to Exit
					</label>
				</div>


				<div className="flex col-span-2">
					<div className="flex items-center h-5">
						<input name="useRealApi" onChange={handleChange} type="checkbox" className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600" />
					</div>
					<div className="ms-2 font-semibold">
						<p  className="mb-2">Use SerpAPI to load results from Google Flights</p>
						<p id="useMockApi-text" className="text-xs font-normal text-gray-700">The number of free API calls are limited to 100 per month, also needs API key env variable set for the backend.</p>
					</div>
				</div>



				<button type="submit" className="col-span-2 bg-blue-500 text-white p-2 rounded">Search Flights</button>
			</div>
		</form>
	);
};

export default SearchForm;
