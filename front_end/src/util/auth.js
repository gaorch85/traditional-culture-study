// 使用 sessionStorage 管理 token

export function getToken() 
{
    console.log("sessionStorage 获取token")
    return sessionStorage.getItem('token');
}
  
export function setToken(token) 
{
    console.log("sessionStorage 设置token")
    sessionStorage.setItem('token', token);
}
  
export function removeToken() 
{
    console.log("sessionStorage 删除token")
    sessionStorage.removeItem('token');
}
