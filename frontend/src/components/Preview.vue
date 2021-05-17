<template>
	<div class="preview-window">
		<div class="feature-box">
			<button
				class="feature-link"
				title="Close"
				:disabled="loading"
				@click="close"
			>
				<span
					class="material-icons"
					v-bind:class="{ 'disable-icons': loading }"
					>close</span
				>
			</button>

			<button
				v-if="isImageSaved && currentUser"
				class="feature-link"
				title="Delete"
				@click="remove"
				:disabled="loading"
				v-bind:class="{ 'disable-icons': loading }"
			>
				<span class="material-icons">delete</span>
			</button>

			<button
				v-if="!isImageSaved && currentUser"
				class="feature-link"
				title="Save"
				@click="save"
				:disabled="loading"
				v-bind:class="{ 'disable-icons': loading }"
			>
				<span class="material-icons-outlined">save</span>
			</button>

			<button
				v-if="isImageSaved"
				class="feature-link"
				title="Download"
				@click="showExport = true"
				:disabled="loading"
				v-bind:class="{ 'disable-icons': loading }"
			>
				<span class="material-icons">download</span>
			</button>

			<button
				class="feature-link"
				title="Edit"
				:disabled="loading"
				v-bind:class="{ 'disable-icons': loading }"
				@click="edit"
			>
				<span class="material-icons">edit</span>
			</button>

			<button
				class="feature-link"
				title="Details"
				:disabled="loading"
				v-bind:class="{ 'disable-icons': loading }"
				@click="details"
			>
				<span class="material-icons-outlined">info</span>
			</button>
		</div>

		<div class="preview-visual">
			<img class="preview-image" :src="image" @error="$emit('close')" />
			<div class="chevrons" v-if="!loading">
				<span class="material-icons chevron" @click="$emit('loadPrevious')" @keydown.arrow-left="$emit('loadPrevious')">
					chevron_left
				</span>
				<span class="material-icons chevron" @click="$emit('loadNext')">
					chevron_right
				</span>
			</div>	
		</div>

		<div id="details-panel" class="preview-features">
			<h2 class="title">Details</h2>

			<div class="preview-details">
				<div>
					<h3 class="category">ID</h3>
					<p v-if="isImageSaved" class="paragraph">{{ id }}</p>
				</div>

				<div>
					<h3 class="category">Name</h3>
					<p class="paragraph">{{ name }}</p>
				</div>

				<div>
					<h3 class="category">Type</h3>
					<p class="paragraph">{{ type }}</p>
				</div>

				<div>
					<h3 class="category">Dimensions</h3>
					<p class="paragraph">{{ dimensions }}</p>
				</div>
			</div>
		</div>

		<div id="edit-panel" class="preview-features">
			<div v-if="!isImageSaved" class="warning">
				<p class="warning-msg">
					<span class="material-icons-outlined warning-span">
						warning </span
					>{{ warning }}
				</p>
			</div>
			<h2 class="title">Edit</h2>

			<div class="preview-details">
				<form
					class="preview-feature"
					@submit.prevent="onSubmit('resize')"
				>
					<h3 class="category">Resize [WIP]</h3>
					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						Resize
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('toGrayscale')"
				>
					<h3 class="category">Convert to grayscale</h3>
					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						to Gray Scale
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('toNegative')"
				>
					<h3 class="category">Convert to negative</h3>
					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						Apply Negative Filter
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('changeBrightness' + args(x1))"
				>
					<h3 class="category">Apply a gain to the brightness</h3>
					<input
						class="preview-input"
						v-model="x1"
						type="number"
						name="gain"
						min="-255"
						max="255"
						required
					/>
					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						Change Brightness
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('colorize' + args(x2))"
				>
					<h3 class="category">Colorize by setting the hue</h3>

					<input
						class="preview-input"
						v-model="x2"
						type="number"
						name="hue"
						min="0"
						max="360"
						required
					/>

					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						Colorize
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('extendDynamics')"
				>
					<h3 class="category">Extend the dynamics</h3>
					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						Extend Dynamic
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('equalizeHistogram' + args(x3))"
				>
					<h3 class="category">
						Equalize the saturation or brightness histogram
					</h3>

					<div class="choice">
						<input
							class="preview-radio"
							type="radio"
							v-model="x3"
							name="channel"
							value="1"
							id="c1"
							required
							:disabled="loading"
							checked
						/>
						<label for="c1">Saturation</label>

						<input
							class="preview-radio"
							type="radio"
							v-model="x3"
							name="channel"
							value="2"
							:disabled="loading"
							id="c2"
						/>
						<label for="c2">Brightness</label>
					</div>

					<input
						type="submit"
						class="theme-button"
						value="Equalize"
						:disabled="loading"
					/>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit(n1 + args(x4))"
				>
					<h3 class="category">Blur filter</h3>

					<input
						class="preview-input"
						type="number"
						name="radius"
						v-model="x4"
						min="1"
						:disabled="loading"
						max="10"
						required
					/>

					<div class="choice">
						<input
							class="preview-radio"
							type="radio"
							name="algorithm"
							v-model="n1"
							value="meanFilter"
							id="mf"
							:disabled="loading"
							checked
						/>
						<label for="mf">Mean</label>

						<input
							class="preview-radio"
							type="radio"
							name="algorithm"
							v-model="n1"
							value="gaussianFilter"
							:disabled="loading"
							id="gf"
						/>
						<label for="gf">Gaussian</label>
					</div>

					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						Apply filter"
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('sobelOperator')"
				>
					<h3 class="category">Sobel operator</h3>
					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						Apply Sobel operator"
					</button>
				</form>

				<form
					class="preview-feature"
					style="padding-bottom: 60px"
					@submit.prevent="onSubmit(n2)"
				>
					<h3 class="category">Mirror</h3>

					<div class="choice">
						<input
							class="preview-radio"
							type="radio"
							name="algorithm"
							value="horizontalMirror"
							v-model="n2"
							:disabled="loading"
							id="hm"
							checked
						/>
						<label for="hm">Horizontal</label>

						<input
							class="preview-radio"
							type="radio"
							name="algorithm"
							v-model="n2"
							:disabled="loading"
							value="verticalMirror"
							id="vm"
						/>
						<label for="vm">Vertical</label>

						<input
							class="preview-radio"
							v-model="n2"
							type="radio"
							name="algorithm"
							:disabled="loading"
							value="completeMirror"
							id="cm"
						/>
						<label for="cm" style="grid-column: span 2"
							>Complete</label
						>
					</div>

					<button
						class="theme-button"
						type="submit"
						:disabled="loading"
					>
						Apply Filter
					</button>
				</form>
			</div>
		</div>

		<div class="export-window" v-if="showExport">
			<Export
				@closeExport="showExport = false"
				:id="id"
				:name="name"
			></Export>
		</div>

		<loading-screen v-if="loading" :title="'Algorithm in Process ...'"/>
	</div>
