import { request,baseUrl,getWxLogin } from '../../utils/request';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    baseUrl:"",
    userName:"",
    password:"",
    isUserNameFocus:false,
    isPassWordFocus:false,
    isbinding:0
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    wx.hideHomeButton()
    // wx.navigateTo({
    //   url: '/pages/identify/index',
    // })
  },
  onlogin(){
    this.isLogin()
  },
async isLogin(){
      wx.showModal({
          title: '友情提示',
          content: '微信授权登陆',
          complete: async (res) => {
              if (res.confirm) {
                  await getWxLogin().then(async (res)=>{
                      let loginParam={
                          code:res.code,
                      }
                      await this.wxlogin(loginParam);
                  })
                  if(this.data.isbinding===0){
                      console.log("当前微信未绑定")
                      wx.redirectTo({
                        url: '/pages/bind/index',
                      })
                  }else{
                      wx.switchTab({
                        url: '/pages/home/index'
                      })
                  } 
              }
          }
        })
},
async wxlogin(loginParam){
    const result = await request({url:"/weixin/login",data:loginParam,method:"post"});
    console.log(result)
    if(result.code===200){
        const token = result.data;
        wx.setStorageSync('token', token);
    }
    //微信用户与角色绑定
    if(Object.keys(loginParam).length==1&&result.code===200){
        this.setData({
            isbinding:1
        })
    }
},
  
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    wx.hideHomeButton()
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})