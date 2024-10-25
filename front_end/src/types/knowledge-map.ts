// src/types/knowledge-map.ts

// 节点类型枚举
  export enum NodeType {
    ROOT = 'root',           // 根节点：中国传统文化
    CATEGORY = 'category',   // 主分类：如历史、文学、哲学等
    SUBCATEGORY = 'subcategory', // 子分类：如诗词、散文等
    TOPIC = 'topic'         // 具体知识点：如某个具体作品、人物等
  }
  
  // 节点状态枚举
  export enum NodeStatus {
    LOCKED = 'locked',       // 锁定状态
    UNLOCKED = 'unlocked',   // 已解锁
    IN_PROGRESS = 'in_progress',
    COMPLETED = 'completed'  // 已完成学习
  }
  
  // 节点接口定义
  export interface KnowledgeNode {
    id: string;             // 唯一标识符
    name: string;           // 节点名称
    type: NodeType;         // 节点类型
    status: NodeStatus;     // 节点状态
    description?: string;   // 节点描述
    icon?: string;         // 节点图标
    children?: KnowledgeNode[]; // 子节点
    parentId?: string;     // 父节点ID
    difficulty?: number;   // 难度等级（1-5）
    prerequisites?: string[]; // 前置知识点ID
    progress?: number;     // 学习进度（0-100）
    position?: {          // 节点在图中的位置
      x: number;
      y: number;
    };
    unlockRequirements?: {
      prerequisiteProgress: number; // 需要完成的前置节点进度
      userLevel?: number; // 需要达到的用户等级
    };
  }

  
  export interface NodeConnection {
    source: string; // 源节点ID
    target: string; // 目标节点ID
    type: 'prerequisite' | 'relationship';
    strength?: number; // 关联强度，可用于连线样式
  }

  export interface MapState {
    zoom: number;
    pan: { x: number; y: number };
    selectedNodeId: string | null;
    hoveredNodeId: string | null;
  }

