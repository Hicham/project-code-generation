import { defineStore } from 'pinia';
import axios from '../axios-auth';
import axiosInstance from '../axios-instance';
import jwtDecode from "jwt-decode";

export const useStore = defineStore('counter', {
    state: () => ({
        token: '',
        user: {
            id: 0,
            email: '',
            roles: null,
            firstName: '',
            lastName: '',
            isApproved: ''
        },
        loginType: 0
    }),
    getters: {
        isLoggedIn: (state) => state.token != '',
    },
    actions: {
        login(email, password, isAtm) {
            return new Promise((resolve, reject) => {
                axiosInstance.post("/login", {
                    email: email,
                    password: password
                })
                    .then((res) => {
                        axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data.token;
                        this.token = res.data.token;

                        let decoded = jwtDecode(this.token);
                        this.user.email = decoded.sub;
                        this.user.id = decoded.userId;
                        this.user.roles = decoded.auth;

                        let loginType = isAtm ? '2' : '1';
                        this.loginType = loginType;

                        localStorage.setItem('token', res.data.token);
                        localStorage.setItem('loginType', loginType);


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
                    this.user.id = userData.userId;
                    this.user.firstName = userData.firstName;
                    this.user.lastName = userData.lastName;
                    this.user.isApproved = userData.approved;

                    // Optionally store these details in localStorage
                    localStorage.setItem('user', JSON.stringify(this.user));
                })
                .catch((error) => {
                    console.error('Failed to fetch user details:', error);
                    throw error;
                });
        },
        logout() {
            this.token = '';
            this.user = {
                id: 0,
                email: '',
                roles: null,
                firstName: '',
                lastName: '',
                isApproved: ''
            };
            this.loginType = 0;

            localStorage.removeItem('token');
            localStorage.removeItem('user');
            localStorage.removeItem('loginType');

            delete axios.defaults.headers.common['Authorization'];
        },
        autoLogin() {
            let token = localStorage.getItem('token');
            let user = localStorage.getItem('user');
            let loginType = localStorage.getItem('loginType');

            if (token && user) {
                this.token = token;
                this.user = JSON.parse(user);
                this.loginType = loginType;
                axios.defaults.headers.common['Authorization'] = 'Bearer ' + token;
            } else {
                // Clear localStorage if any required data is missing
                this.logout();
            }
        }
    }
});
