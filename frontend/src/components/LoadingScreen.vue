<template>
	<div class="loading-window">
		<div class="loading-content">
			
				
			<h1> {{title}}</h1>
		</div>
	</div>
</template>


<script>
export default {
	props:{
		title: String,
	},

	methods:{
		beforeAppear(){
			console.log("before apppear");
		},
		appear(){
			console.log("appear");
		},
		afterAppear(){
			console.log("After Appear");
		}
	}
}
</script>

<style scoped>
.loading-window {
	position: fixed;
	top: 0;
	left: 0;
	bottom: 0;
	right: 0;
	width: 100vw;
	height: 100vh;
	background-color: rgba(32, 33, 36, 0.9);
}

.loading-content {
	display: grid;
	justify-content: center;
	align-content: center;
	width: 100vw;
	height: 100vh;
}


.dots {
	display: flex;
}

.dot1 {
	background-color: crimson;
}

.dot1,
.dot2,
.dot3 {
	width: 15px;
	height: 15px;
	border-radius: 50%;
	margin: 12px;
}

.fade-enter-active {
  animation: go 10s;
}

@keyframes go {
  from {
    background: crimson;
  }
  to {
    background: aqua;
  }
}
</style>
