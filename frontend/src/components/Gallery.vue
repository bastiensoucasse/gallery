<template>
    <div class="gallery">
        <div class="gallery-wrapper">
            <div
                class="gallery-content"
                v-for="image in response"
                :key="image.id"
                @click="preview(image.id)"
            >
                <img class="gallery-image" :src="'/images/' + image.id" />
            </div>
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
        delete(id) {
            axios
                .delete("images/" + id)
                .then(() => {
                    location.reload();
                })
                .catch((e) => {
                    this.errors.push(e);
                });
        },
        preview(id) {
            const app = document.getElementById("app");

            let pBackground = document.createElement("div");
            pBackground.classList.add("preview-background");
            app.append(pBackground);

            let pWindow = document.createElement("div");
            pWindow.classList.add("preview-window");
            app.append(pWindow);

            pWindow.innerHTML = `
                <div class="preview-header">
                    <button class="preview-link" onclick="document.querySelector('.preview-window').style.gridTemplateColumns='100fr 350px';document.querySelector('.preview-features').style.display='block';"><span class="material-icons">edit</span></button>
                    <button class="preview-link" onclick="alert('Cette fonctionnalité est encore à implémenter.');"><span class="material-icons">download</span></button>
                    <button class="preview-link" onclick="alert('Cette fonctionnalité est encore à implémenter.');"><span class="material-icons">delete</span></button>
                    <button class="preview-link" onclick="document.querySelector('.preview-background').remove();document.querySelector('.preview-window').remove();"><span class="material-icons">close</span></button>
                </div>
                <div class="preview-visual">
                    <img class="preview-image" src="/images/` + id + `"
                        onerror="document.querySelector('.preview-background').remove();document.querySelector('.preview-window').remove();" />
                </div>
                <div class="preview-features">
                    <form class="preview-feature" method="get" action="/images/` + id + `">
                        <b>Convert to grayscale</b>
                        <input type="hidden" name="algorithm" value="toGrayscale" />
                        <div class="preview-actions">
                            <input class="preview-button" type="submit" value="To grayscale" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" action="/images/` + id + `">
                        <b>Apply a gain to the brightness</b>
                        <input type="hidden" name="algorithm" value="changeBrightness" />
                        <div class="preview-actions">
                            <input class="preview-input" type="number" name="gain" min="-255" max="255" required />
                            <input class="preview-button" type="submit" value="Change brightness" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" action="/images/` + id + `">
                        <b>Colorize by setting the hue</b>
                        <input type="hidden" name="algorithm" value="colorize" />
                        <div class="preview-actions">
                            <input class="preview-input" type="number" name="hue" min="0" max="360" required />
                            <input class="preview-button" type="submit" value="Colorize" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" action="/images/` + id + `">
                        <b>Extend the dynamics</b>
                        <input type="hidden" name="algorithm" value="extendDynamics" />
                        <div class="preview-actions">
                            <input class="preview-button" type="submit" value="Extend dynamics" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" action="/images/` + id + `">
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
                    <form class="preview-feature" method="get" action="/images/` + id + `">
                        <b>Mean filter</b>
                        <input type="hidden" name="algorithm" value="meanFilter" />
                        <div class="preview-actions">
                            <input class="preview-input" type="number" name="radius" min="1" max="10" required />
                            <input class="preview-button" type="submit" value="Apply filter" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" action="/images/` + id + `">
                        <b>Gaussian filter</b>
                        <input type="hidden" name="algorithm" value="gaussianFilter" />
                        <div class="preview-actions">
                            <input class="preview-input" type="number" name="radius" min="1" max="10" required />
                            <input class="preview-button" type="submit" value="Apply filter" />
                        </div>
                    </form>
                    <form class="preview-feature" method="get" action="/images/` + id + `">
                        <b>Sobel operator</b>
                        <input type="hidden" name="algorithm" value="sobelOperator" />
                        <div class="preview-actions">
                            <input class="preview-button" type="submit" value="Apply operator" />
                        </div>
                    </form>
                </div>
            `;
        },
    },
    mounted() {
        this.callRestService();
    },
};
</script>


<style>
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
    cursor: pointer;
}

.gallery-content:hover {
    box-shadow: 0 19px 38px rgba(0, 0, 0, 0.3), 0 15px 12px rgba(0, 0, 0, 0.22);
    transform: scale(1.04);
}

.gallery-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    user-select: none;
}

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
