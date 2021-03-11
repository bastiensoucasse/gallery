<template>
    <div class="gallery">
        <div class="gallery-wrapper">
            <a
                class="gallery-content"
                v-for="r in response"
                :key="r.id"
                :href="'/images/' + r.id"
            >
                <img :src="'/images/' + r.id" />
            </a>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "Gallery",
    data() {
        return {
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
    },
    mounted() {
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

.gallery-content {
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
}

.gallery-content:hover {
    box-shadow: 0 19px 38px rgba(0, 0, 0, 0.3), 0 15px 12px rgba(0, 0, 0, 0.22);
    transform: scale(1.04);
}

.gallery-content > img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}
</style>
