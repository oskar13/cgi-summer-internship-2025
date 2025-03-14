# Backend

## **Requirements**
Ensure you have the following installed on your machine:
- **Java 21** (JDK)
- **Option 1: IDE like IntelliJ**
    - Lombok plugin configured
    - Configure SERP_API_KEY env variable in run configuration.
- **Option 2: Maven** 
    - Configure SERP_API_KEY variable for you shell
    - Run:

        `mvn spring-boot:run`

- **Option3: Docker**
    - Docker installed
    - `.evn.local` file in folder has api key 
    - Run:

        `docker build -t booknfly-backend .`
        
        `docker run -p 8080:8080 --env-file .env.local booknfly-backend`
    



---

# Deployment

The Dockerfile in the project root packages backend and frontend code in one container. When changing the port of the backend server make sure to update the API base url env variable in the frontend.

---

## API Documentation From Frontend to Backend

The mock and SerpApi data sources will output identically structured data to the front-end.

The API follows closely the API design of https://serpapi.com/google-flights-api.

Main difference being that minimum average legroom (min_avg_legroom) is not a query option in the SerpApi so I have to filter the results further myself.

### Flight Info API `POST /api/(mock)/flights`
**POST request body structure overview:**

```json
{
    "origin": "String - Departure airport IATA code",
    "destination": "String - Arrival airport IATA code",
    "type": "Integer - 1 - Round trip (not implemented yet), 2 - One way",
    "adults": "Integer - Parameter defines the number of adults. Default to 1.",
    "children": "Integer - Parameter defines the number of children. Default to 0.",
    "outbound_date": "String - Parameter defines the outbound date. The format is YYYY-MM-DD. e.g. 2025-03-02",
    "return_date": "String - Parameter defines the return date. The format is YYYY-MM-DD. e.g. 2025-03-08",
    "travel_class": "Integer (optional) - Parameter defines the travel class. Available options: 1 - Economy (default), 2 - Premium economy, 3 - Business, 4 - First",
    "max_price": "Integer (optional) - If not present defaults to unlimited",
    "max_duration": "Integer (optional) - Parameter defines the maximum flight duration, in minutes. For example, specify 1500 for 25 hours.",
    "stops": "Integer (optional) - Defines the number of stops during the flight. Available options: 0 - Any number of stops (default), 1 - Nonstop only, 2 - 1 stop or fewer, 3 - 2 stops or fewer",
    "min_avg_legroom": "Integer (optional) - Defines minimal average legroom for the flight in centimeters. Must be over 50",
    "extra_legroom": "Boolean (optional) - On seating plan search for seats marked with extra legroom.",
    "window_seats": "Boolean (optional) - On seating plan search for seats marked as window seats.",
    "group_seating": "Boolean (optional) - On seating plan find seats next to each other depending on the number of tickets.",
    "close_to_exit": "Boolean (optional) - On seating plan find seats that are marked close to exit."
}

```

#### Example
**Endpoint:** `POST /api/flights`

*Request body:*
```json
{
    "origin": "TLL",
    "destination": "BER",
    "type": 2,
    "adults": 0,
    "children": 0,
    "outbound_date": "2025-03-15",
    "return_date": "2025-03-16",
    "travel_class": "1",
    "max_price": "1000",
    "max_duration": "1000",
    "stops": 0,
    "min_avg_legroom": 70,
    "extra_legroom": false,
    "window_seats": true,
    "group_seating": false,
    "close_to_exit": false
}

```

*Response:*

