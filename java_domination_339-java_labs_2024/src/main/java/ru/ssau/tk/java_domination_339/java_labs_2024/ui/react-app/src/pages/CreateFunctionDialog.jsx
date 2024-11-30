import React from 'react';
import {Grid2X2, LineChart} from 'lucide-react';

export const CreateFunctionDialog = ({isOpen, onClose, onTypeSelect}) => {
    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal-content max-w-md">
                <h3 className="text-xl font-semibold mb-6 text-gray-900 dark:text-white">
                    Выберите тип создания функции
                </h3>
                <div className="space-y-4">
                    <button
                        className="w-full btn btn-secondary flex items-center justify-center gap-3
                                 hover:bg-blue-50 dark:hover:bg-blue-900/30
                                 transition-colors duration-200"
                        onClick={() => onTypeSelect('array')}
                    >
                        <Grid2X2 className="w-5 h-5"/>
                        Создать из массива
                    </button>
                    <button
                        className="w-full btn btn-secondary flex items-center justify-center gap-3
                                 hover:bg-blue-50 dark:hover:bg-blue-900/30
                                 transition-colors duration-200"
                        onClick={() => onTypeSelect('function')}
                    >
                        <LineChart className="w-5 h-5"/>
                        Создать из функции
                    </button>
                    <button
                        className="w-full btn btn-primary"
                        onClick={onClose}
                    >
                        Отмена
                    </button>
                </div>
            </div>
        </div>
    );
};