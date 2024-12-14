import React, {useState, useEffect} from 'react';
import {Plus, Trash2, AlertCircle} from 'lucide-react';
import api from '../services/api';
import {Alert, AlertDescription} from "../components/ui/alert";

const FunctionTableImproved = ({
                                   functionId,
                                   points = [],
                                   isEditable = true,
                                   onYChange,
                                   canInsert = false,
                                   canRemove = false,
                                   onRemovePoint,
                                   onInsertPoint
                               }) => {
    const [tableData, setTableData] = useState([]);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);
    const [modifiedData, setModifiedData] = useState({});
    const [insertX, setInsertX] = useState('');
    const [insertY, setInsertY] = useState('');

    useEffect(() => {
        const loadPoints = async () => {
            if (!functionId || !points.length) {
                setLoading(false);
                return;
            }

            try {
                const newPoints = [];
                for (let i = 0; i < points.length; i++) {
                    const [xResponse, yResponse] = await Promise.all([
                        api.post('/api/tabulated-function-operations/getX', null, {
                            params: {functionId, index: i}
                        }),
                        api.post('/api/tabulated-function-operations/getY', null, {
                            params: {functionId, index: i}
                        })
                    ]);

                    newPoints.push({
                        index: i,
                        x: Number(xResponse.data),
                        y: Number(yResponse.data)
                    });
                }
                setTableData(newPoints.sort((a, b) => a.x - b.x));
                setError(null);
            } catch (error) {
                console.error('Error loading points:', error);
                setError('Ошибка при загрузке точек');
            } finally {
                setLoading(false);
            }
        };

        loadPoints();
    }, [functionId, points]);

    const handleYChange = (index, newValue) => {
        if (!isEditable) return;

        setModifiedData(prev => ({
            ...prev,
            [index]: newValue
        }));
    };

    const saveChanges = async () => {
        try {
            const updatePromises = Object.entries(modifiedData).map(([index, y]) =>
                api.post('/api/tabulated-function-operations/setY', null, {
                    params: {
                        functionId,
                        index: parseInt(index),
                        y
                    }
                })
            );

            const results = await Promise.all(updatePromises);
            const finalResult = results[results.length - 1];

            if (finalResult?.data) {
                onYChange && onYChange(finalResult.data);
                setModifiedData({});
                setError(null);
            }
        } catch (error) {
            setError('Ошибка при обновлении значений Y');
        }
    };

    const handleRemove = async (x) => {
        if (!canRemove || !onRemovePoint) return;

        if (points.length <= 2) {
            setError('Невозможно удалить точку: функция должна содержать минимум 2 точки');
            return;
        }

        try {
            await onRemovePoint(x);
            setError(null);
        } catch (error) {
            setError(error.message || 'Ошибка при удалении точки');
        }
    };

    const handleInsert = async () => {
        if (!canInsert || !onInsertPoint) return;

        if (!insertX.trim() || !insertY.trim()) {
            setError('Заполните оба поля');
            return;
        }

        try {
            const x = parseFloat(insertX);
            const y = parseFloat(insertY);

            if (isNaN(x) || isNaN(y)) {
                setError('Значения должны быть числами');
                return;
            }

            await onInsertPoint(x, y);
            setInsertX('');
            setInsertY('');
            setError(null);
        } catch (error) {
            setError(error.message || 'Ошибка при вставке точки');
        }
    };

    if (loading) {
        return (
            <div className="text-center p-4 text-gray-600 dark:text-gray-300">
                Загрузка...
            </div>
        );
    }

    if (!tableData.length) {
        return (
            <div className="text-center p-4 text-gray-600 dark:text-gray-300">
                Нет данных
            </div>
        );
    }

    return (
        <div className="mt-4">
            {canInsert && isEditable && (
                <div className="mb-4 flex gap-2">
                    <input
                        type="number"
                        value={insertX}
                        onChange={(e) => setInsertX(e.target.value)}
                        className="input flex-1"
                        placeholder="X"
                        step="any"
                    />
                    <input
                        type="number"
                        value={insertY}
                        onChange={(e) => setInsertY(e.target.value)}
                        className="input flex-1"
                        placeholder="Y"
                        step="any"
                    />
                    <button
                        onClick={handleInsert}
                        className="btn btn-secondary flex items-center gap-2"
                        disabled={!insertX || !insertY}
                    >
                        <Plus className="w-4 h-4"/>
                        Вставить точку
                    </button>
                </div>
            )}

            <div className={`${tableData.length > 10 ? 'max-h-[400px] overflow-y-auto' : ''}`}>
                <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700">
                    <thead className="bg-gray-50 dark:bg-gray-800">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                            X
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                            Y
                        </th>
                        {canRemove && isEditable && (
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
                                Действия
                            </th>
                        )}
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200 dark:bg-gray-900 dark:divide-gray-700">
                    {tableData.map((point) => (
                        <tr key={point.index} className="hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors">
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 dark:text-gray-100">
                                {point.x}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm">
                                {isEditable ? (
                                    <input
                                        type="number"
                                        value={modifiedData[point.index] ?? point.y}
                                        onChange={(e) => handleYChange(point.index, parseFloat(e.target.value))}
                                        className="input w-full"
                                        step="any"
                                    />
                                ) : (
                                    <span className="text-gray-900 dark:text-gray-100">{point.y}</span>
                                )}
                            </td>
                            {canRemove && isEditable && (
                                <td className="px-6 py-4 whitespace-nowrap text-sm">
                                    <button
                                        onClick={() => handleRemove(point.x)}
                                        className="text-red-600 hover:text-red-700 dark:text-red-500
                                                     dark:hover:text-red-400 transition-colors flex items-center gap-1"
                                        title="Удалить точку"
                                    >
                                        <Trash2 className="w-4 h-4"/>
                                        Удалить
                                    </button>
                                </td>
                            )}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {isEditable && Object.keys(modifiedData).length > 0 && (
                <button
                    onClick={saveChanges}
                    className="mt-4 btn btn-primary w-full"
                >
                    Сохранить изменения
                </button>
            )}

            {error && (
                <Alert className="mt-2 bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                    <AlertDescription className="text-red-600 dark:text-red-300 flex items-center gap-2">
                        <AlertCircle className="w-4 h-4"/>
                        {error}
                    </AlertDescription>
                </Alert>
            )}
        </div>
    );
};

export default FunctionTableImproved;