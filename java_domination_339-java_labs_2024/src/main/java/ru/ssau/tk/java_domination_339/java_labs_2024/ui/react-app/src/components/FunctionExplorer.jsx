import React, {useState, useEffect} from 'react';
import CommonModal from './CommonModal';
import {FunctionExplorerSection} from '../pages/ModalWrappers';
import SaveDropdown from '../pages/SaveDropdown';
import {CreateFunctionDialog} from '../pages/CreateFunctionDialog';
import TabulatedFunctionCreator from '../pages/FunctionCreator';
import {uploadFunction, saveFunction, checkFunctionCapabilities} from '../lib/functionUtils';
import {Alert, AlertDescription} from "./ui/alert";
import {AlertCircle} from 'lucide-react';
import api from '../services/api';
import FunctionControls from "../pages/FunctionControls";

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
        const checkCapabilities = async () => {
            if (currentFunction?.hash_function) {
                const capabilities = await checkFunctionCapabilities(currentFunction.hash_function);
                setCanInsert(capabilities.canInsert);
                setCanRemove(capabilities.canRemove);
            }
        };

        checkCapabilities();
    }, [currentFunction?.hash_function]);

    const handleFileUpload = async (formData, fileExtension) => {
        setLoading(true);
        try {
            const result = await uploadFunction(formData, fileExtension);
            setCurrentFunction(result);
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
            await saveFunction(currentFunction, format);
            setError(null);
        } catch (error) {
            setError('Ошибка при сохранении файла');
        } finally {
            setLoading(false);
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

            if (response.data.newFunctionId) {
                const updatedFunction = await api.get(`/api/functions/${response.data.newFunctionId}`);
                if (updatedFunction.data) {
                    setCurrentFunction(updatedFunction.data);
                }
            }

            return response.data;
        } catch (error) {
            throw error;
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
            title="Изучение функции"
        >
            <div className="space-y-4">
                <div className="flex flex-col space-y-4">
                    <FunctionControls
                        onCreateClick={() => setShowCreateDialog(true)}
                        onFileUpload={handleFileUpload}
                        loading={loading}
                    />

                    {currentFunction && (
                        <>
                            <div className="mt-4">
                                <SaveDropdown
                                    onSave={handleSaveToFile}
                                    disabled={loading}
                                />
                            </div>

                            <FunctionExplorerSection
                                function={currentFunction}
                                onPointChange={handlePointChange}
                                onApplyCalculate={handleApplyCalculate}
                                onInsertPoint={handleInsertPoint}
                                onRemovePoint={handleRemovePoint}
                                canInsert={canInsert}
                                canRemove={canRemove}
                            />
                        </>
                    )}

                    {error && (
                        <Alert className="bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                            <AlertDescription className="text-red-600 dark:text-red-300 flex items-center gap-2">
                                <AlertCircle className="w-4 h-4"/>
                                {error}
                            </AlertDescription>
                        </Alert>
                    )}
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

export default FunctionExplorer;