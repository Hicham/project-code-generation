import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from '@/stores/store';

import Home from '../components/Home.vue';
import Login from '../components/Login.vue';
import MyAccount from '../components/MyAccount.vue';
import Register from '../components/Register.vue';
import Atm from '../components/atm/atm.vue';

import Accounts from "@/components/employee/Accounts.vue";
import NoAccounts from "@/components/employee/NoAccount.vue";
import Transactions from "@/components/employee/Transactions.vue";
import UserTransactions from "@/components/employee/UserTransactions.vue";
import Users from "@/components/employee/Users.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/register', component: Register},
    { path: '/account', component: MyAccount, meta: { requiresAuth: true, loginType: 1 } },
    { path: '/atm', component: Atm, meta: { requiresAuth: true, loginType: 2 } },
    { path: '/admin/accounts', component: Accounts, meta: { requiresAuth: true, loginType: 1, role: 'ROLE_ADMIN' } },
    { path: '/admin/noaccounts', component: NoAccounts, meta: { requiresAuth: true, loginType: 1, role: 'ROLE_ADMIN' } },
    { path: '/admin/transactions', component: Transactions, meta: { requiresAuth: true, loginType: 1, role: 'ROLE_ADMIN' } },
    { path: '/admin/users/:userId/Transactions', component: UserTransactions, meta: { requiresAuth: true, loginType: 1, role: 'ROLE_ADMIN' } },
    { path: '/admin/users', component: Users, meta: { requiresAuth: true, loginType: 1, role: 'ROLE_ADMIN' } },
    { path: '/register', component: Register}
  ]
})

router.beforeEach((to, from, next) => {
  const store = useStore();
  const isLoggedIn = store.isLoggedIn;
  const loginType = store.loginType;
  const userRoles = store.user.roles;

  if (to.meta.requiresAuth) {
    if (!isLoggedIn) {
      next('/login');
    } else {
      if (to.meta.loginType == loginType) {
        if (to.meta.role) {
          if (userRoles.includes(to.meta.role)) {
            next();
          } else {
            next('/');
          }
        } else {
          next();
        }
      } else {
        next('/');
      }
    }
  } else {
    next();
  }
});

export default router
