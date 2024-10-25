<template>
  <div class="knowledge-map">
    <!-- 控制面板 -->
    <div class="knowledge-map__controls">
      <el-button-group>
        <el-button @click="handleZoomIn" :disabled="scale >= 2">
          <el-icon><ZoomIn /></el-icon>
        </el-button>
        <el-button @click="handleZoomOut" :disabled="scale <= 0.5">
          <el-icon><ZoomOut /></el-icon>
        </el-button>
        <el-button @click="handleReset">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </el-button-group>
    </div>

    <!-- 知识图谱容器 -->
    <div 
      class="knowledge-map__container" 
      ref="containerRef"
      @mousedown="startPan"
      @wheel="handleWheel"
    >
      <!-- 连线层 -->
      <div class="connections-layer" :style="transformStyle">
        <node-connection
          v-for="connection in connections"
          :key="`${connection.source}-${connection.target}`"
          :connection="connection"
          :source-node="getNodeById(connection.source)"
          :target-node="getNodeById(connection.target)"
          :source-position="getNodePosition(connection.source)"
          :target-position="getNodePosition(connection.target)"
        />
      </div>

      <!-- 节点层 -->
      <div class="nodes-layer" :style="transformStyle">
        <draggable-node
          v-for="node in flattenedNodes"
          :key="node.id"
          :node="node"
          :scale="scale"
          @node-click="handleNodeClick"
          @update-position="handleNodePositionUpdate"
        />
      </div>
    </div>

    <!-- 节点详情对话框 -->
    <el-dialog
      v-model="showNodeDetails"
      :title="selectedNode?.name"
      width="50%"
      destroy-on-close
    >
      <node-details 
        v-if="selectedNode"
        :node="selectedNode"
        @update="handleNodeUpdate"
      />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ZoomIn, ZoomOut, Refresh } from '@element-plus/icons-vue'
import DraggableNode from '@/components/DraggableNode.vue'
import NodeConnection from '@/components/NodeConnection.vue'
import NodeDetails from './NodeDetails.vue'
import type { KnowledgeNode, NodeConnection as INodeConnection } from '../types/knowledge-map'
import { NodeType, NodeStatus } from '../types/knowledge-map'

// 状态
const scale = ref(1)
const panOffset = ref({ x: 0, y: 0 })
const showNodeDetails = ref(false)
const selectedNode = ref<KnowledgeNode | null>(null)
const containerRef = ref<HTMLElement | null>(null)

// 示例数据
const knowledgeData = ref<KnowledgeNode>({
  id: 'root',
  name: '中国传统文化',
  type: NodeType.ROOT,
  status: NodeStatus.UNLOCKED,
  children: [
    {
      id: 'literature',
      name: '文学',
      type: NodeType.CATEGORY,
      status: NodeStatus.UNLOCKED,
      children: [
        {
          id: 'poetry',
          name: '诗词',
          type: NodeType.SUBCATEGORY,
          status: NodeStatus.UNLOCKED,
          children: [
            {
              id: 'tang-poetry',
              name: '唐诗',
              type: NodeType.TOPIC,
              status: NodeStatus.UNLOCKED,
              progress: 30,
              difficulty: 3
            }
          ]
        }
      ]
    },
    {
      id: 'philosophy',
      name: '哲学',
      type: NodeType.CATEGORY,
      status: NodeStatus.UNLOCKED,
      children: [
        {
          id: 'confucianism',
          name: '儒家思想',
          type: NodeType.SUBCATEGORY,
          status: NodeStatus.LOCKED,
          prerequisites: ['poetry'],
          unlockRequirements: {
            prerequisiteProgress: 60
          }
        }
      ]
    }
  ]
})

