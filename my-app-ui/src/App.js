import React from "react";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import EmployeesPage from "./pages/EmployeesPage";
import "./App.css";

function HomePage() {
  return (
    <div className="HomePage">
      <h1>Welcome</h1>
      <p>Use the navigation above to view all employees.</p>
    </div>
  );
}

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <header className="App-header">
          <nav className="App-nav">
            <Link to="/">Home</Link>
            <Link to="/employees">Employees</Link>
          </nav>
        </header>

        <main className="App-main">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/employees" element={<EmployeesPage />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
