import { createRouter, createWebHistory } from 'vue-router'

import Home from '../components/customer/Home.vue';
import Login from '../components/Login.vue';
import MyAccount from '../components/MyAccount.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/myaccount', component: MyAccount},
  ]
})

export default router
