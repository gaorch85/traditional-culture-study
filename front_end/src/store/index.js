import { createStore } from 'vuex'
import user from './modules/user'
import knowledge from './modules/knoeledge'

export default createStore({
  modules: {
    user,
    knowledge
  }
})