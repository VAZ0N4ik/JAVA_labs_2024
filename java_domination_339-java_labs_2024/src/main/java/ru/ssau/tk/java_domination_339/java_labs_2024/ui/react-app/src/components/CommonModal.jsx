import React from 'react';
import { X } from 'lucide-react';

// Общий компонент для модальных окон
const CommonModal = ({ isOpen, onClose, title, children }) => {
    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal-content max-w-7xl mx-auto">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                        {title}
                    </h2>
                    <button
                        onClick={onClose}
                        className="text-gray-500 hover:text-gray-700 dark:text-gray-400
                                 dark:hover:text-gray-200 transition-colors"
                        aria-label="Закрыть"
                    >
                        <X className="w-6 h-6" />
                    </button>
                </div>
                {children}
            </div>
        </div>
    );
};

export default CommonModal;