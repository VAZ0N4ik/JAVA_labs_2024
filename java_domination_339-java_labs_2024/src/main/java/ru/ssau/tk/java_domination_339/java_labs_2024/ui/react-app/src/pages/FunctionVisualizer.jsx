import React, {useState} from 'react';
import {LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer} from 'recharts';
import {Calculator, AlertCircle} from 'lucide-react';
import {Alert, AlertDescription} from '../components/ui/alert';

const FunctionVisualizer = ({
                                functionId,
                                points = [],
                                onPointChange,
                                onApplyCalculate,
                                canInsert
                            }) => {
    const [applyX, setApplyX] = useState('');
    const [calculatedY, setCalculatedY] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleCalculate = async () => {
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

            const result = await onApplyCalculate(x);
            setCalculatedY(result.value);
            setApplyX('');
            setError(null);
        } catch (err) {
            setError(err.response?.data?.message || 'Ошибка при вычислении значения');
            setCalculatedY(null);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="space-y-4">
            <div
                className="h-64 w-full bg-white dark:bg-gray-800 rounded-lg p-4 border border-gray-200 dark:border-gray-700">
                <ResponsiveContainer>
                    <LineChart
                        data={points.map((point, index) => ({x: point.x, y: point.y, index}))}
                        margin={{top: 10, right: 10, left: 10, bottom: 10}}
                    >
                        <CartesianGrid
                            strokeDasharray="3 3"
                            stroke="#374151"
                            opacity={0.2}
                        />
                        <XAxis
                            dataKey="x"
                            type="number"
                            domain={['dataMin', 'dataMax']}
                            tickFormatter={val => val.toFixed(2)}
                            stroke="#6B7280"
                            fontSize={12}
                        />
                        <YAxis
                            type="number"
                            domain={['auto', 'auto']}
                            tickFormatter={val => val.toFixed(2)}
                            stroke="#6B7280"
                            fontSize={12}
                        />
                        <Tooltip
                            formatter={(value, name) => [value.toFixed(4), name === 'y' ? 'Y' : 'X']}
                            labelFormatter={(label) => `X: ${parseFloat(label).toFixed(4)}`}
                            contentStyle={{
                                backgroundColor: 'rgb(31, 41, 55)',
                                border: 'none',
                                borderRadius: '0.375rem',
                                color: '#F3F4F6'
                            }}
                        />
                        <Line
                            type="monotone"
                            dataKey="y"
                            stroke="#3B82F6"
                            strokeWidth={2}
                            dot={{
                                r: 4,
                                fill: '#3B82F6',
                                stroke: '#2563EB'
                            }}
                            activeDot={{
                                r: 6,
                                fill: '#2563EB',
                                stroke: '#1D4ED8'
                            }}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>

            <div className="flex items-end gap-2">
                <div className="flex-1">
                    <label className="block text-sm font-medium mb-1 text-gray-700 dark:text-gray-300">
                        Значение X:
                    </label>
                    <input
                        type="number"
                        value={applyX}
                        onChange={(e) => setApplyX(e.target.value)}
                        className="input"
                        placeholder="Введите X для вычисления"
                        step="any"
                        disabled={loading}
                    />
                </div>
                <button
                    onClick={handleCalculate}
                    className="btn btn-primary flex items-center gap-2"
                    disabled={loading || !applyX}
                >
                    <Calculator className="w-4 h-4"/>
                    {loading ? 'Вычисление...' : 'Вычислить'}
                </button>
                {calculatedY !== null && (
                    <div className="text-sm bg-blue-50 dark:bg-blue-900/30 px-3 py-2 rounded-md">
                        <span className="font-medium mr-1 text-blue-700 dark:text-blue-300">Y =</span>
                        <span className="text-blue-600 dark:text-blue-200">{calculatedY.toFixed(4)}</span>
                    </div>
                )}
            </div>

            {error && (
                <Alert className="bg-red-50 border-red-200 dark:bg-red-900/30 dark:border-red-800">
                    <AlertDescription className="text-red-600 dark:text-red-300 flex items-center gap-2">
                        <AlertCircle className="w-4 h-4"/>
                        {error}
                    </AlertDescription>
                </Alert>
            )}
        </div>
    );
};

export default FunctionVisualizer;