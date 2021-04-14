<template>
  <div class="container">
    
      <h3>
        <strong>{{Hi}}</strong> Profile
      </h3>
    <!--
    <p>
      <strong>Token:</strong>
      {{currentUser.jwt.substring(0, 20)}} ... {{currentUser.jwt.substr(currentUser.accessToken.length - 20)}}
    </p>
    <p>
      <strong>Id:</strong>
      {{currentUser.id}}
    </p>
    <p>
      <strong>Email:</strong>
      {{currentUser.email}}
    </p>
    <strong>Authorities:</strong>
    <ul>
      <li v-for="(role,index) in currentUser.roles" :key="index">{{role}}</li>
    </ul> -->
  </div>
</template>

<script>
export default {
  
  name: 'Profile',
  
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
  }
  
};

</script>