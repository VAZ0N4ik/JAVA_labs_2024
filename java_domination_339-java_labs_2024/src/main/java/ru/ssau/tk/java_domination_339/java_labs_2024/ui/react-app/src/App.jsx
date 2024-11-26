import React, { useState } from 'react';
import TabulatedFunctionCreator from './pages/FunctionCreator';
import Login from './components/Login';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));

  return (
      <div className="App min-h-screen bg-gray-50">
        {!isLoggedIn ? (
            <Login onSuccess={() => setIsLoggedIn(true)} />
        ) : (
            <TabulatedFunctionCreator />
        )}
      </div>
  );
}

export default App;