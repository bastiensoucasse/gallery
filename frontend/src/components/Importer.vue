<template>
    <div class="importer-window">
        <div class="importer-content">
            <div class="feature-box">
                <button class="feature-link" @click="$emit('close')">
                    <span class="material-icons">close</span>
                </button>
            </div>

            <h2 class="title">Import content</h2>

            <p class="paragraph">
                To add images (JPEG or TIFF only) to your gallery,<br />
                click on this button or drop your image on it.
            </p>

            <input
                class="importer-input"
                type="file"
                id="file"
                ref="file"
                @change="upload"
                multiple
            />

            <label class="theme-button" for="file">Choose an image</label>
        </div>
    </div>
</template>

<script>
import ImageService from "@/services/image.service";

export default {
    name: "Importer",

    emits: {
        close: null,
    },

    computed:{
    currentUser() {
			return this.$store.state.auth.user;
		},
    },

    methods: {
        upload() {
            this.file = this.$refs.file.files[0];
            if (this.file.size > 1048576) {
                //database allowing up to 1048576 bytes for an image
                alert("File too big (> 1MB)");
                return;
            }
            let formData = new FormData();

            formData.append("file", this.file);
            ImageService.save(this.currentUser, formData).then(
                response =>{
                    console.log("upload successful !");
                    location.href = "/" + response.data;
                },
                error =>{
                    alert(error);
                }
            );
            /*axios
                .post("/images", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                })
                .then((r) => {
                    location.href = "/" + r.data;
                });
            */
        },
    },

    data() {
        return {
            file: "",
        };
    },
};
</script>

<style scoped>
.importer-window {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    width: 100%;
    height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: rgba(32, 33, 36, 0.9);
    text-align: left;
}

.importer-content {
    max-height: 80vh;
    max-width: 80%;
    border-radius: 24px;
    box-shadow: 0 8px 10px 1px rgba(0, 0, 0, 0.14),
        0 3px 14px 2px rgba(0, 0, 0, 0.12), 0 5px 5px -3px rgba(0, 0, 0, 0.2);
    background-color: #323539;
    padding: 40px 48px;
    padding-bottom: 22px;
}

.importer-input {
    display: none;
}
</style>
