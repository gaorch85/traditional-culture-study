// components/DraggableNode.vue
<template>
  <div
    ref="nodeRef"
    class="draggable-node"
    :class="[
      `draggable-node--${node.type}`,
      `draggable-node--${node.status}`,
      { 'is-dragging': isDragging }
    ]"
    :style="nodeStyle"
    @mousedown="startDrag"
    @click.stop="handleClick"
  >
    <div class="draggable-node__content">
      <el-icon v-if="node.icon"><component :is="node.icon" /></el-icon>
      <span class="draggable-node__name">{{ node.name }}</span>
      <div class="draggable-node__status">
        <el-icon v-if="node.status === NodeStatus.LOCKED"><Lock /></el-icon>
        <el-tag 
          v-if="node.difficulty" 
          size="small" 
          :type="getDifficultyType(node.difficulty)"
        >
          难度: {{ node.difficulty }}
        </el-tag>
      </div>
    </div>

    <el-progress
      v-if="node.progress !== undefined"
      :percentage="node.progress"
      :status="getProgressStatus(node.progress)"
      :stroke-width="4"
      class="draggable-node__progress"
    />

    <div class="draggable-node__prerequisites" v-if="node.prerequisites?.length">
      <span class="prerequisites-label">前置要求:</span>
      <el-tag 
        v-for="preReq in node.prerequisites" 
        :key="preReq"
        size="small"
        effect="plain"
      >
        {{ getPrerequisiteName(preReq) }}
      </el-tag>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Lock } from '@element-plus/icons-vue'
import type { KnowledgeNode } from '../types/knowledge-map'
import { NodeStatus } from '../types/knowledge-map'

const props = defineProps<{
  node: KnowledgeNode
  scale: number
}>()

const emit = defineEmits<{
  (e: 'node-click', node: KnowledgeNode): void
  (e: 'update-position', nodeId: string, position: { x: number; y: number }): void
}>()

const nodeRef = ref<HTMLElement | null>(null)
const isDragging = ref(false)
const position = ref(props.node.position || { x: 0, y: 0 })

// 计算节点样式
const nodeStyle = computed(() => ({
  transform: `translate(${position.value.x}px, ${position.value.y}px) scale(${props.scale})`,
  zIndex: isDragging.value ? 1000 : 1
}))

// 拖拽相关
let startX = 0
let startY = 0
let originalX = 0
let originalY = 0

const startDrag = (event: MouseEvent) => {
  if (props.node.status === NodeStatus.LOCKED) return
  
  isDragging.value = true
  startX = event.clientX
  startY = event.clientY
  originalX = position.value.x
  originalY = position.value.y

  document.addEventListener('mousemove', handleDrag)
  document.addEventListener('mouseup', stopDrag)
}

const handleDrag = (event: MouseEvent) => {
  if (!isDragging.value) return

  const dx = (event.clientX - startX) / props.scale
  const dy = (event.clientY - startY) / props.scale

  position.value = {
    x: originalX + dx,
    y: originalY + dy
  }
}

const stopDrag = () => {
  isDragging.value = false
  document.removeEventListener('mousemove', handleDrag)
  document.removeEventListener('mouseup', stopDrag)
  emit('update-position', props.node.id, position.value)
}

// 难度等级样式
const getDifficultyType = (difficulty: number) => {
  if (difficulty <= 2) return 'success'
  if (difficulty <= 4) return 'warning'
  return 'danger'
}

// 进度状态
const getProgressStatus = (progress: number) => {
  if (progress >= 100) return 'success'
  if (progress >= 60) return 'warning'
  return ''
}

// 获取前置节点名称
const getPrerequisiteName = (preReqId: string) => {
  // 这里需要通过某种方式获取节点名称，可以通过props传入或者使用store
  return `节点 ${preReqId}`
}

const handleClick = () => {
  if (!isDragging.value) {
    emit('node-click', props.node)
  }
}
</script>

<style scoped>
.draggable-node {
  position: absolute;
  padding: 16px;
  border-radius: 8px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: move;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  user-select: none;
  min-width: 200px;
}

.draggable-node:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.is-dragging {
  opacity: 0.8;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.draggable-node__content {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.draggable-node__name {
  flex: 1;
  font-weight: 500;
  font-size: 16px;
}

.draggable-node__status {
  display: flex;
  align-items: center;
  gap: 8px;
}

.draggable-node__prerequisites {
  margin-top: 8px;
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  align-items: center;
}

.prerequisites-label {
  font-size: 12px;
  color: #909399;
}

/* 节点类型样式 */
.draggable-node--root {
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f1ff 100%);
  border: 2px solid #409eff;
}

.draggable-node--category {
  background: linear-gradient(135deg, #f5f7fa 0%, #f0f2f5 100%);
  border: 1px solid #dcdfe6;
}

.draggable-node--subcategory {
  background: white;
  border: 1px solid #e4e7ed;
}

.draggable-node--topic {
  background: #fafafa;
  border: 1px solid #ebeef5;
}

/* 节点状态样式 */
.draggable-node--locked {
  opacity: 0.7;
  background: #f5f5f5;
  cursor: not-allowed;
}

.draggable-node--completed {
  border-color: #67c23a;
}
</style>