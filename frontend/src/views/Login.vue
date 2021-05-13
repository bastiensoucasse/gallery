<template>
	<div class="form">
		<form class="custom-form" name="form" @submit.prevent="handleLogin">
			<div class="form-group">
				<div class="welcome-form"> 
					<label class="form-message"> Let's get you logged in !</label>
					<label class="small-form-message">Log into your account </label>
				</div>
			</div>
			<div class="form-group">
				<label class="form-label" for="username">Username</label>
				<input
					v-model="user.username"
					type="text"
					class="form-control"
					name="username"
                    required
				/>
			</div>
			<div class="form-group">
				<label class="form-label" for="password">Password</label>
				<input
					v-model="user.password"
					type="password"
					class="form-control"
					name="password"
                    required
				/>
			</div>
			<div class="form-group">
				<input class="submit-button" :disabled="loading" value="Login" type="submit">
			</div>
			<div class="form-group">
				<label>You don't have an account yet ? <router-link to="/register">Sign up here </router-link> </label>
			</div>
			<div class="form-group">
				<div v-if="message" class="alert-danger" role="alert">
					<span class="material-icons-outlined">
						dangerous
					</span>
					{{ message }}
				</div>
			</div>
		</form>
		<loading-screen v-if="loading"/>
	</div>
</template>

<script>
import User from "@/models/user";
import LoadingScreen from '../components/LoadingScreen.vue';

export default {
  components: { LoadingScreen },
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
                console.log("user from login.vue " + this.user.username);
				this.$store.dispatch("auth/login", this.user).then(
					() => {
						this.$router.push("/");
                        
					},
					error => {
						this.loading = false;
						this.message = error.response.data.message + " !";
					}
				);
			}
		}
		
              
	}
};
</script>

<style scoped>


.form-control, .submit-button{
	padding: 13px 8px;
	box-sizing: border-box;
	border-radius: 10px;
	width: 100%;
	font-size: 1.05em;
	background-color: #312f30;
	color: white;
	border-color: rgba(0, 0, 0, 0);
	box-shadow: none;
	-webkit-box-shadow: none;
}

.form-control:focus{
	border-radius: 10px;
	border-color: #0064c8;
	outline: none;
}

.submit-button{
	background-color:  rgb(0, 89, 179);
	border: 1px;
	color: white;
	font-size: 1.2em;
	cursor: pointer;
}

.form {
	display: grid;
	justify-content: center;
	align-content: center;
	width: 100%;
	height: 100%;
	margin-top: 15px;
	padding-top: 15px;
}

.custom-form{
	width: auto;
	height: auto;
	border-radius: 5px;
	padding: 12px;

	
}

.form-control[type=submit]{
	background-color: #0064c8;
	color: white;
	border: 1px;

}

label {
	display: block;
	margin: 10px;
	text-align: left;
	
}

.form-label{
	font-size: 1.2em;
	color: rgb(179, 179, 179);
}

.form-message{
	font-size: 1.5em;
	margin-bottom: 15px;
}

.welcome-form{
	width: auto;
	height: auto;
	margin-bottom: 45px;
}

.small-form-message{

	color: rgb(179, 179, 179);
}

.form-group {
	margin-left: 30px;
	margin-right: 30px;
	margin-top: 0px;
	margin-bottom: 30px;
}

.login-logo{
	display: grid;
	justify-content: center;
	align-content: center;
	background-color: #0064c8;
	border-radius: 50%;
	width: 145px;
	height: 145px;
	margin: auto;
}

.alert-danger{
	display: flex;
	justify-content: center;
	text-align: center;
	color: rgb(226, 54, 54);


}

a{
	color: coral;
}
.material-icons-outlined{
	width: auto;
	height: auto;
	margin-left: 12px;
	margin-right: 12px;
}

</style>


