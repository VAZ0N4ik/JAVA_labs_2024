import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { Alert, AlertTitle, AlertDescription } from "../components/ui/alert";
import TabulatedFunctionCreator from './FunctionCreator';
import { saveAs } from 'file-saver';

const FunctionTable = ({ functionId, points = [], isEditable = true, onYChange }) => {
    const [tableData, setTableData] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        console.log('Effect triggered with:', { functionId, pointsLength: points.length });

        const loadPoints = async () => {
            if (!functionId || !points.length) {
                setLoading(false);
                return;
            }

            try {
                const newPoints = [];
                for (let i = 0; i < points.length; i++) {
                    const [xResponse, yResponse] = await Promise.all([
                        api.post('/api/tabulated-function-operations/getX', null, {
                            params: { functionId, index: i }
                        }),
                        api.post('/api/tabulated-function-operations/getY', null, {
                            params: { functionId, index: i }
                        })
                    ]);

                    newPoints.push({
                        index: i,
                        x: Number(xResponse.data),
                        y: Number(yResponse.data)
                    });
                }
                setTableData(newPoints);
                setError(null);
            } catch (error) {
                console.error('Error loading points:', error);
                setError('Ошибка при загрузке точек');
            } finally {
                setLoading(false);
            }
        };

        loadPoints();
    }, [functionId, points]);

    if (loading) {
        return <div className="text-center p-4">Загрузка...</div>;
    }

    const handleYChange = async (index, newValue) => {
        if (!isEditable) return;

        try {
            const response = await api.post('/api/tabulated-function-operations/setY', null, {
                params: {
                    functionId,
                    index,
                    y: newValue
                }
            });

            if (response.data) {
                onYChange && onYChange(response.data);

                const newTableData = [...tableData];
                const pointIndex = newTableData.findIndex(p => p.index === index);
                if (pointIndex !== -1) {
                    newTableData[pointIndex] = {
                        ...newTableData[pointIndex],
                        y: newValue
                    };
                    setTableData(newTableData);
                }
                setError(null);
            }
        } catch (error) {
            console.error('Error updating Y value:', error);
            setError('Ошибка при обновлении значения Y');
        }
    };

    if (loading) {
        return <div className="text-center p-4">Загрузка...</div>;
    }

    if (!tableData.length) {
        return <div className="text-center p-4">Нет данных</div>;
    }

    return (
        <div className="mt-4">
            <div className="overflow-x-auto shadow ring-1 ring-black ring-opacity-5 rounded-lg">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">X</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Y</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {tableData.map((point) => (
                        <tr key={point.index}>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                {point.x}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                {isEditable ? (
                                    <input
                                        type="number"
                                        value={point.y}
                                        onChange={(e) => handleYChange(point.index, parseFloat(e.target.value))}
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
            {error && (
                <Alert className="mt-2 bg-red-50 border-red-200">
                    <AlertDescription className="text-red-600">{error}</AlertDescription>
                </Alert>
            )}
        </div>
    );
};

const CreateFunctionDialog = ({ isOpen, onClose, onTypeSelect }) => {  // переименовали onSelectType в onTypeSelect
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg shadow-xl">
                <h3 className="text-lg font-medium mb-4">Выберите тип создания функции</h3>
                <div className="space-y-3">
                    <button
                        className="w-full btn btn-primary"
                        onClick={() => onTypeSelect('array')}  // изменено с onSelectType на onTypeSelect
                    >
                        Создать из массива
                    </button>
                    <button
                        className="w-full btn btn-primary"
                        onClick={() => onTypeSelect('function')}  // изменено с onSelectType на onTypeSelect
                    >
                        Создать из функции
                    </button>
                    <button
                        className="w-full btn btn-secondary"
                        onClick={onClose}
                    >
                        Отмена
                    </button>
                </div>
            </div>
        </div>
    );
};

