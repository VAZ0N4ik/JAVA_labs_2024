import React, {useEffect, useState} from 'react';
import api from '../services/api';
import { Alert, AlertTitle, AlertDescription } from "../components/ui/alert";
import TabulatedFunctionCreator from './FunctionCreator';

const CreateFunctionDialog = ({ isOpen, onClose, onTypeSelect }) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg shadow-xl">
                <h3 className="text-lg font-medium mb-4">Выберите тип создания функции</h3>
                <div className="space-y-3">
                    <button
                        className="w-full btn btn-primary"
                        onClick={() => onTypeSelect('array')}
                    >
                        Создать из массива
                    </button>
                    <button
                        className="w-full btn btn-primary"
                        onClick={() => onTypeSelect('function')}
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

    const handleYChange = async (index, newValue) => {
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
                setModifiedData({});
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

const FunctionIntegral = ({ isOpen, onClose }) => {
    const [sourceFunction, setSourceFunction] = useState(null);
    const [result, setResult] = useState(null);
    const [showCreateDialog, setShowCreateDialog] = useState(false);
    const [showCreator, setShowCreator] = useState(false);
    const [creatorType, setCreatorType] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [threads, setThreads] = useState(1);

    const handleSelectCreateType = (type) => {
        setShowCreateDialog(false);
        setCreatorType(type);
        setShowCreator(true);
    };

    const handleIntegrate = async () => {
        if (!sourceFunction?.hash_function) {
            setError('Необходимо выбрать функцию');
            return;
        }

        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/integral', null, {
                params: {
                    functionId: sourceFunction.hash_function,
                    threads: threads
                }
            });

            if (response.data !== undefined) {
                setResult(response.data);  // Теперь это числовое значение
                setError(null);
            } else {
                throw new Error('Ответ от сервера не содержит данных');
            }
        } catch (error) {
            console.error('Integration error:', error);
            setError(error.response?.data?.message || 'Ошибка при интегрировании');
        } finally {
            setLoading(false);
        }
    };

    const handleFileUpload = async (file) => {
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await api.post('/api/function-io/input', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            if (response.data) {
                setSourceFunction(response.data);
                setError(null);
            }
        } catch (error) {
            console.error('Upload error:', error);
            setError('Ошибка при загрузке файла');
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-7xl shadow-lg rounded-md bg-white">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {/* Исходная функция */}
                    <div>
                        <h3 className="text-lg font-medium mb-4">Исходная функция</h3>
                        <div className="space-y-3">
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => setShowCreateDialog(true)}
                            >
                                Создать функцию
                            </button>
                            <label className="block">
                                <span className="sr-only">Загрузить функцию</span>
                                <input
                                    type="file"
                                    onChange={(e) => handleFileUpload(e.target.files[0])}
                                    className="block w-full text-sm text-gray-500
                                       file:mr-4 file:py-2 file:px-4
                                       file:rounded-md file:border-0
                                       file:text-sm file:font-semibold
                                       file:bg-blue-50 file:text-blue-700
                                       hover:file:bg-blue-100"
                                    accept=".txt"
                                />
                            </label>
                        </div>
                        {sourceFunction && (
                            <FunctionTable
                                functionId={sourceFunction.hash_function}
                                points={sourceFunction.points}
                                isEditable={true}
                                onYChange={setSourceFunction}
                            />
                        )}
                    </div>

                    {/* Результат */}
                    <div>
                        <h3 className="text-lg font-medium mb-4">Интеграл функции</h3>
                        <div className="space-y-3">
                            <div className="flex items-center space-x-2">
                                <label className="block text-sm font-medium text-gray-700">
                                    Кол-во потоков:
                                </label>
                                <input
                                    type="number"
                                    min="1"
                                    value={threads}
                                    onChange={(e) => setThreads(parseInt(e.target.value) || 1)}
                                    className="w-20 px-2 py-1 border rounded focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                            <button
                                className="btn btn-primary w-full"
                                onClick={handleIntegrate}
                                disabled={loading || !sourceFunction}
                            >
                                Интегрировать
                            </button>
                            {result !== null && (
                                <div className="bg-gray-100 p-4 rounded-md text-center">
                                    <h4 className="text-lg font-semibold">Значение интеграла:</h4>
                                    <p className="text-2xl font-bold">{result.toFixed(4)}</p>
                                </div>
                            )}
                        </div>
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
                    isOpen={showCreateDialog}
                    onClose={() => setShowCreateDialog(false)}
                    onTypeSelect={handleSelectCreateType}
                />

                {showCreator && (
                    <TabulatedFunctionCreator
                        isOpen={showCreator}
                        onClose={() => setShowCreator(false)}
                        onSuccess={(func) => {
                            setSourceFunction(func);
                            setShowCreator(false);
                        }}
                        creatorType={creatorType}
                    />
                )}
            </div>
        </div>
    );
};

export default FunctionIntegral;