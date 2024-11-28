import React, { useState } from 'react';
import api from '../services/api';
import { Alert, AlertTitle, AlertDescription } from "./ui/alert";

const Register = ({ onSuccess }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            setIsLoading(true);
            setError(null);

            const response = await api.post('/auth/sign-up', {
                username,
                password
            });

            if (response.data.token) {
                onSuccess(); // Возвращаемся к форме входа
                setUsername('');
                setPassword('');
            }
        } catch (error) {
            console.error('Registration error:', error);
            setError(error.response?.data?.message || 'Ошибка при регистрации');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="card">
            <h2 className="text-2xl font-bold mb-6 text-center">Регистрация</h2>
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Имя пользователя
                    </label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className="input"
                        required
                        minLength={5}
                        maxLength={50}
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                        Пароль
                    </label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="input"
                        required
                        minLength={8}
                    />
                </div>
                {error && (
                    <Alert className="bg-red-50 border-red-200">
                        <AlertTitle className="text-red-700">Ошибка</AlertTitle>
                        <AlertDescription className="text-red-600">
                            {error}
                        </AlertDescription>
                    </Alert>
                )}
                <button
                    type="submit"
                    className="btn btn-primary w-full"
                    disabled={isLoading}
                >
                    {isLoading ? 'Регистрация...' : 'Зарегистрироваться'}
                </button>
            </form>
        </div>
    );
};

export default Register;