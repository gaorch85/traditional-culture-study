// 封装 axios 网络请求，在后续获取所有数据时，必须验证token

import axios from 'axios'
import { getToken } from '@/util/auth'
import eventBus from '@/util/eventBus'

// create an axios instance
const service = axios.create({

  // baseURL 根据环境变量改变，使得Mock只拦截Mock环境变量
  baseURL: process.env.VUE_APP_BASE_API, 
  timeout: 5000 
})

// 请求拦截器
service.interceptors.request.use(
  config => {

    // 在所有网络请求的请求头中加 token
    config.headers['X-Token'] = getToken()

    return config
  },
  error => {

    console.log(error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response) => {
      if(response.data.code == 401) {
        // 触发显示登录弹窗的事件
        eventBus.emit('show-login-dialog')
        // 可以选择是否要返回错误
        // return Promise.reject(new Error('Unauthorized'))
      }
      return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // 对于网络错误也触发显示登录弹窗的事件
      eventBus.emit('show-login-dialog')
    }
    return Promise.reject(error);
  }
);


export default service
