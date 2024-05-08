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
                      <label class="form-label" for="email">Email address</label>
                      <input type="email" id="email" class="form-control form-control-lg" />
                    </div>
                    <div class="mb-4">
                      <label class="form-label" for="password">Password</label>
                      <input type="password" id="password" class="form-control form-control-lg" />
                    </div>
                    <div class="row mb-4">
                      <div class="col">
                        <button class="btn btn-primary btn-lg btn-block w-100" type="button">Login</button>
                      </div>
                      <div class="col">
                        <button class="btn btn-secondary btn-lg btn-block w-100" type="button">Login ATM</button>
                      </div>
                    </div>

                    <div>
                      <div>
                        <a class="small" href="#">Forgot password</a>
                      </div>
                      <div>
                        <a class="small" href="/register">Register here</a>
                      </div>
                    </div>

                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { storeToRefs } from 'pinia';
import axios from '../axios-auth';
import { useStore } from '@/stores/store';

export default {
  name: "Login",
  setup() {
    const store = useStore();
    return { store };
  },
  data() {
    return {
      email: "",
      password: "",
      errorMessage : "",
    };
  },
  methods: {
    login() {
      this.store.login(this.email, this.password)
          .then((res) => {
            this.$router.replace("/home");
          })
          .catch((error) => this.errorMessage = error.data.errorMessage);
    },
  }

};
</script>


<style>

</style>