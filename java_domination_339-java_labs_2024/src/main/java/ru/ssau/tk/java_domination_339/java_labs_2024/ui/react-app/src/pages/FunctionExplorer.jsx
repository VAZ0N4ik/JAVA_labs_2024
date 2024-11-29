import React, {useState, useEffect} from 'react';
import {Download} from 'lucide-react';
import {Alert, AlertTitle, AlertDescription} from '../components/ui/alert';
import FunctionVisualizer from './FunctionVisualizer';
import {CreateFunctionDialog} from './CreateFunctionDialog';
import TabulatedFunctionCreator from './FunctionCreator';
import api from '../services/api';

export const FunctionExplorer = ({isOpen, onClose}) => {
    const [currentFunction, setCurrentFunction] = useState(null);
    const [showCreateDialog, setShowCreateDialog] = useState(false);
    const [showCreator, setShowCreator] = useState(false);
    const [creatorType, setCreatorType] = useState(null);
    const [error, setError] = useState(null);
    const [canInsert, setCanInsert] = useState(false);
    const [canRemove, setCanRemove] = useState(false);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const checkFunctionCapabilities = async () => {
            if (!currentFunction?.hash_function) return;

            try {
                const [insertResponse, removeResponse] = await Promise.all([
                    api.get('/api/tabulated-function-operations/is-insert', {
                        params: {functionId: currentFunction.hash_function}
                    }),
                    api.get('/api/tabulated-function-operations/is-remove', {
                        params: {functionId: currentFunction.hash_function}
                    })
                ]);
                setCanInsert(insertResponse.data);
                setCanRemove(removeResponse.data);
            } catch (error) {
                console.error('Error checking capabilities:', error);
                // Не показываем ошибку пользователю, просто отключаем возможности
                setCanInsert(false);
                setCanRemove(false);
            }
        };

        checkFunctionCapabilities();
    }, [currentFunction?.hash_function]);

    const checkFunctionCapabilities = async () => {
        try {
            const [insertResponse, removeResponse] = await Promise.all([
                api.get('/api/tabulated-function-operations/is-insert', {
                    params: {functionId: currentFunction.hash_function}
                }),
                api.get('/api/tabulated-function-operations/is-remove', {
                    params: {functionId: currentFunction.hash_function}
                })
            ]);
            setCanInsert(insertResponse.data);
            setCanRemove(removeResponse.data);
        } catch (error) {
            console.error('Error checking capabilities:', error);
        }
    };

    const handlePointChange = async (index, newY) => {
        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/setY', null, {
                params: {
                    functionId: currentFunction.hash_function,
                    index,
                    y: newY
                }
            });

            if (response.data) {
                setCurrentFunction(response.data);
                setError(null);
            }
        } catch (error) {
            console.error('Point change error:', error);
            setError('Ошибка при изменении точки');
        } finally {
            setLoading(false);
        }
    };

    const handleApplyCalculate = async (x) => {
        try {
            const response = await api.post('/api/tabulated-function-operations/apply', null, {
                params: {
                    functionId: currentFunction.hash_function,
                    x
                }
            });

            // Если получили новый хеш функции после вставки
            if (response.data.newFunctionId) {
                // Запрашиваем обновленную функцию
                const updatedFunctionResponse = await api.get(`/api/functions/${response.data.newFunctionId}`);
                if (updatedFunctionResponse.data) {
                    setCurrentFunction(updatedFunctionResponse.data);
                }
            }

            return response.data.value; // Возвращаем значение для отображения
        } catch (error) {
            console.error('Apply calculation error:', error);
            throw error;
        }
    };

    const handleInsertPoint = async (x, y) => {
        setLoading(true);
        try {
            console.log('Inserting point:', {x, y, functionId: currentFunction.hash_function});
            const response = await api.post('/api/tabulated-function-operations/insert', null, {
                params: {
                    functionId: currentFunction.hash_function,
                    x,
                    y
                }
            });

            if (response.data) {
                console.log('Insert response:', response.data);
                // Обновляем текущую функцию с новыми данными
                setCurrentFunction(response.data);
                setError(null);
            }
        } catch (error) {
            console.error('Insert error:', error);
            setError('Ошибка при вставке точки');
            throw error;
        } finally {
            setLoading(false);
        }
    };

    const handleRemovePoint = async (x) => {
        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/remove', null, {
                params: {
                    functionId: currentFunction.hash_function,
                    x
                }
            });

            if (response.data) {
                // Обновляем функцию новыми данными из ответа
                const updatedFunctionResponse = await api.get(`/api/functions/${response.data.hash_function}`);
                if (updatedFunctionResponse.data) {
                    setCurrentFunction(updatedFunctionResponse.data);
                    setError(null);
                }
            }
        } catch (error) {
            console.error('Remove error:', error);
            setError('Ошибка при удалении точки');
        } finally {
            setLoading(false);
        }
    };

    const handleFileUpload = async (file) => {
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            let response;
            // Определяем тип файла по расширению
            const fileExtension = file.name.split('.').pop().toLowerCase();

            switch (fileExtension) {
                case 'json':
                    response = await api.post('/api/function-io/input-json', formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                    break;
                case 'xml':
                    response = await api.post('/api/function-io/input-xml', formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                    break;
                case 'txt':
                default:
                    response = await api.post('/api/function-io/input', formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
                    break;
            }

            if (response.data) {
                setCurrentFunction(response.data);
                setError(null);
            }
        } catch (error) {
            console.error('Upload error:', error);
            setError(error.response?.data?.message || 'Ошибка при загрузке файла');
        }
    };

    const handleSaveToFile = async (format = 'binary') => {
        try {
            const endpoint = format === 'json' ? '/api/function-io/output-json' :
                format === 'xml' ? '/api/function-io/output-xml' :
                    '/api/function-io/output';

            const response = await api.get(endpoint, {
                params: {hash: currentFunction.hash_function},
                responseType: 'blob'
            });

            const fileExtension = format === 'json' ? 'json' :
                format === 'xml' ? 'xml' :
                    'txt';

            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `function_${currentFunction.hash_function}.${fileExtension}`);
            document.body.appendChild(link);
            link.click();
            link.remove();
            window.URL.revokeObjectURL(url);
        } catch (error) {
            console.error('Save error:', error);
            setError('Ошибка при сохранении файла');
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div className="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
                <h2 className="text-2xl font-bold mb-6">Изучение функции</h2>

                <div className="space-y-4">
                    <div className="flex gap-4">
                        <button
                            className="btn btn-primary flex-1"
                            onClick={() => setShowCreateDialog(true)}
                            disabled={loading}
                        >
                            Создать функцию
                        </button>
                        <label className="block flex-1">
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
                                accept=".txt,.json,.xml"
                            />
                            <span className="mt-1 text-xs text-gray-500">
                                Поддерживаемые форматы: TXT, JSON, XML
                             </span>
                        </label>
                    </div>

                    {currentFunction && (
                        <>
                            <div className="mb-4">
                                <pre className="text-xs bg-gray-100 p-2 rounded">
                                    {JSON.stringify({
                                        currentHash: currentFunction.hash_function,
                                        pointCount: currentFunction.points?.length,
                                        canInsert,
                                        canRemove
                                    }, null, 2)}
                                </pre>
                            </div>

                            <FunctionVisualizer
                                key={currentFunction.hash_function}
                                functionId={currentFunction.hash_function}
                                points={currentFunction.points}
                                isEditable={true}
                                onPointChange={handlePointChange}
                                onApplyCalculate={handleApplyCalculate}
                                onInsertPoint={handleInsertPoint}
                                onRemovePoint={handleRemovePoint}
                                canInsert={canInsert}
                                canRemove={canRemove}
                            />

                            <div className="flex gap-4 mt-4">
                                <div className="dropdown">
                                    <button
                                        className="btn btn-secondary"
                                        disabled={loading}
                                    >
                                        <Download className="w-4 h-4 mr-2"/>
                                        Сохранить как
                                    </button>
                                    <div className="dropdown-content">
                                        <button
                                            onClick={() => handleSaveToFile('binary')}
                                            className="btn btn-ghost w-full text-left"
                                        >
                                            Бинарный файл (.txt)
                                        </button>
                                        <button
                                            onClick={() => handleSaveToFile('json')}
                                            className="btn btn-ghost w-full text-left"
                                        >
                                            JSON файл (.json)
                                        </button>
                                        <button
                                            onClick={() => handleSaveToFile('xml')}
                                            className="btn btn-ghost w-full text-left"
                                        >
                                            XML файл (.xml)
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </>
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
                            disabled={loading}
                        >
                            Закрыть
                        </button>
                    </div>

                    <CreateFunctionDialog
                        isOpen={showCreateDialog}
                        onClose={() => setShowCreateDialog(false)}
                        onTypeSelect={(type) => {
                            setCreatorType(type);
                            setShowCreateDialog(false);
                            setShowCreator(true);
                        }}
                    />

                    {showCreator && (
                        <TabulatedFunctionCreator
                            isOpen={showCreator}
                            onClose={() => setShowCreator(false)}
                            onSuccess={(func) => {
                                setCurrentFunction(func);
                                setShowCreator(false);
                            }}
                            creatorType={creatorType}
                        />
                    )}
                </div>
            </div>
        </div>
    );
};

export default FunctionExplorer;