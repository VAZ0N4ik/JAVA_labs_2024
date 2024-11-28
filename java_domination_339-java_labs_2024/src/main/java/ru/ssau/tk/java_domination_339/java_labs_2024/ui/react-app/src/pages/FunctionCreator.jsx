import React, { useState, useEffect } from 'react';
import { AlertCircle } from 'lucide-react';
import { Alert, AlertTitle, AlertDescription } from "../components/ui/alert";
import { createArrayFunction, createMathFunction } from '../services/functionService';
import api from "../services/api";

const ErrorModal = ({ message, onClose }) => (
    <div className="error-modal">
        <div className="error-modal-content">
            <Alert className="bg-red-50 border-red-200">
                <AlertTitle className="text-red-700 flex items-center gap-2">
                    <AlertCircle className="h-4 w-4" />
                    Ошибка
                </AlertTitle>
                <AlertDescription className="text-red-600 mt-2">{message}</AlertDescription>
            </Alert>
            <button
                onClick={onClose}
                className="btn btn-primary mt-4 w-full"
            >
                Закрыть
            </button>
        </div>
    </div>
);

const ArrayFunctionCreator = ({ onSubmit, onError }) => {
    const [pointCount, setPointCount] = useState('');
    const [points, setPoints] = useState([]);

    const handlePointCountSubmit = () => {
        try {
            const count = parseInt(pointCount);
            if (isNaN(count)) {
                onError('Количество точек должно быть числом');
                return;
            }
            if (count < 2) {
                onError('Минимальное количество точек: 2');
                return;
            }
            setPoints(Array(count).fill().map(() => ({ x: '', y: '' })));
        } catch (error) {
            onError(error.message);
        }
    };

    const handlePointChange = (index, field, value) => {
        const newPoints = [...points];
        newPoints[index] = { ...newPoints[index], [field]: value };
        setPoints(newPoints);
    };

    const handleSubmit = async () => {
        try {
            if (points.some(p => p.x === '' || p.y === '')) {
                onError('Все поля должны быть заполнены');
                return;
            }

            const validPoints = points.map(p => ({
                x: parseFloat(p.x),
                y: parseFloat(p.y)
            }));

            if (validPoints.some(p => isNaN(p.x) || isNaN(p.y))) {
                onError('Все значения должны быть числами');
                return;
            }

            for (let i = 1; i < validPoints.length; i++) {
                if (validPoints[i].x <= validPoints[i - 1].x) {
                    onError('Значения X должны быть строго возрастающими');
                    return;
                }
            }

            await onSubmit(validPoints);
            setPoints([]);
            setPointCount('');
        } catch (error) {
            onError(error.message);
        }
    };

    return (
        <div className="space-y-4">
            {!points.length ? (
                <div className="space-y-4">
                    <input
                        type="number"
                        value={pointCount}
                        onChange={(e) => setPointCount(e.target.value)}
                        placeholder="Количество точек"
                        className="input"
                        min="2"
                    />
                    <button onClick={handlePointCountSubmit} className="btn btn-primary w-full">
                        Создать таблицу
                    </button>
                </div>
            ) : (
                <div className="space-y-4">
                    <div className="table-container">
                        <table className="table">
                            <thead>
                            <tr>
                                <th>X</th>
                                <th>Y</th>
                            </tr>
                            </thead>
                            <tbody>
                            {points.map((point, index) => (
                                <tr key={index}>
                                    <td>
                                        <input
                                            type="number"
                                            value={point.x}
                                            onChange={(e) => handlePointChange(index, 'x', e.target.value)}
                                            className="input"
                                            step="any"
                                        />
                                    </td>
                                    <td>
                                        <input
                                            type="number"
                                            value={point.y}
                                            onChange={(e) => handlePointChange(index, 'y', e.target.value)}
                                            className="input"
                                            step="any"
                                        />
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                    <button onClick={handleSubmit} className="btn btn-success w-full">
                        Создать функцию
                    </button>
                </div>
            )}
        </div>
    );
};

const MathFunctionCreator = ({ onSubmit, onError }) => {
    // Изменяем начальное состояние на пустой массив
    const [functions, setFunctions] = useState([]);
    const [selectedFunction, setSelectedFunction] = useState('');
    const [xFrom, setXFrom] = useState('');
    const [xTo, setXTo] = useState('');
    const [pointCount, setPointCount] = useState('');

    // Загрузка списка функций при монтировании компонента
    useEffect(() => {
        const loadFunctions = async () => {
            try {
                const response = await api.get('/api/function-creation/math-functions');
                // response.data уже является массивом строк
                setFunctions(response.data);
            } catch (error) {
                onError('Ошибка при загрузке списка функций');
            }
        };
        loadFunctions();
    }, [onError]);

    const handleSubmit = async () => {
        try {
            if (!selectedFunction) {
                onError('Выберите функцию');
                return;
            }

            const from = parseFloat(xFrom);
            const to = parseFloat(xTo);
            const count = parseInt(pointCount);

            console.log('Submitting values:', {
                selectedFunction,
                from,
                to,
                count
            });

            if (isNaN(from) || isNaN(to) || isNaN(count)) {
                onError('Все поля должны быть заполнены числами');
                return;
            }

            if (count < 2) {
                onError('Минимальное количество точек: 2');
                return;
            }

            if (from >= to) {
                onError('Значение "От" должно быть меньше значения "До"');
                return;
            }

            await onSubmit({
                name: selectedFunction,
                from,
                to,
                count
            });

            setSelectedFunction('');
            setXFrom('');
            setXTo('');
            setPointCount('');
        } catch (error) {
            console.error('Submit error:', error);
            onError(error.message);
        }
    };

    return (
        <div className="space-y-4">
            <select
                value={selectedFunction}
                onChange={(e) => setSelectedFunction(e.target.value)}
                className="select"
            >
                <option value="">Выберите функцию</option>
                {Array.isArray(functions) && functions.map(name => (
                    <option key={name} value={name}>{name}</option>
                ))}
            </select>

            <div className="grid grid-cols-2 gap-4">
                <input
                    type="number"
                    value={xFrom}
                    onChange={(e) => setXFrom(e.target.value)}
                    placeholder="От"
                    className="input"
                    step="any"
                />
                <input
                    type="number"
                    value={xTo}
                    onChange={(e) => setXTo(e.target.value)}
                    placeholder="До"
                    className="input"
                    step="any"
                />
            </div>

            <input
                type="number"
                value={pointCount}
                onChange={(e) => setPointCount(e.target.value)}
                placeholder="Количество точек"
                className="input"
                min="2"
            />

            <button onClick={handleSubmit} className="btn btn-success w-full">
                Создать функцию
            </button>
        </div>
    );
};

const TabulatedFunctionCreator = () => {
    const [activeTab, setActiveTab] = useState('array');
    const [error, setError] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [successMessage, setSuccessMessage] = useState('');

    useEffect(() => {
        const token = localStorage.getItem('token');
        setIsLoggedIn(!!token);
    }, []);

    const handleArraySubmit = async (points) => {
        try {
            setIsLoading(true);
            console.log('Submitting points:', points);
            await createArrayFunction(points);
            setSuccessMessage('Функция успешно создана!');
            setTimeout(() => setSuccessMessage(''), 3000);
        } catch (error) {
            console.error('Error:', error);
            setError(error.message);
        } finally {
            setIsLoading(false);
        }
    };

    const handleMathFunctionSubmit = async (data) => {
        try {
            setIsLoading(true);
            console.log('Submitting math function data:', data);

            // Проверяем все данные перед отправкой
            if (!data.name || !data.from || !data.to || !data.count) {
                throw new Error('Все поля должны быть заполнены');
            }

            // Добавляем явное преобразование типов
            const requestData = {
                name: data.name,
                from: Number(data.from),
                to: Number(data.to),
                count: Number(data.count)
            };

            console.log('Formatted request data:', requestData);
            const result = await createMathFunction(
                requestData.name,
                requestData.from,
                requestData.to,
                requestData.count
            );
            console.log('Response:', result);
            setSuccessMessage('Функция успешно создана!');
            setTimeout(() => setSuccessMessage(''), 3000);
        } catch (error) {
            console.error('Full error object:', error);
            // Проверяем, является ли это ошибкой авторизации
            if (error.response?.status === 403 || error.response?.status === 401) {
                localStorage.removeItem('token');
                window.location.reload();
            } else {
                // Для других ошибок просто показываем сообщение
                setError(error.message || 'Произошла ошибка при создании функции');
            }
        } finally {
            setIsLoading(false);
        }
    };

    if (!isLoggedIn) {
        return (
            <div className="max-w-2xl mx-auto mt-8">
                <Alert className="bg-yellow-50 border-yellow-200">
                    <AlertTitle className="text-yellow-700">Требуется авторизация</AlertTitle>
                    <AlertDescription className="text-yellow-600">
                        Пожалуйста, войдите в систему для создания функций.
                    </AlertDescription>
                </Alert>
            </div>
        );
    }

    return (
        <div className="max-w-2xl mx-auto mt-8">
            <div className="flex mb-4">
                <button
                    onClick={() => setActiveTab('array')}
                    className={`tab flex-1 ${activeTab === 'array' ? 'tab-active' : 'tab-inactive'}`}
                >
                    Создать из массивов
                </button>
                <button
                    onClick={() => setActiveTab('function')}
                    className={`tab flex-1 ${activeTab === 'function' ? 'tab-active' : 'tab-inactive'}`}
                >
                    Создать из функции
                </button>
            </div>

            <div className="card">
                {activeTab === 'array' ? (
                    <ArrayFunctionCreator
                        onSubmit={handleArraySubmit}
                        onError={setError}
                    />
                ) : (
                    <MathFunctionCreator
                        onSubmit={handleMathFunctionSubmit}
                        onError={setError}
                    />
                )}
            </div>

            {isLoading && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white p-4 rounded-lg">
                        <p className="text-lg">Создание функции...</p>
                    </div>
                </div>
            )}

            {successMessage && (
                <div className="fixed bottom-4 right-4 bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg">
                    {successMessage}
                </div>
            )}

            {error && <ErrorModal message={error} onClose={() => setError(null)} />}
        </div>
    );
};

export default TabulatedFunctionCreator;