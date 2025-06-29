// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../components/auth/LoginPage.vue'


const routes = [
    {
        path: '/login',
        name: 'Login',
        component: LoginView
    },
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/movies',
        name: 'MovieList',
        component: () => import('../components/user/MovieList.vue')
    },
    {
        path: '/cinemas',
        name: 'CinemaList',
        component: () => import('../components/user/CinemaList.vue')
    },
    {
        path: '/seats',
        name: 'SeatSelection',
        component: () => import('../components/user/SeatSelection.vue')
    },
    {
        path: '/screenings',
        name: 'ScreeningList',
        component: () => import('../components/user/ScreeningList.vue')
    }

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router