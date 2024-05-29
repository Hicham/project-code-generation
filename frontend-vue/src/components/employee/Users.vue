<template>
  <div class="container">
    <div v-if="error" class="row">
      <div class="col-12 text-center">
        <p class="text-danger">{{ error }}</p>
      </div>
    </div>
    <div class="row">
      <div class="col-md-8">
        <input v-model="searchQuery" @keyup.enter="fetchUsers" type="text" class="form-control" placeholder="Search for user">
      </div>
      <div class="col-md-4">
        <button @click="fetchUsers" class="btn btn-primary">Search</button>
      </div>
    </div>
    <div class="row">
      <div v-for="user in users" :key="user.userId" class="col-12">
        <div class="card mb-2">
          <div class="card-body">
            <h5 class="card-title">User ID: {{ user.userId }}</h5>
            <p class="card-text">Role: {{ user.roleName }}</p>
            <p class="card-text">Email: {{ user.email }}</p>
            <p class="card-text">Name: {{ user.firstName + " " +  user.lastName}}</p>
            <p class="card-text">Phone Number: {{ user.phoneNumber }}</p>
            <p class="card-text">BSN Number: {{ user.bsnnumber }}</p>
            <p class="card-text">Approved: {{ user.approved }}</p>
            <button type="button" class="btn btn-primary" @click="this.$router.push('/admin/users/' + user.userId + '/Transactions/')" >See transactions </button>
          </div>
        </div>
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
  name: "Users",
  data() {
    return {
      users: [],
      currentPage: 1,
      totalPages: 1,
      error: null,
      searchQuery: ''
    };
  },
  mounted() {
    this.fetchUsers();
  },
  methods: {
    fetchUsers() {
      this.error = null;
      const params = {
        pageNumber: this.currentPage - 1,
        email: this.searchQuery
      };
      axiosInstance.get(`/api/users`, {
        params: params,
        headers: {
          Authorization: 'Bearer ' + useStore().token,
        }
      })
          .then(response => {
            this.users = response.data.content;
            this.totalPages = response.data.totalPages;
          })
          .catch(error => {
            this.error = "Error fetching users: " + error.message;
          });
    },
    goToPage(page) {
      if (page !== this.currentPage) {
        this.currentPage = page;
        this.fetchUsers();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchUsers();
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchUsers();
      }
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
