<template>
  <div class="modal-mask">
    <div class="modal-wrapper">
      <div class="modal-container">
        <div class="modal-header">
          <slot name="header">
            <div class="close-button">
              <button class="feature-link" @click="$emit('closeExport')">
                <span class="material-icons">close</span>
              </button>
            </div>
            <h3>Export</h3>
          </slot>
        </div>

        <div class="modal-body">
          <slot name="body">
            Choose a format
            <select v-model="selected">
              <option
                v-for="format in supportedFormat"
                :key="format"
                :value="format.type"
                >{{ format.subType }}
              </option>
            </select>
          </slot>
        </div>

        <div class="modal-footer">
          <slot name="footer">
            <!--default footer -->
            <button class="modal-default-button" @click="downloadToFormat()">
              download
            </button>
          </slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Export",

  data() {
    return {
      supportedFormat: [],
      selected: "image/jpeg"
    };
  },

  props: {
    id: Number,
  },

  emits: {
    closeExport: null
  },

  methods: {
    downloadToFormat() {
      axios.get("images/" + this.id + "?format=" + this.selected, { responseType: "blob" }).then(r => {
        var reader = new window.FileReader();
        reader.readAsDataURL(r.data);
        reader.onload = () => {
          const link = document.createElement("a");
          link.href = reader.result;
          link.setAttribute("download", r.headers.name);
          document.body.appendChild(link);
          link.click();
        };
      });
    },

    listSupportedMediaTypes() {
      axios.get("images/supportedMedia").then(r => {
        this.supportedFormat = r.data;
      });
    }
  },
  mounted() {
    this.listSupportedMediaTypes();
  }
};
</script>

<style>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.modal-container {
  width: 300px;
  margin: 0px auto;
  padding: 20px 30px;
  background-color: rgb(38, 40, 43);
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
  font-family: Helvetica, Arial, sans-serif;
}

.modal-header h3 {
  margin-top: 0;
  padding: auto;
  /*color: #42b983;*/
}

.close-button {
  float: right;
}

.modal-body {
  margin: 20px 0;
}

.modal-default-button {
  display: block;
  margin-top: 1rem;
}

/*
 * The following styles are auto-applied to elements with
 * transition="modal" when their visibility is toggled
 * by Vue.js.
 *
 * You can easily play with the modal transition by editing
 * these styles.
 */

.modal-enter {
  opacity: 0;
}

.modal-leave-active {
  opacity: 0;
}

.modal-enter .modal-container,
.modal-leave-active .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
</style>
