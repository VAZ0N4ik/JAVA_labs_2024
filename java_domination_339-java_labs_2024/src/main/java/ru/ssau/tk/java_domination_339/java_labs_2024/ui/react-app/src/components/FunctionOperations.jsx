import React, {useState} from 'react';
import {Plus, Minus, X as Multiply, Divide} from 'lucide-react';
import CommonModal from './CommonModal';
import {FunctionSection, OperationResultSection} from '../pages/ModalWrappers';
import {uploadFunction, saveFunction, checkFunctionCapabilities} from '../lib/functionUtils';
import {CreateFunctionDialog} from '../pages/CreateFunctionDialog';
import TabulatedFunctionCreator from '../pages/FunctionCreator';
import api from '../services/api';

const OperationButton = ({onClick, loading, disabled, icon: Icon, children}) => (
    <button
        className="btn btn-primary w-full flex items-center justify-center gap-2"
        onClick={onClick}
        disabled={loading || disabled}
    >
        <Icon className="w-4 h-4"/>
        {children}
    </button>
);

const FunctionOperations = ({isOpen, onClose}) => {
    const [function1, setFunction1] = useState(null);
    const [function2, setFunction2] = useState(null);
    const [result, setResult] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const [showCreateDialog1, setShowCreateDialog1] = useState(false);
    const [showCreateDialog2, setShowCreateDialog2] = useState(false);
    const [showCreator1, setShowCreator1] = useState(false);
    const [showCreator2, setShowCreator2] = useState(false);
    const [creatorType, setCreatorType] = useState(null);

    const [canInsert1, setCanInsert1] = useState(false);
    const [canRemove1, setCanRemove1] = useState(false);
    const [canInsert2, setCanInsert2] = useState(false);
    const [canRemove2, setCanRemove2] = useState(false);

    const handleFileUpload = async (formData, fileExtension, setFunction, setCanInsert, setCanRemove) => {
        setLoading(true);
        try {
            const result = await uploadFunction(formData, fileExtension);
            setFunction(result);
            const capabilities = await checkFunctionCapabilities(result.hash_function);
            setCanInsert(capabilities.canInsert);
            setCanRemove(capabilities.canRemove);
            setError(null);
        } catch (error) {
            setError('Ошибка при загрузке файла: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleOperation = async (operation) => {
        if (!function1 || !function2) {
            setError('Необходимо выбрать обе функции');
            return;
        }

        // Для деления проверяем нули
        if (operation === 'divide') {
            try {
                const points = function2.points;
                for (let i = 0; i < points.length; i++) {
                    const response = await api.post('/api/tabulated-function-operations/getY', null, {
                        params: {
                            functionId: function2.hash_function,
                            index: i
                        }
                    });
                    if (response.data === 0) {
                        setError('Невозможно выполнить деление: в знаменателе присутствуют нулевые значения');
                        return;
                    }
                }
            } catch (error) {
                setError('Ошибка при проверке значений второй функции');
                return;
            }
        }

        setLoading(true);
        try {
            const response = await api.post(`/api/tabulated-function-operations/${operation}`, null, {
                params: {
                    functionId1: function1.hash_function,
                    functionId2: function2.hash_function
                }
            });

            // Проверяем результат
            for (let i = 0; i < response.data.points.length; i++) {
                const y = await api.post('/api/tabulated-function-operations/getY', null, {
                    params: {
                        functionId: response.data.hash_function,
                        index: i
                    }
                });

                if (isNaN(y.data) || !isFinite(y.data) || Math.abs(y.data) > Number.MAX_SAFE_INTEGER) {
                    setError('Операция привела к некорректным значениям. Проверьте исходные функции');
                    return;
                }
            }

            setResult(response.data);
            setError(null);
        } catch (error) {
            const errorMessage = error.response?.data?.message || 'Ошибка при выполнении операции';
            if (errorMessage.includes('ArithmeticException')) {
                setError('Арифметическая ошибка: некорректные значения');
            } else if (errorMessage.includes('IllegalArgumentException')) {
                setError('Некорректные аргументы операции');
            } else {
                setError(errorMessage);
            }
        } finally {
            setLoading(false);
        }
    };

    const handleSaveToFile = async (func, format) => {
        setLoading(true);
        try {
            await saveFunction(func, format);
            setError(null);
        } catch (error) {
            setError('Ошибка при сохранении файла');
        } finally {
            setLoading(false);
        }
    };

    const handleRemovePoint = async (func, setFunc, x) => {
        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/remove', null, {
                params: {
                    functionId: func.hash_function,
                    x
                }
            });
            if (response.data) {
                setFunc(response.data);
                setError(null);
            }
        } catch (error) {
            setError('Ошибка при удалении точки');
        } finally {
            setLoading(false);
        }
    };

    const handleInsertPoint = async (func, setFunc, x, y) => {
        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/insert', null, {
                params: {
                    functionId: func.hash_function,
                    x,
                    y
                }
            });
            if (response.data) {
                setFunc(response.data);
                setError(null);
            }
        } catch (error) {
            setError('Ошибка при вставке точки');
        } finally {
            setLoading(false);
        }
    };

    return (
        <CommonModal
            isOpen={isOpen}
            onClose={onClose}
            title="Операции над функциями"
        >
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <FunctionSection
                    title="Первая функция"
                    function={function1}
                    setFunction={setFunction1}
                    loading={loading}
                    onCreateClick={() => setShowCreateDialog1(true)}
                    onFileUpload={(formData, ext) => handleFileUpload(formData, ext, setFunction1, setCanInsert1, setCanRemove1)}
                    onSave={(format) => handleSaveToFile(function1, format)}
                    canInsert={canInsert1}
                    canRemove={canRemove1}
                    onRemovePoint={(x) => handleRemovePoint(function1, setFunction1, x)}
                    onInsertPoint={(x, y) => handleInsertPoint(function1, setFunction1, x, y)}
                />

                <FunctionSection
                    title="Вторая функция"
                    function={function2}
                    setFunction={setFunction2}
                    loading={loading}
                    onCreateClick={() => setShowCreateDialog2(true)}
                    onFileUpload={(formData, ext) => handleFileUpload(formData, ext, setFunction2, setCanInsert2, setCanRemove2)}
                    onSave={(format) => handleSaveToFile(function2, format)}
                    canInsert={canInsert2}
                    canRemove={canRemove2}
                    onRemovePoint={(x) => handleRemovePoint(function2, setFunction2, x)}
                    onInsertPoint={(x, y) => handleInsertPoint(function2, setFunction2, x, y)}
                />

                <div className="space-y-4">
                    <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                        Результат
                    </h3>

                    <div className="space-y-2">
                        <OperationButton
                            onClick={() => handleOperation('add')}
                            loading={loading}
                            disabled={!function1 || !function2}
                            icon={Plus}
                        >
                            Сложить
                        </OperationButton>
                        <OperationButton
                            onClick={() => handleOperation('subtract')}
                            loading={loading}
                            disabled={!function1 || !function2}
                            icon={Minus}
                        >
                            Вычесть
                        </OperationButton>
                        <OperationButton
                            onClick={() => handleOperation('multiply')}
                            loading={loading}
                            disabled={!function1 || !function2}
                            icon={Multiply}
                        >
                            Умножить
                        </OperationButton>
                        <OperationButton
                            onClick={() => handleOperation('divide')}
                            loading={loading}
                            disabled={!function1 || !function2}
                            icon={Divide}
                        >
                            Разделить
                        </OperationButton>
                    </div>

                    <OperationResultSection
                        result={result}
                        onSave={(format) => handleSaveToFile(result, format)}
                        loading={loading}
                        error={error}
                    />
                </div>
            </div>

            <CreateFunctionDialog
                isOpen={showCreateDialog1}
                onClose={() => setShowCreateDialog1(false)}
                onTypeSelect={(type) => {
                    setCreatorType(type);
                    setShowCreateDialog1(false);
                    setShowCreator1(true);
                }}
            />

            <CreateFunctionDialog
                isOpen={showCreateDialog2}
                onClose={() => setShowCreateDialog2(false)}
                onTypeSelect={(type) => {
                    setCreatorType(type);
                    setShowCreateDialog2(false);
                    setShowCreator2(true);
                }}
            />

            {showCreator1 && (
                <TabulatedFunctionCreator
                    isOpen={showCreator1}
                    onClose={() => setShowCreator1(false)}
                    onSuccess={(func) => {
                        setFunction1(func);
                        setShowCreator1(false);
                        checkFunctionCapabilities(func.hash_function).then(
                            capabilities => {
                                setCanInsert1(capabilities.canInsert);
                                setCanRemove1(capabilities.canRemove);
                            }
                        );
                    }}
                    creatorType={creatorType}
                />
            )}

            {showCreator2 && (
                <TabulatedFunctionCreator
                    isOpen={showCreator2}
                    onClose={() => setShowCreator2(false)}
                    onSuccess={(func) => {
                        setFunction2(func);
                        setShowCreator2(false);
                        checkFunctionCapabilities(func.hash_function).then(
                            capabilities => {
                                setCanInsert2(capabilities.canInsert);
                                setCanRemove2(capabilities.canRemove);
                            }
                        );
                    }}
                    creatorType={creatorType}
                />
            )}
        </CommonModal>
    );
};

export default FunctionOperations;