<template>
    <div class="preview-window">
        <div class="feature-box">
            <button class="feature-link" @click="$emit('close')">
                <span class="material-icons">close</span>
            </button>

            <button class="feature-link" @click="remove">
                <span class="material-icons">delete</span>
            </button>

            <button class="feature-link" @click="download">
                <span class="material-icons">download</span>
            </button>

            <button class="feature-link" @click="edit">
                <span class="material-icons">edit</span>
            </button>

            <button class="feature-link" @click="details">
                <span class="material-icons-outlined">info</span>
            </button>
        </div>

        <div class="preview-visual">
            <img
                class="preview-image"
                :src="'/images/' + id"
                @error="$emit('close')"
            />
        </div>

        <div id="details-panel" class="preview-features">
            <h2 class="title">Details</h2>

            <div class="preview-details">
                <div>
                    <h3 class="category">ID</h3>
                    <p class="paragraph">{{ id }}</p>
                </div>
                <div>
                    <h3 class="category">Name</h3>
                    <p class="paragraph">{{ name }}</p>
                </div>
            </div>
        </div>

        <div id="edit-panel" class="preview-features">
            <h2 class="title">Edit</h2>

            <div class="preview-details">
                <form
                    class="preview-feature"
                    method="get"
                    :action="'/images/' + id"
                >
                    <h3 class="category">Convert to grayscale</h3>

                    <input type="hidden" name="algorithm" value="toGrayscale" />

                    <input
                        class="theme-button"
                        type="submit"
                        value="To grayscale"
                    />
                </form>

                <form
                    class="preview-feature"
                    method="get"
                    :action="'/images/' + id"
                >
                    <h3 class="category">Apply a gain to the brightness</h3>

                    <input
                        type="hidden"
                        name="algorithm"
                        value="changeBrightness"
                    />

                    <input
                        class="preview-input"
                        type="number"
                        name="gain"
                        min="-255"
                        max="255"
                        required
                    />

                    <input
                        class="theme-button"
                        type="submit"
                        value="Change brightness"
                    />
                </form>

                <form
                    class="preview-feature"
                    method="get"
                    :action="'/images/' + id"
                >
                    <h3 class="category">Colorize by setting the hue</h3>

                    <input type="hidden" name="algorithm" value="colorize" />

                    <input
                        class="preview-input"
                        type="number"
                        name="hue"
                        min="0"
                        max="360"
                        required
                    />

                    <input
                        class="theme-button"
                        type="submit"
                        value="Colorize"
                    />
                </form>

                <form
                    class="preview-feature"
                    method="get"
                    :action="'/images/' + id"
                >
                    <h3 class="category">Extend the dynamics</h3>

                    <input
                        type="hidden"
                        name="algorithm"
                        value="extendDynamics"
                    />

                    <input
                        class="theme-button"
                        type="submit"
                        value="Extend dynamics"
                    />
                </form>

                <form
                    class="preview-feature"
                    method="get"
                    :action="'/images/' + id"
                >
                    <h3 class="category">
                        Equalize the saturation or brightness histogram
                    </h3>

                    <input
                        type="hidden"
                        name="algorithm"
                        value="equalizeHistogram"
                    />

                    <div class="choice">
                        <input
                            class="preview-radio"
                            type="radio"
                            name="channel"
                            value="1"
                            checked
                            id="c1"
                        />
                        <label for="c1">Saturation</label>

                        <input
                            class="preview-radio"
                            type="radio"
                            name="channel"
                            value="2"
                            id="c2"
                        />
                        <label for="c2">Brightness</label>
                    </div>

                    <input
                        class="theme-button"
                        type="submit"
                        value="Equalize histogram"
                    />
                </form>

                <form
                    class="preview-feature"
                    method="get"
                    :action="'/images/' + id"
                >
                    <h3 class="category">Blur filter</h3>

                    <input
                        class="preview-input"
                        type="number"
                        name="radius"
                        min="1"
                        max="10"
                        required
                    />

                    <div class="choice">
                        <input
                            class="preview-radio"
                            type="radio"
                            name="algorithm"
                            value="meanFilter"
                            id="mf"
                            checked
                        />
                        <label for="mf">Mean</label>

                        <input
                            class="preview-radio"
                            type="radio"
                            name="algorithm"
                            value="gaussianFilter"
                            id="gf"
                        />
                        <label for="gf">Gaussian</label>
                    </div>

                    <input
                        class="theme-button"
                        type="submit"
                        value="Apply filter"
                    />
                </form>

                <form
                    class="preview-feature"
                    method="get"
                    style="padding-bottom: 60px"
                    :action="'/images/' + id"
                >
                    <h3 class="category">Sobel operator</h3>

                    <input
                        type="hidden"
                        name="algorithm"
                        value="sobelOperator"
                    />

                    <input
                        class="theme-button"
                        type="submit"
                        value="Apply operator"
                    />
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";

export default {
    name: "Preview",

    props: {
        id: Number,
        name: String,
    },

    emits: {
        close: null,
    },

    methods: {
        remove() {
            axios.delete("images/" + this.id).then(() => {
                location.reload();
            });
        },

        download() {
            axios
                .get("images/" + this.id, { responseType: "blob" })
                .then((r) => {
                    var reader = new window.FileReader();
                    reader.readAsDataURL(r.data);
                    reader.onload = () => {
                        const link = document.createElement("a");
                        link.href = reader.result;
                        link.setAttribute("download", this.name);
                        document.body.appendChild(link);
                        link.click();
                    };
                });
        },

        edit() {
            document.querySelector(
                ".preview-window"
            ).style.gridTemplateColumns = "100fr 350px";
            document.querySelector("#details-panel").style.display = "none";
            document.querySelector("#edit-panel").style.display = "block";
        },

        details() {
            document.querySelector(
                ".preview-window"
            ).style.gridTemplateColumns = "100fr 350px";
            document.querySelector("#edit-panel").style.display = "none";
            document.querySelector("#details-panel").style.display = "block";
        },
    },
};
</script>

<style scoped>
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
    padding: 56px 24px 0 24px;
    text-align: left;
    background-color: rgb(38, 40, 43);
    box-shadow: 0 4px 5px 0 rgba(0, 0, 0, 0.14),
        0 1px 10px 0 rgba(0, 0, 0, 0.12), 0 2px 4px -1px rgba(0, 0, 0, 0.2);
    max-height: 100vh;
    display: none;
    overflow: hidden;
    font-size: 14px;
}

.preview-details {
    height: 100%;
    overflow-y: scroll;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    padding: 2px;
    padding-top: 6px;
}

.preview-details::-webkit-scrollbar {
    display: none;
}

.category {
    font-size: 14px;
    margin: 0;
    margin-bottom: 6px;
}

input[type="number"] {
    display: block;
    width: calc(100% - 12px);
    height: 22px;
    margin-bottom: 6px;
    border: none;
    border-radius: 4px;
    padding: 2px 6px;
}

.choice {
    display: grid;
    grid-template-columns: 50fr 50fr;
    grid-gap: 6px;
    margin-bottom: 6px;
}

.choice label {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 24px;
    font-weight: 600;
    text-align: center;
    cursor: pointer;
    background-color: #202124;
    border: 2px solid #202124;
    border-radius: 4px;
    user-select: none;
}

.choice input:checked + label {
    border-color: rgb(0, 89, 179);
}

.choice input {
    display: none;
}
</style>
