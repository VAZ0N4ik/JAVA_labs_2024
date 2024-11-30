import React, {useState, useEffect} from 'react';
import {Save, X} from 'lucide-react';
import api from '../services/api';
import {Alert, AlertTitle, AlertDescription} from "../components/ui/alert";

const SettingsModal = ({isOpen, onClose}) => {
    const [selectedFactory, setSelectedFactory] = useState('');
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const loadSettings = async () => {
            if (!isOpen) return;

            setLoading(true);
            try {
                const response = await api.get('/api/settings/factory-type');
                const factoryType = response.data.factoryType === 'ARRAY_FACTORY' ? 'Массив' : 'Связный список';
                setSelectedFactory(factoryType);
            } catch (error) {
                setError('Ошибка при загрузке настроек');
            } finally {
                setLoading(false);
            }
        };

        loadSettings();
    }, [isOpen]);

    const handleSave = async () => {
        setLoading(true);
        try {
            await api.post('/api/settings/factory-type', null, {
                params: {name: selectedFactory}
            });
            setSuccess(true);
            setTimeout(() => {
                setSuccess(false);
                onClose();
            }, 1500);
        } catch (error) {
            setError('Ошибка при сохранении настроек');
        } finally {
            setLoading(false);
        }
    };

    if (!isOpen) return null;

    return (
        <div className="modal">
            <div className="modal-content">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                        Настройки
                    </h2>
                    <button
                        onClick={onClose}
                        className="text-gray-500 hover:text-gray-700 dark:text-gray-400
                                 dark:hover:text-gray-200 transition-colors"
                    >
                        <X className="w-6 h-6"/>
                    </button>
                </div>

                <div className="mb-6">
                    <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                        Тип фабрики табулированных функций
                    </label>
                    <select
                        value={selectedFactory}
                        onChange={(e) => setSelectedFactory(e.target.value)}
                        className="input"
                        disabled={loading}
                    >
                        <option value="Массив">Массив</option>
                        <option value="Связный список">Связный список</option>
                    </select>
                </div>

                {error && (
                    <Alert className="mb-4 bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                        <AlertTitle className="text-red-700 dark:text-red-400">
                            Ошибка
                        </AlertTitle>
                        <AlertDescription className="text-red-600 dark:text-red-300">
                            {error}
                        </AlertDescription>
                    </Alert>
                )}

                {success && (
                    <Alert className="mb-4 bg-green-50 border-green-200 dark:bg-green-900/30 dark:border-green-800">
                        <AlertTitle className="text-green-700 dark:text-green-400">
                            Успех
                        </AlertTitle>
                        <AlertDescription className="text-green-600 dark:text-green-300">
                            Настройки успешно сохранены
                        </AlertDescription>
                    </Alert>
                )}

                <div className="flex justify-end gap-4">
                    <button
                        onClick={onClose}
                        className="btn btn-secondary"
                        disabled={loading}
                    >
                        Отмена
                    </button>
                    <button
                        onClick={handleSave}
                        className="btn btn-primary flex items-center gap-2"
                        disabled={loading}
                    >
                        <Save className="w-4 h-4"/>
                        {loading ? 'Сохранение...' : 'Сохранить'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default SettingsModal;