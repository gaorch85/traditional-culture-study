<template>
  <div class="knowledge-tree-container">
    <div class="knowledge-tree-wrapper">
      <div class="knowledge-tree">
        <div v-for="node in knowledgeNodes" :key="node.id" 
             class="node-wrapper"
             :style="{ left: node.x + 'px', top: node.y + 'px' }">
          <div class="node" 
               :class="{ completed: isNodeCompleted(node.id) }"
               @click="handleNodeClick(node)">
            <el-tooltip 
              :content="node.name"
              placement="top">
              <div class="node-content">
                <div class="node-icon" :style="{ backgroundColor: node.color }"></div>
                <span>{{ node.name }}</span>
                <!-- 仅保留完成状态图标 -->
                <div class="status-icon">
                  <el-icon v-if="isNodeCompleted(node.id)" class="completed-icon"><Check /></el-icon>
                </div>
              </div>
            </el-tooltip>
          </div>
        </div>
        
        <!-- 连接线 -->
        <svg class="connections">
          <path v-for="(connection, index) in connections" 
                :key="index"
                :d="connection.path"
                class="connection-line"/>
        </svg>
      </div>
    </div>

    <!-- 知识点详情弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="selectedNode?.name"
      width="30%">
      <div class="dialog-content">
        <div class="node-status">
          <el-tag :type="isNodeCompleted(selectedNode.id) ? 'success' : 'info'">
            {{ isNodeCompleted(selectedNode.id) ? '已完成' : '未完成' }}
          </el-tag>
        </div>
        <p class="description">{{ selectedNode?.description }}</p>
        <div class="button-container">
          <el-button type="primary" @click="startLearning">立即学习</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
 
<script setup>
import { ref, computed } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { Check } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const store = useStore()
const router = useRouter()
// 修改知识节点数据
const knowledgeNodes = ref([
  // 哲学思想类
  {
    id: 'A',
    name: '儒家思想',
    description: '以仁、义、礼、智、信为核心，强调修身齐家治国平天下。对中国传统文化有深远影响，是中华文明的重要基石。',
    x: 100,
    y: 50,
    color: '#FF6B6B',
    dependencies: [],
    category: '哲学思想'
  },
  {
    id: 'B',
    name: '道家思想',
    description: '以老子、庄子为代表，强调"道法自然"，追求天人合一。主张无为而治，对中国哲学产生深远影响。',
    x: 100,
    y: 225,
    color: '#4ECDC4',
    dependencies: [],
    category: '哲学思想'
  },
  {
    id: 'C',
    name: '佛教哲学',
    description: '融合了印度佛教与中国传统思想，形成具有中国特色的佛教文化体系，强调因果轮回与心性修养。',
    x: 100,
    y: 300,
    color: '#45B7D1',
    dependencies: [],
    category: '哲学思想'
  },
  {
    id: 'D',
    name: '兵法与军事思想',
    description: '以孙子兵法为代表的中国古代军事理论，强调谋略与智慧，影响深远。',
    x: 100,
    y: 400,
    color: '#96CEB4',
    dependencies: [],
    category: '哲学思想'
  },

  // 历史文化类
  {
    id: 'E',
    name: '中华历史概述',
    description: '系统介绍中国历史发展脉络，包括重要朝代更替、文明演进等基础知识。',
    x: 100,
    y: 500,
    color: '#FFEEAD',
    dependencies: [],
    category: '历史文化'
  },
  {
    id: 'F',
    name: '重要历史事件',
    description: '探讨对中国历史产生重大影响的关键事件，分析其历史意义和现实启示。',
    x: 750,
    y: 325,
    color: '#D4A5A5',
    dependencies: ['E'],
    category: '历史文化'
  },
  {
    id: 'G',
    name: '传统节日',
    description: '介绍中国传统节日的起源、习俗和文化内涵，展现中华民族的精神特质。',
    x: 750,
    y: 400,
    color: '#9DC8C8',
    dependencies: ['E'],
    category: '历史文化'
  },

  // 文学艺术类
  {
    id: 'H',
    name: '古典诗词',
    description: '探讨中国古典诗词的艺术特色、代表作品和文化价值。融合儒道思想精华。',
    x: 450,
    y: 150,
    color: '#FF9999',
    dependencies: ['A', 'B'],
    category: '文学艺术'
  },
  {
    id: 'I',
    name: '小说与散文',
    description: '介绍中国古典小说、散文的发展历程和代表作品，展现文学艺术魅力。',
    x: 750,
    y: 475,
    color: '#85D6FF',
    dependencies: ['E'],
    category: '文学艺术'
  },
  {
    id: 'J',
    name: '书法与绘画',
    description: '探讨中国传统书法和绘画艺术的特点、技法和美学价值。',
    x: 100,
    y: 600,
    color: '#90EE90',
    dependencies: [],
    category: '文学艺术'
  },
  {
    id: 'K',
    name: '戏曲与民间艺术',
    description: '介绍中国戏曲艺术和民间艺术形式，展现民族文化特色。',
    x: 750,
    y: 550,
    color: '#FFB366',
    dependencies: ['E'],
    category: '文学艺术'
  },

  // 科学技术类
  {
    id: 'L',
    name: '中医理论',
    description: '探讨中医药学的基本理论、诊疗方法和养生智慧。',
    x: 450,
    y: 100,
    color: '#FF99CC',
    dependencies: ['A', 'B'],
    category: '科学技术'
  },
  {
    id: 'M',
    name: '古代农业科技',
    description: '介绍中国古代农业技术发展和创新，展现科技智慧。',
    x: 750,
    y: 625,
    color: '#99CCFF',
    dependencies: ['E'],
    category: '科学技术'
  },
  {
    id: 'N',
    name: '天文历法',
    description: '探讨中国古代天文观测和历法发展，展现科学成就。',
    x: 750,
    y: 700,
    color: '#99FF99',
    dependencies: ['E'],
    category: '科学技术'
  },

  // 礼仪与伦理类
  {
    id: 'O',
    name: '传统礼仪',
    description: '介绍中国传统礼仪规范和文化内涵，体现礼仪之邦特色。',
    x: 750,
    y: 25,
    color: '#FFCC99',
    dependencies: ['A'],
    category: '礼仪与伦理'
  },
  {
    id: 'P',
    name: '家庭伦理',
    description: '探讨中国传统家庭伦理观念和价值体系。',
    x: 750,
    y: 75,
    color: '#CC99FF',
    dependencies: ['A'],
    category: '礼仪与伦理'
  },


  // 宗教信仰类
  {
    id: 'Q',
    name: '道教文化',
    description: '探讨道教文化的发展历程、核心理念和社会影响。',
    x: 400,
    y: 225,
    color: '#99FFCC',
    dependencies: ['B'],
    category: '宗教信仰'
  },
  {
    id: 'R',
    name: '佛教文化',
    description: '介绍佛教在中国的传播、发展和本土化过程。',
    x: 400,
    y: 300,
    color: '#FFFF99',
    dependencies: ['C'],
    category: '宗教信仰'
  }
])

