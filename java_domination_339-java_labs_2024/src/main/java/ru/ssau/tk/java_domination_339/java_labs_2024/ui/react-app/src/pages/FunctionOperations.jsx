import React, { useState } from 'react';
import api from '../services/api';
import { Alert, AlertTitle, AlertDescription } from "../components/ui/alert";
import TabulatedFunctionCreator from './FunctionCreator';

const FunctionTable = ({ points, isEditable = true }) => {
    return (
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
                        <td className="text-gray-600">{point.x}</td>
                        <td>
                            {isEditable ? (
                                <input
                                    type="number"
                                    value={point.y}
                                    className="input"
                                    step="any"
                                    disabled={!isEditable}
                                />
                            ) : (
                                <span>{point.y}</span>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

const FunctionOperations = ({ isOpen, onClose }) => {
    const [function1, setFunction1] = useState(null);
    const [function2, setFunction2] = useState(null);
    const [result, setResult] = useState(null);
    const [showCreator1, setShowCreator1] = useState(false);
    const [showCreator2, setShowCreator2] = useState(false);
    const [error, setError] = useState(null);

    const handleOperation = async (operation) => {
        if (!function1?.id || !function2?.id) {
            setError('Необходимо выбрать обе функции');
            return;
        }

        try {
            const response = await api.post(`/api/tabulated-function-operations/${operation}`, null, {
                params: {
                    functionId1: function1.id,
                    functionId2: function2.id
                }
            });
            setResult(response.data);
        } catch (error) {
            setError(error.response?.data?.message || 'Ошибка при выполнении операции');
        }
    };

    const handleFileUpload = async (file, setFunction) => {
        try {
            const formData = new FormData();
            formData.append('file', file);
            const response = await api.post('/api/function-io/input', formData);
            setFunction(response.data);
        } catch (error) {
            setError('Ошибка при загрузке файла');
        }
    };

    const handleSaveToFile = async (functionData) => {
        try {
            const response = await api.get('/api/function-io/output', {
                params: {
                    hash: functionData.hash_function,
                    path: `function_${functionData.id}.txt`
                }
            });
            // Здесь можно добавить обработку успешного сохранения
        } catch (error) {
            setError('Ошибка при сохранении файла');
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 overflow-y-auto h-full w-full">
            <div className="relative top-20 mx-auto p-5 border w-11/12 shadow-lg rounded-md bg-white">
                <div className="grid grid-cols-3 gap-4">
                    {/* Первая функция */}
                    <div>
                        <h3 className="text-lg font-medium mb-2">Первая функция</h3>
                        <div className="space-y-2">
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => setShowCreator1(true)}
                            >
                                Создать
                            </button>
                            <input
                                type="file"
                                onChange={(e) => handleFileUpload(e.target.files[0], setFunction1)}
                                className="w-full"
                            />
                            {function1 && (
                                <button
                                    className="btn btn-secondary w-full"
                                    onClick={() => handleSaveToFile(function1)}
                                >
                                    Сохранить
                                </button>
                            )}
                        </div>
                        {function1 && <FunctionTable points={function1.points} />}
                    </div>

                    {/* Вторая функция */}
                    <div>
                        <h3 className="text-lg font-medium mb-2">Вторая функция</h3>
                        <div className="space-y-2">
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => setShowCreator2(true)}
                            >
                                Создать
                            </button>
                            <input
                                type="file"
                                onChange={(e) => handleFileUpload(e.target.files[0], setFunction2)}
                                className="w-full"
                            />
                            {function2 && (
                                <button
                                    className="btn btn-secondary w-full"
                                    onClick={() => handleSaveToFile(function2)}
                                >
                                    Сохранить
                                </button>
                            )}
                        </div>
                        {function2 && <FunctionTable points={function2.points} />}
                    </div>

                    {/* Результат */}
                    <div>
                        <h3 className="text-lg font-medium mb-2">Результат</h3>
                        <div className="space-y-2">
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => handleOperation('add')}
                            >
                                Сложить
                            </button>
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => handleOperation('subtract')}
                            >
                                Вычесть
                            </button>
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => handleOperation('multiply')}
                            >
                                Умножить
                            </button>
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => handleOperation('divide')}
                            >
                                Разделить
                            </button>
                            {result && (
                                <button
                                    className="btn btn-secondary w-full"
                                    onClick={() => handleSaveToFile(result)}
                                >
                                    Сохранить
                                </button>
                            )}
                        </div>
                        {result && <FunctionTable points={result.points} isEditable={false} />}
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

                <button
                    onClick={onClose}
                    className="mt-4 btn btn-secondary"
                >
                    Закрыть
                </button>

                {showCreator1 && (
                    <TabulatedFunctionCreator
                        isOpen={showCreator1}
                        onClose={() => setShowCreator1(false)}
                        onSuccess={(func) => {
                            setFunction1(func);
                            setShowCreator1(false);
                        }}
                    />
                )}

                {showCreator2 && (
                    <TabulatedFunctionCreator
                        isOpen={showCreator2}
                        onClose={() => setShowCreator2(false)}
                        onSuccess={(func) => {
                            setFunction2(func);
                            setShowCreator2(false);
                        }}
                    />
                )}
            </div>
        </div>
    );
};

export default FunctionOperations;