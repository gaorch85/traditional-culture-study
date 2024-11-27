<template>
  <div class="home">
    <el-row class="hero" type="flex" justify="center" align="middle">
      <el-col :span="16" class="text-center">
        <h1 class="hero-title">探索中华文化的奥秘</h1>
        <p class="hero-subtitle">与AI同行，开启你的文化启智之旅</p>
        <el-button type="primary" size="large" @click="startExplore">开始探索</el-button>
      </el-col>
    </el-row>

    <el-row class="features" :gutter="20">
      <el-col :span="16">
        <el-row :gutter="20" v-for="(module, index) in randomModules" :key="module.id">
          <el-col :span="24">
            <el-card class="feature-card" @click="goToMap(module.id)">
              <h3>{{ module.title }}</h3>
              <p>{{ module.overview }}</p>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="8">
        <DailyChallenge></DailyChallenge>
      </el-col>
    </el-row>

    <el-row class="community" :gutter="20">
      <el-col :span="24">
        <h2>热门讨论</h2>
        <el-row :gutter="20">
          <el-col :span="8" v-for="(topic, index) in allTopics" :key="index">
            <el-card class="community-card" @click="goToBlog(index + 1)">
              <h3>{{ topic.title }}</h3>
              <p>{{ "用户：" + topic.username }}</p>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, inject, onMounted } from 'vue'
import { getToken, setToken, removeToken } from '@/util/auth'
import { useRouter } from 'vue-router'
import { api_getHot } from '@/api/blog'
import DailyChallenge from '@/components/DailyChallenge.vue'
import data from './Explore/data.json'

const router = useRouter()
const showLoginDialog = inject('showLoginDialog')

const allTopics = ref([])
const randomModules = ref([])

// Function to get two unique random modules
const getRandomModules = () => {
  const shuffled = [...data.posts].sort(() => 0.5 - Math.random())
  return shuffled.slice(0, 2)
}

// Function to extract overview content
const extractOverview = async (postId) => {
  try {
    const response = await fetch(`/data/${postId}.md`)
    const content = await response.text()
    
    // Find the overview section
    const overviewMatch = content.match(/##\s*概述([\s\S]*?)##/i)
    
    if (overviewMatch && overviewMatch[1]) {
      // Remove extra whitespace and newlines
      return overviewMatch[1].trim().replace(/\n/g, ' ')
    }
    
    return '暂无内容'
  } catch (error) {
    console.error('Failed to fetch module content:', error)
    return '内容加载失败'
  }
}

onMounted(async () => {
  // Fetch hot topics
  if(getToken()) {
    const response = await api_getHot()
    allTopics.value = response.data.data.items
  } else {
    allTopics.value = [
      { title: '如何理解中国传统文化中的"中庸之道"？', username: '墨香斋主' },
      { title: '汉字的演变历程及其文化意义', username: '墨香斋主' },
      { title: '中国古代四大发明对世界文明的影响', username: '墨香斋主' },
      { title: '我的篆刻学习之路', username: '墨香斋主' },
      { title: '从《论语》中学习为人处世的智慧', username: '儒学爱好者' },
      { title: '中国传统节日的由来与现代传承', username: '文化传播者' },
    ]
  }

  // Get random modules and fetch their content
  const randomPosts = getRandomModules()
  
  const modulesWithContent = await Promise.all(
    randomPosts.map(async (post) => ({
      ...post,
      overview: await extractOverview(post.id)
    }))
  )

  randomModules.value = modulesWithContent
})

const startExplore = () => {
  router.push('/explore')
}

const goToMap = (postId) => {
  router.push(`/explore/details/${postId}`)
}

const goToBlog = (id) => {
  router.push(`/blog/details/${id}`)
}
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.hero {
  background: linear-gradient(135deg, #ff4e50, #f9d423);
  color: white;
  padding: 60px 0;
  border-radius: 8px;
  margin-bottom: 40px;
}

.hero-title {
  font-size: 2.5em;
  margin-bottom: 20px;
}

.hero-subtitle {
  font-size: 1.2em;
  margin-bottom: 30px;
}

.features {
  margin-bottom: 40px;
}

.feature-card {
  height: 80%;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 80px; 
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.daily-challenge, .story-generator {
  margin-bottom: 40px;
}

.community-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.community-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.submit-btn, .generate-btn {
  margin-top: 20px;
}

h2 {
  margin-bottom: 20px;
  color: #409EFF;
}
</style>