// 添加类别颜色映射
const categoryColors = {
  '哲学思想': '#FFE4E1',
  '历史文化': '#E0FFFF',
  '文学艺术': '#F0FFF0',
  '科学技术': '#FFF0F5',
  '礼仪与伦理': '#F5F5DC',
  '宗教信仰': '#E6E6FA'
}

// 添加类别标签显示
const showCategoryLabel = computed(() => {
  const categories = {}
  knowledgeNodes.value.forEach(node => {
    if (!categories[node.category]) {
      categories[node.category] = {
        name: node.category,
        color: categoryColors[node.category],
        x: node.x - 150,
        y: node.y
      }
    }
  })
  return Object.values(categories)
})
  
const isNodeCompleted = (nodeId) => {
  return store.getters['knowledge/getNodeStatus'](nodeId)
}

  const dialogVisible = ref(false)
  const selectedNode = ref(null)
  
  const connections = [
    { path:`M 200 50 L 600 50`},
    { path:`M 600 50 L 600 25`},
    { path:`M 600 50 L 600 75`},
    { path:`M 600 25 L 650 25`},
    { path:`M 600 75 L 650 75`},
    { path:`M 200 60 L 250 60`},
    { path:`M 200 220 L 250 220`},
    { path:`M 250 60 L 250 220`},
    { path:`M 200 230 L 300 230`},
    { path:`M 250 130 L 310 130`},
    { path:`M 310 130 L 310 100`},
    { path:`M 310 130 L 310 150`},
    { path:`M 310 150 L 350 150`},
    { path:`M 310 100 L 350 100`},
    { path:`M 200 300 L 300 300`},
    { path:`M 200 500 L 550 500`},
    { path:`M 550 325 L 550 700`},
    { path:`M 550 700 L 650 700`},
    { path:`M 550 625 L 650 625`},
    { path:`M 550 550 L 650 550`},
    { path:`M 550 475 L 650 475`},
    { path:`M 550 400 L 650 400`},
    { path:`M 550 325 L 650 325`},

  ]

  // 处理节点点击
  const handleNodeClick = (node) => {
    selectedNode.value = node
    dialogVisible.value = true
  }
  
  // 开始学习
  const startLearning = () => {
    if (selectedNode.value) {
      //selectedNode.value.completed = true
      dialogVisible.value = false
      const id = selectedNode.value.id
      router.push({ name: 'exploreDetail', params: { id } });
      // ElMessage.success('知识点学习完成！')
    }
  }
  </script>
  
<style scoped>
.knowledge-tree-container {
  width: 100%;
  min-height: 100vh;
  padding: 20px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
}

.knowledge-tree-wrapper {
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  overflow-x: auto;
  position: relative;
}

.knowledge-tree {
  position: relative;
  width: 100%;
  height: 100%;
  min-width: 1000px;
  padding: 20px;
}

.node-wrapper {
  position: absolute;
  transform: translate(-50%, -50%);
}

.node {
  padding: 10px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.node:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.node.completed {
  border-color: #67c23a;
  background: #f0f9eb;
}

.node-content {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 150px;
}

.node-icon {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.status-icon {
  margin-left: auto;
}

.completed-icon {
  color: #67c23a;
}

.connections {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.connection-line {
  fill: none;
  stroke: #dcdfe6;
  stroke-width: 2;
  stroke-dasharray: 5,5;
}

.dialog-content {
  padding: 20px 0;
}

.node-status {
  margin-bottom: 15px;
}

.description {
  line-height: 1.6;
  color: #606266;
  margin-bottom: 20px;
}

.button-container {
  text-align: center;
}

/* 响应式调整 */
@media screen and (max-width: 1200px) {
  .knowledge-tree-wrapper {
    padding: 0 20px;
  }
}

/* 添加滚动条样式 */
.knowledge-tree-wrapper::-webkit-scrollbar {
  height: 8px;
}

.knowledge-tree-wrapper::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.knowledge-tree-wrapper::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.knowledge-tree-wrapper::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style>