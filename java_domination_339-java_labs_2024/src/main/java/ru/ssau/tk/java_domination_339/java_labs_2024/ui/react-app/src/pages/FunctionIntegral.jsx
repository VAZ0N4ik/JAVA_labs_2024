import React, {useState} from 'react';
import {Sigma, Calculator, AlertCircle} from 'lucide-react';
import CommonModal from '../components/CommonModal';
import FunctionControls from './FunctionControls';
import FunctionTableImproved from './FunctionTableImproved';
import {uploadFunction, checkFunctionCapabilities} from '../lib/functionUtils';
import {CreateFunctionDialog} from './CreateFunctionDialog';
import TabulatedFunctionCreator from './FunctionCreator';
import {Alert, AlertDescription} from "../components/ui/alert";
import api from '../services/api';

const FunctionIntegral = ({isOpen, onClose}) => {
    const MAX_THREADS = 4; // максимальное количество потоков

    const [currentFunction, setCurrentFunction] = useState(null);
    const [showCreateDialog, setShowCreateDialog] = useState(false);
    const [showCreator, setShowCreator] = useState(false);
    const [creatorType, setCreatorType] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [threads, setThreads] = useState(2);
    const [result, setResult] = useState(null);
    const [canInsert, setCanInsert] = useState(false);
    const [canRemove, setCanRemove] = useState(false);

    const handleFileUpload = async (formData, fileExtension) => {
        setLoading(true);
        try {
            const result = await uploadFunction(formData, fileExtension);
            setCurrentFunction(result);
            const capabilities = await checkFunctionCapabilities(result.hash_function);
            setCanInsert(capabilities.canInsert);
            setCanRemove(capabilities.canRemove);
            setError(null);
            setResult(null);
        } catch (error) {
            setError('Ошибка при загрузке файла: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleCalculate = async () => {
        if (!currentFunction) {
            setError('Необходимо выбрать функцию');
            return;
        }

        if (threads < 1) {
            setError('Количество потоков должно быть положительным числом');
            return;
        }

        setLoading(true);
        try {
            const response = await api.get('/api/tabulated-function-operations/integral', {
                params: {
                    functionId: currentFunction.hash_function,
                    threads: threads
                }
            });

            setResult(response.data);
            setError(null);
        } catch (error) {
            setError(error.response?.data?.message || 'Ошибка при вычислении интеграла');
        } finally {
            setLoading(false);
        }
    };

    const handleRemovePoint = async (x) => {
        if (!canRemove) return;
        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/remove', null, {
                params: {
                    functionId: currentFunction.hash_function,
                    x
                }
            });
            if (response.data) {
                setCurrentFunction(response.data);
                setResult(null);
                setError(null);
            }
        } catch (error) {
            setError('Ошибка при удалении точки');
        } finally {
            setLoading(false);
        }
    };

    const handleInsertPoint = async (x, y) => {
        if (!canInsert) return;
        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/insert', null, {
                params: {
                    functionId: currentFunction.hash_function,
                    x,
                    y
                }
            });
            if (response.data) {
                setCurrentFunction(response.data);
                setResult(null);
                setError(null);
            }
        } catch (error) {
            setError('Ошибка при вставке точки');
        } finally {
            setLoading(false);
        }
    };

    const handleThreadsChange = (e) => {
        const value = parseInt(e.target.value);
        if (isNaN(value) || value < 1) {
            setThreads(1);
        } else if (value > MAX_THREADS) {
            setThreads(MAX_THREADS);
        } else {
            setThreads(value);
        }
    };

    return (
        <CommonModal
            isOpen={isOpen}
            onClose={onClose}
            title="Вычисление определённого интеграла"
        >
            <div className="space-y-6">
                <div className="space-y-4">
                    <FunctionControls
                        onCreateClick={() => setShowCreateDialog(true)}
                        onFileUpload={handleFileUpload}
                        loading={loading}
                    />

                    {currentFunction && (
                        <FunctionTableImproved
                            functionId={currentFunction.hash_function}
                            points={currentFunction.points}
                            isEditable={true}
                            onYChange={setCurrentFunction}
                            canRemove={canRemove}
                            canInsert={canInsert}
                            onRemovePoint={handleRemovePoint}
                            onInsertPoint={handleInsertPoint}
                        />
                    )}
                </div>

                <div className="space-y-4">
                    <div className="flex items-end gap-4">
                        <div className="flex-1">
                            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                                Количество потоков (макс. {MAX_THREADS})
                            </label>
                            <input
                                type="number"
                                value={threads}
                                onChange={handleThreadsChange}
                                className="input"
                                min="1"
                                max={MAX_THREADS}
                                disabled={loading}
                            />
                            {threads > MAX_THREADS && (
                                <div className="text-sm text-red-500 mt-1">
                                    Максимальное количество потоков: {MAX_THREADS}
                                </div>
                            )}
                        </div>
                        <button
                            onClick={handleCalculate}
                            className="btn btn-primary flex items-center gap-2"
                            disabled={loading || !currentFunction}
                        >
                            {loading ? (
                                <>
                                    <Calculator className="w-4 h-4 animate-spin"/>
                                    Вычисление...
                                </>
                            ) : (
                                <>
                                    <Sigma className="w-4 h-4"/>
                                    Вычислить интеграл
                                </>
                            )}
                        </button>
                    </div>

                    {result !== null && (
                        <div className="p-4 bg-blue-50 dark:bg-blue-900/30 rounded-lg border
                                    border-blue-100 dark:border-blue-800">
                            <div className="text-lg font-medium text-blue-900 dark:text-blue-100">
                                Результат:
                            </div>
                            <div className="text-2xl font-bold text-blue-700 dark:text-blue-300">
                                {result.toFixed(6)}
                            </div>
                        </div>
                    )}
                </div>

                {error && (
                    <Alert className="bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                        <AlertDescription className="text-red-600 dark:text-red-300 flex items-center gap-2">
                            <AlertCircle className="w-4 h-4"/>
                            {error}
                        </AlertDescription>
                    </Alert>
                )}

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
                            setResult(null);
                            checkFunctionCapabilities(func.hash_function).then(
                                capabilities => {
                                    setCanInsert(capabilities.canInsert);
                                    setCanRemove(capabilities.canRemove);
                                }
                            );
                        }}
                        creatorType={creatorType}
                    />
                )}
            </div>
        </CommonModal>
    );
};

export default FunctionIntegral;