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
        <el-row :gutter="20">
          <el-col :span="24">
            <el-card class="feature-card" @click="goToMap('/map1')">
              <h3>文化知识地图 1</h3>
              <p>可视化知识结构，探索文化脉络</p>
              <p>占位符</p>
              <p>占位符</p>
              <p>占位符</p>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 40px;">
          <el-col :span="24">
            <el-card class="feature-card" @click="goToMap('/map2')">
              <h3>文化知识地图 2</h3>
              <p>可视化知识结构，探索文化脉络</p>
              <p>占位符</p>
              <p>占位符</p>
              <p>占位符</p>
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
              <p>{{ topic.description }}</p>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, inject } from 'vue'
import { useRouter } from 'vue-router'
import DailyChallenge from '@/components/DailyChallenge.vue'

const router = useRouter()
const showLoginDialog = inject('showLoginDialog')

const allTopics = ref([
  { title: '如何理解中国传统文化中的"中庸之道"？', description: '用户：墨香斋主' },
  { title: '汉字的演变历程及其文化意义', description: '用户：墨香斋主' },
  { title: '中国古代四大发明对世界文明的影响', description: '用户：墨香斋主' },
  { title: '我的篆刻学习之路', description: '用户：墨香斋主' },
  { title: '从《论语》中学习为人处世的智慧', description: '用户：儒学爱好者' },
  { title: '中国传统节日的由来与现代传承', description: '用户：文化传播者' },
])

const startExplore = () => {
  router.push('/explore')
}

const goToMap = (route) => {
  router.push(route)
}

const goToBlog = (id) => {
  router.push(`/blog/${id}`)
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
  height: 100%;
  cursor: pointer;
  transition: all 0.3s ease;
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