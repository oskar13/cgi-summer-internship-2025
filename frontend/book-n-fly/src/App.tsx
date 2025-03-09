import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Home from "./pages/Home.tsx";
import './App.css'
import FlightDetails from "./pages/FlightDetails.tsx";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/flight/:booking_token" element={<FlightDetails />} />
            </Routes>
        </Router>
    );
}

export default App;
