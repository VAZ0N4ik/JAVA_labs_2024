import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { Alert, AlertTitle, AlertDescription } from "../components/ui/alert";

const FunctionTable = ({ functionId, points = [], isEditable = true, onYChange }) => {
    const [tableData, setTableData] = useState([]);
    const [error, setError] = useState(null);
    const [modifiedData, setModifiedData] = useState({});

    useEffect(() => {
        if (!functionId || !points.length) return;

        const loadPointValues = async () => {
            const newPoints = [];
            for (let i = 0; i < points.length; i++) {
                try {
                    const [xResponse, yResponse] = await Promise.all([
                        api.post('/api/tabulated-function-operations/getX', null, {
                            params: { functionId, index: i }
                        }),
                        api.post('/api/tabulated-function-operations/getY', null, {
                            params: { functionId, index: i }
                        })
                    ]);
                    newPoints.push({ x: xResponse.data, y: yResponse.data });
                } catch (error) {
                    setError('Ошибка при загрузке точек');
                }
            }
            setTableData(newPoints);
        };

        loadPointValues();
    }, [functionId, points]);

    const handleYChange = (index, newValue) => {
        if (!isEditable) return;

        setModifiedData(prev => ({
            ...prev,
            [index]: newValue
        }));
    };

    const saveChanges = async () => {
        try {
            const updatePromises = Object.entries(modifiedData).map(([index, y]) =>
                api.post('/api/tabulated-function-operations/setY', null, {
                    params: {
                        functionId,
                        index: parseInt(index),
                        y
                    }
                })
            );

            const results = await Promise.all(updatePromises);
            const finalResult = results[results.length - 1];

            if (finalResult?.data) {
                onYChange && onYChange(finalResult.data);
                setModifiedData({}); // Очищаем изменения
            }
        } catch (error) {
            setError('Ошибка при сохранении изменений');
        }
    };

    if (!points.length) {
        return <div className="text-center p-4">Функция не создана</div>;
    }

    return (
        <div className="mt-4">
            <div className="overflow-x-auto shadow ring-1 ring-black ring-opacity-5 rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">X</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Y</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {tableData.map((point, index) => (
                        <tr key={index}>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                {point.x}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                {isEditable ? (
                                    <input
                                        type="number"
                                        value={modifiedData[index] ?? point.y}
                                        onChange={(e) => handleYChange(index, parseFloat(e.target.value))}
                                        className="w-full px-2 py-1 border rounded focus:ring-2 focus:ring-blue-500 focus:outline-none"
                                        step="any"
                                    />
                                ) : (
                                    point.y
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            {isEditable && Object.keys(modifiedData).length > 0 && (
                <button
                    onClick={saveChanges}
                    className="mt-4 btn btn-primary w-full"
                >
                    Сохранить изменения
                </button>
            )}
            {error && (
                <Alert className="mt-2 bg-red-50 border-red-200">
                    <AlertDescription className="text-red-600">{error}</AlertDescription>
                </Alert>
            )}
        </div>
    );
};

const ArrayFunctionCreator = ({ onSubmit, onError }) => {
    const [pointCount, setPointCount] = useState('');
    const [points, setPoints] = useState([]);
    const [createdFunction, setCreatedFunction] = useState(null);

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

            // Изменяем формат отправки данных
            const params = new URLSearchParams();
            validPoints.forEach((point) => {
                params.append('x', point.x);
                params.append('y', point.y);
            });

            const response = await api.post(`/api/function-creation/create-from-points?${params.toString()}`);

            setCreatedFunction(response.data);
            onSubmit(response.data);
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
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200">
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

            {createdFunction && (
                <div className="mt-4">
                    <h3 className="text-lg font-medium mb-2">Созданная функция</h3>
                    <FunctionTable
                        functionId={createdFunction.id}
                        points={createdFunction.points}
                        isEditable={true}
                        onYChange={(updatedFunction) => {
                            setCreatedFunction(updatedFunction);
                            onSubmit(updatedFunction);
                        }}
                    />
                </div>
            )}
        </div>
    );
};

const MathFunctionCreator = ({ onSubmit, onError }) => {
    const [selectedFunction, setSelectedFunction] = useState('');
    const [xFrom, setXFrom] = useState('');
    const [xTo, setXTo] = useState('');
    const [pointCount, setPointCount] = useState('');
    const [createdFunction, setCreatedFunction] = useState(null);
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

            setCreatedFunction(response.data);
            onSubmit(response.data);
        } catch (error) {
            onError(error.message);
        }
    };

    return (
        <div className="space-y-4">
            <select
                value={selectedFunction}
                onChange={(e) => setSelectedFunction(e.target.value)}
                className="select w-full"
            >
                <option value="">Выберите функцию</option>
                {functions.map(name => (
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

            {createdFunction && (
                <div className="mt-4">
                    <h3 className="text-lg font-medium mb-2">Созданная функция</h3>
                    <FunctionTable
                        functionId={createdFunction.id}
                        points={createdFunction.points}
                        isEditable={true}
                        onYChange={(updatedFunction) => {
                            setCreatedFunction(updatedFunction);
                            onSubmit(updatedFunction);
                        }}
                    />
                </div>
            )}
        </div>
    );
};

const TabulatedFunctionCreator = ({ isOpen, onClose, onSuccess, creatorType = 'array' }) => {
    const [error, setError] = useState(null);

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-6 w-full max-w-2xl mx-4 max-h-[90vh] overflow-y-auto">
                <h2 className="text-2xl font-bold mb-6">
                    {creatorType === 'array' ? 'Создание функции из точек' : 'Создание функции из математической функции'}
                </h2>

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
                    <Alert className="mt-4 bg-red-50 border-red-200">
                        <AlertTitle className="text-red-700">Ошибка</AlertTitle>
                        <AlertDescription className="text-red-600">
                            {error}
                        </AlertDescription>
                    </Alert>
                )}

                <div className="mt-6 flex justify-end">
                    <button
                        onClick={onClose}
                        className="btn btn-secondary"
                    >
                        Закрыть
                    </button>
                </div>
            </div>
        </div>
    );
};

export default TabulatedFunctionCreator;