Page({

  /**
   * 页面的初始数据
   */
  data: {
    activity:[],
    activityList:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.request({
      url: 'http://127.0.0.1:4523/m1/3238345-0-default/activity/getActivityDetail',
      method:"GET",
      success:(result)=>{
        console.log(result)
        this.setData({
          activity:result.data
        })
      }
    })
    wx.request({
      url: 'http://127.0.0.1:4523/m1/3238345-0-default/activity/getActivityList',
      method:"GET",
      success:(result)=>{
        console.log(result)
        this.setData({
          activityList:result.data.data
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {


    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    
  }
})