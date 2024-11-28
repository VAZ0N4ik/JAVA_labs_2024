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

const FunctionDifferential = ({ isOpen, onClose }) => {
    const [sourceFunction, setSourceFunction] = useState(null);
    const [result, setResult] = useState(null);
    const [showCreator, setShowCreator] = useState(false);
    const [error, setError] = useState(null);

    const handleDifferentiate = async () => {
        if (!sourceFunction?.id) {
            setError('Необходимо выбрать функцию');
            return;
        }

        try {
            const response = await api.post('/api/tabulated-function-operations/derive', null, {
                params: {
                    functionId: sourceFunction.id
                }
            });
            setResult(response.data);
        } catch (error) {
            setError(error.response?.data?.message || 'Ошибка при дифференцировании');
        }
    };

    const handleFileUpload = async (file) => {
        try {
            const formData = new FormData();
            formData.append('file', file);
            const response = await api.post('/api/function-io/input', formData);
            setSourceFunction(response.data);
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
                <div className="grid grid-cols-2 gap-4">
                    {/* Исходная функция */}
                    <div>
                        <h3 className="text-lg font-medium mb-2">Исходная функция</h3>
                        <div className="space-y-2">
                            <button
                                className="btn btn-primary w-full"
                                onClick={() => setShowCreator(true)}
                            >
                                Создать
                            </button>
                            <input
                                type="file"
                                onChange={(e) => handleFileUpload(e.target.files[0])}
                                className="w-full"
                            />
                            {sourceFunction && (
                                <button
                                    className="btn btn-secondary w-full"
                                    onClick={() => handleSaveToFile(sourceFunction)}
                                >
                                    Сохранить
                                </button>
                            )}
                        </div>
                        {sourceFunction && <FunctionTable points={sourceFunction.points} />}
                    </div>

                    {/* Производная */}
                    <div>
                        <h3 className="text-lg font-medium mb-2">Производная функции</h3>
                        <div className="space-y-2">
                            <button
                                className="btn btn-primary w-full"
                                onClick={handleDifferentiate}
                                disabled={!sourceFunction}
                            >
                                Дифференцировать
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

                {showCreator && (
                    <TabulatedFunctionCreator
                        isOpen={showCreator}
                        onClose={() => setShowCreator(false)}
                        onSuccess={(func) => {
                            setSourceFunction(func);
                            setShowCreator(false);
                        }}
                    />
                )}
            </div>
        </div>
    );
};

export default FunctionDifferential;