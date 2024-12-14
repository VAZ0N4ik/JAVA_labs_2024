import React from 'react';
import {AlertCircle} from 'lucide-react';
import FunctionTableImproved from './FunctionTableImproved';
import FunctionVisualizer from './FunctionVisualizer';
import SaveDropdown from './SaveDropdown';
import {Alert, AlertDescription} from "../components/ui/alert";

export const OperationResultSection = ({
                                           result,
                                           onSave,
                                           loading,
                                           error
                                       }) => (
    <div>
        {error && (
            <Alert className="mb-4 bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                <AlertDescription className="text-red-600 dark:text-red-300 flex items-center gap-2">
                    <AlertCircle className="w-4 h-4"/>
                    {error}
                </AlertDescription>
            </Alert>
        )}

        {result && (
            <>
                <FunctionTableImproved
                    functionId={result.hash_function}
                    points={result.points}
                    isEditable={false}
                />
                <div className="mt-4">
                    <SaveDropdown
                        onSave={onSave}
                        disabled={loading}
                    />
                </div>
            </>
        )}
    </div>
);

export const FunctionSection = ({
                                    title,
                                    function: func,
                                    setFunction,
                                    loading,
                                    onCreateClick,  // Добавляем этот проп
                                    onFileUpload,
                                    canInsert,
                                    canRemove,
                                    onRemovePoint,
                                    onInsertPoint,
                                    error
                                }) => (
    <div className="space-y-4">
        <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
            {title}
        </h3>

        <div className="space-y-3">
            <button
                className="btn btn-primary w-full"
                onClick={onCreateClick}  // Добавляем обработчик
                disabled={loading}
            >
                Создать функцию
            </button>

            <label className="block">
                <input
                    type="file"
                    onChange={(e) => {
                        const file = e.target.files[0];
                        if (file) {
                            const formData = new FormData();
                            formData.append('file', file);
                            onFileUpload(formData, file.name.split('.').pop().toLowerCase());
                        }
                    }}
                    className="block w-full text-sm text-gray-500 dark:text-gray-400
                        file:mr-4 file:py-2 file:px-4
                        file:rounded-md file:border-0
                        file:text-sm file:font-semibold
                        file:bg-blue-50 file:text-blue-700 dark:file:bg-blue-900 dark:file:text-blue-300
                        hover:file:bg-blue-100 dark:hover:file:bg-blue-800"
                    accept=".txt,.json,.xml"
                />
            </label>
        </div>

        {func && (
            <FunctionTableImproved
                functionId={func.hash_function}
                points={func.points}
                isEditable={true}
                onYChange={setFunction}
                canRemove={canRemove}
                canInsert={canInsert}
                onRemovePoint={onRemovePoint}
                onInsertPoint={onInsertPoint}
            />
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
);

export const FunctionExplorerSection = ({
                                            function: func,
                                            onPointChange,
                                            onApplyCalculate,
                                            onInsertPoint,
                                            onRemovePoint,
                                            canInsert,
                                            canRemove
                                        }) => (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="space-y-4">
            <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                График функции
            </h3>
            <FunctionVisualizer
                functionId={func.hash_function}
                points={func.points}
                onPointChange={onPointChange}
                onApplyCalculate={onApplyCalculate}
                canInsert={canInsert}
            />
        </div>

        <div className="space-y-4">
            <h3 className="text-xl font-semibold text-gray-900 dark:text-white">
                Таблица значений
            </h3>
            <FunctionTableImproved
                functionId={func.hash_function}
                points={func.points}
                isEditable={true}
                onYChange={onPointChange}
                canRemove={canRemove}
                canInsert={canInsert}
                onRemovePoint={onRemovePoint}
                onInsertPoint={onInsertPoint}
            />
        </div>
    </div>
);