<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-3">
    <div class="container">
      <router-link class="navbar-brand" to="/">
        <i class="fa-solid fa-piggy-bank me-1" style="font-size: 1em;"></i>Mybank
      </router-link>

      <router-link class="navbar-brand" to="/myaccount">Account</router-link>
      <router-link v-if="!isLoggedIn" class="navbar-brand" to="/login">Login</router-link>
      <router-link v-if="isLoggedIn" class="navbar-brand" to="/myaccountdetails">
        {{ firstName }}
      </router-link>

      <div class="nav-item dropdown">
        <a class="navbar-brand dropdown-toggle" href="#" id="adminDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
          Admin
        </a>
        <ul class="dropdown-menu" aria-labelledby="adminDropdown">
          <li><router-link class="dropdown-item" to="/admin/accounts">Accounts</router-link></li>
          <li><router-link class="dropdown-item" to="/admin/transactions">Transactions</router-link></li>
          <li><router-link class="dropdown-item" to="/admin/users">Users</router-link></li>
          <li><router-link class="dropdown-item" to="/admin/noaccounts">NoAccounts</router-link></li>
        </ul>
      </div>

      <button v-if="isLoggedIn" @click="logout" class="btn btn-primary">Logout</button>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav ml-auto">
        </ul>
      </div>
    </div>
  </nav>

</template>

<script>
import { computed } from 'vue';
import { useStore } from '@/stores/store';
import { useRouter } from 'vue-router';

export default {
  name: "Navigation",
  setup() {
    const store = useStore();
    const router = useRouter();

    const isLoggedIn = computed(() => store.isLoggedIn);
    const firstName = computed(() => store.user.firstName);

    const logout = () => {
      store.logout();
      router.push('/');
    };

    return {
      isLoggedIn,
      firstName,
      logout
    };
  }
};
</script>

<style>
</style>
