import request from '@/utils/request'
import { praseStrEmpty } from '@/utils/sg'
import uploadService from '@/utils/uploadService'
import downloadService from '@/utils/downloadService'
// 查询用户列表
export function listUser(query) {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query
  })
}
// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/system/user/' + praseStrEmpty(userId),
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/system/user',
    method: 'post',
    data: data
  })
}
// 删除用户
export function delUser(userIds) {
  const data = typeof(userIds)==='string'?[userIds]:[...userIds];
  return request({
    url: '/system/user',
    method: 'delete',
    data
  })  
}
// 用户密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/system/user/resetPwd',
    method: 'put',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/system/user',
    method: 'put',
    data: data
  })
}
// 用户状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/system/user/changeStatus',
    method: 'put',
    data: data
  })
}
export function templateDownLoad(){
	return downloadService({
	  url: '/system/user/templateDownLoad',
	  method: 'get',
	})
}

export function excelAddUsers(file){
		  let formData = new FormData()
		  formData.append('multipartFile', file)
		 return uploadService({
		   url: '/system/user/excelAddUsers',
		   method: 'post',
			 data:formData,
		 })
}

export function userImportRecordList(recordQueryParams) {
  return request({
    url: '/system/user/userImportRecordList',
    method: 'get',
    params:recordQueryParams
  })
}
export function failUploadDownload(recordId){
	return downloadService({
	  url: '/system/user/failUploadDownload/'+recordId,
	  method: 'get',
	})
}