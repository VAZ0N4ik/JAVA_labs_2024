import React, {useState, useEffect} from 'react';
import {AlertCircle, X} from 'lucide-react';
import api from '../services/api';
import {Alert, AlertTitle, AlertDescription} from "../components/ui/alert";

const ArrayFunctionCreator = ({onSubmit, onError}) => {
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
            if (count > 100) {
                onError('Максимальное количество точек: 100');
                return;
            }
            setPoints(Array(count).fill().map(() => ({ x: '', y: '' })));
        } catch (error) {
            onError(error.message);
        }
    };

    const handlePointChange = (index, field, value) => {
        const newPoints = [...points];
        newPoints[index] = {...newPoints[index], [field]: value};
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

            const params = new URLSearchParams();
            validPoints.forEach((point) => {
                params.append('x', point.x);
                params.append('y', point.y);
            });

            const response = await api.post(`/api/function-creation/create-from-points?${params.toString()}`);
            //setCreatedFunction(response.data);
            onSubmit(response.data);
        } catch (error) {
            onError(error.message);
        }
    };

    return (
        <div className="space-y-4">
            {!points.length ? (
                <div className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                            Количество точек
                        </label>
                        <input
                            type="number"
                            value={pointCount}
                            onChange={(e) => setPointCount(e.target.value)}
                            className="input"
                            min="2"
                            max="100"
                            placeholder="Введите количество точек"
                        />
                    </div>
                    <button
                        onClick={handlePointCountSubmit}
                        className="btn btn-primary w-full"
                    >
                        Создать таблицу
                    </button>
                </div>
            ) : (
                <div className="space-y-4">
                    <div className="overflow-x-auto rounded-lg border border-gray-200 dark:border-gray-700">
                        <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
                            <thead className="bg-gray-50 dark:bg-gray-800">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase">
                                    X
                                </th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase">
                                    Y
                                </th>
                            </tr>
                            </thead>
                            <tbody className="bg-white divide-y divide-gray-200 dark:bg-gray-900 dark:divide-gray-700">
                            {points.map((point, index) => (
                                <tr key={index} className="hover:bg-gray-50 dark:hover:bg-gray-800">
                                    <td className="px-6 py-4">
                                        <input
                                            type="number"
                                            value={point.x}
                                            onChange={(e) => handlePointChange(index, 'x', e.target.value)}
                                            className="input"
                                            step="any"
                                            placeholder="X"
                                        />
                                    </td>
                                    <td className="px-6 py-4">
                                        <input
                                            type="number"
                                            value={point.y}
                                            onChange={(e) => handlePointChange(index, 'y', e.target.value)}
                                            className="input"
                                            step="any"
                                            placeholder="Y"
                                        />
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                    <button
                        onClick={handleSubmit}
                        className="btn btn-primary w-full"
                    >
                        Создать функцию
                    </button>
                </div>
            )}
        </div>
    );
};

const MathFunctionCreator = ({onSubmit, onError}) => {
    const [selectedFunction, setSelectedFunction] = useState('');
    const [xFrom, setXFrom] = useState('');
    const [xTo, setXTo] = useState('');
    const [pointCount, setPointCount] = useState('');
    const [functions, setFunctions] = useState([]);

    useEffect(() => {
        const loadFunctions = async () => {
            try {
                const response = await api.get('/api/function-creation/functions-to-create');
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

            if (isNaN(from) || isNaN(to) || isNaN(count)) {
                onError('Все поля должны быть заполнены числами');
                return;
            }

            if (count < 2) {
                onError('Минимальное количество точек: 2');
                return;
            }

            if (count > 100) {
                onError('Максимальное количество точек: 100');
                return;
            }

            if (from >= to) {
                onError('Значение "От" должно быть меньше значения "До"');
                return;
            }

            const response = await api.post('/api/function-creation/create-from-math-function', null, {
                params: {
                    name: selectedFunction,
                    from,
                    to,
                    count
                }
            });

            onSubmit(response.data);
        } catch (error) {
            onError(error.response?.data?.message || 'Ошибка при создании функции');
        }
    };

    return (
        <div className="space-y-4">
            <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                    Функция
                </label>
                <select
                    value={selectedFunction}
                    onChange={(e) => setSelectedFunction(e.target.value)}
                    className="input"
                >
                    <option value="">Выберите функцию</option>
                    {functions.map(name => (
                        <option key={name} value={name}>{name}</option>
                    ))}
                </select>
            </div>

            <div className="grid grid-cols-2 gap-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                        От
                    </label>
                    <input
                        type="number"
                        value={xFrom}
                        onChange={(e) => setXFrom(e.target.value)}
                        className="input"
                        step="any"
                        placeholder="Начало интервала"
                    />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                        До
                    </label>
                    <input
                        type="number"
                        value={xTo}
                        onChange={(e) => setXTo(e.target.value)}
                        className="input"
                        step="any"
                        placeholder="Конец интервала"
                    />
                </div>
            </div>

            <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                    Количество точек
                </label>
                <input
                    type="number"
                    value={pointCount}
                    onChange={(e) => setPointCount(e.target.value)}
                    className="input"
                    min="2"
                    max="100"
                    placeholder="Количество точек разбиения"
                />
            </div>

            <button
                onClick={handleSubmit}
                className="btn btn-primary w-full"
            >
                Создать функцию
            </button>
        </div>
    );
};

const TabulatedFunctionCreator = ({isOpen, onClose, onSuccess, creatorType = 'array'}) => {
    const [error, setError] = useState(null);

    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal-content">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                        {creatorType === 'array'
                            ? 'Создание функции из точек'
                            : 'Создание функции из математической функции'
                        }
                    </h2>
                    <button
                        onClick={onClose}
                        className="text-gray-500 hover:text-gray-700 dark:text-gray-400
                                 dark:hover:text-gray-200 transition-colors"
                    >
                        <X className="w-6 h-6"/>
                    </button>
                </div>

                {creatorType === 'array' ? (
                    <ArrayFunctionCreator
                        onSubmit={onSuccess}
                        onError={setError}
                    />
                ) : (
                    <MathFunctionCreator
                        onSubmit={onSuccess}
                        onError={setError}
                    />
                )}

                {error && (
                    <Alert className="mt-4 bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                        <AlertTitle className="text-red-700 dark:text-red-400">
                            Ошибка
                        </AlertTitle>
                        <AlertDescription className="text-red-600 dark:text-red-300 flex items-center gap-2">
                            <AlertCircle className="w-4 h-4"/>
                            {error}
                        </AlertDescription>
                    </Alert>
                )}
            </div>
        </div>
    );
};

export default TabulatedFunctionCreator;