import React from 'react';
import {Upload, Trash2} from 'lucide-react';

const FunctionControls = ({
                              onCreateClick,
                              onFileUpload,
                              onClearFunction,
                              loading,
                              hasFunction = false
                          }) => {
    const handleFileUpload = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append('file', file);

        try {
            await onFileUpload(formData, file.name.split('.').pop().toLowerCase());
        } catch (error) {
            console.error('Upload error:', error);
        }

        // Очищаем input для возможности повторной загрузки того же файла
        e.target.value = '';
    };

    return (
        <div className="space-y-3">
            <div className="flex gap-2">
                <button
                    className="btn btn-primary flex-1"
                    onClick={onCreateClick}
                    disabled={loading}
                >
                    Создать функцию
                </button>
                {hasFunction && (
                    <button
                        className="btn btn-error"
                        onClick={onClearFunction}
                        disabled={loading}
                    >
                        <Trash2 className="w-4 h-4"/>
                    </button>
                )}
            </div>

            <label className="flex items-center gap-2 cursor-pointer">
                <div className="flex-1 relative">
                    <input
                        type="file"
                        onChange={handleFileUpload}
                        className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
                        accept=".txt,.json,.xml"
                        disabled={loading}
                    />
                    <div className="btn btn-secondary w-full flex items-center justify-center gap-2">
                        <Upload className="w-4 h-4"/>
                        Загрузить из файла
                    </div>
                </div>
            </label>

            <div className="text-xs text-gray-500 dark:text-gray-400">
                Поддерживаемые форматы: TXT, JSON, XML
            </div>
        </div>
    );
};

export default FunctionControls;