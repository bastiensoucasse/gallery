<template>
    <div class="tools">
        <div class="preview">
            <h3>Preview an image</h3>
            <div class="input">
                <select v-model="selected" @change="loadSelected()">
                    <option v-for="r in response" :key="r.id" :value="r.id">
                        {{ r.name }}
                    </option>
                </select>
                <div class="double-buttons">
                    <button @click="downloadSelected()">Download</button>
                    <button @click="deleteSelected()">Delete</button>
                </div>
            </div>
            <img id="img-display" />
        </div>
        <div class="upload">
            <h3>Upload a new image</h3>
            <div class="input">
                <input
                    type="file"
                    id="file"
                    ref="file"
                    @change="handleFileUpload()"
                />
                <button @click="submitFile()">Submit</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "Tools",
    data() {
        return {
            file: "",
            response: [],
            errors: [],
        };
    },
    methods: {
        callRestService() {
            axios
                .get("images")
                .then((r) => {
                    this.response = r.data;
                })
                .catch((e) => {
                    this.errors.push(e);
                });
        },
        loadSelected() {
            let imageEl = document.getElementById("img-display");

            axios
                .get("images/" + this.selected, { responseType: "blob" })
                .then((response) => {
                    var reader = new window.FileReader();
                    reader.readAsDataURL(response.data);
                    reader.onload = function () {
                        imageEl.setAttribute("src", reader.result);
                    };
                })
                .catch((e) => {
                    this.errors.push(e);
                });
        },
        downloadSelected() {
            axios
                .get("images/" + this.selected, { responseType: "blob" })
                .then((response) => {
                    var reader = new window.FileReader();
                    reader.readAsDataURL(response.data);
                    reader.onload = function () {
                        const link = document.createElement("a");
                        link.href = reader.result;
                        link.setAttribute("download", "image.jpg");
                        document.body.appendChild(link);
                        link.click();
                    };
                })
                .catch((e) => {
                    this.errors.push(e);
                });
        },
        deleteSelected() {
            axios
                .delete("images/" + this.selected)
                .then(() => {
                    location.reload();
                })
                .catch((e) => {
                    this.errors.push(e);
                });
        },
        handleFileUpload() {
            this.file = this.$refs.file.files[0];
        },
        submitFile() {
            let formData = new FormData();
            formData.append("file", this.file);
            axios
                .post("/images", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                })
                .then(() => {
                    location.reload();
                })
                .catch((e) => {
                    this.errors.push(e);
                });
        },
    },
    mounted() {
        this.callRestService();
        this.selected = 0;
        this.loadSelected();
    },
};
</script>

<style scoped>
button,
input,
select {
    display: block;
    width: 200px;
    max-width: calc(100% - 48px);
    margin: 6px 24px;
    user-select: none;
    padding: 2px;
}

#img-display {
    display: block;
    width: 600px;
    max-width: calc(100% - 48px);
    margin: 24px auto;
    border-radius: 24px;
}

.double-buttons {
    display: grid;
    grid-gap: 6px;
    grid-template-columns: repeat(2, calc(50% - 3px));
    width: 200px;
    max-width: calc(100% - 48px);
}

.double-buttons button {
    max-width: 100%;
    margin: 0;
}

.input {
    display: flex;
    flex-direction: column;
    align-items: center;
}
</style>
