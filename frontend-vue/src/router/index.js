import { createRouter, createWebHistory } from 'vue-router'

import Home from '../components/Home.vue';
import Login from '../components/Login.vue';
import MyAccount from '../components/employee/MyAccount.vue';
import Register from '../components/Register.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/myaccount', component: MyAccount},
    { path: '/register', component: Register}
  ]
})

export default router
