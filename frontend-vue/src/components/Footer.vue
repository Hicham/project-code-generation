<template>
  <footer class="footer text-white mt-5">
    <div class="container py-4">
      <div class="row">
        <div class="col-md-4 mb-3">
          <h5>About MyBank</h5>
          <p>MyBank is committed to providing the best banking services to our customers. Your satisfaction is our priority.</p>
        </div>
        <div class="col-md-4 mb-3">
          <h5>Contact Us</h5>
          <ul class="list-unstyled">
            <li>Email: support@mybank.com</li>
            <li>Phone: +31 625145263</li>
            <li>Address: Bijdorplaan 15, 2015 CE Haarlem</li>
          </ul>
        </div>
        <div class="col-md-4 mb-3">
          <h5>Follow Us</h5>
          <ul class="list-unstyled">
            <li><a href="#" class="text-white"><i class="fab fa-facebook-f"></i> Facebook</a></li>
            <li><a href="#" class="text-white"><i class="fab fa-twitter"></i> Twitter</a></li>
            <li><a href="#" class="text-white"><i class="fab fa-linkedin-in"></i> LinkedIn</a></li>
            <li><a href="#" class="text-white"><i class="fab fa-instagram"></i> Instagram</a></li>
          </ul>
        </div>
      </div>
      <div class="text-center pt-3">
        <p>&copy; 2024 MyBank. All Rights Reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer',
};
</script>

<style scoped>
.footer {
  background-color: #007bff;
  color: white;
  padding: 20px 0;
}

.footer a {
  color: white;
  text-decoration: none;
}

.footer a:hover {
  text-decoration: underline;
}
</style>