<details>
<summary>Expand for full response</summary>

    ```json
    
    
        {
    
            "best_flights":
            [
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 05:30"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Riia lennujaam",
                                "id": "RIX",
                                "time": "2025-03-15 06:20"
                            }
                            ,
                            "duration": 50,
                            "airplane": "Airbus A220-300 Passenger",
                            "airline": "Air Baltic",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/BT.png",
                            "travel_class": "Economy",
                            "flight_number": "BT 362",
                            "legroom": "76 cm",
                            "extensions":
                            [
                                "Average legroom (76 cm)",
                                "Carbon emissions estimate: 47 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Riia lennujaam",
                                "id": "RIX",
                                "time": "2025-03-15 07:10"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-15 07:55"
                            }
                            ,
                            "duration": 105,
                            "airplane": "Airbus A220-300 Passenger",
                            "airline": "Air Baltic",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/BT.png",
                            "travel_class": "Economy",
                            "flight_number": "BT 211",
                            "legroom": "76 cm",
                            "extensions":
                            [
                                "Average legroom (76 cm)",
                                "Carbon emissions estimate: 94 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 50,
                            "name": "Riia lennujaam",
                            "id": "RIX"
                        }
                    ]
                    ,
                    "total_duration": 205,
                    "carbon_emissions":
                    {
                        "this_flight": 142000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 4
                    }
                    ,
                    "price": 114,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/BT.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZ3RDVkRNMk1ueENWREl4TVJvS0NNcFlFQUlhQTBWVlVqZ2NjUFpiIixbWyJUTEwiLCIyMDI1LTAzLTE1IiwiUklYIixudWxsLCJCVCIsIjM2MiJdLFsiUklYIiwiMjAyNS0wMy0xNSIsIkJFUiIsbnVsbCwiQlQiLCIyMTEiXV1d"
                }
                ,
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 14:30"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Stockholmi Arlanda lennujaam",
                                "id": "ARN",
                                "time": "2025-03-15 14:30"
                            }
                            ,
                            "duration": 60,
                            "airplane": "Airbus A320neo",
                            "airline": "Scandinavian Airlines",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/SK.png",
                            "travel_class": "Economy",
                            "flight_number": "SK 1773",
                            "legroom": "74 cm",
                            "extensions":
                            [
                                "Below average legroom (74 cm)",
                                "In-seat USB outlet",
                                "Carbon emissions estimate: 51 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Stockholmi Arlanda lennujaam",
                                "id": "ARN",
                                "time": "2025-03-15 16:00"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-15 17:35"
                            }
                            ,
                            "duration": 95,
                            "airplane": "Canadair RJ 900",
                            "airline": "Scandinavian Airlines",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/SK.png",
                            "travel_class": "Economy",
                            "flight_number": "SK 2679",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Carbon emissions estimate: 132 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 90,
                            "name": "Stockholmi Arlanda lennujaam",
                            "id": "ARN"
                        }
                    ]
                    ,
                    "total_duration": 245,
                    "carbon_emissions":
                    {
                        "this_flight": 183000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 35
                    }
                    ,
                    "price": 124,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/SK.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZzFUU3pFM056TjhVMHN5TmpjNUdnb0lzMkFRQWhvRFJWVlNPQnh3aFdRPSIsW1siVExMIiwiMjAyNS0wMy0xNSIsIkFSTiIsbnVsbCwiU0siLCIxNzczIl0sWyJBUk4iLCIyMDI1LTAzLTE1IiwiQkVSIixudWxsLCJTSyIsIjI2NzkiXV1d"
                }
                ,
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 09:30"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-15 10:30"
                            }
                            ,
                            "duration": 120,
                            "airplane": "Boeing 737",
                            "airline": "Ryanair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/FR.png",
                            "travel_class": "Economy",
                            "flight_number": "FR 2733",
                            "legroom": "76 cm",
                            "extensions":
                            [
                                "Average legroom (76 cm)",
                                "Carbon emissions estimate: 96 kg"
                            ]
                        }
                    ]
                    ,
                    "total_duration": 120,
                    "carbon_emissions":
                    {
                        "this_flight": 97000,
                        "typical_for_this_route": 136000,
                        "difference_percent": -29
                    }
                    ,
                    "price": 143,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/FR.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZ1pHVWpJM016TWFDZ2piYnhBQ0dnTkZWVkk0SEhEM2N3PT0iLFtbIlRMTCIsIjIwMjUtMDMtMTUiLCJCRVIiLG51bGwsIkZSIiwiMjczMyJdXV0="
                }
                ,
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 06:00"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 06:30"
                            }
                            ,
                            "duration": 30,
                            "airplane": "Embraer 190",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1036",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Carbon emissions estimate: 51 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 07:05"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-15 08:00"
                            }
                            ,
                            "duration": 115,
                            "airplane": "Airbus A321 (Sharklets)",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1431",
                            "legroom": "76 cm",
                            "extensions":
                            [
                                "Average legroom (76 cm)",
                                "Wi-Fi for a fee",
                                "Carbon emissions estimate: 125 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 35,
                            "name": "Helsingi Vantaa lennujaam",
                            "id": "HEL"
                        }
                    ]
                    ,
                    "total_duration": 180,
                    "carbon_emissions":
                    {
                        "this_flight": 177000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 30
                    }
                    ,
                    "price": 152,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZzFCV1RFd016WjhRVmt4TkRNeEdnb0kvM1VRQWhvRFJWVlNPQnh3dVhvPSIsW1siVExMIiwiMjAyNS0wMy0xNSIsIkhFTCIsbnVsbCwiQVkiLCIxMDM2Il0sWyJIRUwiLCIyMDI1LTAzLTE1IiwiQkVSIixudWxsLCJBWSIsIjE0MzEiXV1d"
                }
            ]
            ,
            "other_flights":
            [
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 21:25"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Riia lennujaam",
                                "id": "RIX",
                                "time": "2025-03-15 22:15"
                            }
                            ,
                            "duration": 50,
                            "airplane": "Airbus A220-300 Passenger",
                            "airline": "Air Baltic",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/BT.png",
                            "travel_class": "Economy",
                            "flight_number": "BT 318",
                            "legroom": "76 cm",
                            "extensions":
                            [
                                "Average legroom (76 cm)",
                                "Carbon emissions estimate: 47 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Riia lennujaam",
                                "id": "RIX",
                                "time": "2025-03-16 18:10"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-16 18:55"
                            }
                            ,
                            "duration": 105,
                            "airplane": "Airbus A220-300 Passenger",
                            "airline": "Air Baltic",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/BT.png",
                            "travel_class": "Economy",
                            "flight_number": "BT 213",
                            "legroom": "76 cm",
                            "extensions":
                            [
                                "Average legroom (76 cm)",
                                "Carbon emissions estimate: 94 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 1195,
                            "name": "Riia lennujaam",
                            "id": "RIX",
                            "overnight": true
                        }
                    ]
                    ,
                    "total_duration": 1350,
                    "carbon_emissions":
                    {
                        "this_flight": 142000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 4
                    }
                    ,
                    "price": 108,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/BT.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZ3RDVkRNeE9IeENWREl4TXhvS0NQSlRFQUlhQTBWVlVqZ2NjSWRYIixbWyJUTEwiLCIyMDI1LTAzLTE1IiwiUklYIixudWxsLCJCVCIsIjMxOCJdLFsiUklYIiwiMjAyNS0wMy0xNiIsIkJFUiIsbnVsbCwiQlQiLCIyMTMiXV1d"
                }
                ,
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 17:20"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 17:55"
                            }
                            ,
                            "duration": 35,
                            "airplane": "ATR 72",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1026",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Carbon emissions estimate: 25 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-16 11:15"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-16 12:15"
                            }
                            ,
                            "duration": 120,
                            "airplane": "Airbus A320",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1433",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Wi-Fi for a fee",
                                "Carbon emissions estimate: 124 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 1040,
                            "name": "Helsingi Vantaa lennujaam",
                            "id": "HEL",
                            "overnight": true
                        }
                    ]
                    ,
                    "total_duration": 1195,
                    "carbon_emissions":
                    {
                        "this_flight": 150000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 10
                    }
                    ,
                    "price": 132,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZzFCV1RFd01qWjhRVmt4TkRNekdnb0lyMllRQWhvRFJWVlNPQnh3bm1vPSIsW1siVExMIiwiMjAyNS0wMy0xNSIsIkhFTCIsbnVsbCwiQVkiLCIxMDI2Il0sWyJIRUwiLCIyMDI1LTAzLTE2IiwiQkVSIixudWxsLCJBWSIsIjE0MzMiXV1d"
                }
                ,
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 06:00"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 06:30"
                            }
                            ,
                            "duration": 30,
                            "airplane": "Embraer 190",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1036",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Carbon emissions estimate: 51 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 11:15"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-15 12:15"
                            }
                            ,
                            "duration": 120,
                            "airplane": "Airbus A319",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1433",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Wi-Fi for a fee",
                                "Carbon emissions estimate: 136 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 285,
                            "name": "Helsingi Vantaa lennujaam",
                            "id": "HEL"
                        }
                    ]
                    ,
                    "total_duration": 435,
                    "carbon_emissions":
                    {
                        "this_flight": 188000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 38
                    }
                    ,
                    "price": 136,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZzFCV1RFd016WjhRVmt4TkRNekdnb0lvR29RQWhvRFJWVlNPQnh3b1c0PSIsW1siVExMIiwiMjAyNS0wMy0xNSIsIkhFTCIsbnVsbCwiQVkiLCIxMDM2Il0sWyJIRUwiLCIyMDI1LTAzLTE1IiwiQkVSIixudWxsLCJBWSIsIjE0MzMiXV1d"
                }
                ,
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 08:25"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 08:55"
                            }
                            ,
                            "duration": 30,
                            "airplane": "ATR 72",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1012",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Carbon emissions estimate: 26 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 17:25"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-15 18:25"
                            }
                            ,
                            "duration": 120,
                            "airplane": "Airbus A321 (Sharklets)",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1435",
                            "legroom": "76 cm",
                            "extensions":
                            [
                                "Average legroom (76 cm)",
                                "Wi-Fi for a fee",
                                "Carbon emissions estimate: 125 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 510,
                            "name": "Helsingi Vantaa lennujaam",
                            "id": "HEL"
                        }
                    ]
                    ,
                    "total_duration": 660,
                    "carbon_emissions":
                    {
                        "this_flight": 152000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 12
                    }
                    ,
                    "price": 137,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZzFCV1RFd01USjhRVmt4TkRNMUdnb0lvMm9RQWhvRFJWVlNPQnh3cFc0PSIsW1siVExMIiwiMjAyNS0wMy0xNSIsIkhFTCIsbnVsbCwiQVkiLCIxMDEyIl0sWyJIRUwiLCIyMDI1LTAzLTE1IiwiQkVSIixudWxsLCJBWSIsIjE0MzUiXV1d"
                }
                ,
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 22:20"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 22:55"
                            }
                            ,
                            "duration": 35,
                            "airplane": "ATR 72",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1032",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Carbon emissions estimate: 26 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-16 07:05"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-16 08:00"
                            }
                            ,
                            "duration": 115,
                            "airplane": "Airbus A320",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1431",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Wi-Fi for a fee",
                                "Carbon emissions estimate: 124 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 490,
                            "name": "Helsingi Vantaa lennujaam",
                            "id": "HEL",
                            "overnight": true
                        }
                    ]
                    ,
                    "total_duration": 640,
                    "carbon_emissions":
                    {
                        "this_flight": 150000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 10
                    }
                    ,
                    "price": 157,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZzFCV1RFd016SjhRVmt4TkRNeEdnb0kxSG9RQWhvRFJWVlNPQnh3cFg4PSIsW1siVExMIiwiMjAyNS0wMy0xNSIsIkhFTCIsbnVsbCwiQVkiLCIxMDMyIl0sWyJIRUwiLCIyMDI1LTAzLTE2IiwiQkVSIixudWxsLCJBWSIsIjE0MzEiXV1d"
                }
                ,
                {
                    "flights":
                    [
                        {
                            "departure_airport":
                            {
                                "name": "Tallinna Lennujaam",
                                "id": "TLL",
                                "time": "2025-03-15 14:40"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 15:10"
                            }
                            ,
                            "duration": 30,
                            "airplane": "ATR 72",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1020",
                            "legroom": "79 cm",
                            "extensions":
                            [
                                "Average legroom (79 cm)",
                                "Carbon emissions estimate: 26 kg"
                            ]
                        }
                        ,
                        {
                            "departure_airport":
                            {
                                "name": "Helsingi Vantaa lennujaam",
                                "id": "HEL",
                                "time": "2025-03-15 17:25"
                            }
                            ,
                            "arrival_airport":
                            {
                                "name": "Berliini Brandenburgi lennujaam",
                                "id": "BER",
                                "time": "2025-03-15 18:25"
                            }
                            ,
                            "duration": 120,
                            "airplane": "Airbus A321 (Sharklets)",
                            "airline": "Finnair",
                            "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                            "travel_class": "Economy",
                            "flight_number": "AY 1435",
                            "legroom": "76 cm",
                            "extensions":
                            [
                                "Average legroom (76 cm)",
                                "Wi-Fi for a fee",
                                "Carbon emissions estimate: 125 kg"
                            ]
                        }
                    ]
                    ,
                    "layovers":
                    [
                        {
                            "duration": 135,
                            "name": "Helsingi Vantaa lennujaam",
                            "id": "HEL"
                        }
                    ]
                    ,
                    "total_duration": 285,
                    "carbon_emissions":
                    {
                        "this_flight": 152000,
                        "typical_for_this_route": 136000,
                        "difference_percent": 12
                    }
                    ,
                    "price": 175,
                    "type": "One way",
                    "airline_logo": "https://www.gstatic.com/flights/airline_logos/70px/AY.png",
                    "booking_token": "WyJDalJJWVhoeWJXdGZOMEp1YTNkQlNHNVBTMEZDUnkwdExTMHRMUzB0TFhscFltVm5NVUZCUVVGQlIyWkZOSGhqVDNCamJGRkJFZzFCV1RFd01qQjhRVmt4TkRNMUdnc0krNGNCRUFJYUEwVlZVamdjY0l5TkFRPT0iLFtbIlRMTCIsIjIwMjUtMDMtMTUiLCJIRUwiLG51bGwsIkFZIiwiMTAyMCJdLFsiSEVMIiwiMjAyNS0wMy0xNSIsIkJFUiIsbnVsbCwiQVkiLCIxNDM1Il1dXQ=="
                }
            ]
    
            ,
            "airports":
            [
                {
                    "departure":
                    [
                        {
                            "airport":
                            {
                                "id": "TLL",
                                "name": "Tallinna Lennujaam"
                            }
                            ,
                            "city": "Tallinn",
                            "country": "Eesti",
                            "country_code": "EE",
                            "image": "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTZH2wmJfREDNcsNHSzTFYC0EKfQX5s8bKeLeq58xR4-JuF5Cz0VEL0VARJoXO_FAPUzF_TXmzL8d_zPg",
                            "thumbnail": "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcSuRMfT3_gAy_HdYuuvD02MMssN23H8BaPEFbOD17M6ZdytjOZp-6ySnDgK1IIdMs5oMyi8d2xi4UCqVwGtoy1pczA29OiLXhddw0MT4xI"
                        }
                    ]
                    ,
                    "arrival":
                    [
                        {
                            "airport":
                            {
                                "id": "BER",
                                "name": "Berliini Brandenburgi lennujaam"
                            }
                            ,
                            "city": "Berliin",
                            "country": "Saksamaa",
                            "country_code": "DE",
                            "image": "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQt2sQ7Aqn9XDqztmoYElQwE26oMklrcGD7-ZuKOUPZVDpWlL-yn0uHraRpfjA9kXtiLrBxI-YW45Rn4g",
                            "thumbnail": "https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTWlCvQfTpTUuTsCeI1S4Yw2Y5H4T77pM--xjPOa8J2BBUf2IPwdlOWEEgXN-UK1iZEuid20zbzO_nW3qSbU4dk9KwrbnjlUxopo6ayjnY"
                        }
                    ]
                }
            ],
            "seating_options": {
                "extra_legroom": false,
                "window_seats": true,
                "group_seating": false,
                "close_to_exit": false
            }
        }
    
    ```
    
