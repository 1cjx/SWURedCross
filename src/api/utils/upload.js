import uploadService from '@/utils/uploadService'
export function uploadImg(file,path){
		  let formData = new FormData()
		  formData.append('multipartFile', file)
			formData.append('path',path)
		 return uploadService({
		   url: '/system/upload',
		   method: 'post',
			 data:formData,
		 })
}