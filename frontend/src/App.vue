<template>
	<div class="wrap">
		<div id="navbar" style="position: relative;"  class="navigation">
			<nav class="navbar">
				<ul>
					<li class="nav-item" style="float:left">
						<router-link class="nav-link" to="/">
							<div class="home-button">
								<span class="material-icons-outlined">
									home
								</span>
								Home
							</div>
						</router-link>
					</li>

					<li
						v-if="!currentUser"
						class="nav-item"
						style="float:right"
					>
						<router-link
							v-if="!registerPage"
							to="/register"
							class="nav-link"
						>
							<div class="register">
								<span class="material-icons-outlined">
									account_circle
								</span>
								Sign up
							</div>
						</router-link>
					</li>
					<li
						v-if="!currentUser"
						class="nav-item"
						style="float:right"
					>
						<router-link
							v-if="!loginPage"
							to="/login"
							class="nav-link"
						>
							<div class="login">
								<span class="material-icons-outlined"
									>login</span
								>
								Login
							</div>
						</router-link>
					</li>
					<li class="nav-item" style="float:right">
						<a
							v-if="currentUser"
							class="nav-link"
							@click.prevent="logOut"
						>
							<div class="logout">
								<span class="material-icons-outlined">
									logout
								</span>
								Logout
							</div>
						</a>
					</li>
				</ul>
			</nav>
		</div>
		<div class="app-content">
			<router-view />
		</div>
	</div>
</template>

<script>
export default {
	data() {
		return {
			navbar: null,
			offset: null,
		};
	},
	computed: {
		currentUser() {
			return this.$store.state.auth.user;
		},
		loginPage() {
			return this.$route.path === "/login";
		},
		registerPage() {
			return this.$route.path === "/register";
		},
		
	},
	methods: {
		logOut() {
			this.$store.dispatch("auth/logout");
			this.$router.push("/login");
		},
		stickNavBar() {
			if (window.pageYOffset > this.offset) {
				this.navbar.style.position = 'fixed';
			} else {
				this.navbar.style.position = 'relative';
			}
		},
		
	},
	mounted() {
		this.navbar = document.getElementById("navbar");
		this.offset = this.navbar.offsetTop;
		window.addEventListener('scroll', this.stickNavBar);
		
	}
};
</script>

<style>
html {
	height: 100%;
}
body {
	font: 400 16px/20px "Open Sans", sans-serif;
	color: #ffffff;
	background-color: #202124;
	margin: 0;
	height: auto;
	width: 100%;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
}

header {
	position: fixed;
	top: 0;
	left: 0;
	display: flex;
	padding: 0 24px;
	width: auto;
	height: auto;
}

.logo {
	margin: 16px 0;
	font-weight: 600;
	font-size: 18px;
	line-height: 24px;
	text-transform: uppercase;
}

.title {
	margin: 0;
	margin-bottom: 14px;
	font-weight: 600;
	font-size: 16px;
	line-height: 20px;
	text-transform: uppercase;
}

.paragraph {
	position: relative;
	display: block;
	margin: 0;
	margin-bottom: 20px;
	font-size: 14px;
	line-height: 20px;
	color: rgb(128, 128, 128);
}

.theme-button {
	position: relative;
	display: flex;
	align-items: center;
	justify-content: center;
	box-sizing: border-box;
	width: 100%;
	height: 32px;
	min-width: 60px;
	min-height: 32px;
	padding: 2px 16px;
	margin-bottom: 28px;
	font-size: 14px;
	font-weight: 600;
	line-height: 16px;
	border: none;
	border-radius: 4px;
	background-color: #0064c8;
	color: #ffffff;
	transition: background-color 0.17s ease;
	cursor: pointer;
	user-select: none;
}

.theme-button:hover {
	background-color: #0059b3;
}

.feature-box {
	position: absolute;
	top: 0px;
	right: 0px;
	display: flex;
	flex-direction: row-reverse;
	padding: 11px 8px;
	z-index: 2;
}

.feature-link {
	display: flex;
	align-items: center;
	justify-content: center;
	width: 34px;
	height: 34px;
	margin-right: 8px;
	cursor: pointer;
	background: none;
	border: none;
	border-radius: 6px;
	color: #ffffff;
	user-select: none;
}

.feature-link:hover {
	background: rgba(255, 255, 255, 0.1);
}

.register {
	display: flex;
	justify-content: center;
	background-color: #0059b3;
	width: auto;
	height: auto;
	padding: 8px;
	padding-left: 25px;
	padding-right: 25px;
	border-radius: 4px;
}
.home-button,
.login {
	padding: 8px;
}

.home,
.login,
.home-button,
.logout {
	display: flex;
	justify-content: center;
	align-items: center;
	width: auto;
	height: 100%;
	padding-left: 25px;
	padding-right: 25px;
}

li {
	display: inline;
	margin: 10px;
	color: #ffffff;
}

.navigation {
	margin: auto;
	top: 0;
	width: 100%;
	height: auto;
	/*background: #15181b;*/
	overflow: hidden;
}

.wrap {
	width: 100%;
	margin: 0 auto;

	height: 100%;
}

.app-content {
	height: unset;
	width: auto;
}

nav {
	padding: 10px;
	list-style-type: none;
	margin: 0px;
	padding: 0px;
}

.nav-link {
	font-size: 1.2em;
}

ul {
	overflow: hidden;
	padding-left: 20px;
	padding-right: 20px;
}

#app {
	width: 100%;
	height: 100%;
	display: inline-block;
}
</style>

<style scoped>
a {
	color: #ffffff;
	text-decoration: none;
	font-size: 1em;
	cursor: pointer;
}
.material-icons-outlined {
	margin-right: 10px;
}
</style>
