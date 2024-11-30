import React from 'react';
import {Download, File, FileJson, FileCode} from 'lucide-react';

const SaveDropdown = ({onSave, disabled}) => (
    <div className="dropdown">
        <button
            className="btn btn-secondary w-full flex items-center justify-center gap-2"
            disabled={disabled}
        >
            <Download className="w-4 h-4"/>
            Сохранить
        </button>
        <div className="dropdown-content">
            <button
                onClick={() => onSave('binary')}
                className="w-full px-4 py-2 text-left flex items-center gap-2
                         text-gray-700 dark:text-gray-200
                         hover:bg-gray-100 dark:hover:bg-gray-700
                         transition-colors duration-150"
            >
                <File className="w-4 h-4"/>
                Бинарный файл (.txt)
            </button>
            <button
                onClick={() => onSave('json')}
                className="w-full px-4 py-2 text-left flex items-center gap-2
                         text-gray-700 dark:text-gray-200
                         hover:bg-gray-100 dark:hover:bg-gray-700
                         transition-colors duration-150"
            >
                <FileJson className="w-4 h-4"/>
                JSON файл (.json)
            </button>
            <button
                onClick={() => onSave('xml')}
                className="w-full px-4 py-2 text-left flex items-center gap-2
                         text-gray-700 dark:text-gray-200
                         hover:bg-gray-100 dark:hover:bg-gray-700
                         transition-colors duration-150"
            >
                <FileCode className="w-4 h-4"/>
                XML файл (.xml)
            </button>
        </div>
    </div>
);

export default SaveDropdown;