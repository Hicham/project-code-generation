<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card">
          <div class="card-body">
            <h1 class="mb-4">ATM Interface</h1>
            <select class="form-select form-select-lg mb-3" v-model="selectedAccount" @change="selectAccount">
              <option v-for="account in accounts" :key="account.iban" :value="account">
                {{ account.iban }}
              </option>
            </select>

            <div>
              <h3>Balance: €{{ balance }}</h3>
            </div>

            <div class="alert alert-danger" v-if="errorMessage">
              {{ errorMessage }}
            </div>

            <div class="mb-3">
              <div class="input-group mb-2">
                <input type="number" class="form-control" placeholder="Enter amount" v-model="withdrawAmount">
                <button class="btn btn-primary" @click="withdraw">Withdraw</button>
              </div>
              <div class="input-group mb-2">
                <input type="number" class="form-control" placeholder="Enter amount" v-model="depositAmount">
                <button class="btn btn-success" @click="deposit">Deposit</button>
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

export default {
  name: "Atm",
  data() {
    return {
      accounts: [],
      selectedAccount: null,
      balance: 0,
      withdrawAmount: 0,
      depositAmount: 0,
      errorMessage: ""
    };
  },
  mounted() {
    this.getAccounts();
  },
  methods: {
    getAccounts() {
      axiosInstance
          .get(`/api/users/${useStore().user.id}/accounts/checking`, {
            headers: {
              Authorization: 'Bearer ' + useStore().token,
            }
          })
          .then((result) => {
            this.accounts = result.data.content;
            if (this.selectedAccount) {
              this.selectedAccount = this.accounts.find(account => account.iban === this.selectedAccount.iban) || this.accounts[0];
            } else if (this.accounts.length > 0) {
              this.selectedAccount = this.accounts[0];
            }
            this.selectAccount();
          })
          .catch((error) => console.error("Error fetching accounts:", error));
    },
    selectAccount() {
      if (this.selectedAccount) {
        this.balance = this.selectedAccount.balance;
      }
    },
    withdraw() {
      axiosInstance
          .post(`/api/accounts/${this.selectedAccount.iban}/withdraw`, {
            amount: this.withdrawAmount,
          }, {
            headers: {
              Authorization: 'Bearer ' + useStore().token,
            },
          })
          .then((response) => {
            this.withdrawAmount = 0;
            this.getAccounts();
            this.errorMessage = ""; // Clear the error message on success
          })
          .catch((error) => {
            this.errorMessage = error.response.data;
            console.error("Withdrawal failed:", error.response.data);
          });
    },
    deposit() {
      if (this.depositAmount <= 0) {
        alert("Invalid deposit amount");
        return;
      }
      axiosInstance
          .post(`/api/accounts/${this.selectedAccount.iban}/deposit`, {
            amount: this.depositAmount,
          }, {
            headers: {
              Authorization: 'Bearer ' + useStore().token,
            },
          })
          .then((response) => {
            this.depositAmount = 0;
            this.getAccounts();
          })
          .catch((error) => {
            console.error("Deposit failed:", error);
          });
    }
  }
}
</script>
