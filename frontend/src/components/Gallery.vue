<template>
    <div class="gallery">
        <div class="gallery-wrapper">
            <div
                :key="image.id"
                v-for="image in response"
                v-on:click="loadPreview(image.id, image.name)"
                class="gallery-image"
            >
                <img :src="'/images/' + image.id" />
            </div>
        </div>

        <div class="gallery-import" @click="loadImporter">
            <span class="material-icons">add</span>
        </div>

        <preview
            :id="preview"
            :name="name"
            @close="closePreview"
            v-if="preview != -1"
        ></preview>

        <importer @close="closeImporter" v-if="importing"></importer>
    </div>
</template>

<script>
import Preview from "@/components/Preview.vue";
import Importer from "@/components/Importer.vue";
import axios from "axios";

export default {
    name: "Gallery",

    components: {
        Preview,
        Importer,
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

        loadPreview(id, name) {
            this.name = name;
            this.preview = Number(id);
        },

        closePreview() {
            this.name = "";
            this.preview = -1;
        },

        loadImporter() {
            this.importing = true;
        },

        closeImporter() {
            this.importing = false;
        },
    },

    data() {
        return {
            response: [],
            errors: [],
            preview: Number,
            name: String,
            importing: Boolean,
        };
    },

    mounted() {
        this.name = "";
        this.preview = -1;
        this.importing = false;
        this.callRestService();
    },
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
    margin-top: 56px;
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
