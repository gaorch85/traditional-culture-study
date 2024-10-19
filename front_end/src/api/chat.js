import request from "@/util/request"

// list chatHistory
export function api_listChatHistory() {
    return request({
      url: `/message/list`,
      method: 'get'
    })
}


// get response
export function api_getResponse(data) {
    return request({
      url: `/message/ask`,
      method: 'post',
      data
    })
  }