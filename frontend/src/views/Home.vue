<template>
    <div class="home">
        <header>
            <h1>Gallery</h1>
        </header>
        <component v-bind:is="currentComponent" @load-preview="loadPreview" @close-preview="closePreview" />
        <Tools />
        <footer></footer>
    </div>
</template>

<script>
import Gallery from "@/components/Gallery.vue";
import Tools from "@/components/Tools.vue";
import ImagePreview from "@/components/ImagePreview.vue";

export default {
    name: "Home",
    components: {
        Gallery,
        Tools,
        ImagePreview,
    },
    data() {
        return{
            currentComponent: undefined,

        };
    },

    methods :{
        loadPreview(id){
            this.currentComponent = ImagePreview;
            ImagePreview.id = id;
        },
        closePreview(){
            this.currentComponent = Gallery;
        }
    },

    mounted() {
        console.log(this.currentComponent);
        this.currentComponent = Gallery;
        console.log(this.currentComponent);

    },
};
</script>
