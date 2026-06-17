import React from "react";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";

import "./App.css";
import EmployeeAgGrid from "./pages/EmployeeAgGrid";
import AgGridPage1 from "./pages/AgGridPage1";

function HomePage() {
  return (
    <div>
      <h1>Welcome</h1>
      <p>Use the navigation above to view all employees.</p>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <div>
        <header>
          <nav>
            <Link to="/">Home</Link>
            <Link to="/employees">Employees</Link>
          </nav>
        </header>

        <main>
          <Routes>
            <Route path="/" element={<HomePage />} />

            <Route path="/employees" element={<AgGridPage1 />} />
            <Route path="/grid1" element={<EmployeeAgGrid />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
