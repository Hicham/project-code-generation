<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card">
          <div class="card-body">
            <h1 class="mb-4">Account Management</h1>
            <select class="form-select form-select-lg mb-3" v-model="selectedAccount" @change="selectAccount">
              <option v-for="account in accounts" :key="account.iban" :value="account">
                {{ account.iban }}
              </option>
            </select>

            <!-- Display checking account balance -->
            <div v-if="selectedAccount && selectedAccount.accountType === 'CHECKING'">
              <h3>Balance CHECKING: €{{ balance }}</h3>
            </div>

            <!-- Display savings account balance -->
            <div v-if="selectedAccount && selectedAccount.accountType === 'SAVINGS'">
              <h3>Balance SAVINGS: €{{ balance }}</h3>
            </div>

            <router-link to="/transfer" class="btn btn-primary">Transfer</router-link>

            <!-- Transaction list -->
            <div class="transaction-container">
              <h3>Transactions</h3>
              <div class="transaction-list">
                <ul>
                  <li v-for="transaction in transactions" :key="transaction.id" class="transaction">
                    <div>
                      <span class="transaction-date">{{ formatDate(transaction.timestamp) }}</span>
                      <span>{{ transaction.name }} Name of transaction</span>
                    </div>
                    <span>
                      <span v-if="transaction.type === 'WITHDRAW'" class="withdraw">- €{{ transaction.amount }}</span>
                      <span v-else class="deposit">+ €{{ transaction.amount }}</span>
                    </span>
                  </li>
                </ul>
              </div>
              <div v-if="transactions.length === 0" class="no-transactions">
                No transactions available.
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axiosInstance from '@/axios-instance';
import { useStore } from '@/stores/store';
import { ref, onMounted } from 'vue';

export default {
  name: "AccountManagement",
  setup() {
    const store = useStore();
    const accounts = ref([]);
    const selectedAccount = ref(null);
    const balance = ref(0);
    const transactions = ref([]);

    const getAccounts = () => {
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
            if (selectedAccount.value) {
              selectedAccount.value = accounts.value.find(account => account.iban === selectedAccount.value.iban) || accounts.value[0];
            } else if (accounts.value.length > 0) {
              selectedAccount.value = accounts.value[0];
            }
            selectAccount();
          })
          .catch((error) => console.error("Error fetching accounts:", error));
    };

    const selectAccount = () => {
      if (selectedAccount.value) {
        balance.value = selectedAccount.value.balance;
        getTransactions(selectedAccount.value.iban);
      }
    };

    const getTransactions = (iban) => {
      axiosInstance
          .get(`/api/accounts/${iban}/transactions`, {
            headers: {
              Authorization: 'Bearer ' + store.token,
            },
          })
          .then((response) => {
            transactions.value = response.data.content; // Assuming transactions are stored in the 'content' property
          })
          .catch((error) => {
            console.error("Error fetching transactions:", error);
          });
    };

    // Function to format timestamp to date string
    const formatDate = (timestamp) => {
      const date = new Date(timestamp);
      return date.toLocaleDateString();
    };

    onMounted(() => {
      getAccounts();
    });

    return {
      accounts,
      selectedAccount,
      balance,
      transactions,
      selectAccount,
      formatDate,
    };
  },
};
</script>

<style scoped>
.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  margin-top: 0;
}

.card-body {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.transaction-container {
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 20px;
  margin-top: 20px;
}

.transaction-list {
  margin-top: 20px;
}

.transaction-date {
  font-size: 12px;
  color: #888;
  margin-right: 30px;
}

.transaction {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ccc;
}

.deposit {
  color: green;
}

.withdraw {
  color: red;
}

.no-transactions {
  padding: 10px 0;
}
</style>
