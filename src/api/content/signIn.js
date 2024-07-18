import request from '@/utils/request'
import download from '@/utils/downloadService'
export function list(query) {
  return request({
    url: '/system/signInUser/list',
    method: 'get',
    params: query
  })
}

export function exportSignInUser(query){
  return download({
    url:'/system/signInUser/export',
    method:'get',
    params:query
  })
}
