<template>
  <Navigation/>
  <router-view />
  <Footer/>
</template>

<script>
import Navigation from './components/Navigation.vue'
import { useStore } from './stores/store';
import Footer from './components/Footer.vue'

export default {
  name: "App",
  setup() {
    const store = useStore();
    store.autoLogin();
  },
  components: {
    Navigation,
    Footer
  }
};
</script>

<style>
</style>
