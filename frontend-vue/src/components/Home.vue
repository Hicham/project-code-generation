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




      <div>
        <h2>Cows</h2>
        <ul>
          <li v-for="cow in cows" :key="cow.id">
            Name: {{ cow.name }} - Age: {{ cow.age }}
          </li>
        </ul>

        <h2>Cheeses</h2>
        <ul>
          <li v-for="cheese in cheeses" :key="cheese.id">
            Name: {{ cheese.name }} - Price: {{ cheese.price }} - Made from (Cow Name): {{ cheese.cowName }}
          </li>
        </ul>
      </div>



    </div>
  </section>
</template>

<script>

import axiosInstance from '../axios-instance';
import { ref, onMounted } from 'vue';

export default {
  name: 'Homepage',
  setup() {
    const cows = ref([]);
    const cheeses = ref([]);

    onMounted(async () => {
      try {
        const cowResponse = await axiosInstance.get('/api/cows');
        cows.value = cowResponse.data;

        const cheeseResponse = await axiosInstance.get('/api/cheeses');
        cheeses.value = cheeseResponse.data;
      } catch (error) {
        console.error(error);
      }
    });

    return { cows, cheeses };
  }
};

</script>

<style scoped>
/* Add your custom styles here */
</style>
