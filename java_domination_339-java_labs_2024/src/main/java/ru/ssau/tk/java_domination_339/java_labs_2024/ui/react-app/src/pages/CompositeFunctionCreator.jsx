import React, {useState} from 'react';
import {Binary, AlertCircle, ArrowRight} from 'lucide-react';
import CommonModal from '../components/CommonModal';
import {FunctionSection} from './ModalWrappers';
import {Alert, AlertTitle, AlertDescription} from "../components/ui/alert";
import {uploadFunction, checkFunctionCapabilities} from '../lib/functionUtils';
import api from '../services/api';
import TabulatedFunctionCreator from "./FunctionCreator";
import {CreateFunctionDialog} from "./CreateFunctionDialog";

const CompositeFunctionCreator = ({isOpen, onClose}) => {
    const [function1, setFunction1] = useState(null);
    const [function2, setFunction2] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [localizedName, setLocalizedName] = useState('');

    const [canInsert1, setCanInsert1] = useState(false);
    const [canRemove1, setCanRemove1] = useState(false);
    const [canInsert2, setCanInsert2] = useState(false);
    const [canRemove2, setCanRemove2] = useState(false);

    const [showCreateDialog1, setShowCreateDialog1] = useState(false);
    const [showCreateDialog2, setShowCreateDialog2] = useState(false);
    const [showCreator1, setShowCreator1] = useState(false);
    const [showCreator2, setShowCreator2] = useState(false);
    const [creatorType, setCreatorType] = useState(null);

    const handleFileUpload = async (formData, fileExtension, isFirstFunction = true) => {
        setLoading(true);
        try {
            const result = await uploadFunction(formData, fileExtension);
            const capabilities = await checkFunctionCapabilities(result.hash_function);

            if (isFirstFunction) {
                setFunction1(result);
                setCanInsert1(capabilities.canInsert);
                setCanRemove1(capabilities.canRemove);
            } else {
                setFunction2(result);
                setCanInsert2(capabilities.canInsert);
                setCanRemove2(capabilities.canRemove);
            }

            setError(null);
        } catch (error) {
            setError('Ошибка при загрузке файла: ' + error.message);
        } finally {
            setLoading(false);
        }
    };

    const handleRemovePoint = async (x, isFirstFunction = true) => {
        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/remove', null, {
                params: {
                    functionId: isFirstFunction ? function1.hash_function : function2.hash_function,
                    x
                }
            });
            if (response.data) {
                if (isFirstFunction) {
                    setFunction1(response.data);
                } else {
                    setFunction2(response.data);
                }
                setError(null);
            }
        } catch (error) {
            setError('Ошибка при удалении точки');
        } finally {
            setLoading(false);
        }
    };

    const handleInsertPoint = async (x, y, isFirstFunction = true) => {
        setLoading(true);
        try {
            const response = await api.post('/api/tabulated-function-operations/insert', null, {
                params: {
                    functionId: isFirstFunction ? function1.hash_function : function2.hash_function,
                    x,
                    y
                }
            });
            if (response.data) {
                if (isFirstFunction) {
                    setFunction1(response.data);
                } else {
                    setFunction2(response.data);
                }
                setError(null);
            }
        } catch (error) {
            setError('Ошибка при вставке точки');
        } finally {
            setLoading(false);
        }
    };

    const handleCreateComposite = async () => {
        if (!function1 || !function2) {
            setError('Необходимо выбрать обе функции');
            return;
        }

        if (!localizedName.trim()) {
            setError('Необходимо указать название функции');
            return;
        }

        setLoading(true);
        try {
            // Проверяем существование функции с таким именем
            const response = await api.get('/api/function-creation/functions-to-create');
            if (response.data.includes(localizedName)) {
                setError('Функция с таким названием уже существует');
                setLoading(false);
                return;
            }

            await api.post('/api/function-creation/create-composite', null, {
                params: {
                    hash1: function1.hash_function,
                    hash2: function2.hash_function,
                    name: localizedName
                }
            });

            setSuccess('Композитная функция успешно создана');
            setError(null);

            setTimeout(() => {
                onClose();
            }, 1500);
        } catch (error) {
            setError(error.response?.data?.message || 'Ошибка при создании композитной функции');
        } finally {
            setLoading(false);
        }
    };

    return (
        <CommonModal
            isOpen={isOpen}
            onClose={onClose}
            title="Создание композитной функции"
        >
            <div className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <FunctionSection
                        title="Первая функция"
                        function={function1}
                        setFunction={setFunction1}
                        loading={loading}
                        onCreateClick={() => setShowCreateDialog1(true)}
                        onFileUpload={(formData, ext) => handleFileUpload(formData, ext, true)}
                        canInsert={canInsert1}
                        canRemove={canRemove1}
                        onRemovePoint={(x) => handleRemovePoint(x, true)}
                        onInsertPoint={(x, y) => handleInsertPoint(x, y, true)}
                    />

                    <FunctionSection
                        title="Вторая функция"
                        function={function2}
                        setFunction={setFunction2}
                        loading={loading}
                        onCreateClick={() => setShowCreateDialog2(true)}
                        onFileUpload={(formData, ext) => handleFileUpload(formData, ext, false)}
                        canInsert={canInsert2}
                        canRemove={canRemove2}
                        onRemovePoint={(x) => handleRemovePoint(x, false)}
                        onInsertPoint={(x, y) => handleInsertPoint(x, y, false)}
                    />
                </div>

                {(function1 && function2) && (
                    <div className="flex items-center gap-4 bg-blue-50 dark:bg-blue-900/30
                                  p-4 rounded-lg border border-blue-100 dark:border-blue-800">
                        <div className="flex-1 text-center font-medium text-gray-700 dark:text-gray-300">
                            {function1.hash_function}
                        </div>
                        <ArrowRight className="w-6 h-6 text-blue-500 dark:text-blue-400"/>
                        <div className="flex-1 text-center font-medium text-gray-700 dark:text-gray-300">
                            {function2.hash_function}
                        </div>
                    </div>
                )}

                <div className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                            Локализованное название функции
                        </label>
                        <input
                            type="text"
                            value={localizedName}
                            onChange={(e) => setLocalizedName(e.target.value)}
                            className="input"
                            placeholder="Введите название функции"
                            disabled={loading || !function1 || !function2}
                        />
                    </div>

                    <button
                        className="btn btn-primary w-full flex items-center justify-center gap-2"
                        onClick={handleCreateComposite}
                        disabled={loading || !function1 || !function2 || !localizedName.trim()}
                    >
                        <Binary className="w-4 h-4"/>
                        {loading ? 'Создание...' : 'Создать композитную функцию'}
                    </button>
                </div>

                {error && (
                    <Alert className="bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                        <AlertTitle className="text-red-700 dark:text-red-400">
                            Ошибка
                        </AlertTitle>
                        <AlertDescription className="text-red-600 dark:text-red-300 flex items-center gap-2">
                            <AlertCircle className="w-4 h-4"/>
                            {error}
                        </AlertDescription>
                    </Alert>
                )}

                {success && (
                    <Alert className="bg-green-50 border-green-200 dark:bg-green-900/30 dark:border-green-800">
                        <AlertTitle className="text-green-700 dark:text-green-400">
                            Успех
                        </AlertTitle>
                        <AlertDescription className="text-green-600 dark:text-green-300">
                            {success}
                        </AlertDescription>
                    </Alert>
                )}

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
            </div>
        </CommonModal>
    );
};

export default CompositeFunctionCreator;