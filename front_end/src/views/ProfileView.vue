<template>
  <div class="profile">
    <div class="user-profile">
      <aside class="sidebar">
        <div class="avatar-container">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleAvatarChange"
          >
            <img v-if="userInfo.avatarUrl" :src="userInfo.avatarUrl" class="avatar" alt="Avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </div>
        <div class="user-info">
          <div class="name">
            <h2>{{ userInfo.username }}</h2>
            <el-button @click="updateUsername" icon="Edit" circle></el-button>
          </div>
          <div class="line"></div>
          <img src="@/assets/img/huajuan.png" alt="" width="260" height="430">
          <img src="@/assets/img/guoyun.png" alt="" width="260" height="250">
        </div>
      </aside>

      <main class="content">
        <section class="score-section">
          <div class="score">
            <h3>用户分数：</h3>
            <h1>{{ userInfo.score }}</h1>
            <p>超过的用户百分比：{{ userInfo.percentile }}%</p>
          </div>
          <img src="@/assets/img/maobi.png" alt="" width="150" height="200" style="margin-right:20px;">
        </section>

        <section class="achievements-section">
          <h3>已解锁成就</h3>
          <div class="achievements">
            <div v-for="achievement in unlockedAchievements" :key="achievement.level" class="achievement" :class="achievement.class">
              {{ achievement.content }}
            </div>
          </div>
        </section>

        <section class="rules-section">
          <h3>成就规则介绍</h3>
          <div class="line1">
            <div v-for="achievement in achievements.slice(0, 3)" :key="achievement.label" class="level">
              <img :src="achievement.addr" alt="" width="120" height="120">
              <p>{{ achievement.content }}: {{ achievement.points }}分</p>
            </div>
          </div>
          <div class="line2">
            <div v-for="achievement in achievements.slice(3)" :key="achievement.label" class="level">
              <img :src="achievement.addr" alt="" width="120" height="120">
              <p>{{ achievement.content }}: {{ achievement.points }}分</p>
            </div>
            <img src="@/assets/img/qingchun.png" alt="" width="300" height="300">
          </div>
        </section>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useStore } from 'vuex'
import { api_getPoint, api_getPointPercent, api_updateUsername, api_uploadAvatar } from '@/api/user'

const store = useStore()

const userInfo = ref({
  avatarUrl: computed(() => store.state.user.avatar),
  username: computed(() => store.state.user.username),
  score: 0,
  percentile: 0
})

const achievements = ref([
  { label: 'A', points: 100, addr: require('@/assets/img/1.png'), content: '小试牛刀', class: 'achievement-a' },
  { label: 'B', points: 200, addr: require('@/assets/img/2.png'), content: '初露锋芒', class: 'achievement-b' },
  { label: 'C', points: 300, addr: require('@/assets/img/3.png'), content: '才华横溢', class: 'achievement-c' },
  { label: 'D', points: 400, addr: require('@/assets/img/4.png'), content: '学富五车', class: 'achievement-d' },
  { label: 'E', points: 500, addr: require('@/assets/img/5.png'), content: '天下无双', class: 'achievement-e' },
])

const unlockedAchievements = computed(() => {
  return achievements.value.filter(achievement => userInfo.value.score >= achievement.points)
})

const updateUsername = async () => {
  try {
    const { value: newNickname } = await ElMessageBox.prompt('请输入新的昵称', '修改昵称', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputValue: userInfo.value.nickname
    })
    if (newNickname) {
      const response = await api_updateUsername(newNickname);
      if(response.data.code == 200)
      {
        ElMessage.success('修改成功')
        await store.dispatch('user/getUserInfo')
      }
      else
        ElMessage.error('修改失败')
    }
    else
      ElMessage.error('昵称不能为空')
  } catch {
    ElMessage.info('取消修改')
  }
}

