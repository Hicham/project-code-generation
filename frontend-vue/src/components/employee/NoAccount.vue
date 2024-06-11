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
          <td>{{ user.firstName }}</td>
          <td>{{ user.lastName }}</td>
          <td>{{ user.bsnnumber }}</td>
          <td>{{ user.phoneNumber }}</td>
          <td>{{ user.email }}</td>
          <td>
            <button class="btn btn-primary" @click="openApproveModal(user)">Approve</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div v-if="showModal" class="modal fade show" style="display: block;" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
          <div class="modal-body">
            <p>Set transaction limits for user ID: {{ selectedUser.email }}</p>
            <div class="form-group">
              <label for="dailyLimit">Daily Limit</label>
              <input type="number" class="form-control" id="dailyLimit" v-model="limits.dailyLimit">
            </div>
            <div class="form-group">
              <label for="absoluteLimit">Absolute Limit</label>
              <input type="number" class="form-control" id="absoluteLimit" v-model="limits.absoluteLimit">
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-primary" @click="approveUser">Approve</button>
            <button type="button" class="btn btn-secondary" @click="closeModal">Close</button>
          </div>
        </div>
      </div>
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
import {useStore} from "@/stores/store";

export default {
  name: "NoAccount",
  setup() {
    const users = ref([]);
    const showModal = ref(false);
    const showPopup = ref(false);
    const selectedUser = ref(null);
    const limits = ref({
      dailyLimit: 0,
      absoluteLimit: 0,
    });

    const openApproveModal = (user) => {
      selectedUser.value = user;
      showModal.value = true;
    };

    const closeModal = () => {
      showModal.value = false;
    };
    const approveUser = async () => {
      try {
        await axiosInstance.post(
            `/api/accounts/approve`,
            {
              userId: selectedUser.value.userId,
              transactionLimit: {
                dailyLimit: limits.value.dailyLimit,
              },
              absoluteLimit: limits.value.absoluteLimit,
            },
            {
              headers: {
                Authorization: 'Bearer ' + useStore().token,
              },
            }
        );
        users.value = users.value.filter(user => user.userId !== selectedUser.value.userId);
        showModal.value = false;
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
        const userResponse = await axiosInstance.get('/api/users/unapproved', {
          headers: {
            Authorization: 'Bearer ' + useStore().token,
          },
        });
        users.value = userResponse.data;
      } catch (error) {
        console.error(error);
      }
    });

    return {
      users,
      showModal,
      selectedUser,
      limits,
      openApproveModal,
      closeModal,
      approveUser,
      showPopup,
      closePopup,
    };
  },
};

</script>

<style scoped>

</style>