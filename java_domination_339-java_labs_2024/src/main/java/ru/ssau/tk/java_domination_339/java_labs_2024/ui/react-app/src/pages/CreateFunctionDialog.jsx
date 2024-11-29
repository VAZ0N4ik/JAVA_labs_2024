export const CreateFunctionDialog = ({ isOpen, onClose, onTypeSelect }) => {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded-lg shadow-xl">
                <h3 className="text-lg font-medium mb-4">Выберите тип создания функции</h3>
                <div className="space-y-3">
                    <button
                        className="w-full btn btn-primary"
                        onClick={() => onTypeSelect('array')}
                    >
                        Создать из массива
                    </button>
                    <button
                        className="w-full btn btn-primary"
                        onClick={() => onTypeSelect('function')}
                    >
                        Создать из функции
                    </button>
                    <button
                        className="w-full btn btn-secondary"
                        onClick={onClose}
                    >
                        Отмена
                    </button>
                </div>
            </div>
        </div>
    );
};