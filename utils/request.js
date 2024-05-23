// 定义请求根路径baseUrl
// const baseUrl = "http://127.0.0.1:4523/m1/3238345-0-default";

const baseUrl = "http://124.70.136.242:7777";
// const baseUrl = "http://localhost:7777";
//同时并发请求的次数
let ajaxTimes = 0;
/**
 * 返回请求根路径baseUrl
 */
export const getBaseUrl =()=>{
  return baseUrl;
}
/**
 * wx login封装
 */
export const getWxLogin =()=>{
  return new Promise((resolve,reject)=>{
    wx.login({
      timeout:5000,
      success:(res)=>{
        resolve(res);
      },
      fail:(err)=>{
        reject(err);
      }
    })
  });
}
/**
 * wx getUserProfile封装
 */
export const getUserProfile =()=>{
  return new Promise((resolve,reject)=>{
    wx.getUserProfile({
      desc: '获取用户信息',
      success:(res)=>{
        resolve(res)
      },
      fail:(err)=>{
        reject(err);
      }
    })
  });
}
/**
 * 后端请求工具类
 * @param {*} params 
 */
export const request=(params)=>{
  let header = {}

  if(params.url!=='/weixin/login'){
    header["token"] = wx.getStorageSync('token')
  }
  var start = new Date().getTime();
  ajaxTimes++;
  wx.showLoading({
    title: '加载中...',
    mask:true
  })
  while(true){
    if(new Date().getTime()-start>1*100)break;
  }
  console.log( baseUrl + params.url)
  return new Promise((resolve,reject)=>{
    wx.request({
      ...params,
      header,
      url : baseUrl + params.url,
      success:(result)=>{
        if(result.data.code===401){
          wx.redirectTo({
            url: '/pages/login/index',
          })
        }
        else{
          resolve(result.data)
        }
      },
      fail:(err)=>{
        reject(err)
      },
      complete:()=>{
        ajaxTimes--;
        if(ajaxTimes==0){
          wx.hideLoading();//关闭加载
        }
      }
    })
  })
}