const handleAvatarChange = async (file) => {
  if (!beforeAvatarUpload(file.raw)) {
    return
  }

  try {
    const response = await api_uploadAvatar(file.raw)
    if (response.data.code === 200) {
      ElMessage.success('头像修改成功')
      await store.dispatch('user/getUserInfo')
    } else {
      ElMessage.error('头像上传失败')
    }
  } catch (error) {
    console.error('上传出错:', error)
    ElMessage.error('头像上传出错')
  }
}

const beforeAvatarUpload = (file) => { 
  console.log(file.type) 
  const isPNG = file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isPNG) {
    ElMessage.error('上传头像图片只能是 PNG 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
    return false
  }
  return true
}

onMounted(async () => {
  const scoreResponse = await api_getPoint();
  if(scoreResponse.data.code == 200)
    userInfo.value.score = scoreResponse.data.data;
  else
    ElMessage.error('网络异常');
  const scorePercentResponse = await api_getPointPercent();
  if(scorePercentResponse.data.code == 200)
    userInfo.value.percentile = scorePercentResponse.data.data;
  else
    ElMessage.error('网络异常');
})
</script>

<style scoped>
  .profile {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }

  .user-profile {
    display: flex;
    padding: 20px;
    background-color: #f5f5f5;
  }

  .sidebar {
    text-align: center;
    width: 250px;
    padding: 20px;
    background: floralwhite;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    height: 1038px;
  }

  .avatar-container {
    width: 178px;
    height: 178px;
    margin: 0 auto;
    position: relative;
  }

  .avatar-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 50%;
    cursor: pointer;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

  .avatar-uploader:hover {
    border-color: #409eff;
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100%;
    height: 100%;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 100%;
    height: 100%;
    display: block;
    object-fit: cover;
    border-radius: 50%;
  }

  .user-info {
    text-align: center;
    margin-top: 20px;
  }

  .user-info button {
    background: floralwhite;
  }

  .user-info h2 {
    margin: 0;
    font-size: 18px;
  }

  .user-info p {
    font-size: 14px;
    color: #666;
  }

  button {
    margin-top: 5px;
    cursor: pointer;
    border: none;
    padding: 5px 10px;
    border-radius: 5px;
    transition: background-color 0.3s;
  }

  .content {
    flex: 1;
    margin-left: 20px;
  }
  
  .score-section{
    display:flex;
    background: linear-gradient(to bottom, #323232 0%, #3F3F3F 40%, #1C1C1C 150%), linear-gradient(to top, rgba(255,255,255,0.40) 0%, rgba(0,0,0,0.25) 200%);
    background-blend-mode: multiply;
    color: white;
    margin-bottom: 20px;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  .score-section h1{
    font-size:50px;
  }
  
  .achievements-section{
    background-image: linear-gradient(to right, #3ab5b0 0%, #3d99be 31%, #56317a 100%);
    margin-bottom: 20px;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  .rules-section {
    background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
    margin-bottom: 20px;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  
  .achievements {
    display: flex;
    flex-wrap: wrap;
  }
  
  .achievement {
    margin-right: 10px;
    padding: 5px 10px;
    border-radius: 4px;
    color: white;
  }
  
  .achievement-a {
    background-image: linear-gradient(to right, #8c754b, #aa915f);
  }
  
  .achievement-b {
     background-image: linear-gradient(to right, #222222, #444444);
  }
  
  .achievement-c {
     color:black;
     background-image: linear-gradient(to right, #C0C0C0, #E0E0E0);
  }
  
  .achievement-d {
    color:black;
    background-image: linear-gradient(to right, #FFD700, #FFD700);
  }
  
  .achievement-e {
    background-image: linear-gradient(to right, rgba(128, 0, 128, 0.8), rgba(180, 0, 180, 0.8));
  }
  
  .line{
    border-top: 1px solid #000; /* 1px宽的黑色线条 */
    margin: 5px 0; /* 上下外边距5px，左右0 */
  }
  
  .name{
    margin-left: 30%;
    display:flex;
  }
  
  .line1,.line2{
    display:flex;
  }
  
  .level {
    margin:20px;
  }
  
  .myBio p{
    font-weight: bold;
    font-size:15px;
  }
  
  .tip h1{
    font-size:1px;
  }
</style>