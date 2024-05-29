<template>
  <div class="container">
    <div v-if="error" class="row">
      <div class="col-12 text-center">
        <p class="text-danger">{{ error }}</p>
      </div>
    </div>
    <div class="row mb-3">
      <div class="col-md-8">
        <input type="text" class="form-control" placeholder="Search for user">
      </div>
      <div class="col-md-4">
        <select class="form-select">
          <option selected>Select a user</option>
        </select>
      </div>
    </div>
    <div class="row">
      <div class="col-12">
        <table class="table table-striped">
          <thead>
          <tr>
            <th scope="col">Transaction ID</th>
            <th scope="col">Source IBAN</th>
            <th scope="col">Destination IBAN</th>
            <th scope="col">Amount</th>
            <th scope="col">Type</th>
            <th scope="col">Timestamp</th>
            <th scope="col">Initiated Transfer</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="transaction in transactions" :key="transaction.id">
            <td>{{ transaction.id }}</td>
            <td>{{ transaction.sourceIBAN || 'N/A' }}</td>
            <td>{{ transaction.destinationIBAN }}</td>
            <td>{{ transaction.amount }}</td>
            <td>{{ transaction.type }}</td>
            <td>{{ formatTimestamp(transaction.timestamp) }}</td>
            <td>{{ transaction.user.email }}</td>
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
  </div>
</template>

<script>
import axiosInstance from '@/axios-instance';
import { useStore } from "@/stores/store";

export default {
  name: "Transactions",
  data() {
    return {
      transactions: [],
      currentPage: 1,
      totalPages: 1,
      error: null
    };
  },
  mounted() {
    this.fetchTransactions();
  },
  methods: {
    fetchTransactions() {
      this.error = null;
      axiosInstance.get(`/api/transactions?pageNumber=${this.currentPage - 1}`, {
        headers: {
          Authorization: 'Bearer ' + useStore().token,
        }
      })
          .then(response => {
            this.transactions = response.data.content;
            this.totalPages = response.data.totalPages;
          })
          .catch(error => {
            this.error = "Error fetching transactions: " + error.message;
          });
    },
    goToPage(page) {
      if (page !== this.currentPage) {
        this.currentPage = page;
        this.fetchTransactions();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchTransactions();
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchTransactions();
      }
    },
    formatTimestamp(timestamp) {
      const date = new Date(timestamp * 1000);
      return date.toLocaleString();
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
