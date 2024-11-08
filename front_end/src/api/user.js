import request from "@/util/request"

// 登录函数
export function api_login(data) {
  return request({
    url: `/login`,
    method: 'post',
    data
  })
}

// 注册函数
export function api_signup(data) {
  return request({
    url: `/signup`,
    method: 'post',
    data
  })
}

// 注销账号
export function api_deactivateAccount() {
    return request({
      url: `/user`,
      method: 'delete'
    })
}

// 获取用户名
export function api_getUsername() {
    return request({
      url: `/username`,
      method: 'get'
    })
}

// 更改用户名
export function api_updateUsername(username) {
    return request({
      url: `/username/${username}`,
      method: 'put',
    })
}

// 修改密码
export function api_changePassword(data) {
  return request({
    url: `/changePassword`,
    method: 'put',
    data
  })
}

// 获取积分
export function api_getPoint() {
  return request({
    url: `/points`,
    method: 'get',
  })
}

// 获取积分超越百分比
export function api_getPointPercent() {
  return request({
    url: `/percent`,
    method: 'get',
  })
}

// 增加积分
export function api_increasePoint(num) {
  return request({
    url: `/points/${num}`,
    method: 'post',
  })
}
 
// 上传头像
export function api_uploadAvatar(avatar) {
  const formData = new FormData();
  formData.append('file', avatar);
  return request({
    url: `/pic`,
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取头像
export function api_getAvatar() {
  return request({
    url: `/pic`,
    method: 'get',
    responseType: 'blob',
  })
}


// 获取用户 id
export function api_getUserId() {
  return request({
    url: `/getUserId`,
    method: 'get'
  })
}