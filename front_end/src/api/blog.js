import request from "@/util/request"

export function api_getAll(sortIndex) 
{
    return request({
      url: `/blog/list/${sortIndex}`,
      method: 'get'
    })
}

export function api_getHot() 
{
    return request({
      url: `/blog/hotlist`,
      method: 'get'
    })
}

export function api_insert(data) 
{
    return request({
      url: '/blog',
      method: 'post',
      data: data
    })
}

export function api_delete(postId) 
{
    return request({
      url: `/blog/${postId}`,
      method: 'delete'
    })
}

export function api_update_status(postId) 
{
    return request({
      url: `/blog/update_status/${postId}`,
      method: 'put'
    })
}

export function api_update(postId, data) 
{
    return request({
      url: `/blog/${postId}`,
      method: 'put',
      data: data
    })
}

export function api_getPostById(postId) 
{
    return request({
      url: `/blog/${postId}`,
      method: 'get'
    })
}

export function api_insert_favorite(postId) 
{
    return request({
      url: `/blog/favorite/${postId}`,
      method: 'post'
    })
}

export function api_delete_favorite(favoriteId) 
{
  return request({
    url: `/blog/favorite/${favoriteId}`,
    method: 'delete'
  })
}

export function api_insert_like(postId) 
{
    return request({
      url: `/blog/like/${postId}`,
      method: 'post'
    })
}

export function api_delete_like(likeId) 
{
    return request({
      url: `/blog/like/${likeId}`,
      method: 'delete'
    })
}

export function api_getCommentsById(postId) 
{
    return request({
      url: `/blog/comment/${postId}`,
      method: 'get'
    })
}

export function api_add_comment(postId, comment)
{
  return request({
    url: `/blog/comment/${postId}`,
    method: 'post',
    data: comment
  })
}

export function api_delete_comment(id)
{
  return request({
    url: `/blog/comment/${id}`,
    method: 'delete'
  })
}

// import axios from 'axios'

// const token = "eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIxMjM0NTYiLCJpYXQiOjE3MzA5NTUyNTQsImV4cCI6MTczMDk5ODQ1NH0.l94WcbFJuxoSux9FqC3LpiOA_IEJ2ywvTCt7CeiKZ-iWTdHczDD--lzuVPVbPtn9iK6m6PrPitZnSxLVdgHFsA"

// export function api_getAll(sortIndex) 
// {
       
//    return axios.get(`http://localhost:8899/blog/list/${sortIndex}`,
//     {
//         headers: {
//             'X-token': token
//         }
//     }
//    )
// }


// export function api_insert(data) 
// {
//     return axios.post('http://localhost:8899/blog', data, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_delete(postId) 
// {
//     return axios.delete(`http://localhost:8899/blog/${postId}`, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_update_status(postId) 
// {
//     return axios.put(`http://localhost:8899/blog/update_status/${postId}`, {}, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_update(postId, data) 
// {
//     return axios.put(`http://localhost:8899/blog/${postId}`, data, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_getPostById(postId) 
// {
//     return axios.get(`http://localhost:8899/blog/${postId}`, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_insert_favorite(postId) 
// {
//     return axios.post(`http://localhost:8899/blog/favorite/${postId}`, {}, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_delete_favorite(favoriteId) 
// {
//     return axios.delete(`http://localhost:8899/blog/favorite/${favoriteId}`, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_insert_like(postId) 
// {
//     return axios.post(`http://localhost:8899/blog/like/${postId}`, {}, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_delete_like(likeId) 
// {
//     return axios.delete(`http://localhost:8899/blog/like/${likeId}`, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_getCommentsById(postId) 
// {
//     return axios.get(`http://localhost:8899/blog/comment/${postId}`, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_add_comment(postId, comment)
// {
//     return axios.post(`http://localhost:8899/blog/comment/${postId}`, comment, {
//         headers: {
//             'X-token': token
//         }
//     });
// }

// export function api_delete_comment(id)
// {
//     return axios.delete(`http://localhost:8899/blog/comment/${id}`, {
//         headers: {
//             'X-token': token
//         }
//     });
// }
