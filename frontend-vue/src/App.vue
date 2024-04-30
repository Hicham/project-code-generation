<template>
  <navigation/>
  <router-view />
</template>

<script>
import Navigation from './components/Navigation.vue'
import { useStore } from './stores/store';

export default {
  name: "App",
  setup() {
    const store = useStore();
    store.autoLogin();
  },
  components: {
    Navigation
  }
};
</script>

<style>
</style>
