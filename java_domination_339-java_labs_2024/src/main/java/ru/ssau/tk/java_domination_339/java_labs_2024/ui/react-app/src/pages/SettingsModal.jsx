import React, { useState, useEffect } from 'react';
import api from '../services/api';
import { Alert, AlertTitle, AlertDescription } from "../components/ui/alert";

const SettingsModal = ({ isOpen, onClose }) => {
    const [selectedFactory, setSelectedFactory] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        // Загружаем текущие настройки при открытии модального окна
        const loadSettings = async () => {
            try {
                const response = await api.get('/api/settings/factory-type');
                const factoryType = response.data.factoryType === 'ARRAY_FACTORY' ? 'Массив' : 'Связный список';
                setSelectedFactory(factoryType);
            } catch (error) {
                setError('Ошибка при загрузке настроек');
            }
        };

        if (isOpen) {
            loadSettings();
        }
    }, [isOpen]);

    const handleSave = async () => {
        try {
            await api.post('/api/settings/factory-type', null, {
                params: { name: selectedFactory }
            });
            setSuccess(true);
            setTimeout(() => {
                setSuccess(false);
                onClose();
            }, 1500);
        } catch (error) {
            setError('Ошибка при сохранении настроек');
        }
    };

    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white rounded-lg p-6 w-full max-w-md">
                <h2 className="text-2xl font-bold mb-4">Настройки</h2>

                <div className="mb-4">
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Тип фабрики табулированных функций
                    </label>
                    <select
                        value={selectedFactory}
                        onChange={(e) => setSelectedFactory(e.target.value)}
                        className="select w-full"
                    >
                        <option value="Массив">Массив</option>
                        <option value="Связный список">Связный список</option>
                    </select>
                </div>

                {error && (
                    <Alert className="mb-4 bg-red-50 border-red-200">
                        <AlertTitle className="text-red-700">Ошибка</AlertTitle>
                        <AlertDescription className="text-red-600">
                            {error}
                        </AlertDescription>
                    </Alert>
                )}

                {success && (
                    <Alert className="mb-4 bg-green-50 border-green-200">
                        <AlertTitle className="text-green-700">Успех</AlertTitle>
                        <AlertDescription className="text-green-600">
                            Настройки успешно сохранены
                        </AlertDescription>
                    </Alert>
                )}

                <div className="flex justify-end gap-4">
                    <button
                        onClick={onClose}
                        className="btn btn-secondary"
                    >
                        Отмена
                    </button>
                    <button
                        onClick={handleSave}
                        className="btn btn-primary"
                    >
                        Сохранить
                    </button>
                </div>
            </div>
        </div>
    );
};

export default SettingsModal;