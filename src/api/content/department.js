import request from '@/utils/request'

export function listDepartment(query) {
  return request({
    url: '/system/department/list',
    method: 'get',
    params: query
  })
}
// 查询分类详细
export function getDepartment(id) {
  return request({
    url: '/system/department/' + id,
    method: 'get'
  })
}

// 新增分类
export function addDepartment(data) {
  return request({
    url: '/system/department',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateDepartment(data) {
  return request({
    url: '/system/department',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delDepartment(departmentIds) {
  const data = typeof(departmentIds)==='string'?[departmentIds]:[...departmentIds];
  return request({
    url: '/system/department',
    method: 'delete',
    data
  })  
}


