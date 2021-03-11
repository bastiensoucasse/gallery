<template>
    <div class="home">
        <header>
            <h1>Gallery</h1>
        </header>
        <Gallery />
        <Tools />
        <footer></footer>
    </div>
</template>

<script>
import Gallery from "@/components/Gallery.vue";
import Tools from "@/components/Tools.vue";

export default {
    name: "Home",
    components: {
        Gallery,
        Tools,
    },
};
</script>
