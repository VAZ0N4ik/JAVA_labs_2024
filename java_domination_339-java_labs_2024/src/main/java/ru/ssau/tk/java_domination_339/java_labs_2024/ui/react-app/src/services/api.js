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
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => response,
    (error) => {
        // Проверяем, есть ли токен и является ли запрос авторизационным
        const isAuthRequest = error.config.url.includes('/auth/');
        const hasToken = localStorage.getItem('token');

        // Перезагружаем страницу только если это не запрос авторизации и есть токен
        if ((error.response?.status === 401 || error.response?.status === 403) && !isAuthRequest && hasToken) {
            console.error('Session expired');
            localStorage.removeItem('token');
            window.location.reload();
        }
        return Promise.reject(error);
    }
);

export default api;