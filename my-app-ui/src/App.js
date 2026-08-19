import { useState } from "react";
import "./App.css";
import { CounterDisplay } from "./CounterDisplay";
import CounterActions from "./CounterActions";
import { createContext } from "react";
import CounterContext from "./CounterContext";
function App() {
  const [count, setCount] = useState(0);

  const handleIncrement = () => {
    setCount((prevCount) => prevCount + 1);
  };

  const handleReset = () => {
    setCount(0);
  };

  
  return (
    <CounterContext.Provider
      value={{
        count,
        increment: handleIncrement,
        reset: handleReset,
      }}
    >
      <div className="App">
        <h1>Simple state example</h1>
        <CounterDisplay />
        <CounterActions />
      </div>
    </CounterContext.Provider>
  );
}

export default App;
