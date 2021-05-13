<template>
    <div class="window">
        <div class="content">
            <div class="feature-box">
                <button class="feature-link" @click="$emit('closeDialog')">
                    <span class="material-icons">close</span>
                </button>
            </div>

            <h2 class="title">{{title}}</h2>

            <p class="paragraph">
                {{msg}}
            </p>

        </div>
    </div>
</template>

<script>


export default {
    name: "DialogBox",

    props:{
        title: String,
        msg: String,
    },

    emits: {
        closeDialog: null,
    },


};
</script>

<style scoped>
.window {
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

.content {
    max-height: 80vh;
    max-width: 80%;
    border-radius: 24px;
    box-shadow: 0 8px 10px 1px rgba(0, 0, 0, 0.14),
        0 3px 14px 2px rgba(0, 0, 0, 0.12), 0 5px 5px -3px rgba(0, 0, 0, 0.2);
    background-color: #323539;
    padding: 40px 48px;
    padding-bottom: 22px;
}


</style>
