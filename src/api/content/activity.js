import request from '@/utils/request'

// 获取活动列表
export function listActivity(query) {
  return request({
    url: '/system/activity/list',
    method: 'get',
    params: query
  })
}
// 查询活动详细
export function getActivityDetail(id) {
  return request({
    url: '/system/activity/' + id,
    method: 'get'
  })
}

// 查询班次详情
export function getActivityAssignmentDetail(id) {
  return request({
    url: '/system/activity/assignment/' + id,
    method: 'get'
  })
}
// 新增活动
export function addActivity(data) {
  return request({
    url: '/system/activity',
    method: 'post',
    data: data
  })
}

// 修改活动
export function updateActivity(data) {
  return request({
    url: '/system/activity',
    method: 'put',
    data: data
  })
}

// 删除分类
export function deleteActivity(id) {
  return request({
    url: '/system/activity/' + id,
    method: 'delete'
  })
}
//修改活动状态
export function changeActivityStatus(activityId, status) {
  const data = {
    activityId,
    status
  }
  return request({
    url: '/system/activity/changeActivityStatus',
    method: 'put',
    data: data
  })
}