<template>
    <div class="home">
        <header>
            <h1 class="logo">Gallery</h1>
        </header>
        
        <gallery></gallery>
        
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
