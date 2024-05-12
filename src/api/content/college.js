import request from '@/utils/request'

// 查询所有角色
export function listAllcollege(query) {
  return request({
    url: '/system/college/list',
    method: 'get',
		params: query
  })
}
