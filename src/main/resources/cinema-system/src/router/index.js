// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../components/auth/LoginPage.vue'
import OrderSuccess from "@/components/user/OrderSuccess.vue";


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
        path: '/order-success/:orderId',
        name: 'OrderSuccess',
        component: OrderSuccess,
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