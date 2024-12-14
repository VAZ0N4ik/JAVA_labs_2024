import api from './api';

export const createArrayFunction = async (points) => {
    try {
        // Преобразуем массив точек в два массива x и y
        const xValues = points.map(p => p.x);
        const yValues = points.map(p => p.y);

        // Формируем URL с параметрами
        const url = '/api/function-creation/create-from-points?' +
            `x=${xValues.join(',')}&y=${yValues.join(',')}`;

        const response = await api.post(url);
        return response.data;
    } catch (error) {
        console.error('Function creation error:', error);
        throw new Error(error.response?.data?.message || 'Ошибка при создании функции');
    }
};

export const createMathFunction = async (functionName, xFrom, xTo, pointCount) => {
    try {
        console.log('Creating math function with params:', {
            functionName,
            xFrom,
            xTo,
            pointCount
        });

        // Формируем URL с параметрами
        const params = new URLSearchParams({
            name: functionName,
            from: xFrom.toString(),
            to: xTo.toString(),
            count: pointCount.toString()
        });

        const url = `/api/function-creation/create-from-math-function?${params.toString()}`;
        console.log('Request URL:', url);

        const response = await api.post(url);
        console.log('Server response:', response.data);
        return response.data;
    } catch (error) {
        console.error('Detailed error info:', {
            error: error,
            response: error.response,
            status: error.response?.status,
            data: error.response?.data
        });

        // Если это не ошибка авторизации, пробрасываем ее дальше для обработки
        if (error.response?.status !== 401 && error.response?.status !== 403) {
            throw new Error(error.response?.data?.message || 'Ошибка при создании функции');
        }
        throw error; // Пробрасываем ошибки авторизации дальше
    }
};