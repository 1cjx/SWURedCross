import request from '@/utils/request'
// 查询所有职称
export function listAllTitle() {
  return request({
    url: '/system/select/listAllTitle',
    method: 'get'
  })
}
export function listAllCategory() {
  return request({
    url: '/system/select/listAllCategory',
    method: 'get',
  })
}
export function listAllCollege() {
  return request({
    url: '/system/select/listAllCollege',
    method: 'get',
  })
}
export function listAllDepartment() {
  return request({
    url: '/system/select/listAllDepartment',
    method: 'get',
  })
}
export function listAllLocation() {
  return request({
    url: '/system/select/listAllLocation',
    method: 'get',
  })
}
export function listAllPost(categoryId) {
  return request({
    url: '/system/select/listAllPost/'+categoryId,
    method: 'get',
  })
}
export function listAllTimeSlot() {
  return request({
    url: '/system/select/listAllTimeSlot',
    method: 'get',
  })
}
export function listAllAssignmentType() {
  return request({
    url: '/system/select/listAllActivityAssignmentType',
    method: 'get',
  })
}