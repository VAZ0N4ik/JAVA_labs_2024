import React, {useState, useEffect} from 'react';
import {ThemeProvider} from './components/ThemeProvider';
import MainPage from './pages/MainPage';
import Login from './components/Login';
import Register from './components/Register';
import {LogOut} from 'lucide-react';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [showRegister, setShowRegister] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('token');
        setIsLoggedIn(!!token);
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
    };

    return (
        <ThemeProvider>
            <div className="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-200">
                {!isLoggedIn ? (
                    <div className="max-w-md mx-auto pt-8 px-4">
                        {!showRegister ? (
                            <>
                                <Login onSuccess={() => setIsLoggedIn(true)}/>
                                <button
                                    onClick={() => setShowRegister(true)}
                                    className="mt-4 text-blue-600 hover:text-blue-700 dark:text-blue-400
                                             dark:hover:text-blue-300 text-sm w-full text-center"
                                >
                                    Зарегистрироваться
                                </button>
                            </>
                        ) : (
                            <>
                                <Register onSuccess={() => setShowRegister(false)}/>
                                <button
                                    onClick={() => setShowRegister(false)}
                                    className="mt-4 text-blue-600 hover:text-blue-700 dark:text-blue-400
                                             dark:hover:text-blue-300 text-sm w-full text-center"
                                >
                                    Вернуться к входу
                                </button>
                            </>
                        )}
                    </div>
                ) : (
                    <div className="min-h-screen">
                        <div className="bg-white dark:bg-gray-800 shadow transition-colors duration-200">
                            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                                <div className="flex justify-between h-16 items-center">
                                    <h1 className="text-xl font-bold text-gray-900 dark:text-gray-100">
                                        Табулированные функции
                                    </h1>
                                    <button
                                        onClick={handleLogout}
                                        className="inline-flex items-center px-4 py-2 rounded-md
                                                 text-white bg-red-600 hover:bg-red-700
                                                 dark:bg-red-500 dark:hover:bg-red-600
                                                 transition-colors duration-200
                                                 focus:outline-none focus:ring-2 focus:ring-offset-2
                                                 focus:ring-red-500 dark:focus:ring-red-400"
                                    >
                                        <LogOut className="w-4 h-4 mr-2"/>
                                        Выйти
                                    </button>
                                </div>
                            </div>
                        </div>
                        <MainPage/>
                    </div>
                )}
            </div>
        </ThemeProvider>
    );
}

export default App;