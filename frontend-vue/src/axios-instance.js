
import axios from 'axios';

const instance = axios.create({
    baseURL: 'http://localhost:8080/api/' // Adjust the baseURL to include the correct port and path
});

export default instance;
