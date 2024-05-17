import { defineStore } from 'pinia'
import axios from '../axios-auth';
import axiosInstance from '../axios-instance';

export const useStore = defineStore('counter', {
    state: () => ({
        token: '',
        user: '',  // This will hold the email of the logged-in user
        userId: '',
        firstName: '',
        lastName: '',
        isApproved: ''
    }),
    getters: {
        isLoggedIn: (state) => state.token != '',
    },
    actions: {
        decodeJwt(token) {
            return JSON.parse(atob(token.split('.')[1]));
        },
        login(email, password) {
            return new Promise((resolve, reject) => {
                axiosInstance.post("/login", {
                    email: email,
                    password: password,
                })
                    .then((res) => {
                        axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data.token;
                        this.token = res.data.token;
                        this.user = email;

                        localStorage.setItem('token', res.data.token);
                        localStorage.setItem('user', email);

                        // Fetch user details using email
                        this.fetchUserDetails(email).then(() => {
                            resolve();
                        }).catch((error) => {
                            reject(error);
                        });
                    })
                    .catch((error) => reject(error.response));
            });
        },
        fetchUserDetails(email) {
            return axiosInstance.get(`/api/users/${email}`)
                .then((res) => {
                    const userData = res.data;

                    this.userId = userData.userId;
                    this.firstName = userData.firstName;
                    this.lastName = userData.lastName;
                    this.isApproved = userData.approved;

                    // Optionally store these details in localStorage
                    localStorage.setItem('userId', userData.userId);
                    localStorage.setItem('firstName', userData.firstName);
                    localStorage.setItem('lastName', userData.lastName);
                    localStorage.setItem('isApproved', userData.approved);


                })
                .catch((error) => {
                    console.error('Failed to fetch user details:', error);
                    throw error;
                });
        },
        logout() {
            this.token = '';
            this.user = '';
            this.userId = '';
            this.firstName = '';
            this.lastName = '';
            this.isApproved = '';

            localStorage.removeItem('token');
            localStorage.removeItem('user');
            localStorage.removeItem('userId');
            localStorage.removeItem('firstName');
            localStorage.removeItem('lastName');
            localStorage.removeItem('isApproved');

            delete axios.defaults.headers.common['Authorization'];
        },
        autoLogin() {
            let token = localStorage.getItem('token');
            let user = localStorage.getItem('user');
            let userId = localStorage.getItem('userId');
            let firstName = localStorage.getItem('firstName');
            let lastName = localStorage.getItem('lastName');
            let isApproved = localStorage.getItem('isApproved');

            if (token && user && userId && firstName && lastName && isApproved) {
                this.token = token;
                this.user = user;
                this.userId = userId;
                this.firstName = firstName;
                this.lastName = lastName;
                this.isApproved = isApproved;

                axios.defaults.headers.common['Authorization'] = 'Bearer ' + token;
            } else {
                // Clear localStorage if any required data is missing
                localStorage.removeItem('token');
                localStorage.removeItem('user');
                localStorage.removeItem('userId');
                localStorage.removeItem('firstName');
                localStorage.removeItem('lastName');
                localStorage.removeItem('isApproved');
            }
        }

    },
});
