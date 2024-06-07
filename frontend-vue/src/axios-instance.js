//
// import axios from 'axios';
//
// const instance = axios.create({
//     baseURL: 'http://localhost:8080/' // Adjust the baseURL to include the correct port and path
// });
//
// export default instance;


import axios from "axios";

const instance = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
});

export default instance;