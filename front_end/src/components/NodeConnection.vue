// components/NodeConnection.vue
<template>
  <svg class="node-connection" :class="{ 'is-prerequisite': isPrerequisite }">
    <path
      :d="pathData"
      :class="{
        'is-completed': isCompleted,
        'is-locked': isLocked
      }"
      @mouseenter="handleMouseEnter"
      @mouseleave="handleMouseLeave"
    />
    <text
      v-if="showLabel && connection.type === 'prerequisite'"
      :x="labelPosition.x"
      :y="labelPosition.y"
      class="connection-label"
    >
      需要 {{ prerequisiteProgress }}% 完成度
    </text>
  </svg>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import type { NodeConnection, KnowledgeNode } from '../types/knowledge-map'
import { NodeStatus } from '../types/knowledge-map'

const props = defineProps<{
  connection: NodeConnection
  sourceNode: KnowledgeNode
  targetNode: KnowledgeNode
  sourcePosition: { x: number; y: number }
  targetPosition: { x: number; y: number }
}>()

const showLabel = ref(false)

const isPrerequisite = computed(() => props.connection.type === 'prerequisite')
const isCompleted = computed(() => props.sourceNode.status === NodeStatus.COMPLETED)
const isLocked = computed(() => props.targetNode.status === NodeStatus.LOCKED)

// 计算路径
const pathData = computed(() => {
  const { sourcePosition: source, targetPosition: target } = props
  const dx = target.x - source.x
  const dy = target.y - source.y
  
  // 使用贝塞尔曲线创建平滑的连线
  const controlPoint1 = {
    x: source.x + dx * 0.5,
    y: source.y
  }
  const controlPoint2 = {
    x: target.x - dx * 0.5,
    y: target.y
  }
  
  return `M ${source.x} ${source.y} 
          C ${controlPoint1.x} ${controlPoint1.y},
            ${controlPoint2.x} ${controlPoint2.y},
            ${target.x} ${target.y}`
})

// 计算标签位置
const labelPosition = computed(() => {
  const { sourcePosition: source, targetPosition: target } = props
  return {
    x: (source.x + target.x) / 2,
    y: (source.y + target.y) / 2 - 10
  }
})

const prerequisiteProgress = computed(() => {
  return props.targetNode.unlockRequirements?.prerequisiteProgress || 100
})

const handleMouseEnter = () => {
  showLabel.value = true
}

const handleMouseLeave = () => {
  showLabel.value = false
}
</script>

<style scoped>
.node-connection {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  overflow: visible;
}

path {
  fill: none;
  stroke: #dcdfe6;
  stroke-width: 2;
  pointer-events: auto;
  transition: stroke 0.3s ease, stroke-width 0.3s ease;
}

path:hover {
  stroke-width: 3;
}

.is-prerequisite path {
  stroke: #409eff;
  stroke-dasharray: 5, 5;
}

.is-completed path {
  stroke: #67c23a;
}

.is-locked path {
  stroke: #909399;
  opacity: 0.5;
}

.connection-label {
  font-size: 12px;
  fill: #606266;
  text-anchor: middle;
  pointer-events: none;
}
</style>