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
            <li><strong>BSN number:</strong> {{ userObject }}</li>
            <li><strong>Phone number:</strong> {{ userObject.phoneNumber }}</li>
          </ul>

          <ul class="list-unstyled">
            <h3 class="mt-4">Accounts</h3>
            <li v-for="account in accounts" :key="account.iban">
              <strong>Account IBAN:</strong> {{ account.iban }}<br />
              <strong>Account Balance:</strong> € {{ account.balance }}
            </li>
          </ul>
          <h3>Total Balance: € {{ totalBalance }}</h3>
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
import axiosInstance from '@/axios-instance';
import { useStore } from '@/stores/store';
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Account',
  setup() {
    const store = useStore();
    const router = useRouter();

    const isLoggedIn = computed(() => store.isLoggedIn);
    const userObject = computed(() => store.user);

    const accounts = ref([]);
    const totalBalance = computed(() => {
      return accounts.value.reduce((acc, curr) => acc + curr.balance, 0);
    });

    const fetchUser = async () => {
      try {
        const response = await axiosInstance.get(`/api/users/${userObject.value.email}`);
        const user = response.data;

        // Update the userObject with fetched user data including BSN number and phone number
        store.user = {
          ...store.user,
          email: user.email,
          firstName: user.firstName,
          lastName: user.lastName,
          phoneNumber: user.phoneNumber,
          roleName: user.roleName,
          BSNNumber: user.BSNNumber,
        };
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };

    const fetchUserAccounts = () => {
      axiosInstance
          .get('/api/accounts', {
            headers: {
              Authorization: 'Bearer ' + store.token,
            },
            params: {
              userId: store.user.id,
              isChecking: false,
            },
          })
          .then((result) => {
            accounts.value = result.data.content;
          })
          .catch((error) => console.error("Error fetching accounts:", error));
    };

    const logout = () => {
      store.logout();
      router.push('/');
    };

    onMounted(() => {
      if (isLoggedIn.value) {
        fetchUser();
        fetchUserAccounts();
      }
    });

    return {
      isLoggedIn,
      userObject,
      accounts,
      totalBalance,
      logout,
    };
  },
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
