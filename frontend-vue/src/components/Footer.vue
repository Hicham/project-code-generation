<template>
  <div>
    <!-- Navigation Bar -->
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

    <!-- Content Placeholder -->
    <router-view></router-view>

    <!-- Footer -->
    <footer class="bg-dark text-white mt-5">
      <div class="container py-4">
        <div class="row">
          <div class="col-md-4 mb-3">
            <h5>About MyBank</h5>
            <p>MyBank is committed to providing the best banking services to our customers. Your satisfaction is our priority.</p>
          </div>
          <div class="col-md-4 mb-3">
            <h5>Contact Us</h5>
            <ul class="list-unstyled">
              <li>Email: support@mybank.com</li>
              <li>Phone: +1 800 123 456</li>
              <li>Address: 123 Banking St, Finance City, USA</li>
            </ul>
          </div>
          <div class="col-md-4 mb-3">
            <h5>Follow Us</h5>
            <ul class="list-unstyled">
              <li><a href="#" class="text-white"><i class="fab fa-facebook-f"></i> Facebook</a></li>
              <li><a href="#" class="text-white"><i class="fab fa-twitter"></i> Twitter</a></li>
              <li><a href="#" class="text-white"><i class="fab fa-linkedin-in"></i> LinkedIn</a></li>
              <li><a href="#" class="text-white"><i class="fab fa-instagram"></i> Instagram</a></li>
            </ul>
          </div>
        </div>
        <div class="text-center pt-3">
          <p>&copy; 2024 MyBank. All Rights Reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import { computed } from 'vue';
import { useStore } from '@/stores/store';
import { useRouter } from 'vue-router';

export default {
  name: "Footer",
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
.footer {
  background-color: #343a40;
  color: white;
  padding: 20px 0;
}
.footer a {
  color: white;
  text-decoration: none;
}
.footer a:hover {
  text-decoration: underline;
}
</style>
