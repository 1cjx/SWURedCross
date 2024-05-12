import request from '@/utils/request'

export function listLocation(query) {
  return request({
    url: '/system/location/list',
    method: 'get',
    params: query
  })
}
// 查询地点详细
export function getLocation(id) {
  return request({
    url: '/system/location/' + id,
    method: 'get'
  })
}

// 新增地点
export function addLocation(data) {
  return request({
    url: '/system/location',
    method: 'post',
    data: data
  })
}


// 修改地点
export function updateLocation(data) {
  return request({
    url: '/system/location',
    method: 'put',
    data: data
  })
}

// 删除地点
export function delLocation(locationIds) {
  const data = typeof(locationIds)==='string'?[locationIds]:[...locationIds];
  return request({
    url: '/system/location',
    method: 'delete',
    data
  })  
}


//修改状态
export function changeLocationStatus(id,status) {
	const data = {
	  id,
	  status
	}
  return request({
    url: '/system/location/changeStatus',
    method: 'put',
    data: data
  })
}