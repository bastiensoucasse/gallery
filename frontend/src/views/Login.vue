<template>
	<div class="form">
		<form name="form" @submit.prevent="handleLogin">
			<div class="form-group">
				<label for="username">Username</label>
				<input
					v-model="user.username"
					type="text"
					class="form-control"
					name="username"
                    placeholder="username"
                    required
				/>
			</div>
			<div class="form-group">
				<label for="password">Password</label>
				<input
					v-model="user.password"
					type="password"
					class="form-control"
					name="password"
                    placeholder="password"
                    required
				/>
			</div>
			<div class="form-group">
				<button class="Login" :disabled="loading" type="submit">
					<span>Login</span>
				</button>
			</div>
			<div class="form-group">
				<div v-if="message" class="alert alert-danger" role="alert">
					{{ message }}
				</div>
			</div>
		</form>
	</div>
</template>

<script>
import User from "@/models/user";

export default {
	name: "Login",

	data() {
		return {
			user: new User(" ", " "),
			loading: false,
			message: ""
		};
	},
	computed: {
		loggedIn() {
			return this.$store.state.auth.status.loggedIn;
		}
	},
	created() {
		if (this.loggedIn) {
			this.$router.push("/");
		}
	},

	methods: {
		/*login(){
            axios.post('auth/signin', {username: this.user.username, password: this.user.password})
            .then(resp => {
                alert(resp.data);
            });
        },*/

		handleLogin() {
			this.loading = true;
			if (this.user.username && this.user.password) {
				this.$store.dispatch("auth/login", this.user).then(
					() => {
						this.$router.push("/");
                        
					},
					error => {
						this.loading = false;
						this.message =
							(error.response && error.response.data) ||
							error.message ||
							error.toString();
					}
				);
			}
		}
		/*
        handleLogin(){
            this.loading = true;
            this.$validator.validateAll().then(isValid =>{
                if(!isValid){
                    this.loading = false;
                    return;
                }
                if(this.user.username && this.user.password){
                    this.$store.dispatch("auth/login", this.user).then(
                        () => {
                            this.$router.push('/profile');
                        },
                        error => {
                            this.loading = false;
                            this.message = (error.response && error.response.data) ||
                                            error.message  ||
                                            error.toString();
                        }
                    );
                }
            });
        }
        */
	}
};
</script>

<style>
.form {
	width: 100%;
	height: 100%;
	display: grid;
	place-items: center;
}

label {
	display: block;
	margin: 10px;
}

.form-group {
	margin: 30px;
}
</style>
