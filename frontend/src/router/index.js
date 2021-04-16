import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login'
import Register from '../views/Register'
import Board from '../views/Board'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        beforeEnter: (from) => {
            if(from.name === 'Login' && this.$store.state.auth.loggedIn){
                location.reload();
            }
        }
    },

    {
        path: '/:preview',
        name: 'Preview',
        component: Home
    },
    {
        path: "/login",
        name: 'Login',
        component: Login,
    },
    {
        path: '/register',
        name: 'Register',
        component: Register
    },
    {
        path: '/board',
        name: 'Board',
        component: Board
    },
    {
        path: '/profile',
        name: 'Profile',
        component: () => import("../views/Profile.vue")
    },

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});

/*router.beforeEach((to, from, next) => {
    const publicPages = ['/login', '/register', '/home', '/'];
    const authRequired = !publicPages.includes(to.path);
    const loggedIn = localStorage.getItem('user');
  
    // trying to access a restricted page + not logged in
    // redirect to login page
    if (authRequired && !loggedIn) {
      next('/login');
    } else {
      next();
    }
  });*/

export default router
