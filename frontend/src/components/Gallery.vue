<template>
	<div class="gallery">
		<div class="gallery-wrapper">
			<div
				:key="image.id"
				v-for="image in response"
				v-on:click="
					loadPreview(
						image.id,
						image.name,
						images[image.id],
						image.type,
						image.size
					)
				"
				class="gallery-image"
			>
				<img :src="images[image.id]" />
			</div>
		</div>

		<div
			class="gallery-import"
			v-if="currentUser"
			title="Import a new image"
			@click="loadImporter"
		>
			<span class="material-icons">add</span>
		</div>

		<dialog-box
			:dialog-title="dialog_title"
			:dialog-msg="dialog_msg"
			@closeDialog="closeDialog"
			v-if="alertBox"
		></dialog-box>

		<preview
			:id="id"
			:name="name"
			:type="type"
			:dimensions="dimensions"
			:image="selectedImageData"
			@close="closePreview"
			@updatePreview="loadPreview"
			@saveImage="savePreview"
			@loadNext="loadNextImagePreview"
			@loadPrevious="loadPreviousImagePreview"
			v-if="isImageSelected"
		></preview>

		<importer @close="closeImporter" v-if="importing"></importer>
	</div>
</template>

<script>
import Preview from "@/components/Preview.vue";
import Importer from "@/components/Importer.vue";
import ImageService from "@/services/image.service";
import DialogBox from "@/components/DialogBox.vue";

export default {
	name: "Gallery",

	components: {
		Preview,
		Importer,
		DialogBox
	},

	data() {
		return {
			response: [],
			images: {},
			errors: [],
			selectedImageData: null,
			image_key: null,
			id: NaN,
			name: "",
			type: "",
			dimensions: "",
			importing: false,
			alertBox: false,
			dialog_title: String,
			dialog_msg: String
		};
	},

	computed: {
		currentUser() {
			return this.$store.state.auth.user;
		},
		loggedIn() {
			if (this.currentUser != null) {
				return "user/" + this.currentUser.id + "/images";
			} else {
				return "/images";
			}
		},
		isImageSelected() {
			if (this.selectedImageData != null) {
				return true;
			}
			return false;
		}
	},

	methods: {
		reloadPreview(id, name, data, type, size){
			this.id = Number(id);
			this.name = name;
			this.selectedImageData = data;
			this.type = type;
			this.dimensions = size.replaceAll("*", " × ");
		},

		loadPreview(id, name, data, type, size) {
			this.reloadPreview(id, name, data, type, size);
			this.image_key = this.getImageKey();
		},
		
		getImageKey(){
			let index;
			for(index = 0; index < this.response.length; index++){
				if(this.response[index].id == this.id){
					return index;
				}
			}
			return index;
		},
		savePreview(id) {
			this.cacheImages(this.currentUser);
			this.id = id;
		},
		

		closePreview() {
			this.id = -1;
			this.name = "";
			this.type = "";
			this.dimensions = "";
			this.selectedImageData = null;
			this.image_key = null;

		},
		loadNextImagePreview(){
			this.image_key = this.nextImageKey();
			console.log(this.image_key);
			let img = this.response[this.image_key];
			this.reloadPreview(img.id, img.name, this.images[img.id], img.type, img.size);
		},
		loadPreviousImagePreview(){
			this.image_key = this.previousImageKey();
			console.log(this.image_key);
			let img = this.response[this.image_key];
			this.reloadPreview(img.id, img.name, this.images[img.id], img.type, img.size);
		}, 

		nextImageKey(){
			return (this.image_key+1) % this.response.length;
		},

		previousImageKey(){
			return (((this.image_key-1) % this.response.length) + this.response.length)% this.response.length;
		},
		deleteImage(image_id) {
			delete this.images[image_id];
			var index;
			for (index in this.response) {
				if (this.response[index].id === image_id) {
					this.response.splice(index, 1);
				}
			}
		},

		loadImporter() {
			if (this.currentUser) this.importing = true;
			else
				this.dialogBox(
					"We're sorry",
					"To be able to import images you need to be logged in."
				);
		},

		closeImporter() {
			this.importing = false;
		},

		getImageList(user) {
			ImageService.getImageList(user)
				.then(response => {
					this.response = response.data;
				})
				.catch(e => {
					this.errors.push(e);
				});
		},

		downloadImage(user, image_id) {
			ImageService.getData(user, image_id).then(
				response => {
					let reader = new window.FileReader();
					reader.readAsDataURL(response.data);
					reader.onload = () => {
						if (!(image_id in this.images)) {
							this.images[image_id] = reader.result;
						}
					};
				},
				error => {
					this.errors.push(error);
				}
			);
		},

		cacheImages(user) {
			ImageService.getImageList(user)
				.then(response => {
					this.response = response.data;
					let img;
					for (img in this.response) {
						this.downloadImage(user, this.response[img].id);
					}
				})
				.catch(e => {
					this.errors.push(e);
				});
		}
	},

	mounted: async function() {
		await this.cacheImages(this.currentUser);
	}
};
</script>

<style scoped>
.gallery-wrapper {
	display: flex;
	justify-content: center;
	align-items: center;
	flex-wrap: wrap;
	width: 600px;
	max-width: calc(100% - 48px);
	margin: auto;
	padding: 8px 0;
}

.gallery {
	margin: auto;
}

.gallery-image {
	display: block;
	height: 120px;
	width: 120px;
	margin: 8px;
	overflow: hidden;
	border-radius: 12px;
	box-shadow: 0 10px 20px rgba(0, 0, 0, 0.19), 0 6px 6px rgba(0, 0, 0, 0.23);
	transition-property: box-shadow, transform;
	transition-timing-function: ease-in-out;
	transition-duration: 200ms;
	cursor: pointer;
}

.gallery-image:hover {
	box-shadow: 0 19px 38px rgba(0, 0, 0, 0.3), 0 15px 12px rgba(0, 0, 0, 0.22);
	transform: scale(1.04);
}

.gallery-image > img {
	width: 100%;
	height: 100%;
	object-fit: cover;
	user-select: none;
}

.gallery-import {
	position: fixed;
	bottom: 24px;
	right: 24px;
	display: flex;
	align-items: center;
	justify-content: center;
	width: 48px;
	height: 48px;
	margin: 24px;
	border-radius: 50%;
	background-color: #0064c8;
	font-weight: 600;
	font-size: 20px;
	color: #ffffff;
	transition: background-color 0.17s ease;
	cursor: pointer;
	user-select: none;
}

.gallery-import:hover {
	background-color: #0059b3;
}
</style>
