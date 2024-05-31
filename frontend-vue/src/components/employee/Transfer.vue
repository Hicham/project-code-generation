<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card">
          <div class="card-body">
            <h1 class="mb-4 text-center">Transfer</h1>
            <div class="mb-4">
              <label class="form-label">Select Your Account</label>
              <select class="form-select" v-model="selectedAccount" @change="selectAccount">
                <option v-for="account in accounts" :key="account.iban" :value="account">
                  {{ account.iban }}
                </option>
              </select>
            </div>
            <div v-if="selectedAccount">
              <div class="account-details mb-4">
                <div class="detail">
                  <strong>First Name:</strong> {{ selectedAccount.user.firstName }}
                </div>
                <div class="detail">
                  <strong>Last Name:</strong> {{ selectedAccount.user.lastName }}
                </div>
                <div class="detail">
                  <strong>Balance:</strong> €{{ balance }}
                </div>
                <div class="detail">
                  <strong>IBAN:</strong> {{ selectedAccount.iban }}
                </div>
              </div>

              <div class="mb-3">
                <label class="form-label">Description (max 50 characters)</label>
                <textarea class="form-control" v-model="description" maxlength="50" placeholder="Enter description" rows="3"></textarea>
              </div>

              <div class="mb-3">
                <label class="form-label">Amount to Transfer</label>
                <input type="number" class="form-control" v-model="transferAmount">
              </div>

              <div class="mb-3">
                <label class="form-label">Destination Account</label>
                <select class="form-select" v-model="destinationAccount">
                  <option v-for="account in filteredAccounts" :key="account.iban" :value="account.iban">
                    {{ account.iban }}
                  </option>
                </select>
              </div>

              <button class="btn btn-primary w-100" @click="confirmTransfer">Send</button>

              <div v-if="confirmation" class="alert alert-success mt-3">
                <p>Transfer of €{{ transferAmount }} to {{ destinationAccount }} was successful.</p>
                <p>Description: {{ description }}</p>
              </div>
            </div>
            <div v-else>
              <p>Please select an account.</p>
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
import { ref, onMounted, computed } from 'vue';

export default {
  name: "TransferFunds",
  setup() {
    const store = useStore();
    const accounts = ref([]);
    const selectedAccount = ref(null);
    const balance = ref(0);
    const transferAmount = ref(0);
    const destinationAccount = ref('');
    const description = ref('');
    const confirmation = ref(false);

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
            if (accounts.value.length > 0) {
              selectedAccount.value = accounts.value[0];
              balance.value = selectedAccount.value.balance;
            }
          })
          .catch((error) => console.error("Error fetching accounts:", error));
    };

    const selectAccount = () => {
      if (selectedAccount.value) {
        balance.value = selectedAccount.value.balance;
      }
    };

    const confirmTransfer = () => {
      if (transferAmount.value <= 0 || transferAmount.value > balance.value || !destinationAccount.value) {
        alert("Invalid transfer details");
        return;
      }

      if (confirm("Are you sure you want to transfer €" + transferAmount.value + " to " + destinationAccount.value + "?")) {
        sendTransfer();
      } else {
        // User cancelled
      }
    };

    const sendTransfer = () => {
      axiosInstance
          .post('/api/transactions', {
            sourceIBAN: selectedAccount.value.iban,
            destinationIBAN: destinationAccount.value,
            amount: transferAmount.value,
            description: description.value,
          }, {
            headers: {
              Authorization: 'Bearer ' + store.token,
            },
          })
          .then(() => {
            confirmation.value = true;
            setTimeout(() => {
              confirmation.value = false;
              transferAmount.value = 0;
              description.value = '';
              destinationAccount.value = '';
              getAccounts();
            }, 3000);
          })
          .catch((error) => {
            console.error("Transfer failed:", error);
          });
    };

    const filteredAccounts = computed(() => {
      return accounts.value.filter(account => account.iban !== selectedAccount.value?.iban);
    });


    onMounted(() => {
      getAccounts();
    });

    return {
      accounts,
      selectedAccount,
      balance,
      transferAmount,
      description,
      destinationAccount,
      confirmation,
      selectAccount,
      confirmTransfer,
      sendTransfer,
      filteredAccounts,
    };
  },
};
</script>

<style scoped>
.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  margin-top: 0;
  font-size: 2rem;
  color: #343a40;
}

.card-body {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.account-details {
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 20px;
  margin-bottom: 20px;
  background-color: #f8f9fa;
}

.detail {
  margin-bottom: 10px;
}

.detail strong {
  display: inline-block;
  width: 150px;
}

.btn-primary {
  background-color: #007bff;
  border: none;
}

.alert-success {
  width: 100%;
  text-align: center;
}
</style>
