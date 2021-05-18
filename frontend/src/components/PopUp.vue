<template>
	<div class="window">
		<div class="content">
			<div class="popup">
				<div class="title_pop">
					<h4>
						{{ title }}
					</h4>
				</div>
				<div class="buttons">
					<button @click="$emit('delete')">Yes</button>
					<button @click="$emit('cancel')">No</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	props: {
		title: String
	},
	emits: {
		"delete" : null,
		"cancel" : null,
	}
	
};
</script>

<style scoped>
.window {
	position: fixed;
	
	width: 100%;
	height: 100%;
}

.content {
	display: grid;
	justify-content: center;
	width: 100%;
	height: 100%;
}

.popup {
	margin: 12px;
	width: 25vw;
	height: 18vh;
	background-color: #323539;
	border-radius: 12px;
	
	justify-content: center;
}

.warning-span {
	margin-left: 5px;
	margin-right: 5px;
}

.title_pop {
	text-align: center;
	padding: 14px 40px;
	margin: 0;
}

.buttons{
	display: flex;
	justify-content: center;
	align-content: center;
	gap: 30px;
}


button {
	border-radius: 10px;
	border: none;
	padding: 14px 40px;
	color: white;
	font-size: 16px;
	background-color: #0064c8;
	cursor: pointer;
}
</style>
