import React from 'react';

const FunctionControls = ({
                              onCreateClick,
                              onFileUpload,
                              loading
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
    };

    return (
        <div className="space-y-3">
            <button
                className="btn btn-primary w-full"
                onClick={onCreateClick}
                disabled={loading}
            >
                Создать функцию
            </button>

            <label className="block">
                <span className="sr-only">Загрузить функцию</span>
                <input
                    type="file"
                    onChange={handleFileUpload}
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
    );
};

export default FunctionControls;