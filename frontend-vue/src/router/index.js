import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from '@/stores/store';


import Home from '../components/Home.vue';
import Login from '../components/Login.vue';
import MyAccount from '../components/MyAccount.vue';
import Register from '../components/Register.vue';
import Atm from '../components/atm/atm.vue';
import AtmLogin from '../components/atm/Login.vue';


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/register', component: Register},
    { path: '/atm', component: Atm},
    { path: '/atm/login', component: AtmLogin}
  ]
})

export default router
