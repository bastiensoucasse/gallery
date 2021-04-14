<template>
	<div>
		<div class="title">
			<h1>Test Board</h1>
			<h2>{{ role }}</h2>
		</div>
		<div class="choices">
			<div id="all">
				<button @click="all">Test public</button>
			</div>

			<div id="user">
				<button @click="user">Test User</button>
			</div>

			<div id="premium">
				<button @click="premium">Test Premium</button>
			</div>

			<div id="root">
				<button @click="root">Test Root</button>
			</div>
		</div>
		<div id="message">
			<h3>{{ message }}</h3>
		</div>
	</div>
</template>

<script>
import UserService from "@/services/user.service";
export default {
	data() {
		return {
			message: "",
			error: []
		};
	},

	methods: {
		all() {
			UserService.getPublicContent().then(
				response => {
					this.message = "You have access to: " + response.data;
				},
				error => {
					this.message =
						"Error you should be able to see public content !!!";
					this.error.push(error);
				}
			);
		},
		user() {
			UserService.getUserBoard().then(
				response => {
					this.message = "You have access to: " + response.data;
				},
				error => {
					this.message = "You are not authentified";
					this.error.push(error);
				}
			);
		},
		premium() {
			UserService.getPremiumBoard().then(
				response => {
					this.message = "You have access to: " + response.data;
				},
				error => {
					this.message = "You are not authentified as premium";
					this.error.push(error);
				}
			);
		},
		root() {
			UserService.getRootBoard().then(
				response => {
					this.message = "You have access to: " + response.data;
				},
				error => {
					this.message = "You are not authentified as root";
					this.error.push(error);
				}
			);
		}
	},
	computed: {
		currentUser() {
			return this.$store.state.auth.user;
		},

		role() {
			if (!this.currentUser) {
				return "You are not authentified";
			}
			return (
				"You are authentified as: " + this.currentUser.roles.toString()
			);
		}
	}
};
</script>

<style>
.title {
	height: 100%;
	width: 100%;
	display: flex;
	flex-direction: column;
	justify-content: center;
	text-align: center;
}

#message{
    height: 100%;
	width: 100%;
	display: flex;
	flex-direction: column;
	justify-content: center;
	text-align: center;
}

h1 {
	display: inline;
	text-align: center;
	margin-right: auto;
	margin-left: auto;
}

.choices {
	display: grid;
	justify-content: center;
}

button {
	margin: 30px;
}
</style>
