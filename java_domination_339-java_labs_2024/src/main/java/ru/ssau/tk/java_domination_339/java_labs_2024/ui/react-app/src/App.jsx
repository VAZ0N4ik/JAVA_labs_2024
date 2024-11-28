import React, { useState, useEffect } from 'react';
import TabulatedFunctionCreator from './pages/FunctionCreator';
import Login from './components/Login';
import Register from './components/Register'; // Создадим новый компонент
function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [showRegister, setShowRegister] = useState(false);

    // При запуске приложения всегда очищаем токен
    useEffect(() => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
    };

    return (
        <div className="App min-h-screen bg-gray-50">
            {!isLoggedIn ? (
                <div className="max-w-md mx-auto mt-8">
                    {!showRegister ? (
                        <>
                            <Login onSuccess={() => setIsLoggedIn(true)} />
                            <button
                                onClick={() => setShowRegister(true)}
                                className="mt-4 text-blue-500 hover:text-blue-700"
                            >
                                Зарегистрироваться
                            </button>
                        </>
                    ) : (
                        <>
                            <Register onSuccess={() => setShowRegister(false)} />
                            <button
                                onClick={() => setShowRegister(false)}
                                className="mt-4 text-blue-500 hover:text-blue-700"
                            >
                                Вернуться к входу
                            </button>
                        </>
                    )}
                </div>
            ) : (
                <div>
                    <div className="bg-white shadow">
                        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                            <div className="flex justify-between h-16 items-center">
                                <h1 className="text-xl font-bold">Табулированные функции</h1>
                                <button
                                    onClick={handleLogout}
                                    className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded"
                                >
                                    Выйти
                                </button>
                            </div>
                        </div>
                    </div>
                    <TabulatedFunctionCreator />
                </div>
            )}
        </div>
    );
}

export default App;