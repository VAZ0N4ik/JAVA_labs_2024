import React, { useState, useEffect } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { Alert, AlertTitle, AlertDescription } from '../components/ui/alert';

const FunctionVisualizer = ({
    functionId,
    points = [],
    isEditable = true,
    onPointChange,
    onApplyCalculate,
    onInsertPoint,
    onRemovePoint,
    canInsert = false,
    canRemove = false
}) => {
    const [removeX, setRemoveX] = useState('');
    const [chartData, setChartData] = useState([]);
    const [error, setError] = useState(null);
    const [selectedPoint, setSelectedPoint] = useState(null);
    const [loading, setLoading] = useState(false);

    // Состояния для вычисления и вставки
    const [applyX, setApplyX] = useState('');
    const [calculatedY, setCalculatedY] = useState(null);

    // Состояния для произвольной вставки
    const [insertX, setInsertX] = useState('');
    const [insertY, setInsertY] = useState('');

    useEffect(() => {
        if (points?.length > 0) {
            const formattedData = points
                .map((point, index) => ({
                    x: point.x,
                    y: point.y,
                    index
                }))
                .sort((a, b) => a.x - b.x);
            setChartData(formattedData);
        }
    }, [points, functionId]);

    const handlePointClick = (data) => {
        if (isEditable) {
            setSelectedPoint(data);
        }
    };

    const handleYChange = async (e) => {
        if (!selectedPoint) return;

        const newY = parseFloat(e.target.value);
        if (isNaN(newY)) {
            setError('Значение Y должно быть числом');
            return;
        }

        setLoading(true);
        try {
            await onPointChange?.(selectedPoint.index, newY);
            setSelectedPoint(null);
            setError(null);
        } catch (err) {
            console.error('Point change error:', err);
            setError(err.response?.data?.message || 'Ошибка при изменении точки');
        } finally {
            setLoading(false);
        }
    };

    const handleApplyAndInsert = async () => {
        if (!applyX.trim()) {
            setError('Введите значение X');
            return;
        }

        setLoading(true);
        try {
            const x = parseFloat(applyX);
            if (isNaN(x)) {
                setError('Значение X должно быть числом');
                return;
            }

            const result = await onApplyCalculate?.(x);
            setCalculatedY(result.value);
            setApplyX('');
            setError(null);
        } catch (err) {
            console.error('Apply and insert error:', err);
            setError(err.response?.data?.message || 'Ошибка при вычислении значения');
            setCalculatedY(null);
        } finally {
            setLoading(false);
        }
    };

    const handleManualInsert = async () => {
        if (!insertX.trim() || !insertY.trim()) {
            setError('Заполните оба поля');
            return;
        }

        setLoading(true);
        try {
            const x = parseFloat(insertX);
            const y = parseFloat(insertY);

            if (isNaN(x) || isNaN(y)) {
                setError('Значения должны быть числами');
                return;
            }

            await onInsertPoint?.(x, y);
            setInsertX('');
            setInsertY('');
            setError(null);
        } catch (err) {
            console.error('Manual insert error:', err);
            setError(err.response?.data?.message || 'Ошибка при вставке точки');
        } finally {
            setLoading(false);
        }
    };

    const handleRemoveByX = async () => {
        if (!removeX.trim()) {
            setError('Введите значение X');
            return;
        }

        setLoading(true);
        try {
            const x = parseFloat(removeX);
            if (isNaN(x)) {
                setError('Значение X должно быть числом');
                return;
            }

            await onRemovePoint?.(x);
            setRemoveX('');
            setError(null);
        } catch (err) {
            console.error('Remove point error:', err);
            setError(err.response?.data?.message || 'Ошибка при удалении точки');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="space-y-4">
            <div className="h-64 w-full">
                <ResponsiveContainer>
                    <LineChart
                        data={chartData}
                        margin={{ top: 10, right: 10, left: 10, bottom: 10 }}
                    >
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis
                            dataKey="x"
                            type="number"
                            domain={['dataMin', 'dataMax']}
                            tickFormatter={val => val.toFixed(2)}
                        />
                        <YAxis
                            type="number"
                            domain={['auto', 'auto']}
                            tickFormatter={val => val.toFixed(2)}
                        />
                        <Tooltip
                            formatter={(value, name) => [value.toFixed(4), name === 'y' ? 'Y' : 'X']}
                            labelFormatter={(label) => `X: ${parseFloat(label).toFixed(4)}`}
                        />
                        <Line
                            type="monotone"
                            dataKey="y"
                            stroke="#2563eb"
                            dot={{
                                r: 4,
                                onClick: handlePointClick,
                                style: { cursor: isEditable ? 'pointer' : 'default' }
                            }}
                            activeDot={{ r: 6 }}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>

            {isEditable && (
                <div className="space-y-4">
                    <div className="flex items-end gap-2">
                        <div className="flex-1">
                            <label className="block text-sm font-medium mb-1">
                                Значение X:
                            </label>
                            <input
                                type="number"
                                value={applyX}
                                onChange={(e) => setApplyX(e.target.value)}
                                className="input w-full"
                                placeholder="Введите X для вычисления"
                                step="any"
                                disabled={loading}
                            />
                        </div>
                        <button
                            onClick={handleApplyAndInsert}
                            className="btn btn-primary whitespace-nowrap"
                            disabled={loading || !applyX || !canInsert}
                        >
                            {loading ? 'Обработка...' : 'Вычислить и вставить'}
                        </button>
                        {calculatedY !== null && (
                            <div className="text-sm">
                                <span className="font-medium mr-1">Y =</span>
                                <span className="text-blue-600">{calculatedY.toFixed(4)}</span>
                            </div>
                        )}
                    </div>

                    {canInsert && (
                        <div className="flex gap-2">
                            <input
                                type="number"
                                value={insertX}
                                onChange={(e) => setInsertX(e.target.value)}
                                className="input flex-1"
                                placeholder="X"
                                step="any"
                                disabled={loading}
                            />
                            <input
                                type="number"
                                value={insertY}
                                onChange={(e) => setInsertY(e.target.value)}
                                className="input flex-1"
                                placeholder="Y"
                                step="any"
                                disabled={loading}
                            />
                            <button
                                onClick={handleManualInsert}
                                className="btn btn-secondary whitespace-nowrap"
                                disabled={loading || !insertX || !insertY}
                            >
                                {loading ? 'Вставка...' : 'Вставить точку'}
                            </button>
                        </div>
                    )}

                    {canRemove && (
                        <div className="flex gap-2">
                            <input
                                type="number"
                                value={removeX}
                                onChange={(e) => setRemoveX(e.target.value)}
                                className="input flex-1"
                                placeholder="X точки для удаления"
                                step="any"
                                disabled={loading}
                            />
                            <button
                                onClick={handleRemoveByX}
                                className="btn btn-error whitespace-nowrap"
                                disabled={loading || !removeX}
                            >
                                {loading ? 'Удаление...' : 'Удалить точку'}
                            </button>
                        </div>
                    )}

                </div>
            )}

            {selectedPoint && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white p-6 rounded-lg shadow-xl">
                        <h3 className="text-lg font-medium mb-4">
                            Изменить точку
                        </h3>
                        <div className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium mb-1">
                                    X:
                                </label>
                                <input
                                    type="number"
                                    value={selectedPoint.x}
                                    disabled
                                    className="input w-full"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium mb-1">
                                    Y:
                                </label>
                                <input
                                    type="number"
                                    defaultValue={selectedPoint.y}
                                    onChange={handleYChange}
                                    className="input w-full"
                                    step="any"
                                    disabled={loading}
                                />
                            </div>
                            {canRemove && (
                                <button
                                    onClick={() => onRemovePoint?.(selectedPoint.x)}
                                    className="btn btn-error w-full"
                                    disabled={loading}
                                >
                                    {loading ? 'Удаление...' : 'Удалить точку'}
                                </button>
                            )}
                            <button
                                onClick={() => setSelectedPoint(null)}
                                className="btn btn-secondary w-full"
                                disabled={loading}
                            >
                                Отмена
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {error && (
                <Alert className="bg-red-50 border-red-200">
                    <AlertTitle className="text-red-700">
                        Ошибка
                    </AlertTitle>
                    <AlertDescription className="text-red-600">
                        {error}
                    </AlertDescription>
                </Alert>
            )}
        </div>
    );
};

export default FunctionVisualizer;