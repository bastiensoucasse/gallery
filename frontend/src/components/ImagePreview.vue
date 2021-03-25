<template>
    <div class=preview-window>
         <div class="preview-header">
                    <button class="preview-link" onclick="document.querySelector('.preview-window').style.gridTemplateColumns='100fr 350px';document.querySelector('.preview-features').style.display='block';"><span class="material-icons">edit</span></button>
                    <button class="preview-link" @click="this.download"><span class="material-icons">download</span></button>
                    <button class="preview-link" @click="this.delete"><span class="material-icons">delete</span></button>
                    <button class="preview-link" @click="this.$emit('closePreview')"><span class="material-icons">close</span></button>
        </div>
        <div class="preview-visual">
                    <img class="preview-image" :src="'/images/' + id"
                        onerror="document.querySelector('.preview-background').remove();document.querySelector('.preview-window').remove();" />
        </div>
        <div class="preview-features">
                    <form class="preview-feature" method="get" :action="'/images/' + id">
                        <b>Convert to grayscale</b>
                        <input type="hidden" name="algorithm" value="toGrayscale" />
                        <div class="preview-actions">
                            <input class="preview-button" type="submit" value="To grayscale" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" :action="'/images/' + id">
                        <b>Apply a gain to the brightness</b>
                        <input type="hidden" name="algorithm" value="changeBrightness" />
                        <div class="preview-actions">
                            <input class="preview-input" type="number" name="gain" min="-255" max="255" required />
                            <input class="preview-button" type="submit" value="Change brightness" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" :action="'/images/' + id">
                        <b>Colorize by setting the hue</b>
                        <input type="hidden" name="algorithm" value="colorize" />
                        <div class="preview-actions">
                            <input class="preview-input" type="number" name="hue" min="0" max="360" required />
                            <input class="preview-button" type="submit" value="Colorize" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" :action="'/images/' + id">
                        <b>Extend the dynamics</b>
                        <input type="hidden" name="algorithm" value="extendDynamics" />
                        <div class="preview-actions">
                            <input class="preview-button" type="submit" value="Extend dynamics" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" :action="'/images/' + id">
                        <b>Equalize the saturation or brightness histogram</b>
                        <input type="hidden" name="algorithm" value="equalizeHistogram" />
                        <div class="preview-actions">
                            <div>
                                <label><input class="preview-radio" type="radio" name="channel" value="1" checked />Saturation</label><br />
                                <label><input class="preview-radio" type="radio" name="channel" value="2" />Brightness</label><br />
                            </div>
                            <input class="preview-button" type="submit" value="Equalize histogram" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" :action="'/images/' + id">
                        <b>Mean filter</b>
                        <input type="hidden" name="algorithm" value="meanFilter" />
                        <div class="preview-actions">
                            <input class="preview-input" type="number" name="radius" min="1" max="10" required />
                            <input class="preview-button" type="submit" value="Apply filter" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" :action="'/images/' + id">
                        <b>Gaussian filter</b>
                        <input type="hidden" name="algorithm" value="gaussianFilter" />
                        <div class="preview-actions">
                            <input class="preview-input" type="number" name="radius" min="1" max="10" required />
                            <input class="preview-button" type="submit" value="Apply filter" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" :action="'/images/' + id">
                        <b>Sobel operator</b>
                        <input type="hidden" name="algorithm" value="sobelOperator" />
                        <div class="preview-actions">
                            <input class="preview-button" type="submit" value="Apply operator" />
                        </div>
                    </form>
        </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
    props: {
        id: Number,
        name: String,
    },
    emits: {
        'closePreview': null,
    },
    methods: {
        delete() {
            axios
                .delete("images/" + this.selected)
                .then(() => {
                    location.reload();
                })
        },
        download() {
            axios
                .get("images/" + this.selected, { responseType: "blob" })
                .then((r) => {
                    var reader = new window.FileReader();
                    reader.readAsDataURL(r.data);
                    reader.onload = () => {
                        const link = document.createElement("a");
                        link.href = reader.result;
                        link.setAttribute("download", name);
                        document.body.appendChild(link);
                        link.click();
                    };
                })
        },
    }
}
</script>

<style scoped>
.preview-background {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    width: 100%;
    height: 100vh;
    background-color: #202124;
    opacity: 0.5;
}

.preview-window {
    position: fixed;
    top: 0;
    left: 0;
    bottom: 0;
    right: 0;
    width: 100%;
    height: 100vh;
    display: grid;
    grid-template-columns: 100fr;
}

.preview-header {
    position: absolute;
    top: 12px;
    right: 24px;
    display: grid;
    grid-template-columns: 25fr 25fr 25fr 25fr;
    z-index: 2;
}

.preview-link {
    background: none;
    border: none;
    cursor: pointer;
    border-radius: 6px;
    display: flex;
    justify-content: center;
    align-items: center;
    user-select: none;
    color: white;
    padding: 12px;
}

.preview-visual {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(32, 33, 36, 0.9);
}

.preview-image {
    display: block;
    min-height: 240px;
    min-width: 240px;
    max-height: 80vh;
    max-width: 80%;
    border-radius: 48px;
    user-select: none;
    box-shadow: 0 8px 10px 1px rgba(0, 0, 0, 0.14),
        0 3px 14px 2px rgba(0, 0, 0, 0.12), 0 5px 5px -3px rgba(0, 0, 0, 0.2);
    background-color: #323539;
    overflow: hidden;
}

.preview-features {
    width: calc(100% - 2 * 24px);
    height: calc(100% - 2 * 12px);
    padding: 96px 24px 0 24px;
    text-align: left;
    background-color: rgba(38, 40, 43, 0.9);
    box-shadow: 0 4px 5px 0 rgba(0, 0, 0, 0.14),
        0 1px 10px 0 rgba(0, 0, 0, 0.12), 0 2px 4px -1px rgba(0, 0, 0, 0.2);
    max-height: 100vh;
    overflow-y: auto;
    display: none;
}
.preview-link:hover {
    background: rgba(255, 255, 255, .1);
}

.preview-actions {
    display: grid;
    grid-gap: 8px;
    grid-template-columns: 50fr 50fr;
    margin: 8px 0 32px 0;
}

.preview-button {
    display: block;
    padding: 2px 8px;
    cursor: pointer;
    user-select: none;
    width: 100%;
}

.preview-input {
    display: block;
    width: calc(100% - 8px);
}
</style>
