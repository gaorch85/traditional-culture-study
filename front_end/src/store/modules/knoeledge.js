// src/store/modules/knowledge.js
const state = {
    nodes: {
      'A': { completed: false },
      'B': { completed: false },
      'C': { completed: false },
      'D': { completed: false },
      'E': { completed: false },
      'F': { completed: false },
      'G': { completed: false },
      'H': { completed: false },
      'I': { completed: false },
      'J': { completed: false },
      'K': { completed: false },
      'L': { completed: false },
      'M': { completed: false },
      'N': { completed: false },
      'O': { completed: false },
      'P': { completed: false },
      'Q': { completed: false },
      'R': { completed: false }
    }
  }
  
  const mutations = {
    SET_NODE_COMPLETED(state, { nodeId, completed }) {
      if (state.nodes[nodeId]) {
        state.nodes[nodeId].completed = completed
      }
    }
  }
  
  const actions = {
    completeNode({ commit }, nodeId) {
      commit('SET_NODE_COMPLETED', { nodeId, completed: true })
    },
    resetNode({ commit }, nodeId) {
      commit('SET_NODE_COMPLETED', { nodeId, completed: false })
    }
  }
  
  const getters = {
    getNodeStatus: (state) => (nodeId) => {
      return state.nodes[nodeId]?.completed || false
    },
    getAllNodeStatuses: (state) => {
      return state.nodes
    }
  }
  
  export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
  }