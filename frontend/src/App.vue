<template>
	<div class="navigation">
		<nav class="navbar">
			<ul>
				<li class="full-width" id="home" >
					<router-link class="nav-link" to="/">Home</router-link>
				</li>

				
					<li v-if="!currentUser" class="nav-item">
						<router-link to="/register" class="nav-link"
							>Sign Up</router-link
						>
					</li>

					<li v-if="!currentUser" class="nav-item">
						<router-link to="/login" class="nav-link"
							>Login</router-link
						>
					</li>
				

				<li id="home" class="nav-link">
					<a
						v-if="currentUser"
						class="nav-link"
						@click.prevent="logOut"
						>Logout</a
					>
				</li>

				<li id="board" class="nav-item">
					<router-link to="/board" class="nav-link"
						>Test Board
					</router-link>
				</li>
			</ul>
		</nav>
	</div>
	<router-view />
</template>

<script>



export default {
	data() {
		return {
        };

	},
	computed: {
		currentUser() {
			return this.$store.state.auth.user;
		},
	},
	methods: {
		logOut() {
			this.$store.dispatch("auth/logout");
			this.$router.push("/login");
		}
	},
    
};
</script>

<style>
body {
	font: 400 16px/20px "Open Sans", sans-serif;
	color: #ffffff;
	background-color: #202124;
	margin: 0;
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

li {
	
	display: inline;
	margin: 30px;
	color: #ffffff;
}

.navigation {
	width: 100%;
	height: 100%;
}

nav{
    padding: 10px;
    list-style-type: none;
    margin: 0px;
    padding: 0px;
}

ul{
    display: grid;
    grid-template-columns: 1fr repeat(7, 250px);
    grid-gap: 10px;
    justify-content: center;
    justify-items: center;
    justify-self: start;
}
.full-width{
    grid-column: 1/4;
}



</style>

<style scoped>
a {
	color: #ffffff;
}
</style>
