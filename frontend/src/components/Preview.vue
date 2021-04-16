<template>
<<<<<<< HEAD
  <div class="preview-window">
    <div class="feature-box">
      <button class="feature-link" @click="close">
        <span class="material-icons">close</span>
      </button>

      <button class="feature-link" @click="remove">
        <span class="material-icons">delete</span>
      </button>

      <button class="feature-link" @click="showExport = true">
        <span class="material-icons">download</span>
      </button>

      <button class="feature-link" @click="edit">
        <span class="material-icons">edit</span>
      </button>

      <button class="feature-link" @click="details">
        <span class="material-icons-outlined">info</span>
      </button>

      <button class="feature-link" id="save_btn" @click="save">
        <span class="material-icons-outlined">save</span>
      </button>
    </div>

    <div class="preview-visual">
      <img
        class="preview-image"
        :src="'/images/' + id"
        @error="$emit('close')"
      />
    </div>

    <div id="details-panel" class="preview-features">
      <h2 class="title">Details</h2>

      <div class="preview-details">
        <div>
          <h3 class="category">ID</h3>
          <p class="paragraph">{{ id }}</p>
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
      <h2 class="title">Edit</h2>

      <div class="preview-details">
        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">Resize [WIP]</h3>

          <input type="hidden" name="algorithm" value="resize" />

          <input class="theme-button" type="submit" value="Resize" />
        </form>

        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">Convert to grayscale</h3>

          <input type="hidden" name="algorithm" value="toGrayscale" />

          <input class="theme-button" type="submit" value="To grayscale" />
        </form>

        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">Convert to negative</h3>

          <input type="hidden" name="algorithm" value="toNegative" />

          <input class="theme-button" type="submit" value="To negative" />
        </form>

        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">Apply a gain to the brightness</h3>

          <input type="hidden" name="algorithm" value="changeBrightness" />

          <input
            class="preview-input"
            type="number"
            name="gain"
            min="-255"
            max="255"
            required
          />

          <input class="theme-button" type="submit" value="Change brightness" />
        </form>

        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">Colorize by setting the hue</h3>

          <input type="hidden" name="algorithm" value="colorize" />

          <input
            class="preview-input"
            type="number"
            name="hue"
            min="0"
            max="360"
            required
          />

          <input class="theme-button" type="submit" value="Colorize" />
        </form>

        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">Extend the dynamics</h3>

          <input type="hidden" name="algorithm" value="extendDynamics" />

          <input class="theme-button" type="submit" value="Extend dynamics" />
        </form>

        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">
            Equalize the saturation or brightness histogram
          </h3>

          <input type="hidden" name="algorithm" value="equalizeHistogram" />

          <div class="choice">
            <input
              class="preview-radio"
              type="radio"
              name="channel"
              value="1"
              checked
              id="c1"
            />
            <label for="c1">Saturation</label>

            <input
              class="preview-radio"
              type="radio"
              name="channel"
              value="2"
              id="c2"
            />
            <label for="c2">Brightness</label>
          </div>

          <input
            class="theme-button"
            type="submit"
            value="Equalize histogram"
          />
        </form>

        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">Blur filter</h3>

          <input
            class="preview-input"
            type="number"
            name="radius"
            min="1"
            max="10"
            required
          />

          <div class="choice">
            <input
              class="preview-radio"
              type="radio"
              name="algorithm"
              value="meanFilter"
              id="mf"
              checked
            />
            <label for="mf">Mean</label>

            <input
              class="preview-radio"
              type="radio"
              name="algorithm"
              value="gaussianFilter"
              id="gf"
            />
            <label for="gf">Gaussian</label>
          </div>

          <input class="theme-button" type="submit" value="Apply filter" />
        </form>

        <form class="preview-feature" method="get" :action="'/images/' + id">
          <h3 class="category">Sobel operator</h3>

          <input type="hidden" name="algorithm" value="sobelOperator" />

          <input class="theme-button" type="submit" value="Apply operator" />
        </form>

        <form
          class="preview-feature"
          style="padding-bottom: 60px"
          method="get"
          :action="'/images/' + id"
        >
          <h3 class="category">Mirror</h3>

          <div class="choice">
            <input
              class="preview-radio"
              type="radio"
              name="algorithm"
              value="horizontalMirror"
              id="hm"
              checked
            />
            <label for="hm">Horizontal</label>

            <input
              class="preview-radio"
              type="radio"
              name="algorithm"
              value="verticalMirror"
              id="vm"
            />
            <label for="vm">Vertical</label>

            <input
              class="preview-radio"
              type="radio"
              name="algorithm"
              value="completeMirror"
              id="cm"
            />
            <label for="cm" style="grid-column: span 2">Complete</label>
          </div>

          <input class="theme-button" type="submit" value="Apply filter" />
        </form>
      </div>
    </div>

    <div class="export-window" v-if="showExport">
      <Export @closeExport="showExport = false" :id="id" :name="name"></Export>
    </div>
  </div>
