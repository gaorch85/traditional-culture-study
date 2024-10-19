import { getToken, setToken, removeToken } from '@/util/auth'
import { api_getUsername, api_getAvatar } from '@/api/user'

const state = {
  token: getToken(),
  username: '',
  avatar: ''
}

const getters = {
  isLoggedIn: state => !!state.token
}

const mutations = {
  setToken(state, token) {
    console.log('调用了 setToken')
    state.token = token
  },
  setUserInfo(state, userInfo) {
    console.log('调用了 setUserInfo')
    state.username = userInfo.username
    state.avatar = userInfo.avatar
  },
  clearUserInfo(state) {
    console.log('调用了 clearUserInfo')
    state.token = null
    state.username = ''
    state.avatar = ''
  }
}

const actions = {
  async getUserInfo({ commit }) {
    console.log('要执行 getUserInfo！！')
    if (!getToken()) return
    console.log('此时有token！')
    console.log(getToken())
    commit('setToken', getToken())

    try {
      // TODO: 实现真实的获取用户信息 API 调用
      // const response = await api_getUsername();
      // const username = response.data.data;
      // response = await api_getAvatar();
      // const avatarBlob = new Blob([avatarResponse.data], { type: 'image/jpeg' });
      // const avatarUrl = URL.createObjectURL(avatarBlob);

      // const userInfo = {
      //   username: username,
      //   avatar: avatarUrl
      // }


      // 模拟获取用户信息
      const userInfo = {
        username: 'mockUser_' + Date.now(),
        avatar: require('@/assets/avatar.png')
      }

      
      commit('setUserInfo', userInfo)
    } catch (error) {
      console.error('获取用户信息失败', error)
    }
  }
}

export default {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
}