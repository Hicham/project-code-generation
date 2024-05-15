<template>
  <section>
    <div class="container">
      <div class="row justify-content-center align-items-center">
        <div class="col-md-8">
          <div class="card rounded-3">
            <div class="row g-0">
              <div class="col-md-4">
                <div class="d-flex justify-content-center align-items-center h-100 bg-primary text-white rounded-start">
                  <i class="fa-solid fa-piggy-bank" style="font-size: 3em;"></i>
                </div>
              </div>
              <div class="col-md-8">
                <div class="card-body p-4 p-lg-5">
                  <form>
                    <div class="d-flex align-items-center mb-3 pb-1">
                      <span class="h1 fw-bold mb-0">Mybank</span>
                    </div>
                    <h5 class="fw-normal mb-3 pb-3" style="letter-spacing: 1px;">Sign into your account</h5>
                    <div class="mb-4">
                      <label class="form-label" for="email">Card number</label>
                      <input v-model="cardNumber" type="email" id="cardNumber" class="form-control form-control-lg" />
                    </div>
                    <div class="mb-4">
                      <label class="form-label" for="password">Pincode</label>
                      <input v-model="pincode" type="password" id="pincode" class="form-control form-control-lg" />
                    </div>
                    <div class="row mb-4">
                      <div class="col">
                        <button @click="login" class="btn btn-primary btn-lg btn-block w-100" type="button">Login</button>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>

          <div class="alert alert-danger mt-3" v-if="errorMessage">
            {{ errorMessage }}
          </div>

        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { useStore } from '@/stores/store';
import { useRouter } from 'vue-router';

export default {
  name: "Login",
  data() {
    return {
      cardNumber: "",
      pincode: "",
      errorMessage: "",
    };
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    return { store, router };
  },
  methods: {
    login() {
      this.store.loginCard(this.cardNumber, this.pincode)
          .then(() => {
            this.errorMessage = ""; // Reset error message
            alert("Logged in! Bearer token: " + this.store.token);

            this.router.push("/atm");
          })
          .catch(() => {
            this.errorMessage = "Invalid card combination";
            this.cardNumber = "";
            this.pincode = "";
            setTimeout(() => {
              this.errorMessage = ""; // Clear error message after 10 seconds
            }, 10000); // 10 seconds
          });
    },
  }
};
</script>

