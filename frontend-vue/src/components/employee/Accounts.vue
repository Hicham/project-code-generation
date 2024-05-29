<template>
  <div class="container">
    <div class="row">
      <div class="col-12">
        <table class="table table-striped">
          <thead>
          <tr>
            <th scope="col">IBAN</th>
            <th scope="col">Balance</th>
            <th scope="col">Account Type</th>
            <th scope="col">User Email</th>
            <th scope="col">Active</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="account in accounts" :key="account.iban">
            <td>{{ account.iban }}</td>
            <td>{{ account.balance }}</td>
            <td>{{ account.accountType }}</td>
            <td>{{ account.user.email }}</td>
            <td>{{ account.active }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="row">
      <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ 'disabled': currentPage === 1 }">
            <button class="page-link" @click.prevent="prevPage">&laquo;</button>
          </li>
          <li class="page-item" v-for="page in totalPages" :key="page" :class="{ 'active': page === currentPage }">
            <button class="page-link" @click.prevent="goToPage(page)">{{ page }}</button>
          </li>
          <li class="page-item" :class="{ 'disabled': currentPage === totalPages }">
            <button class="page-link" @click.prevent="nextPage">&raquo;</button>
          </li>
        </ul>
      </nav>
    </div>
    <div class="row">
      <div v-for="account in accounts" :key="account.iban" class="col-12">
        <div class="card mb-2">
          <div class="card-body">
            <h5 class="card-title">IBAN: {{ account.iban }}</h5>
            <p class="card-text">Balance: {{ account.balance }}</p>
            <p class="card-text">Account Type: {{ account.accountType }}</p>
            <p class="card-text">User: {{ account.user.email }}</p>
            <p class="card-text">Active: {{ account.active }}</p>
            <button v-if="!account.active" @click="enableAccount(account)" class="btn btn-success">Enable</button>
            <button v-else @click="disableAccount(account)" class="btn btn-danger">Disable</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axiosInstance from '@/axios-instance';
import { useStore } from "@/stores/store";

export default {
  name: "accounts",
  data() {
    return {
      accounts: [],
      currentPage: 1,
      totalPages: 1,
    };
  },
  mounted() {
    this.fetchAccounts();
  },
  methods: {
    fetchAccounts() {
      axiosInstance.get(`/api/accounts?pageNumber=${this.currentPage - 1}`, {
        headers: {
          Authorization: 'Bearer ' + useStore().token,
        }
      })
          .then(response => {
            this.accounts = response.data.content;
            this.totalPages = response.data.totalPages;
          })
          .catch(error => {
            console.error("Error fetching accounts:", error);
          });
    },
    goToPage(page) {
      if (page !== this.currentPage) {
        this.currentPage = page;
        this.fetchAccounts();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchAccounts();
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchAccounts();
      }
    },
    enableAccount(account) {
      axiosInstance.post(`/api/accounts/enable/${account.id}`, null, {
        headers: { Authorization: 'Bearer ' + useStore().token }
      })
          .then(() => {
            account.active = true;
          })
          .catch(error => {
            console.error("Error enabling account:", error);
          });
    },

    disableAccount(account) {
      axiosInstance.post(`/api/accounts/disable/${account.id}`, null, {
        headers: { Authorization: 'Bearer ' + useStore().token }
      })
          .then(() => {
            account.active = false;
          })
          .catch(error => {
            console.error("Error disabling account:", error);
          });
    }
  }
}
</script>

<style scoped>
.page-item.disabled .page-link {
  pointer-events: none;
  cursor: default;
}

.page-item.active .page-link {
  background-color: #007bff;
  border-color: #007bff;
  color: white;
}
</style>
