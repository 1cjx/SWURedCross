import request from '@/utils/request'

// 获取已注册志愿者数量
export function getVolunteerNums() {
  return request({
    url: '/system/dashboard/getVolunteerNums',
    method: 'get',
  })
}
// 获取总志愿时长
export function getTotalVolunteerTimes() {
  return request({
    url: '/system/dashboard/getTotalVolunteerTimes',
    method: 'get',
  })
}
// 获取活动数
export function getActivityNums() {
  return request({
    url: '/system/dashboard/getActivityNums',
    method: 'get',
  })
}
// 根据传参对活动积极性进行排序
export function getRankingByActivity(query) {
  return request({
    url: '/system/dashboard/getRankingByActivity',
    method: 'get',
		params:query
  })
}

// 获取各类活动参与次数
export function getVariousActivitiesNum() {
  return request({ 
    url: '/system/dashboard/getVariousActivitiesNum',
    method: 'get',
  })
}
