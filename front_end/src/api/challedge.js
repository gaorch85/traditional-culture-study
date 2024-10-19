import request from "@/util/request"

// 获取今日所有信息
export function api_getDailyInfo(date) {
    return request({
      url: `/daily/${date}`,
      method: 'get'
    })
}

// 获取往期主题
export function api_listTopics() {
    return request({
      url: `/daily/list`,
      method: 'get'
    })
}

// 获取历史问题回答
export function api_listAnswers(id) {
    return request({
      url: `/daily/answer/${id}`,
      method: 'get'
    })
}

// 获取 AI 判题结果
export function api_getCurAnswer(data) {
    return request({
      url: `/daily/answer`,
      method: 'post',
      data: data
    })
}

// 获取尝试次数
export function api_getAnswerCount(id) {
    return request({
      url: `/daily/cnt/${id}`,
      method: 'get'
    })
}

// 获取本月尝试次数
export function api_getCurMonth() {
    return request({
      url: `/daily/month`,
      method: 'get'
    })
}

// 获取本月连续通关数
export function api_getCurContinue() {
    return request({
      url: `/daily/continue`,
      method: 'get'
    })
}
