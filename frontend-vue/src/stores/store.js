import { defineStore } from 'pinia'
import axios from '../axios-auth';


export const useStore = defineStore('counter', {
  state: () => ({
    token: '',
    user: ''
  }),
  getters: {
    isLoggedIn: (state) => state.token != '',
  },
  actions: {
      decodeJwt(token)
      {
          return JSON.parse(atob(token.split('.')[1]));
      },
    login(email, password) {
        return new Promise((resolve, reject) => {


        axios.post("/users/login", {
            email: email,
            password: password,
          })
          .then((res) => {

            axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data;

            this.token = res.data;

            let decoded = this.decodeJwt(this.token).sub;

            this.user = decoded;


            localStorage.setItem('token', res.data);
            localStorage.setItem('user', JSON.stringify(decoded));


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


        if (token) {
          this.token = token;
          this.user = JSON.parse(user);
          axios.defaults.headers.common['Authorization'] = 'Bearer ' + token;
        }

      },

  },

})