const FunctionOperations = ({ isOpen, onClose }) => {
    const [function1, setFunction1] = useState(null);
    const [function2, setFunction2] = useState(null);
    const [result, setResult] = useState(null);
    const [showCreateDialog1, setShowCreateDialog1] = useState(false);
    const [showCreateDialog2, setShowCreateDialog2] = useState(false);
    const [showCreator1, setShowCreator1] = useState(false);
    const [showCreator2, setShowCreator2] = useState(false);
    const [creatorType, setCreatorType] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSelectCreateType = (forFunction, type) => {
        if (forFunction === 1) {
            setShowCreateDialog1(false);
            setCreatorType(type);
            setShowCreator1(true);
        } else {
            setShowCreateDialog2(false);
            setCreatorType(type);
            setShowCreator2(true);
        }
    };

    const handleOperation = async (operation) => {
        if (!function1 || !function2) {  // Изменили проверку
            setError('Необходимо выбрать обе функции');
            return;
        }

        setLoading(true);
        try {
            const response = await api.post(`/api/tabulated-function-operations/${operation}`, null, {
                params: {
                    functionId1: function1.hash_function,  // Используем hash_function вместо id
                    functionId2: function2.hash_function
                }
            });

            if (response.data) {
                setResult(response.data);
                setError(null);
            } else {
                throw new Error('Ответ от сервера не содержит данных');
            }
        } catch (error) {
            console.error('Operation error:', error);
            setError(error.response?.data?.message || 'Ошибка при выполнении операции');
        } finally {
            setLoading(false);
        }
    };

    const handleFileUpload = async (file, setFunction) => {
        if (!file) return;

        try {
            const formData = new FormData();
            formData.append('file', file);

            const response = await api.post('/api/function-io/input', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            if (response.data) {
                setFunction(response.data);
                setError(null);
            }
        } catch (error) {
            console.error('Upload error:', error);
            setError('Ошибка при загрузке файла: ' + error.message);
        }
    };

    const handleSaveToFile = async (functionData) => {
        try {
            const response = await api.get('/api/function-io/output', {
                params: {
                    hash: functionData.hash_function
                },
                responseType: 'blob'
            });

            // Используем FileSaver для открытия диалога "Сохранить как"
            saveAs(response.data, `function_${functionData.hash_function}.txt`);

            setError(null);
        } catch (error) {
            console.error('Save error:', error);
            setError('Ошибка при сохранении файла');
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-7xl shadow-lg rounded-md bg-white">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    {/* Первая функция */}
                    <div>
                        <h3 className="text-lg font-medium mb-4">Первая функция</h3>
                        <div className="space-y-3">
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => setShowCreateDialog1(true)}
                            >
                                Создать функцию
                            </button>
                            <label className="block">
                                <span className="sr-only">Загрузить функцию</span>
                                <input
                                    type="file"
                                    onChange={(e) => handleFileUpload(e.target.files[0], setFunction1)}
                                    className="block w-full text-sm text-gray-500
                                        file:mr-4 file:py-2 file:px-4
                                        file:rounded-md file:border-0
                                        file:text-sm file:font-semibold
                                        file:bg-blue-50 file:text-blue-700
                                        hover:file:bg-blue-100"
                                    accept=".txt"
                                />
                            </label>
                            {function1 && (
                                <button
                                    className="btn btn-secondary w-full"
                                    onClick={() => handleSaveToFile(function1)}
                                >
                                    Сохранить в файл
                                </button>
                            )}
                        </div>
                        {function1 && (
                        <FunctionTable
                            functionId={function1.hash_function}
                            points={function1.points}
                            isEditable={true}
                            onYChange={setFunction1}
                        />
                    )}
                    </div>

                    {/* Вторая функция */}
                    <div>
                        <h3 className="text-lg font-medium mb-4">Вторая функция</h3>
                        <div className="space-y-3">
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => setShowCreateDialog2(true)}
                            >
                                Создать функцию
                            </button>
                            <label className="block">
                                <span className="sr-only">Загрузить функцию</span>
                                <input
                                    type="file"
                                    onChange={(e) => handleFileUpload(e.target.files[0], setFunction2)}
                                    className="block w-full text-sm text-gray-500
                                        file:mr-4 file:py-2 file:px-4
                                        file:rounded-md file:border-0
                                        file:text-sm file:font-semibold
                                        file:bg-blue-50 file:text-blue-700
                                        hover:file:bg-blue-100"
                                    accept=".txt"
                                />
                            </label>
                            {function2 && (
                                <button
                                    className="btn btn-secondary w-full"
                                    onClick={() => handleSaveToFile(function2)}
                                >
                                    Сохранить в файл
                                </button>
                            )}
                        </div>
                        {function2 && (
                            <FunctionTable
                                functionId={function2.hash_function}
                                points={function2.points}
                                isEditable={true}
                                onYChange={setFunction2}
                            />
                        )}
                    </div>

                    {/* Результат */}
                    <div>
                        <h3 className="text-lg font-medium mb-4">Результат</h3>
                        <div className="space-y-3">
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => handleOperation('add')}
                                disabled={loading || !function1 || !function2}
                            >
                                Сложить
                            </button>
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => handleOperation('subtract')}
                                disabled={loading || !function1 || !function2}
                            >
                                Вычесть
                            </button>
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => handleOperation('multiply')}
                                disabled={loading || !function1 || !function2}
                            >
                                Умножить
                            </button>
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => handleOperation('divide')}
                                disabled={loading || !function1 || !function2}
                            >
                                Разделить
                            </button>
                            {result && (
                                <button
                                    className="btn btn-secondary w-full"
                                    onClick={() => handleSaveToFile(result)}
                                >
                                    Сохранить результат
                                </button>
                            )}
                        </div>
                        {result && (
                            <FunctionTable
                                functionId={result.id || result.hash_function}
                                points={result.points || []}
                                isEditable={false}
                            />
                        )}
                    </div>
                </div>

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

                <CreateFunctionDialog
                    isOpen={showCreateDialog1}
                    onClose={() => setShowCreateDialog1(false)}
                    onTypeSelect={(type) => handleSelectCreateType(1, type)}  // изменено
                />

                <CreateFunctionDialog
                    isOpen={showCreateDialog2}
                    onClose={() => setShowCreateDialog2(false)}
                    onTypeSelect={(type) => handleSelectCreateType(2, type)}  // изменено
                />
                {showCreator1 && (
                    <TabulatedFunctionCreator
                        isOpen={showCreator1}
                        onClose={() => setShowCreator1(false)}
                        onSuccess={(func) => {
                            console.log('Setting function1:', func);
                            setFunction1(func);
                            setShowCreator1(false);
                        }}
                        creatorType={creatorType}
                    />
                )}

                {showCreator2 && (
                    <TabulatedFunctionCreator
                        isOpen={showCreator2}
                        onClose={() => setShowCreator2(false)}
                        onSuccess={(func) => {
                            console.log('Setting function2:', func);
                            setFunction2(func);
                            setShowCreator2(false);
                        }}
                        creatorType={creatorType}
                    />
                )}
            </div>
        </div>
    );
};
export default FunctionOperations;