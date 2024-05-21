<template>
  <div class="container mt-5">
    <div v-if="users.length === 0" class="alert alert-info">
      No Users to approve
    </div>
    <div v-else>
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
      <tr v-for="user in users" :key="user.userId">
        <td>{{ user.userId }}</td>
        <td>{{ user.firstName }}</td>
        <td>{{ user.lastName }}</td>
        <td>{{ user.bsnnumber }}</td>
        <td>{{ user.phoneNumber }}</td>
        <td>{{ user.email }}</td>
        <td>
          <button class="btn btn-primary" @click="approveUser(user.userId)">Approve</button>
        </td>
      </tr>
      </tbody>
    </table>
    </div>
    <div v-if="showPopup" class="modal fade show" style="display: block;" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
          <div class="modal-body">
            <p>Accounts have been created successfully.</p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" @click="closePopup">Close</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axiosInstance from "../../axios-instance";
import { ref, onMounted } from "vue";

export default {
  name: "NoAccount",
  setup() {
    const users = ref([]);
    const showPopup = ref(false);

    const approveUser = async (userId) => {
      console.log(userId);
      try {
        await axiosInstance.post(`/api/accounts?userId=${userId}`);
        users.value = users.value.filter((user) => user.userId !== userId);
        showPopup.value = true;
      } catch (error) {
        console.error(error);
      }
    };

    const closePopup = () => {
      showPopup.value = false;
    };

    onMounted(async () => {
      try {
        const userResponse = await axiosInstance.get('/api/unapproved-users');
        users.value = userResponse.data;
      } catch (error) {
        console.error(error);
      }
    });

    return { users, approveUser, showPopup, closePopup};
  },
};

</script>

<style scoped>

</style>