<template>
  <el-card class="challenge">
    <div class="date-selector">
      <div v-for="day in weekDays" :key="day.fullDate" 
           class="date-item" 
           :class="{ 'selected': day.date === selectedDate, 'weekend': day.isWeekend }"
           @click="selectDate(day.date, day.fullDate)">
        <div class="weekday">{{ day.weekday }}</div>
        <div class="date">{{ day.date }}</div>
      </div>
    </div>

    <el-divider></el-divider>

    <div class="section challenge-section" @click="goToChallenge" v-if="dailyChallenges[selectedDate]">
      <h3><el-icon><Sunny /></el-icon> 每日挑战</h3>
      <div class="challenge-content">
        <span class="challenge-title">{{ dailyChallenges[selectedDate] }}</span>
      </div>
    </div>
    <div class="section" v-else>
      <h3><el-icon><Sunny /></el-icon> 每日挑战</h3>
      <p>无挑战</p>
    </div>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-statistic title="连续完成" :value="continuousDays" suffix="天"></el-statistic>
      </el-col>
      <el-col :span="8">
        <el-statistic title="本月完成" :value="monthlyCompletions" suffix="次"></el-statistic>
      </el-col>
      <el-col :span="8">
        <el-statistic title="完成率" :value="completionRate" suffix="%"></el-statistic>
      </el-col>
    </el-row>

    <div class="activity-graph">
      <div v-for="(activity, index) in activityData" :key="index" 
           class="activity-cell" 
           :class="getActivityClass(activity, index)"
           :title="`${getCurrentMonth()}月${index + 1}日`"></div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Sunny } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { api_getCurMonth, api_listTopics, api_getCurContinue } from '@/api/challedge'
import { ElMessage } from 'element-plus'

const router = useRouter()
const store = useStore()

const isLoggedIn = computed(() => store.getters['user/isLoggedIn'])

const today = new Date()
const selectedDate = ref(today.getDate())
const selectedFullDate = ref(formatDate(today))
const activityData = ref([])
const dailyChallenges = ref({})
const continuousDays = ref(0)
const monthlyCompletions = ref(0)
const completionRate = ref(0)

const weekDays = computed(() => {
  const days = []
  for (let i = 6; i >= 0; i--) {
    const date = new Date(today)
    date.setDate(today.getDate() - i)
    days.push({
      weekday: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'][date.getDay()],
      date: date.getDate(),
      fullDate: formatDate(date),
      isWeekend: date.getDay() === 0 || date.getDay() === 6,
    })
  }
  return days
})

function formatDate(date) {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  return `${year}-${month}-${day}`
}

onMounted(async () => {
  try {
    const topicsResponse = await api_listTopics()
    if (topicsResponse.data.code === 200) {
      const topicsData = topicsResponse.data.data
      topicsData.forEach(topic => {
        dailyChallenges.value[new Date(topic.date).getDate()] = topic.content
      })
    }
    dailyChallenges.value = {
        [today.getDate()]: '武术',
        [today.getDate() - 1]: '瑜伽',
        [today.getDate() - 2]: '跑步',
        [today.getDate() - 3]: '游泳',
        [today.getDate() - 4]: '举重',
        [today.getDate() - 5]: '骑行',
        [today.getDate() - 6]: '拳击',
      }

    if (isLoggedIn.value) {
      const monthResponse = await api_getCurMonth()
      if (monthResponse.data.code === 200) {
        activityData.value = monthResponse.data.data
        calculateStats()
      }

      const continueResponse = await api_getCurContinue()
      if (continueResponse.data.code === 200) {
        continuousDays.value = continueResponse.data.data
      }
    } else {
      activityData.value = new Array(getDaysInMonth()).fill(null)
    }
  } catch (error) {
    console.error('Error fetching data:', error)
    ElMessage.error('获取数据失败')
  }
})

const calculateStats = () => {
  const today = new Date().getDate()
  let completions = 0
  for (let i = 0; i < today; i++) {
    if (activityData.value[i] >= 1) {
      completions++
    }
  }
  monthlyCompletions.value = completions
  completionRate.value = Math.round((completions / today) * 100)
}

const goToChallenge = () => {
  if (dailyChallenges.value[selectedDate.value]) {
    router.push(`/daily-challedge/${selectedFullDate.value}`)
  }
}

const selectDate = (date, fullDate) => {
  selectedDate.value = date
  selectedFullDate.value = fullDate
}

const getDaysInMonth = () => {
  return new Date(today.getFullYear(), today.getMonth() + 1, 0).getDate()
}

const getCurrentMonth = () => {
  return today.getMonth() + 1
}

const getActivityClass = (activity, day) => {
  if (day >= today.getDate() || activity === null) 
    return ''
  return activity > 0 ? `active-${activity}` : ''
}
</script>

<style scoped>
.challenge {
  width: 100%;
  max-width: 400px;
  margin: auto;
}

.date-selector {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.date-item {
  text-align: center;
  cursor: pointer;
  padding: 5px;
  border-radius: 4px;
  transition: all 0.3s;
}

.date-item:hover {
  background-color: #bfbfbf;
}

.date-item.selected {
  background-color: #d9d9d9;
}

.weekday {
  font-size: 12px;
  color: #888;
}

.date {
  font-size: 16px;
  font-weight: bold;
}

.section {
  margin-bottom: 20px;
}

.section h3 {
  display: flex;
  align-items: center;
  font-size: 16px;
  margin-bottom: 5px;
}

.section h3 .el-icon {
  margin-right: 5px;
}

.challenge-section {
  background-color: #f0f0f0;
  border-radius: 8px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.challenge-section:hover {
  background-color: #e0e0e0;
}

.challenge-section:active {
  transform: scale(0.98);
}

.challenge-content {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.challenge-title {
  color: #333;
}

.activity-graph {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 10px;
  margin-top: 20px;
}

.activity-cell {
  aspect-ratio: 1;
  background-color: #ebedf0;
  border-radius: 8px;
}

.active-1 { background-color: #c6e48b; }
.active-2 { background-color: #7bc96f; }
.active-3 { background-color: #239a3b; }
</style>