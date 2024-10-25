<template>
  <div class="node-details">
    <el-descriptions :column="1" border>
      <el-descriptions-item label="节点类型">
        {{ getNodeTypeLabel(node.type) }}
      </el-descriptions-item>
      
      <el-descriptions-item label="状态">
        <el-tag :type="getStatusType(node.status)">
          {{ getStatusLabel(node.status) }}
        </el-tag>
      </el-descriptions-item>
      
      <el-descriptions-item v-if="node.difficulty" label="难度">
        <el-rate
          v-model="node.difficulty"
          :max="5"
          disabled
          show-score
        />
      </el-descriptions-item>
      
      <el-descriptions-item v-if="node.progress !== undefined" label="学习进度">
        <el-progress
          :percentage="node.progress"
          :status="getProgressStatus(node.progress)"
        />
      </el-descriptions-item>
      
      <el-descriptions-item v-if="node.prerequisites?.length" label="前置要求">
        <el-space wrap>
          <el-tag
            v-for="preReq in node.prerequisites"
            :key="preReq"
            size="small"
            effect="plain"
          >
            {{ getPrerequisiteName(preReq) }}
          </el-tag>
        </el-space>
      </el-descriptions-item>
    </el-descriptions>

    <div class="node-details__actions">
      <el-button
        v-if="node.status === 'locked' && canUnlock"
        type="primary"
        @click="handleUnlock"
      >
        解锁节点
      </el-button>
      
      <el-button
        v-if="node.status === 'unlocked' || node.status === 'in_progress'"
        @click="handleStartLearning"
      >
        开始学习
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { KnowledgeNode } from '../types/knowledge-map'
import { NodeType, NodeStatus } from '../types/knowledge-map'

const props = defineProps<{
  node: KnowledgeNode
}>()

const emit = defineEmits<{
  (e: 'update', node: KnowledgeNode): void
}>()

// 节点类型标签
const getNodeTypeLabel = (type: NodeType) => ({
  [NodeType.ROOT]: '根节点',
  [NodeType.CATEGORY]: '主题类别',
  [NodeType.SUBCATEGORY]: '子类别',
  [NodeType.TOPIC]: '知识点'
})[type]

// 状态标签和样式
const getStatusLabel = (status: NodeStatus) => ({
  [NodeStatus.LOCKED]: '已锁定',
  [NodeStatus.UNLOCKED]: '已解锁',
  [NodeStatus.IN_PROGRESS]: '学习中',
  [NodeStatus.COMPLETED]: '已完成'
})[status]

const getStatusType = (status: NodeStatus) => ({
  [NodeStatus.LOCKED]: 'info',
  [NodeStatus.UNLOCKED]: 'warning',
  [NodeStatus.IN_PROGRESS]: 'primary',
  [NodeStatus.COMPLETED]: 'success'
})[status]

// 进度状态
const getProgressStatus = (progress: number) => {
  if (progress >= 100) return 'success'
  if (progress >= 60) return 'warning'
  return ''
}

// 获取前置节点名称（实际应用中需要通过某种方式获取）
const getPrerequisiteName = (preReqId: string) => {
  return `节点 ${preReqId}` // 这里应该返回实际的节点名称
}

// 检查是否可以解锁
const canUnlock = computed(() => {
  if (!props.node.prerequisites?.length) return true
  
  // 这里需要实际检查前置节点的完成状态
  return props.node.prerequisites.every(preReqId => {
    // 检查前置节点进度是否满足要求
    return true // 实际实现中需要真实的检查逻辑
  })
})

// 操作处理
const handleUnlock = () => {
  if (!canUnlock.value) return
  
  emit('update', {
    ...props.node,
    status: NodeStatus.UNLOCKED
  })
}

const handleStartLearning = () => {
  emit('update', {
    ...props.node,
    status: NodeStatus.IN_PROGRESS
  })
}
</script>

<style scoped>
.node-details {
  padding: 20px;
}



</style>