</details>


### Seat map and suggestions API `POST /api/mock/seating`

Important to note here that there is no real data available for seat layouts so its only available as mock data.

Also different from flights API endpoint seat class was converted from number form to string value representing the name of the class.

**GET query params overview:**
 - "booking_id" - String
 - "numTickets", defaultValue = "1" - Integer 
 - "extraLegroom", defaultValue = "false" - Boolean
 - "windowSeats", defaultValue = "false" - Boolean
 - "groupSeating", defaultValue = "false" - Boolean
 - "closeToExit", defaultValue = "false" - Boolean
 - "seatingClass", defaultValue = "Economy" - String

*Response Structure:*
```json
{
  "seats": [ // Array of seat objects
    {
      "seat_number": "String - Seat row number and seat letter",
      "row": "Integer - Seat row number",
      "seat_class": "String - First Class, Business, Premium Economy, Economy",
      "features": [ // String array of features
        "extra_legroom",
        "window_seat"
      ],
      "available": true, // Not booked
      "suggested": false, // Not suggested to user
      "selected": false // Not selected/confirmed by user
    },
    ...
  ]
}

```

#### Example
**Endpoint:** `GET /api/mock/seating`

*Request:*
curl --location 'http://localhost:8080/api/mock/seating/map?numTickets=3&seatingClass=Economy&booking_id=AA11'

*Response:*
```json
{
  "seats": [
    {
      "seat_number": "1A",
      "row": 1,
      "seat_class": "First Class",
      "features": [
        "extra_legroom",
        "window_seat"
      ],
      "available": true,
      "suggested": false,
      "selected": false
    },
    ...
  ]
}

```