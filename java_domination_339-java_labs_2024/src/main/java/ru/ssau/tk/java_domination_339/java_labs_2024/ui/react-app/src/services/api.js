import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json'
    }
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        console.log('Current token:', token);

        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }

        console.log('Request headers:', config.headers);
        console.log('Full request config:', {
            url: config.url,
            method: config.method,
            headers: config.headers,
            data: config.data
        });
        return config;
    },
    (error) => {
        console.error('Request interceptor error:', error);
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => response,
    (error) => {
        console.error('API Error:', {
            status: error.response?.status,
            data: error.response?.data,
            headers: error.response?.headers,
            config: error.config
        });

        if (error.response?.status === 401) {
            console.error('Unauthorized access');
            localStorage.removeItem('token');
        }
        if (error.response?.status === 403) {
            console.error('Forbidden access');
        }
        return Promise.reject(error);
    }
);

export default api;