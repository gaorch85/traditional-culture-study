import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ExploreView from '../views/KnowledgeMap.vue'
import CommunityView from '../views/CommunityView.vue'
import ProfileView from '../views/ProfileView.vue'

import ChalledgeView from '@/views/ChalledgeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/explore',
    name: 'explore',
    component: ExploreView
  },
  {
    path: '/community',
    name: 'community',
    component: CommunityView
  },
  {
    path: '/daily-challedge/:date',
    name: 'community',
    component: ChalledgeView
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView
  },

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
