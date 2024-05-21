import { defineStore } from 'pinia'
import axios from '../axios-auth';
import axiosInstance from '../axios-instance';
import jwtDecode from "jwt-decode";


export const useStore = defineStore('counter', {
  state: () => ({
    token: '',
    user: {
      id: 0,
      email: ''
    },
      loginType: 0,
  }),
  getters: {
    isLoggedIn: (state) => state.token != '',
  },
  actions: {

      login(email, password, isAtm) {
        return new Promise((resolve, reject) => {

            axiosInstance.post("/login", {
            email: email,
            password: password,
          })
          .then((res) => {

            axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data.token;

            this.token = res.data.token;

            let decoded = jwtDecode(this.token);

            this.user.email = decoded.sub;
            this.user.id = decoded.userId;

            let loginType = isAtm ? '2' : '1';

            this.loginType = loginType


            localStorage.setItem('token', res.data.token);
            localStorage.setItem('user', JSON.stringify(this.user));


            localStorage.setItem('loginType', loginType);



            resolve();
        })

        .catch((error) => reject(error.response));
        });
    },
      logout() {

          this.token = '';
          this.user = {
              id: 0,
              email: ''
          };

          localStorage.removeItem('token');
          localStorage.removeItem('user');
          localStorage.removeItem('loginType');

          delete axios.defaults.headers.common['Authorization'];
      },
    autoLogin() {
        let token = localStorage.getItem('token');
        let user = localStorage.getItem('user');
        let loginType = localStorage.getItem('loginType');


        if (token) {
          this.token = token;

          this.user = JSON.parse(user);
          this.loginType = loginType;
          axios.defaults.headers.common['Authorization'] = 'Bearer ' + token;

        }

      },

  },

})