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
                  <form @submit.prevent="register">
                    <div class="d-flex align-items-center mb-3 pb-1">
                      <span class="h1 fw-bold mb-0">Mybank</span>
                    </div>
                    <h5 class="fw-normal mb-3 pb-3" style="letter-spacing: 1px;">Create an account</h5>
                    <div class="mb-4">
                      <label class="form-label" for="username">Username</label>
                      <input type="text" id="username" class="form-control form-control-lg" v-model="username" />
                    </div>
                    <div class="mb-4">
                      <label class="form-label" for="password">Password</label>
                      <input type="password" id="password" class="form-control form-control-lg" v-model="password" />
                    </div>
                    <div class="mb-4">
                      <label class="form-label" for="firstName">First Name</label>
                      <input type="text" id="firstName" class="form-control form-control-lg" v-model="firstName" />
                    </div>
                    <div class="mb-4">
                      <label class="form-label" for="lastName">Last Name</label>
                      <input type="text" id="lastName" class="form-control form-control-lg" v-model="lastName" />
                    </div>
                    <div class="mb-4">
                      <label class="form-label" for="email">Email address</label>
                      <input type="email" id="email" class="form-control form-control-lg" v-model="email" />
                    </div>
                    <div class="mb-4">
                      <label class="form-label" for="bsn">BSN Number</label>
                      <input type="text" id="bsn" class="form-control form-control-lg" v-model="bsn" />
                    </div>
                    <div class="mb-4">
                      <label class="form-label" for="phone">Phone Number</label>
                      <input type="tel" id="phone" class="form-control form-control-lg" v-model="phoneNumber" />
                    </div>
                    <div class="row mb-4">
                      <div class="col">
                        <button class="btn btn-primary btn-lg btn-block w-100" type="submit">Register</button>
                      </div>
                    </div>
                    <div>
                      <div>
                        <a class="small" href="/login">Already have an account? Login here</a>
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

<script setup>
import { ref } from 'vue';

const firstName = ref('');
const lastName = ref('');
const email = ref('');
const bsn = ref('');
const phoneNumber = ref('');

const register = () => {
  // Handle registration logic
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
