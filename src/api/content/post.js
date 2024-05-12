import request from '@/utils/request'

export function listPost(query) {
  return request({
    url: '/system/post/list',
    method: 'get',
    params: query
  })
}
// 查询分类详细
export function getPost(id) {
  return request({
    url: '/system/post/' + id,
    method: 'get'
  })
}

// 新增分类
export function addPost(data) {
  return request({
    url: '/system/post',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updatePost(data) {
  return request({
    url: '/system/post',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delPost(postIds) {
  const data = typeof(postIds)==='string'?[postIds]:[...postIds];
  return request({
    url: '/system/post',
    method: 'delete',
    data
  })  
}

export function changeStatus(data){
	return request({
		url:	'/system/post/changeStatus',
		method:	'put',
		data:data
	})
}
