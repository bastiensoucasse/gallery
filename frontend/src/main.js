import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import 'animate.css';


const app = createApp(App);//.use(router).mount('#app')
app.use(router);
app.use(store);
app.mount('#app');
