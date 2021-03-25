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

        <preview
            :id="preview"
            :name="name"
            @close="closePreview"
            v-if="preview != -1"
        ></preview>
    </div>
</template>

<script>
import Preview from "@/components/Preview.vue";
import axios from "axios";

export default {
    name: "Gallery",

    components: {
        Preview,
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
    },

    data() {
        return {
            response: [],
            errors: [],
            preview: Number,
            name: String,
        };
    },

    mounted() {
        this.name = "";
        this.preview = -1;
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
    margin: 24px auto;
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
</style>
