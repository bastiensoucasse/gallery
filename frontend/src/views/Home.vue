<template>
    <div class="home">
        <header>
            <h1>Gallery</h1>
        </header>

        <gallery></gallery>

        <footer></footer>
    </div>
</template>

<script>
import Gallery from "@/components/Gallery.vue";

export default {
    name: "Home",

    components: {
        Gallery,
    },
};
</script>