</template>

<script>
import Export from "@/components/Export.vue";
import ImageService from "@/services/image.service";
//import Image from "/@/models/image.js";
import FileParser from "@/services/file-parser";
import LoadingScreen from '@/components/LoadingScreen.vue';

export default {
	components: {
		Export,
		LoadingScreen,
	},
	name: "Preview",

	props: {
		id: Number,
		name: String,
		type: String,
		dimensions: String,
		image: String
	},

	data() {
		return {
			showExport: false,
			parameters: "",
			x1: "",
			x2: "",
			x3: "1",
			x4: "",
			n1: "meanFilter",
			n2: "horizontalMirror",
			loading: false,
		};
	},

	emits: {
		close: null,
		updatePreview: null,
		saveImage: null,
		loadNext: null,
		loadPrevious: null,
	},

	computed: {
		currentUser() {
			return this.$store.state.auth.user;
		},

		isImageSaved() {
			return !isNaN(this.id);
		},
		warning() {
			if (this.currentUser) return "This image is not saved !";
			else return "You can't save images if you're not logged in!";
		}
	},

	methods: {
		args(x) {
			return "&x=" + x;
		},
		onSubmit(parameters) {
			this.loading = true;
			if (!this.isImageSaved) {
				let formData = FileParser.parseURLDataAsFormFile(
					this.image,
					this.name
				);
				ImageService.applyAlgorithmOnFile(
					"temp/?algorithm=" + parameters,
					formData
				)
					.then(response => {
						let reader = ImageService.readBlob(response);
						reader.onload = () => {
							this.$emit(
								"updatePreview",
								NaN,
								response.headers.name,
								reader.result,
								response.headers.type,
								response.headers.size
							);
							this.loading = false;
						};
					})
					.catch(error => {
						alert(error);
						this.loading = false;
					});
			} else {
				ImageService.applyAlgorithm(
					this.id + "?algorithm=" + parameters
				)
					.then(response => {
						let reader = ImageService.readBlob(response);
						reader.onload = () => {
							this.$emit(
								"updatePreview",
								Number(response.headers.id),
								response.headers.name,
								reader.result,
								response.headers.type,
								response.headers.size
							);
							this.loading = false;
						};
					})
					.catch(error => {
						alert(error);
						this.loading = false;
					});
			}
		},
		remove() {
			if (confirm("Dou you really want to delete this image"))
				ImageService.deleteImage(this.id, this.currentUser).then(
					response => {
						this.$emit("deleteImage", this.id);
						alert(response.data);
						location.href = "/";
					},
					error => {
						alert(error);
					}
				);
		},

		edit() {
			document.querySelector(
				".preview-window"
			).style.gridTemplateColumns = "100fr 350px";
			document.querySelector("#details-panel").style.display = "none";
			document.querySelector("#edit-panel").style.display = "block";
		},

		details() {
			document.querySelector(
				".preview-window"
			).style.gridTemplateColumns = "100fr 350px";
			document.querySelector("#edit-panel").style.display = "none";
			document.querySelector("#details-panel").style.display = "block";
		},

		save() {
			let formData = FileParser.parseURLDataAsFormFile(
				this.image,
				this.name
			);
			ImageService.save(this.currentUser, formData).then(
				response => {
					this.$emit("saveImage", Number(response.headers.id));
				},
				error => {
					alert(error);
				}
			);
		},

		close() {
			this.$emit("close");
		},
		logKey(e){
			//console.log(e);
			if(e.key === "ArrowRight" && !this.loading){
				this.$emit('loadNext');
			}else if (e.key === "ArrowLeft" && !this.loading){
				this.$emit('loadPrevious');
			}
		}

	},
	mounted(){
		console.log("preview mounted !");
		document.addEventListener('keydown', this.logKey);

	}
};
</script>

