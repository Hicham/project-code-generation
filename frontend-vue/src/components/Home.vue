<template>
  <section class="homepage py-5">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-md-8">
          <div class="card shadow-lg">
            <div class="card-body text-center">
              <h1 class="card-title">Welcome to Mybank</h1>
              <p class="card-text">
                Thank you for choosing Mybank for your banking needs. We're committed to providing you with excellent service and support.
              </p>
              <p class="card-text">
                Explore our services and features to manage your finances conveniently.
              </p>
              <div class="mt-4">
                <template v-if="!isLoggedIn">
                  <router-link to="/login" class="btn btn-primary btn-lg">Login</router-link>
                  <router-link to="/register" class="btn btn-outline-primary btn-lg ms-2">Register</router-link>
                </template>

                <template v-else-if="isLoggedIn && isUserApproved">
                  <p class="text-success">Your account is approved.</p>
                  <router-link to="/myaccount" class="btn btn-success btn-lg">Go to My Account</router-link>
                  <button @click="logout" class="btn btn-primary btn-lg ms-2">Logout</button>
                </template>

                <template v-else>
                  <p class="text-warning">Your account is not approved yet.</p>
                  <button @click="logout" class="btn btn-primary btn-lg">Logout</button>
                </template>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import axiosInstance from '../axios-instance';
import { useStore } from '@/stores/store';
import { ref, computed, onMounted } from "vue";
import router from "@/router";

export default {
  name: 'Homepage',
  setup() {
    const store = useStore();
    const isLoggedIn = computed(() => store.isLoggedIn);
    const userEmail = computed(() => localStorage.getItem('user'));
    const isUserApproved = ref(null);
    const userObject = ref(null);

    const fetchUser = async () => {
      if (!userEmail.value) {
        isUserApproved.value = false;
        return;
      }

      try {
        const response = await axiosInstance.get(`/api/users/${userEmail.value}`);
        const user = response.data;
        isUserApproved.value = user.approved;
        userObject.value = user;

      } catch (error) {
        console.error('Error fetching user data:', error);
        isUserApproved.value = false;
      }
    };

    onMounted(() => {
      if (isLoggedIn.value) {
        fetchUser();
      }
    });

    return {
      isLoggedIn,
      isUserApproved,
      userObject
    };
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

<style scoped>
.homepage {
  background-color: #f8f9fa;
}

.card {
  background-color: #ffffff;
  border: none;
  border-radius: 10px;
}

.card-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.card-text {
  font-size: 1.2rem;
  margin-bottom: 1.5rem;
}

.btn-lg {
  padding: 0.75rem 1.5rem;
  font-size: 1.2rem;
}

.text-warning {
  color: #ffc107;
}
</style>
