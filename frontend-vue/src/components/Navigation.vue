<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-3">
    <div class="container">
      <router-link class="navbar-brand" to="/home"><i class="fa-solid fa-piggy-bank me-1" style="font-size: 1em;"></i>Mybank</router-link>
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
import {useStore} from '@/stores/store';
import router from '@/router';

export default {
  name: "Navigation",
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
