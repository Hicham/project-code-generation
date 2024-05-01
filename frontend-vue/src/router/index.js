import { createRouter, createWebHistory } from 'vue-router'

import Home from '../components/customer/Home.vue';
import Login from '../components/Login.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Login },
    { path: '/home', component: Home },
  ]
})

export default router
