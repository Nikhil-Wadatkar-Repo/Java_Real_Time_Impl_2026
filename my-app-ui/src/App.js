import React from "react";
import {
  BrowserRouter,
  Routes,
  Route,
  Link,
  createContext,
} from "react-router-dom";

import "./App.css";
import EmployeeAgGrid from "./pages/EmployeeAgGrid";
import AgGridPage1 from "./pages/AgGridPage1";
import Form from "./pages/FormPage";
import FormPage from "./pages/FormPage";
 export const CounterContext = createContext({
   employeeContext: {},
   setEmployeeContext: () => {},
 });
function HomePage() {
  return (
    <div className="container mt-5">
      <h1>Welcome</h1>
      <p>Use the navigation above to view all employees.</p>
    </div>
  );
}


function App() {
 

  return (
    <BrowserRouter>
      <CounterContext.Provider value={{ employeeContext, setEmployeeContext }}>
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
          <div className="container-fluid">
            <Link className="navbar-brand" to="/form">
              Create Resources
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarNav"
              aria-controls="navbarNav"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon"></span>
            </button>
          </div>
          <div className="container-fluid">
            <Link className="navbar-brand" to="/employees">
              Performance Optimization
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarNav"
              aria-controls="navbarNav"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon"></span>
            </button>
          </div>
          <div className="container-fluid">
            <Link className="navbar-brand" to="/employees">
              Batching
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarNav"
              aria-controls="navbarNav"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon"></span>
            </button>
          </div>
          <div className="container-fluid">
            <Link className="navbar-brand" to="/employees">
              N + 1 Problem
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarNav"
              aria-controls="navbarNav"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon"></span>
            </button>
          </div>
          <div className="container-fluid">
            <Link className="navbar-brand" to="/">
              HOME
            </Link>
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarNav"
              aria-controls="navbarNav"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon"></span>
            </button>
          </div>
        </nav>

        <main>
          <Routes>
            <Route path="/" element={<HomePage />} />

            <Route path="/form" element={<FormPage />} />
            <Route path="/employees" element={<EmployeeAgGrid />} />
          </Routes>
        </main>

        {/* <div className="container-fluid">
        <div className="row">
          <div className="col-2" style={{ backgroundColor: "pink" }}>
            col-4
          </div>
          <div className="col-10" style={{ backgroundColor: "yellow" }}>
            col-8
          </div>
        </div>
      </div> */}
      </CounterContext.Provider>
    </BrowserRouter>
  );
}

export default App;
export { CounterContext, CounterProvider };
