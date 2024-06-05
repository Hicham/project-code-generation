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
            <th scope="col">User</th>
            <th scope="col">Active</th>
            <th scope="col">Actions</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="account in accounts" :key="account.iban">
            <td>{{ account.iban }}</td>
            <td>{{ account.balance }}</td>
            <td>{{ account.accountType }}</td>
            <td>{{ account.user.email }}</td>
            <td>{{ account.active }}</td>
            <td>
              <button v-if="!account.active" @click="enableAccount(account)" class="btn btn-success btn-sm">Enable</button>
              <button v-else @click="disableAccount(account)" class="btn btn-danger btn-sm">Disable</button>
              <button @click="openLimitModal(account)" class="btn btn-primary btn-sm">Set Limits</button>
            </td>
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
          <li class="page-item" v-for="page in totalPages" :key="page" :class="{ 'active': page === currentPage } ">
            <button class="page-link" @click.prevent="goToPage(page)">{{ page }}</button>
          </li>
          <li class="page-item" :class="{ 'disabled': currentPage === totalPages }">
            <button class="page-link" @click.prevent="nextPage">&raquo;</button>
          </li>
        </ul>
      </nav>
    </div>
    <div v-if="showLimitModal" class="modal fade show" style="display: block;" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Set Transaction Limits</h5>
            <button type="button" class="close" @click="closeLimitModal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="setLimits">
              <div class="form-group">
                <label for="dailyLimit">Daily Limit</label>
                <input type="number" v-model="limits.dailyLimit" class="form-control" id="dailyLimit" required>
              </div>
              <div class="form-group">
                <label for="absoluteLimit">Absolute Limit</label>
                <input type="number" v-model="limits.absoluteLimit" class="form-control" id="absoluteLimit" required>
              </div>
              <button type="submit" class="btn btn-primary">Save Limits</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axiosInstance from '@/axios-instance';
import {useStore} from "@/stores/store";

export default {
  name: "accounts",
  data() {
    return {
      accounts: [],
      currentPage: 1,
      totalPages: 1,
      showLimitModal: false,
      selectedAccount: null,
      limits: {
        dailyLimit: 0,
        absoluteLimit: 0
      }
    };
  },
  mounted() {
    this.fetchAccounts();
  },
  methods: {
    fetchAccounts() {
      axiosInstance.get(`/api/accounts?pageNumber=${this.currentPage - 1}`, { headers: {
          Authorization: 'Bearer ' + useStore().token,
        }})
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
      axiosInstance.post(`/api/accounts/enable/${account.iban}`, null, {
        headers: { Authorization: 'Bearer ' + useStore().token }
      })
          .then(() => {
            account.active = true;
          })
          .catch(error => {
            console.log(account)
            console.error("Error enabling account:", error);
          });
    },

    disableAccount(account) {
      axiosInstance.post(`/api/accounts/disable/${account.iban}`, null, {
        headers: { Authorization: 'Bearer ' + useStore().token }
      })
          .then(() => {
            account.active = false;
          })
          .catch(error => {
            console.log(account)
            console.error("Error disabling account:", error);
          });
    },
    openLimitModal(account) {
      console.log(account);
      this.selectedAccount = account;
      this.limits.dailyLimit = account.transactionLimit.dailyLimit;
      this.limits.absoluteLimit = account.absoluteLimit;
      this.showLimitModal = true;
    },
    closeLimitModal() {
      this.showLimitModal = false;
      this.selectedAccount = null;
      this.limits = {
        dailyLimit: 0,
        absoluteLimit: 0
      };
    },
    setLimits() {
      axiosInstance.post(`/api/accounts/setLimits/${this.selectedAccount.iban}`, this.limits, {
        headers: { Authorization: 'Bearer ' + useStore().token }
      })
          .then(() => {
            this.selectedAccount.transactionLimit.dailyLimit = this.limits.dailyLimit;
            this.selectedAccount.absoluteLimit = this.limits.absoluteLimit;
            this.closeLimitModal();
          })
          .catch(error => {
            console.error("Error setting limits:", error);
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