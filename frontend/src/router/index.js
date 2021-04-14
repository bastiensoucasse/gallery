import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login'
import Register from '../views/Register'
import Board from '../views/Board'

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home
    },

    {
        path: '/:preview',
        name: 'Preview',
        component: Home
    },
    {
        path: "/login",
        name: 'Login',
        component: Login
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
})

export default router
