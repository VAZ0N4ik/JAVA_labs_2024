import api from './api';

export const createArrayFunction = async (points) => {
    try {
        const token = localStorage.getItem('token');
        console.log('Token before request:', token);

        const requestData = {
            name: "ArrayTabulatedFunction",
            points: points,
            count_point: points.length,
            hash_function: null,
            created_at: new Date().toISOString(),
            update_at: new Date().toISOString()
        };

        console.log('Request data:', requestData);

        const response = await api.post('/api/functions', requestData);
        return response.data;
    } catch (error) {
        console.error('Function creation error:', {
            error: error,
            response: error.response,
            status: error.response?.status,
            data: error.response?.data
        });
        throw new Error(error.response?.data?.message || 'Ошибка при создании функции');
    }
};

export const createMathFunction = async (functionType, xFrom, xTo, pointCount) => {
    try {
        const points = Array.from({ length: pointCount }, (_, i) => {
            const x = parseFloat(xFrom) + (parseFloat(xTo) - parseFloat(xFrom)) * i / (pointCount - 1);
            return {
                x: x,
                y: 0 // !!!!!
            };
        });

        const requestData = {
            name: functionType,
            points: points,
            count_point: pointCount,
            hash_function: null,
            created_at: new Date().toISOString(),
            update_at: new Date().toISOString()
        };

        console.log('Sending request:', requestData);

        const response = await api.post('/api/functions', requestData);
        return response.data;
    } catch (error) {
        console.error('Error creating function:', error.response || error);
        throw new Error(error.response?.data?.message || 'Ошибка при создании функции');
    }
};