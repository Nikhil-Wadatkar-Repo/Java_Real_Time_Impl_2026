import { useContext } from "react";
import CounterContext from "./CounterContext";

export function CounterActions() {
  const { increment, reset } = useContext(CounterContext);

  return (
    <div>
      <h2>Counter actions</h2>
      <button onClick={increment}>Increment</button>
      <button onClick={reset}>Reset</button>
    </div>
  );
}

export default CounterActions;
