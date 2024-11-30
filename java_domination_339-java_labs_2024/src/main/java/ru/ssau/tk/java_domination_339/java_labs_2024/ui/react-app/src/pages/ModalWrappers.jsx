import React from 'react';
import { AlertCircle } from 'lucide-react';
import CommonModal from '../components/CommonModal';
import FunctionControls from './FunctionControls';
import FunctionTableImproved from './FunctionTableImproved';
import FunctionVisualizer from './FunctionVisualizer';
import SaveDropdown from './SaveDropdown';
import { Alert, AlertDescription } from "../components/ui/alert";

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
                    <AlertCircle className="w-4 h-4" />
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
                                    onCreateClick,
                                    onFileUpload,
                                    onSave,
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

        <FunctionControls
            onCreateClick={onCreateClick}
            onFileUpload={onFileUpload}
            loading={loading}
        />

        {func && (
            <>
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
                <SaveDropdown
                    onSave={onSave}
                    disabled={loading}
                />
            </>
        )}

        {error && (
            <Alert className="bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                <AlertDescription className="text-red-600 dark:text-red-300 flex items-center gap-2">
                    <AlertCircle className="w-4 h-4" />
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