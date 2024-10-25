// src/data/sample-knowledge-tree.ts
import { NodeType, NodeStatus, type KnowledgeNode } from '../types/knowledge-map'

export const sampleKnowledgeTree: KnowledgeNode = {
  id: "root",
  name: "中国传统文化",
  type: NodeType.ROOT,
  status: NodeStatus.UNLOCKED,
  description: "中国传统文化知识体系",
  children: [
    {
      id: "literature",
      name: "文学",
      type: NodeType.CATEGORY,
      status: NodeStatus.UNLOCKED,
      description: "中国古典文学",
      children: [
        {
          id: "poetry",
          name: "诗词",
          type: NodeType.SUBCATEGORY,
          status: NodeStatus.UNLOCKED,
          description: "古典诗词",
          children: [
            {
              id: "tangpoetry",
              name: "唐诗",
              type: NodeType.TOPIC,
              status: NodeStatus.LOCKED,
              description: "唐代诗歌",
              difficulty: 2,
              progress: 10
            }
          ]
        }
      ]
    },
    {
      id: "literature",
      name: "文学",
      type: NodeType.CATEGORY,
      status: NodeStatus.UNLOCKED,
      description: "中国古典文学",
      children: [
        {
          id: "poetry",
          name: "诗词",
          type: NodeType.SUBCATEGORY,
          status: NodeStatus.UNLOCKED,
          description: "古典诗词",
          children: [
            {
              id: "tangpoetry",
              name: "唐诗",
              type: NodeType.TOPIC,
              status: NodeStatus.LOCKED,
              description: "唐代诗歌",
              difficulty: 2,
              progress: 0
            }
          ]
        }
      ]
    },
    {
      id: "literature",
      name: "文学",
      type: NodeType.CATEGORY,
      status: NodeStatus.UNLOCKED,
      description: "中国古典文学",
      children: [
        {
          id: "poetry",
          name: "诗词",
          type: NodeType.SUBCATEGORY,
          status: NodeStatus.UNLOCKED,
          description: "古典诗词",
          children: [
            {
              id: "tangpoetry",
              name: "唐诗",
              type: NodeType.TOPIC,
              status: NodeStatus.LOCKED,
              description: "唐代诗歌",
              difficulty: 2,
              progress: 0
            }
          ]
        }
      ]
    },
    {
      id: "philosophy",
      name: "哲学",
      type: NodeType.CATEGORY,
      status: NodeStatus.LOCKED,
      description: "中国传统哲学",
      children: [
        {
          id: "confucianism",
          name: "儒家思想",
          type: NodeType.SUBCATEGORY,
          status: NodeStatus.LOCKED,
          description: "以仁为核心的儒家思想体系",
          difficulty: 3,
          progress: 0
        }
      ]
    }
  ]
}