=======
	<div class="preview-window">
		<div class="feature-box">
			<button class="feature-link" title="Close" @click="close">
				<span class="material-icons">close</span>
			</button>

			<button v-if="isImageSaved" class="feature-link" title="Delete" @click="remove">
				<span class="material-icons">delete</span>
			</button>

			<button v-if="!isImageSaved" class="feature-link" title="Save" @click="save">
				<span class="material-icons-outlined">save</span>
			</button>

			<button v-if="isImageSaved" class="feature-link" title="Download" @click="showExport = true">
				<span class="material-icons">download</span>
			</button>

			<button class="feature-link" title="Edit" @click="edit">
				<span class="material-icons">edit</span>
			</button>

			<button class="feature-link" title="Details" @click="details">
				<span class="material-icons-outlined">info</span>
			</button>
		</div>

		<div class="preview-visual">
			<img class="preview-image" :src="image" @error="$emit('close')" />
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
			<h2 class="title">Edit</h2>

			<div class="preview-details">
				<form
					class="preview-feature"
					@submit.prevent="onSubmit('resize')"
				>
					<h3 class="category">Resize [WIP]</h3>
					<button class="theme-button" type="submit">Resize</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('toGrayscale')"
				>
					<h3 class="category">Convert to grayscale</h3>
					<button class="theme-button" type="submit">
						to Gray Scale
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('toNegative')"
				>
					<h3 class="category">Convert to negative</h3>
					<button class="theme-button" type="submit">
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
					<button class="theme-button" type="submit">
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

					<button class="theme-button" type="submit">Colorize</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('extendDynamics')"
				>
					<h3 class="category">Extend the dynamics</h3>
					<button class="theme-button" type="submit">
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
							checked
						/>
						<label for="c1">Saturation</label>

						<input
							class="preview-radio"
							type="radio"
							v-model="x3"
							name="channel"
							value="2"
							id="c2"
						/>
						<label for="c2">Brightness</label>
					</div>

					<input type="submit" class="theme-button" value="Equalize" />
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
							checked
						/>
						<label for="mf">Mean</label>

						<input
							class="preview-radio"
							type="radio"
							name="algorithm"
							v-model="n1"
							value="gaussianFilter"
							id="gf"
						/>
						<label for="gf">Gaussian</label>
					</div>

					<button class="theme-button" type="submit">
						Apply filter"
					</button>
				</form>

				<form
					class="preview-feature"
					@submit.prevent="onSubmit('sobelOperator')"
				>
					<h3 class="category">Sobel operator</h3>
					<button class="theme-button" type="submit">
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
							id="hm"
							checked
						/>
						<label for="hm">Horizontal</label>

						<input
							class="preview-radio"
							type="radio"
							name="algorithm"
							v-model="n2"
							value="verticalMirror"
							id="vm"
						/>
						<label for="vm">Vertical</label>

						<input
							class="preview-radio"
							v-model="n2"
							type="radio"
							name="algorithm"
							value="completeMirror"
							id="cm"
						/>
						<label for="cm" style="grid-column: span 2"
							>Complete</label
						>
					</div>

					<button class="theme-button" type="submit">
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
	</div>
>>>>>>> preview_vue
</template>

<script>

import Export from "@/components/Export.vue";
import ImageService from "@/services/image.service";
import FileParser from "@/services/file-parser";

