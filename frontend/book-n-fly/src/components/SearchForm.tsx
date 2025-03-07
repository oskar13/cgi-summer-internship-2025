import { useState } from "react";
import { FlightSearchRequest } from "../types";

const SearchForm = ({ onSearch }: { onSearch: (params: FlightSearchRequest) => void }) => {
    const [formData, setFormData] = useState<FlightSearchRequest>({
        origin: "",
        destination: "",
        outbound_date: "",
        travel_class: "1",
        adults: 1,
        children: 0,
        stops: 0,
        seed: Date.now().toString(),
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSearch(formData);
    };

    return (
        <form onSubmit={handleSubmit} className="bg-white p-4 shadow rounded-lg">
            <div className="grid grid-cols-2 gap-4">
                <input name="origin" placeholder="Origin" onChange={handleChange} required className="border p-2 rounded" />
                <input name="destination" placeholder="Destination" onChange={handleChange} required className="border p-2 rounded" />
                <input type="date" name="outbound_date" onChange={handleChange} required className="border p-2 rounded" />
                <select name="travel_class" onChange={handleChange} className="border p-2 rounded">
                    <option value="1">Economy</option>
                    <option value="2">Premium Economy</option>
                    <option value="3">Business</option>
                    <option value="4">First</option>
                </select>
                <input type="number" name="adults" min="1" onChange={handleChange} className="border p-2 rounded" />
                <input type="number" name="children" min="0" onChange={handleChange} className="border p-2 rounded" />
                <button type="submit" className="bg-blue-500 text-white p-2 rounded">Search Flights</button>
            </div>
        </form>
    );
};

export default SearchForm;
