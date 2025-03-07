// AI translated java classes to Typescript counterparts for easy type safety

export interface Airport {
    name: string;
    id: string;
    time: string;
}

export interface Flight {
    departure_airport: Airport;
    arrival_airport: Airport;
    duration: number;
    airplane: string;
    airline: string;
    airline_logo: string;
    travel_class: string;
    flight_number: string;
    extensions?: string[];
    legroom: string;
    overnight: boolean;
}

export interface Layover {
    duration: number;
    location: string;
}

export interface CarbonEmissions {
    this_flight: number; // Carbon emissions in grams
    typical_for_this_route: number; // Typical emissions in grams
    difference_percent: number; // Difference from typical emissions (%)
}

export interface FlightItinerary {
    flights: Flight[];  // Individual flight legs
    layovers?: Layover[];
    total_duration: number;
    carbon_emissions?: CarbonEmissions;
    price: number;
    type?: string;
    airline_logo?: string;
    extensions?: string[];
    departure_token?: string;
    booking_token?: string;
}

export interface SeatingOptions {
    extra_legroom: boolean;
    window_seats: boolean;
    group_seating: boolean;
    close_to_exit: boolean;
    seating_class: number; // Default is 1 (Economy)
}

export interface FlightSearchResponse {
    best_flights: FlightItinerary[];
    other_flights: FlightItinerary[];
    seating_options?: SeatingOptions;
}


export interface FlightSearchRequest {
    origin: string;
    destination: string;
    type?: 1 | 2; // 1 = Round trip (not implemented yet), 2 = One way
    adults?: number;
    children?: number;
    outbound_date: string; // YYYY-MM-DD
    return_date?: string; // YYYY-MM-DD (optional)
    travel_class?: "1" | "2" | "3" | "4"; // 1 = Economy, 2 = Premium Economy, 3 = Business, 4 = First
    max_price?: number;
    max_duration?: number; // Maximum flight duration in minutes
    stops?: 0 | 1 | 2 | 3; // 0 = Any stops, 1 = Nonstop, 2 = 1 stop, 3 = 2 stops
    min_avg_legroom?: number; // Minimum average legroom in cm
    extra_legroom?: boolean;
    window_seats?: boolean;
    group_seating?: boolean;
    close_to_exit?: boolean;
    seed?: string;
}
