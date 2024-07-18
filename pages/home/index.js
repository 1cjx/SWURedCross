// pages/home/index.js
import {
  request,
  getBaseUrl
} from '../../utils/request';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    baseUrl: 'http://redcross.heping.fun',
    swiperList: [{
      id: 1,
      swiperpic: 'image01.jpg',
    }, {
      id: 2,
      swiperpic: 'image02.jpg',
    }, {
      id: 3,
      swiperpic: 'image03.jpg',
    }],
    activity_list: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    
  },
  async getActivityList() {
    console.log("refrash")
    const result = await request({
      url: '/activity/getActivityList',
      data: {
        status: 1,
        locationId: "",
        categoryId: "",
        pageNum: 1,
        pageSize: 2
      },
      method: "GET"
    });
    this.setData({
      activity_list: result.data.rows === undefined ? [] : result.data.rows
    })
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
    this.getActivityList();
      if (typeof this.getTabBar === 'function' && this.getTabBar()) {
        this.getTabBar().setData({
            selected: 0
        })
    }
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
    this.onRefresh();
  },
  onRefresh: function () {
    //导航条加载动画
    wx.showNavigationBarLoading()
    //loading 提示框
    wx.showLoading({
      title: 'Loading...',
    })
    this.getActivityList();
    setTimeout(function () {
      wx.hideLoading();
      //停止下拉刷新
      wx.stopPullDownRefresh();
      wx.hideNavigationBarLoading();
    }, 2000)
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