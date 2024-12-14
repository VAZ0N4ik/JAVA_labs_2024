// frontend/src/services/auth.js
import axios from 'axios';

const API_URL = '/auth/';

class AuthService {
    async login(username, password) {
        const response = await axios.post(API_URL + 'sign-in', {
            username,
            password
        });
        if (response.data.token) {
            localStorage.setItem('token', response.data.token);
        }
        return response.data;
    }

    logout() {
        localStorage.removeItem('token');
    }

    register(username, password) {
        return axios.post(API_URL + 'sign-up', {
            username,
            password
        });
    }
}

export default new AuthService();