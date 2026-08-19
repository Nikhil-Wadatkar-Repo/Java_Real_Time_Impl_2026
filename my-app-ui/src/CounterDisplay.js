import { useContext } from "react";
import CounterContext from "./CounterContext";

export function CounterDisplay() {
  const { count } = useContext(CounterContext);

  return (
    <div>
      <h2>Counter display</h2>
      <p>Current count: {count}</p>
    </div>
  );
}
