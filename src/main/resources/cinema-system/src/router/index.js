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
    },
    {
        path: '/movie-detail',
        name: 'MovieDetail',
        component: () => import('../components/user/MovieDetail.vue')
    },
    {
        path: '/activity',
        name: 'Activity',
        component: () => import('../components/user/Activity.vue')
    },
    {
        path: '/profile',
        name: 'Profile',
        component: () => import('../components/user/Profile.vue')
    },
    {
        path: '/add-activity',
        name: 'AddActivity',
        component: () => import('../components/user/AddActivity.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router