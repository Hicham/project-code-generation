<template>
  <section class="homepage">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-8">
          <div class="card">
            <div class="card-body text-center">
              <h1 class="card-title">Welcome to Mybank</h1>
              <p class="card-text">
                Thank you for choosing Mybank for your banking needs. We're committed to providing you with excellent service and support.
              </p>
              <p class="card-text">
                Explore our services and features to manage your finances conveniently.
              </p>
              <div class="mt-4">
                <router-link to="/login" class="btn btn-primary">Login</router-link>
                <router-link to="/register" class="btn btn-outline-primary ms-2">Register</router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { useStore } from '@/stores/store';
import axios from '../axios-auth';

export default {
  name: 'Homepage',
  setup() {
    // You can add any setup logic here
  }
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
