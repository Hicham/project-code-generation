<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card">
          <div class="card-body">
            <h1 class="mb-4">ATM Interface</h1>
            <div>
              <h3>Balance: ${{ balance }}</h3>
            </div>

            <div class="mb-3">
              <button class="btn btn-primary me-2" @click="withdraw()">Withdraw</button>
              <button class="btn btn-success me-2" @click="deposit()">Deposit</button>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>

</template>


<script>
import axios from "axios";
import { useStore } from '@/stores/store';
import axiosInstance from '@/axios-instance';
import {useRouter} from "vue-router";

export default {
  name: "Atm",
  data() {
    return {
      account: null,
    };
  },
  mounted() {
    this.getAccount();
  },
  methods: {
    getAccount() {
      axiosInstance
          .get("/api/accounts/IBANFAKE1", {
            headers: {
              Authorization: "Bearer " + useStore().token,
            }
          })
          .then((result) => {
            this.account = result.data;
            console.log(this.account);
          })
          .catch((error) => console.log(error));

    }
  }
}

</script>
