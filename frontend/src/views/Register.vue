<template>
<div class="form">
    <form name="form" @submit.prevent="handleRegister">
        <div v-if="!successful">
          <div id="userName" class="form-group">
            <label for="username">Username</label>
            <input
              v-model="this.user.username"
              type="text"
              class="form-control"
              name="username"
            />
          
          </div>

          <div id="email" class="form-group">
            <label for="email">Email</label>
            <input
              v-model="this.user.email"
              
              type="email"
              class="form-control"
              name="email"
            />
          </div>

           <div id="firstName" class="form-group">
            <label for="firstName">Firstname</label>
            <input
              v-model="this.user.firstName"
              
              type="text"
              class="form-control"
              name="firstName"
            />
            
          </div>

           <div id="lastName" class="form-group">
            <label for="lastName">LastName</label>
            <input
              v-model="this.user.lastName"
              
              type="text"
              class="form-control"
              name="lastName"
            />
          </div>


          <div id="password" class="form-group">
            <label for="password">Password</label>
            <input
              v-model="this.user.password"
              type="password"
              class="form-control"
              name="password"
            />
          </div>

          <div id="roles" class="form-group">
            <input type="radio" id="user" value="user" v-model="role" />
            <label for="user">User</label>
            <input type="radio" id="premium" value="premium" v-model="role" />
            <label for="premium">Premium</label>
            <input type="radio" id="root" value="root" v-model="role" />
            <label for="root">Root</label>
           </div>

          <div class="form-group">
            <button class="btn btn-primary btn-block">Sign Up</button>
          </div>

        </div>
      </form>

</div>

</template>


<script>
import User from '../models/user';

export default {
  name: 'Register',
  data() {
    return {
      user: new User('', '', '', '', '', []),
      submitted: false,
      successful: false,
      role: null,
      message: ''
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  mounted() {
    if (this.loggedIn) {
      this.$router.push('/');
    }
  },
  methods: {
    handleRegister(){
      this.message = '';
      this.submitted = true;
      this.user.roles = [this.role.toString()];
      this.$store.dispatch('auth/register', this.user).then(
        data => {
          this.message = data.message;
          this.successful = true;
          this.$router.push('/login');
        },
        error => {
          alert(error)
        }
      )
    }
    /*handleRegister() {
      this.message = '';
      this.submitted = true;
      this.$validator.validate().then(isValid => {
        if (isValid) {
          this.$store.dispatch('auth/register', this.user).then(
            data => {
              this.message = data.message;
              this.successful = true;
            },
            error => {
              this.message =
                (error.response && error.response.data) ||
                error.message ||
                error.toString();
              this.successful = false;
            }
          );
        }
      });
    }*/
  }
};
</script>