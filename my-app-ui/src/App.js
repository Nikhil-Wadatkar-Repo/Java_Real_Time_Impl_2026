import { lazy, Suspense, React } from "react";
import { Routes, Route, Link, createContext } from "react-router-dom";

import "./App.css";
import EmployeeAgGrid from "./pages/EmployeeAgGrid";
import AgGridPage1 from "./pages/AgGridPage1";
import Form from "./pages/FormPage";
import FormPage from "./pages/FormPage";

export const CounterContext = createContext({
  employeeContext: {},
  setEmployeeContext: () => {},
});

function App() {
  const Page1 = lazy(() => import("./pages/Page1"));
  const Page2 = lazy(() => import("./pages/Page2"));

  return (
    <Suspense fallback={<p>Loading...</p>}>
      <Routes>
        <Route path="/page1" element={<Page1 />} />
        <Route path="/page2" element={<Page2 />} />
      </Routes>
    </Suspense>
  );
}

export default App;
