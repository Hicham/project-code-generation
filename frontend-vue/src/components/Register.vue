<template>
  <section>
    <div class="container">
      <div class="row justify-content-center align-items-center">
        <div class="col-md-8">

          <div class="alert alert-danger mt-3" v-if="errorMessage">
            {{ errorMessage }}
          </div>

          <div class="alert alert-success mt-3" v-if="successMessage">
            {{ successMessage }} go to <router-link to="/login">LOGIN!</router-link>
          </div>


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
                      <label class="form-label" for="email">Email address</label>
                      <input type="email" id="email" class="form-control form-control-lg" v-model="email" />
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
import axios from '../axios-instance';

const email = ref('');
const password = ref('');
const firstName = ref('');
const lastName = ref('');
const bsn = ref('');
const phoneNumber = ref('');

const errorMessage = ref('');
const successMessage = ref('');

const register = async () => {
  errorMessage.value = '';
  successMessage.value = '';

  try {
    const response = await axios.post('/api/register', {
      email: email.value,
      password: password.value,
      firstName: firstName.value,
      lastName: lastName.value,
      BSNNumber: bsn.value,
      phoneNumber: phoneNumber.value,
      pinCode: 1234  // Or get this from user input if required
    });
    console.log(response.data);
    if (response.data === "User registered successfully") {
      successMessage.value = 'Account is aangemaakt';
    } else {
      errorMessage.value = response.data;
    }
  } catch (error) {
    console.error('There was an error registering the user!', error);
    errorMessage.value = 'Er was een probleem bij het registreren van de gebruiker';
  }
};
</script>

<style scoped>
/* Add your custom styles hier */
</style>
