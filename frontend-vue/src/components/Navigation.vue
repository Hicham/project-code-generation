<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-3">
    <div class="container">
      <router-link class="navbar-brand" to="/"><i class="fa-solid fa-piggy-bank me-1" style="font-size: 1em;"></i>Mybank</router-link>
      <router-link class="navbar-brand" to="/myaccount">My Account</router-link>
      <router-link v-if="!isLoggedIn" class="navbar-brand" to="/login">Login</router-link>
      <router-link v-if="isLoggedIn" class="navbar-brand" to="#">{{ user.email }}</router-link>
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
import { useStore } from '../stores/store';
import router from '../router/index.js';

export default {
  name: "Navigation",
  computed: {
    isLoggedIn() {
      return useStore().isLoggedIn;
    },
    user() {
      return useStore().user;
    }
  },
  methods: {
    logout() {
      const store = useStore();
      store.logout();
      router.push('/');
    }
  }
};
</script>

<style>
</style>
