<template>
  <div class="container">
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
      <div v-for="transaction in transactions" :key="transaction.id" class="col-12">
        <div class="card mb-2">
          <div class="card-body">
            <h5 class="card-title">Transaction ID: {{ transaction.id }}</h5>
            <p class="card-text">Source IBAN: {{ transaction.sourceIBAN || 'N/A' }}</p>
            <p class="card-text">Destination IBAN: {{ transaction.destinationIBAN }}</p>
            <p class="card-text">Amount: {{ transaction.amount }}</p>
            <p class="card-text">Description: {{ transaction.description }}</p>
            <p class="card-text">Type: {{ transaction.type }}</p>
            <p class="card-text">Timestamp: {{ formatTimestamp(transaction.timestamp) }}</p>
            <p class="card-text">Iniated Transfer: {{ transaction.user.email }}</p>
          </div>
        </div>
      </div>
    </div>
    <div v-if="error" class="row">
      <div class="col-12 text-center">
        <p class="text-danger">{{ error }}</p>
      </div>
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