// 计算属性
const flattenedNodes = computed(() => {
  const nodes: KnowledgeNode[] = []
  const traverse = (node: KnowledgeNode, level: number = 0, index: number = 0) => {
    // 根据层级和索引计算初始位置
    if (!node.position) {
      node.position = {
        x: level * 300,
        y: index * 200
      }
    }
    nodes.push(node)
    node.children?.forEach((child, idx) => {
      traverse(child, level + 1, index + idx)
    })
  }
  traverse(knowledgeData.value)
  return nodes
})

const connections = computed(() => {
  const result: INodeConnection[] = []
  flattenedNodes.value.forEach(node => {
    node.children?.forEach(child => {
      result.push({
        source: node.id,
        target: child.id,
        type: 'relationship',
        strength: 1
      })
    })
    if (node.prerequisites?.length) {
      node.prerequisites.forEach(preReqId => {
        result.push({
          source: preReqId,
          target: node.id,
          type: 'prerequisite'
        })
      })
    }
  })
  return result
})

const transformStyle = computed(() => ({
  transform: `translate(${panOffset.value.x}px, ${panOffset.value.y}px) scale(${scale.value})`
}))

// 方法
const getNodeById = (id: string): KnowledgeNode => {
  return flattenedNodes.value.find(node => node.id === id)!
}

const getNodePosition = (id: string) => {
  const node = getNodeById(id)
  return node.position || { x: 0, y: 0 }
}

// 缩放控制
const handleZoomIn = () => {
  scale.value = Math.min(scale.value + 0.1, 2)
}

const handleZoomOut = () => {
  scale.value = Math.max(scale.value - 0.1, 0.5)
}

const handleReset = () => {
  scale.value = 1
  panOffset.value = { x: 0, y: 0 }
}

// 画布拖动
let isPanning = false
let startX = 0
let startY = 0
let startPanX = 0
let startPanY = 0

const startPan = (event: MouseEvent) => {
  if (event.target === containerRef.value) {
    isPanning = true
    startX = event.clientX
    startY = event.clientY
    startPanX = panOffset.value.x
    startPanY = panOffset.value.y

    document.addEventListener('mousemove', handlePan)
    document.addEventListener('mouseup', stopPan)
  }
}

const handlePan = (event: MouseEvent) => {
  if (!isPanning) return
  
  panOffset.value = {
    x: startPanX + (event.clientX - startX),
    y: startPanY + (event.clientY - startY)
  }
}

const stopPan = () => {
  isPanning = false
  document.removeEventListener('mousemove', handlePan)
  document.removeEventListener('mouseup', stopPan)
}

// 鼠标滚轮缩放
const handleWheel = (event: WheelEvent) => {
  event.preventDefault()
  const delta = event.deltaY > 0 ? -0.1 : 0.1
  const newScale = Math.max(0.5, Math.min(2, scale.value + delta))
  if (newScale !== scale.value) {
    scale.value = newScale
  }
}

// 节点操作
const handleNodeClick = (node: KnowledgeNode) => {
  selectedNode.value = node
  showNodeDetails.value = true
}

const handleNodePositionUpdate = (nodeId: string, position: { x: number; y: number }) => {
  const node = getNodeById(nodeId)
  if (node) {
    node.position = position
  }
}

const handleNodeUpdate = (updatedNode: KnowledgeNode) => {
  // 更新节点数据
  const node = getNodeById(updatedNode.id)
  Object.assign(node, updatedNode)
}

// 初始化
onMounted(() => {
  // 可以添加初始化逻辑，比如从后端加载数据
})
</script>

<style scoped>
.knowledge-map {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  height: 100vh;
  position: relative;
  overflow: hidden;
  /* background: #f5f7fa; */
}

.knowledge-map__controls {
  position: fixed;
  top: 200px;
  right: 200px;
  z-index: 100;
}

.knowledge-map__container {
  width: 100%;
  height: 100%;
  position: relative;
  cursor: grab;
}

.knowledge-map__container:active {
  cursor: grabbing;
}

.connections-layer,
.nodes-layer {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  transform-origin: center;
  transition: transform 0.1s ease;
}

.connections-layer {
  pointer-events: none;
}
</style>