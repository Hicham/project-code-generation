<template>
  <div class="container mt-5">
    <h2 class="mb-4">Unapproved Users</h2>
    <table class="table">
      <thead>
      <tr>
        <th scope="col">First Name</th>
        <th scope="col">Last Name</th>
        <th scope="col">BSN Number</th>
        <th scope="col">Phone Number</th>
        <th scope="col">Email</th>
        <th scope="col"></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.id">
        <td>{{ user.firstName }}</td>
        <td>{{ user.lastName }}</td>
        <td>{{ user.bsnnumber }}</td>
        <td>{{ user.phoneNumber }}</td>
        <td>{{ user.email }}</td>
        <td>
          <button class="btn btn-primary" @click="approveUser(user.id)">Approve</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axiosInstance from "../../axios-instance";
import { ref, onMounted } from "vue";

export default {
  name: "NoAccount",
  setup() {
    const users = ref([]);

    onMounted(async () => {
      try {
        const userResponse = await axiosInstance.get('/api/unapproved-users');
        users.value = userResponse.data;
      } catch (error) {
        console.error(error);
      }
    });

    return { users };
  },
};

</script>

<style scoped>

</style>