export default {
<<<<<<< HEAD
  components: {
    Export,
  },
  name: "Preview",

  props: {
    id: Number,
    name: String,
    type: String,
    dimensions: String,
  },

  data() {
    return {
      showExport: false,
    };
  },

  emits: {
    close: null,
  },

  mounted: function () {
    this.hideOrShow();
  },

  methods: {
    remove() {
      if (confirm("Dou you really want to delete this image"))
        axios.delete("images/" + this.id).then(() => {
          location.href = "/";
        });
    },

    download() {
      axios.get("images/" + this.id, { responseType: "blob" }).then((r) => {
        var reader = new window.FileReader();
        reader.readAsDataURL(r.data);
        reader.onload = () => {
          const link = document.createElement("a");
          link.href = reader.result;
          link.setAttribute("download", this.name);
          document.body.appendChild(link);
          link.click();
        };
      });
    },

    edit() {
      document.querySelector(".preview-window").style.gridTemplateColumns =
        "100fr 350px";
      document.querySelector("#details-panel").style.display = "none";
      document.querySelector("#edit-panel").style.display = "block";
    },

    details() {
      document.querySelector(".preview-window").style.gridTemplateColumns =
        "100fr 350px";
      document.querySelector("#edit-panel").style.display = "none";
      document.querySelector("#details-panel").style.display = "block";
    },

    save() {
      axios.get("images/" + this.id + "/saving").catch(function (error) {
        if (error.response) {
          console.log(error.response.data);
          console.log(error.response.status);
          console.log(error.response.headers);
        }
      });
    },

    close() {
      axios.get("/images/" + this.id + "/properties").then((r) => {
        var img = r.data;
        console.log(img["new"]);
        if (img["new"]) {
          axios.delete("images/" + this.id).then(() => {
            location.href = "/";
          });
        }
        this.$emit("close");
      });
    },
    hideOrShow() {
      axios.get("/images/" + this.id + "/properties").then((r) => {
        var img = r.data;
        if (img["new"]) {
          document.getElementById("save_btn").style.visibility = "visible";
        }
      });
    },
  },
=======
	components: {
		Export
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
			loading: false
		};
	},

	emits: {
		close: null,
		updatePreview: null,
		saveImage: null
	},

	computed: {
		currentUser() {
			return this.$store.state.auth.user;
		},

		isImageSaved() {
			return !isNaN(this.id);
		}
	},

	methods: {
    args(x) {
			return "&x=" + x;
		},
		onSubmit(parameters) {
			if (!this.isImageSaved) {
				alert(
					"If you want to apply an algorithm on this image you have to save it first !"
				);
			} else {
				ImageService.applyAlgorithm(
					this.id + "?algorithm=" + parameters
				)
					.then(response => {
						let reader = ImageService.readBlob(response);
						reader.onload = () => {
							this.$emit(
								"updatePreview",
								response.headers.name,
								Number(response.headers.id),
								response.headers.type,
								response.headers.size,
								reader.result
							);
						};
					})
					.catch(error => {
						alert(error);
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
							this.image, this.name
						);
						ImageService.save(this.currentUser, formData).then(
							response => {
								alert(response.data);

								this.$emit(
									"saveImage",
									Number(response.headers.id)
								);
							},
							error => {
								alert(error);
							});
			/*axios.get("images/" + this.id + "/saving").catch(function(error) {
				if (error.response) {
					console.log(error.response.data);
					console.log(error.response.status);
					console.log(error.response.headers);
				}
			});*/
		},

		close() {
			if (this.currentUser != null) {
				if (isNaN(this.id)) {
					if (
						!confirm(
							"Dou you want to save this image ? If not result will be lost"
						)
					) {
						this.$emit("close");
					} else {
            this.save();
					}
				} else {
					this.$emit("close");
				}
			}else{
        this.$emit("close");
      }
		}
	}
>>>>>>> preview_vue
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
	width: 100%;
	height: 100%;
	display: flex;
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
	height: calc(100% - 2 * 12px);
	padding: 56px 24px 0 24px;
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

#save_btn {
  visibility: hidden;
}
</style>
