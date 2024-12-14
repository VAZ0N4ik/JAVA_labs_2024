import React from 'react';
import {X} from 'lucide-react';

const CommonModal = ({isOpen, onClose, title, children}) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 overflow-hidden">
            <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-4">
                    <div className="modal-content max-w-7xl mx-auto max-h-[90vh] overflow-y-auto">
                        <div
                            className="flex justify-between items-center mb-6 sticky top-0 bg-white dark:bg-gray-800 py-4 z-10">
                            <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                                {title}
                            </h2>
                            <button
                                onClick={onClose}
                                className="text-gray-500 hover:text-gray-700 dark:text-gray-400
                                     dark:hover:text-gray-200 transition-colors"
                            >
                                <X className="w-6 h-6"/>
                            </button>
                        </div>
                        {children}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CommonModal;