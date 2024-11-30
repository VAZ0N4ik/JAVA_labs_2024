import React, {createContext, useContext, useEffect, useState} from 'react';
import {Sun, Moon} from 'lucide-react';

const ThemeContext = createContext({
    theme: 'light',
    toggleTheme: () => {
    }
});

export const useTheme = () => useContext(ThemeContext);

export const ThemeProvider = ({children}) => {
    const [theme, setTheme] = useState('light');

    useEffect(() => {
        // Проверяем сохраненную тему или системные настройки
        const savedTheme = localStorage.getItem('theme');
        const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;

        if (savedTheme) {
            setTheme(savedTheme);
            document.documentElement.classList.toggle('dark', savedTheme === 'dark');
        } else if (prefersDark) {
            setTheme('dark');
            document.documentElement.classList.add('dark');
        }
    }, []);

    const toggleTheme = () => {
        const newTheme = theme === 'light' ? 'dark' : 'light';
        setTheme(newTheme);
        localStorage.setItem('theme', newTheme);
        document.documentElement.classList.toggle('dark');
    };

    return (
        <ThemeContext.Provider value={{theme, toggleTheme}}>
            {children}
            <button
                onClick={toggleTheme}
                className="fixed bottom-4 right-4 p-3 rounded-full bg-gray-100 dark:bg-gray-800
                          shadow-lg hover:shadow-xl transition-shadow"
                aria-label="Toggle theme"
            >
                {theme === 'light' ? (
                    <Moon className="w-5 h-5 text-gray-800 dark:text-gray-200"/>
                ) : (
                    <Sun className="w-5 h-5 text-gray-800 dark:text-gray-200"/>
                )}
            </button>
        </ThemeContext.Provider>
    );
};