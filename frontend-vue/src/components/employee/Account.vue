<template>
  <section class="account-page py-5">
    <div v-if="isLoggedIn" class="container">
      <div class="card shadow-lg">
        <div class="card-body text-center">
          <h1 class="card-title">Welcome back, {{ userObject.firstName }}</h1>
          <p class="card-text">Here are your account details:</p>
          <ul class="list-unstyled">
            <li><strong>Email:</strong> {{ userObject.email }}</li>
            <li><strong>Name:</strong> {{ userObject.firstName }} {{ userObject.lastName }}</li>
            <li v-if="userObject.roleName === 'customer'"><strong>BSN number:</strong> {{ userObject.BSNNumber }}</li>
            <li><strong>Phone number:</strong> {{ userObject.phoneNumber }}</li>
          </ul>
          <button @click="logout" class="btn btn-primary btn-lg">Logout</button>
        </div>
      </div>
    </div>
    <div v-else class="container text-center">
      <div class="card shadow-lg">
        <div class="card-body text-center">
          <h2 class="card-title">You are not logged in</h2>
          <router-link to="/login" class="btn btn-primary btn-lg">Login</router-link>

        </div>
      </div>
    </div>
  </section>
</template>

<script>
import axiosInstance from '../../axios-instance';
import { useStore } from '@/stores/store';
import { ref, computed, onMounted, reactive } from "vue";
import router from "@/router";

export default {
  name: 'Account',
  setup() {
    const store = useStore();
    const isLoggedIn = computed(() => store.isLoggedIn);
    const userEmail = computed(() => localStorage.getItem('user'));
    const isUserApproved = ref(null);

    const userObject = reactive({});

    const fetchUser = async () => {
      if (!userEmail.value) {
        isUserApproved.value = false;
        return;
      }

      try {
        const response = await axiosInstance.get(`/api/users/${userEmail.value}`);
        const user = response.data;
        isUserApproved.value = user.isApproved;

        userObject.email = user.email;
        userObject.firstName = user.firstName;
        userObject.lastName = user.lastName;
        userObject.phoneNumber = user.phoneNumber;
        userObject.roleName = user.roleName;
        userObject.BSNNumber = user.BSNNumber;

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

    const logout = () => {
      const store = useStore();
      store.logout();
      router.push('/');
    };

    return {
      isLoggedIn,
      isUserApproved,
      userObject,
      logout
    };
  }
};
</script>

<style scoped>
.account-page {
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
</style>
