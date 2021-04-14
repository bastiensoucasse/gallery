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
            placeholder="username"
            required
					/>
				</div>

				<div id="email" class="form-group">
					<label for="email">Email</label>
					<input
						v-model="this.user.email"
						type="email"
						class="form-control"
						name="email"
            placeholder="email"
            required
					/>
				</div>

				<div id="firstName" class="form-group">
					<label for="firstName">Firstname</label>
					<input
						v-model="this.user.firstName"
						type="text"
						class="form-control"
						name="firstName"
            placeholder="firstName"
            required
					/>
				</div>

				<div id="lastName" class="form-group">
					<label for="lastName">LastName</label>
					<input
						v-model="this.user.lastName"
						type="text"
						class="form-control"
						name="lastName"
            placeholder="lastName"
            required
					/>
				</div>

				<div id="password" class="form-group">
					<label for="password">Password</label>
					<input
						v-model="this.user.password"
						type="password"
						class="form-control"
						name="password"
            placeholder="password"
            required
					/>
				</div>

				<div id="roles" class="form-group">
					<input
						type="radio"
						id="user"
						value="user"
						v-model="user.roles[0]"
					/>
					<label for="user">User</label>
					<input
						type="radio"
						id="premium"
						value="premium"
						v-model="user.roles[0]"
					/>
					<label for="premium">Premium</label>
					<input
						type="radio"
						id="root"
						value="root"
						v-model="user.roles[0]"
					/>
					<label for="root">Root</label>
				</div>

				<div class="form-group">
					<button class="btn btn-primary btn-block">Sign Up</button>
				</div>

				<div class="form-group">
					<div v-if="message" class="alert alert-danger" role="alert">
						{{ message }}
					</div>
				</div>
			</div>
		</form>
	</div>
</template>

<script>
import User from "../models/user";

export default {
	name: "Register",
	data() {
		return {
			user: new User("", "", "", "", "", [""]),
			submitted: false,
			successful: false,
			message: ""
		};
	},
	computed: {
		loggedIn() {
			return this.$store.state.auth.status.loggedIn;
		}
	},
	mounted() {
		if (this.loggedIn) {
			this.$router.push("/");
		}
	},
	methods: {
		handleRegister() {
			this.message = "";
			this.submitted = true;
			this.$store.dispatch("auth/register", this.user).then(
				data => {
					this.message = data.message;
					this.successful = true;
					this.$router.push("/login");
				},
				error => {
					alert(error);
					this.message =
						(error.response && error.response.data) ||
						error.message ||
						error.toString();
					this.successful = false;
				}
			);
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
