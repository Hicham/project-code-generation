<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-12">
        <div class="card">
          <div class="card-body" v-if="isApproved">
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

              <!-- Dropdown for selecting filter column -->
              <div class="filter-controls">
                <label for="filter-column">Filter By:</label>
                <select id="filter-column" v-model="selectedFilterColumn" class="form-select">
                  <option v-for="column in filterColumns" :key="column" :value="column">
                    {{ column }}
                  </option>
                </select>
                <label for="filter-input">Filter:</label>
                <input id="filter-input" v-model="filterValue" class="form-control" @input="filterTransactions">
              </div>

              <div class="transaction-list">
                <table class="table">
                  <thead>
                  <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Destination</th>
                    <th>Amount</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="transaction in filteredTransactions" :key="transaction.id" class="transaction">
                    <td>{{ formatDate(transaction.timestamp) }}</td>
                    <td>{{ transaction.description }}</td>
                    <td>{{ getDestinationName(transaction.destinationIBAN) }}</td>
                    <td :class="{ 'deposit': transaction.type === 'TRANSFER' && transaction.destinationIBAN === selectedAccount.iban, 'withdraw': transaction.type === 'TRANSFER' && transaction.sourceIBAN === selectedAccount.iban }">
                      <span v-if="transaction.type === 'TRANSFER' && transaction.destinationIBAN === selectedAccount.iban" class="deposit">+ €{{ transaction.amount }}</span>
                      <span v-else-if="transaction.type === 'TRANSFER' && transaction.sourceIBAN === selectedAccount.iban" class="withdraw">- €{{ transaction.amount }}</span>
                      <span v-else-if="transaction.type !== 'TRANSFER'" :class="{ 'deposit': transaction.type === 'DEPOSIT', 'withdraw': transaction.type === 'WITHDRAW' }">
                          <span v-if="transaction.type === 'DEPOSIT'" class="deposit">+ €{{ transaction.amount }}</span>
                          <span v-else class="withdraw">- €{{ transaction.amount }}</span>
                        </span>
                    </td>
                  </tr>
                  </tbody>
                </table>
                <div v-if="filteredTransactions.length === 0" class="no-transactions">
                  No transactions available.
                </div>
              </div>
            </div>
          </div>
          <div class="card-body" v-else>
            <h1>Your account is not approved</h1>
            <router-link to="/" class="btn btn-primary">Go Back</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>



<script>
import axiosInstance from '@/axios-instance';
import { useStore } from '@/stores/store';
import { ref, onMounted, computed } from 'vue';

export default {
  name: "AccountManagement",
  setup() {
    const store = useStore();
    const accounts = ref([]);
    const selectedAccount = ref(null);
    const balance = ref(0);
    const transactions = ref([]);
    const isApproved = ref(store.user.isApproved);
    const selectedFilterColumn = ref('Description');
    const filterValue = ref('');

    const filterColumns = ['Date', 'Description', 'Destination', 'Amount'];

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
      return date.toLocaleString(); // Adjusted to include time
    };

    const getDestinationName = (iban) => {
      const account = accounts.value.find(account => account.iban === iban);
      if (account && account.user) {
        return `${account.user.firstName} ${account.user.lastName}`;
      }
      return iban;
    };

    const filterTransactions = () => {
      if (!filterValue.value) {
        return transactions.value;
      }

      return transactions.value.filter((transaction) => {
        const columnValue = {
          'Date': formatDate(transaction.timestamp),
          'Description': transaction.description,
          'Destination': getDestinationName(transaction.destinationIBAN),
          'Amount': `€${transaction.amount}`,
        }[selectedFilterColumn.value];

        return columnValue && columnValue.toLowerCase().includes(filterValue.value.toLowerCase());
      });
    };

    const filteredTransactions = computed(filterTransactions);

    onMounted(() => {
      if (isApproved.value) {
        getAccounts();
      }
    });

    return {
      accounts,
      selectedAccount,
      balance,
      transactions,
      selectAccount,
      formatDate,
      isApproved,
      getDestinationName,
      selectedFilterColumn,
      filterValue,
      filterColumns,
      filteredTransactions,
      filterTransactions,
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

.filter-controls {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 20px;
}

.filter-controls label {
  margin-right: 10px;
  margin-top: 10px;
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th, .table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

.table th {
  background-color: #f2f2f2;
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


