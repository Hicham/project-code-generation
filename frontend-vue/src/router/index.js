import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from '@/stores/store';


import Home from '../components/Home.vue';
import Login from '../components/Login.vue';
import MyAccount from '../components/MyAccount.vue';
import Register from '../components/Register.vue';
import Atm from '../components/atm/atm.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/register', component: Register},
    { path: '/myaccount', component: MyAccount, meta: { requiresAuth: true, loginType: 1 } },
    { path: '/atm', component: Atm, meta: { requiresAuth: true, loginType: 2 } },
<<<<<<< HEAD
    { path: '/users', component: Users, meta: { requiresAuth: true, loginType: 1 } },

    { path: '/account', component: MyAccount},
    { path: '/register', component: Register}
=======
    { path: '/users', component: Users, meta: { requiresAuth: true, loginType: 1 } }
>>>>>>> parent of 1ea1feb (Merge branch 'duha' of https://github.com/Hicham/project-code-generation into duha)
  ]
})

router.beforeEach((to, from, next) => {
  const store = useStore();
  const isLoggedIn = store.isLoggedIn;
  const loginType = store.loginType;

  if (to.meta.requiresAuth) {
    if (!isLoggedIn) {
      next('/login');
    } else {
      if (to.meta.loginType == loginType) {
        next();
      } else {
        next('/');
      }
    }
  } else {
    next();
  }
});

export default router
