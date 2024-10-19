<template>
  <el-container class="app-container">
    <el-header class="app-header">
      <div class="header-content">
        <router-link to="/" class="logo">
          <img src="@/assets/logo.png" alt="文化寻宝" />
          <span class="site-name">文化启智</span>
        </router-link>
        <el-menu 
          mode="horizontal" 
          router 
          class="nav-menu"
          :default-active="activeIndex"
        >
          <el-menu-item index="/explore">探索</el-menu-item>
          <el-menu-item index="/community">社区</el-menu-item>
        </el-menu>
        <div class="user-area">
          <template v-if="isLoggedIn">
            <el-dropdown @command="handleCommand" trigger="click">
              <span class="el-dropdown-link">
                <el-avatar :size="32" :src="userAvatar"></el-avatar>
                <span class="username">{{ username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <el-button v-else type="primary" @click="handleLogin">登录</el-button>
        </div>
      </div>
    </el-header>
    <el-main>
      <router-view></router-view>
    </el-main>
    <el-footer>
      &copy; 2024 文化启智. 保留所有权利。
    </el-footer>
  </el-container>
  <AICultureGuide />
  <login-register-dialog
    v-model:visible="loginDialogVisible"
    @login-success="handleLoginSuccess"
  />
</template>

<script setup>
import { ref, computed, provide, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import { removeToken, getToken } from '@/util/auth'
import AICultureGuide from '@/components/AICultureGuide.vue'
import LoginRegisterDialog from '@/components/LoginRegisterDialog.vue'
import eventBus from '@/util/eventBus' 

const store = useStore()
const router = useRouter()
const route = useRoute()

const loginDialogVisible = ref(false)
const activeIndex = ref(route.path)

const isLoggedIn = computed(() => store.getters['user/isLoggedIn'])
const username = computed(() => store.state.user.username)
const userAvatar = computed(() => store.state.user.avatar)

watch(() => route.path, (newPath) => {
  activeIndex.value = newPath
})

const handleLogin = () => {
  loginDialogVisible.value = true
}

const handleLoginSuccess = () => {
  loginDialogVisible.value = false
}

const handleLogout = () => {
  removeToken()
  store.commit('user/clearUserInfo')
  router.push('/')
}

const handleCommand = (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    handleLogout()
  }
}

const showLoginDialog = () => {
  loginDialogVisible.value = true
}

// 提供一个函数来让子组件能够显示登录弹窗
provide('showLoginDialog', showLoginDialog)

// 监听事件总线上的 'show-login-dialog' 事件
eventBus.on('show-login-dialog', showLoginDialog)

// 在组件卸载时移除事件监听
// onUnmounted(() => {
//   eventBus.off('show-login-dialog', showLoginDialog)
// })


onMounted(() => {
  if (getToken()) {
    store.dispatch('user/getUserInfo')
  }
})
</script>

<style scoped>
.app-container {
  min-height: 100vh;
}

.app-header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.logo {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #333;
}

.logo img {
  height: 40px;
  margin-right: 10px;
}

.site-name {
  font-size: 20px;
  font-weight: bold;
}

.nav-menu {
  flex-grow: 1;
  justify-content: flex-start;
  margin-left: 40px;
  border-bottom: none;
}

.nav-menu :deep(.el-menu-item) {
  font-size: 16px;
  font-weight: 500;
  color: #606266;
}

.nav-menu :deep(.el-menu-item.is-active) {
  color: #409EFF;
  border-bottom: 2px solid #409EFF;
}

.user-area {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.username {
  margin-left: 8px;
}

.el-dropdown-menu {
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.el-footer {
  background-color: #333;
  color: #fff;
  text-align: center;
  padding: 20px 0;
}

.el-main {
  background-color: #f9fafc;
  min-height: calc(100vh - 60px - 60px); /* Subtracting header and footer heights */
}
</style>