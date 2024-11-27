import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ExploreView from '../views/Explore/KnowledgeTree.vue'
import ExploreDetail from '../views/Explore/ExploreDetail.vue'
import CommunityView from '../views/Community/BlogList.vue'
import Blog from '../views/Community/BlogPost.vue'
import CreateBlog from '@/views/Community/CreateBlog.vue'
import EditBlog from '@/views/Community/EditBlog.vue'
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
    path: '/explore/details/:id',  // 使用占位符 :id
    name: 'exploreDetail',
    component: ExploreDetail,
  },
  {
    path: '/community',
    name: 'community',
    component: CommunityView
  },
  {
    path: '/community/create',
    name: 'createBlog',
    component: CreateBlog
  },
  {
    path: '/blog/details/:id',  // 使用占位符 :id
    name: 'BlogPost',
    component: Blog,
    meta: { title: '默认标题'}
  },
  {
    path: '/blog/edit/:id',  // 使用占位符 :id
    name: 'EditBlog',
    component: EditBlog
  },
  {
    path: '/daily-challedge/:date',
    name: 'daily-challedge',
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
