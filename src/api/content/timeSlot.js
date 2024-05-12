import request from '@/utils/request'

export function listTime(query) {
  return request({
    url: '/system/timeSlot/list',
    method: 'get',
    params: query
  })
}
// 查询分类详细
export function getTime(id) {
  return request({
    url: '/system/timeSlot/' + id,
    method: 'get'
  })
}

// 新增分类
export function addTime(data) {
  return request({
    url: '/system/timeSlot',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateTime(data) {
  return request({
    url: '/system/timeSlot',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delTime(timeSlotIds) {
  console.log(timeSlotIds)
  const data = typeof(timeSlotIds)==='string'?[timeSlotIds]:[...timeSlotIds];
  return request({
    url: '/system/timeSlot',
    method: 'delete',
    data
  })  
}