<style scoped>
.preview-window {
	position: fixed;
	top: 0;
	left: 0;
	bottom: 0;
	right: 0;
	width: 100%;
	height: 100vh;
	display: grid;
	grid-template-columns: 100fr;
}

.preview-visual {
	height: 100%;
	width: 100%;
	display: flex;
	flex-wrap: wrap;
	justify-content: center;
	align-items: center;
	background-color: rgba(32, 33, 36, 0.9);
}



.preview-image {
	display: block;
	min-height: 240px;
	min-width: 240px;
	max-height: 80vh;
	max-width: 80%;
	border-radius: 48px;
	user-select: none;
	box-shadow: 0 8px 10px 1px rgba(0, 0, 0, 0.14),
		0 3px 14px 2px rgba(0, 0, 0, 0.12), 0 5px 5px -3px rgba(0, 0, 0, 0.2);
	background-color: #323539;
	overflow: hidden;
}

.export-window {
	display: grid;
	place-items: center;
}

.preview-features {
	width: calc(100% - 2 * 24px);
	/*height: calc(100% - 2 * 12px);*/
	padding: 56px 24px 24px 24px;
	text-align: left;
	background-color: rgb(38, 40, 43);
	box-shadow: 0 4px 5px 0 rgba(0, 0, 0, 0.14),
		0 1px 10px 0 rgba(0, 0, 0, 0.12), 0 2px 4px -1px rgba(0, 0, 0, 0.2);
	max-height: 100vh;
	display: none;
	overflow: hidden;
	font-size: 14px;
}

.preview-details {
	height: 100%;
	overflow-y: scroll;
	border-top: 1px solid rgba(255, 255, 255, 0.1);
	padding: 2px;
	padding-top: 6px;
}

.preview-details::-webkit-scrollbar {
	display: none;
}

.category {
	font-size: 14px;
	margin: 0;
	margin-bottom: 6px;
}

input[type="number"] {
	display: block;
	width: calc(100% - 12px);
	height: 22px;
	margin-bottom: 6px;
	border: none;
	border-radius: 4px;
	padding: 2px 6px;
}

.choice {
	display: grid;
	grid-template-columns: 100fr 100fr;
	grid-gap: 6px;
	margin-bottom: 6px;
}

.choice label {
	display: flex;
	align-items: center;
	justify-content: center;
	height: 24px;
	font-weight: 600;
	text-align: center;
	cursor: pointer;
	background-color: #202124;
	border: 2px solid #202124;
	border-radius: 4px;
	user-select: none;
}

.choice input:checked + label {
	border-color: rgb(0, 89, 179);
}

.choice input {
	display: none;
}

.warning-span {
	float: left;
	margin-left: 5px;
	margin-right: 5px;
}

.warning-msg {
	display: inline-block;
}

.warning {
	width: auto;
	color: rgb(255, 196, 0);
}

#save_btn {
	visibility: hidden;
}

.disable-icons {
	color: #3c4855;
}

.chevrons{
	display: flex;
	justify-content: center;
	align-items: center;
	flex-basis: 100%;
}

.chevron {
	width: 100px;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100px;
	margin-left: 20px;
	margin-right: 20px;
	/*background-color: crimson;*/
	font-size: 48px;
}

.chevron:hover {
	scale: 20px;
	cursor: pointer;
	transition: font-size 0.15s;
	font-size: 150px;
}

.break {
	flex-basis: 100%;
	height: 0;
}
</style>
