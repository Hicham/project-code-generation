import { defineStore } from 'pinia'
import axios from '../axios-auth';
import axiosInstance from '../axios-instance';


export const useStore = defineStore('counter', {
  state: () => ({
    token: '',
    user: '',
      loginType: 0,
  }),
  getters: {
    isLoggedIn: (state) => state.token != '',
  },
  actions: {
      decodeJwt(token)
      {
          return JSON.parse(atob(token.split('.')[1]));
      },
      login(email, password, isAtm) {
        return new Promise((resolve, reject) => {

            axiosInstance.post("/login", {
            email: email,
            password: password,
          })
          .then((res) => {

            axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data.token;

            this.token = res.data.token;

            let decoded = this.decodeJwt(this.token).sub;

            this.user = decoded;

            this.loginType = 0;


            localStorage.setItem('token', res.data.token);
            localStorage.setItem('user', JSON.stringify(decoded));


            localStorage.setItem('loginType', isAtm ? 1 : 0);

            resolve();
        })

        .catch((error) => reject(error.response));
        });
    },
      logout() {

          this.token = '';
          this.user = '';

          localStorage.removeItem('token');
          localStorage.removeItem('user');

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