import React, {useState} from 'react';
import {TrendingUp} from 'lucide-react';
import CommonModal from './CommonModal';
import {FunctionSection, OperationResultSection} from '../pages/ModalWrappers';
import {uploadFunction, saveFunction, checkFunctionCapabilities} from '../lib/functionUtils';
import {CreateFunctionDialog} from '../pages/CreateFunctionDialog';
import TabulatedFunctionCreator from '../pages/FunctionCreator';
import api from '../services/api';

const FunctionDifferential = ({isOpen, onClose}) => {
    const [sourceFunction, setSourceFunction] = useState(null);
    const [result, setResult] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const [showCreateDialog, setShowCreateDialog] = useState(false);
    const [showCreator, setShowCreator] = useState(false);
    const [creatorType, setCreatorType] = useState(null);
    const [canInsert, setCanInsert] = useState(false);
    const [canRemove, setCanRemove] = useState(false);

    const handleFileUpload = async (formData, fileExtension) => {
        setLoading(true);
        try {
            const result = await uploadFunction(formData, fileExtension);
            setSourceFunction(result);
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

    const handleSaveToFile = async (format) => {
        setLoading(true);
        try {
            await saveFunction(sourceFunction, format);
            setError(null);
        } catch (error) {
            setError('Ошибка при сохранении файла');
        } finally {
            setLoading(false);
        }
    };

    const handleResultSave = async (format) => {
        setLoading(true);
        try {
            await saveFunction(result, format);
            setError(null);
        } catch (error) {
            setError('Ошибка при сохранении результата');
        } finally {
            setLoading(false);
        }
    };

    const handleDifferentiate = async () => {
        if (!sourceFunction?.hash_function) {
            setError('Необходимо выбрать функцию');
            return;
        }

        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/derive', null, {
                params: {
                    functionId: sourceFunction.hash_function
                }
            });

            setResult(response.data);
            setError(null);
        } catch (error) {
            setError(error.response?.data?.message || 'Ошибка при дифференцировании');
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
                    functionId: sourceFunction.hash_function,
                    x
                }
            });
            if (response.data) {
                setSourceFunction(response.data);
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
                    functionId: sourceFunction.hash_function,
                    x,
                    y
                }
            });
            if (response.data) {
                setSourceFunction(response.data);
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
            title="Дифференцирование функции"
        >
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <FunctionSection
                    title="Исходная функция"
                    function={sourceFunction}
                    setFunction={setSourceFunction}
                    loading={loading}
                    onCreateClick={() => setShowCreateDialog(true)}
                    onFileUpload={handleFileUpload}
                    onSave={handleSaveToFile}
                    canInsert={canInsert}
                    canRemove={canRemove}
                    onRemovePoint={handleRemovePoint}
                    onInsertPoint={handleInsertPoint}
                    error={error}
                />

                <div className="space-y-4">
                    <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                        Производная функции
                    </h3>

                    <button
                        className="btn btn-primary w-full flex items-center justify-center gap-2"
                        onClick={handleDifferentiate}
                        disabled={loading || !sourceFunction}
                    >
                        <TrendingUp className="w-4 h-4"/>
                        {loading ? 'Вычисление...' : 'Дифференцировать'}
                    </button>

                    <OperationResultSection
                        result={result}
                        onSave={handleResultSave}
                        loading={loading}
                        error={error}
                    />
                </div>
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
                        setSourceFunction(func);
                        setShowCreator(false);
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
        </CommonModal>
    );
};

export default FunctionDifferential;