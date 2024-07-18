// pages/my/my_index/my_index.js
import {
    request,
    getBaseUrl,
    getWxLogin,
    getUserProfile
} from '../../../utils/request';
Page({
    /**
     * 页面的初始数据
     */
    data: {
        userInfo: {},
        isbinding: 0,
        totalActivityNums: "",
        totalVolunteerTime: ""
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
      if (typeof this.getTabBar === 'function' && this.getTabBar()) {
        this.getTabBar().setData({
            selected: 4
        })
    }
    },
    goMyActivity(){
      wx.switchTab({
        url: '/pages/my_activity/my_activity_index/my_activity_index',
      })
    },
    goMySignIn(){
      wx.navigateTo({
        url: '/pages/sign/sign_set/sign_set',
      })
    },
    goAboutUs(){
      wx.navigateTo({
        url: '/pages/my/my_about/my_about.wxml',
      })
    },
    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onLoad(options) {
      this.getUserInfo()
    },

    async getUserInfo() {
        await request({
            url: "/weixin/user/getUserInfo",
            method: "get"
        }).then((res) => {
            this.setData({
                userInfo: res.data
            })
        })
        this.getUserActivityDetail()
    },
    getUserActivityDetail() {
        request({
            url: "/weixin/user/getUserActivityNumsAndVolunteerTimes",
            method: "get"
        }).then((res) => {
            this.setData({
                totalActivityNums: res.data.totalActivityNums,
                totalVolunteerTime: res.data.totalVolunteerTime
            })
        })
    },
    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {
      this.onRefresh();
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

    },
    quit() {
        wx.showModal({
            title: '提示',
            content: '确定要登出吗',
            success(res) {
                if (res.confirm) {
                    wx.clearStorageSync()
                    wx.reLaunch({
                        url: '/pages/login/index',
                    })
                } else if (res.cancel) {
                    console.log('用户点击取消')
                }
            }
        })
    },
    kefu(){
    },
    onRefresh:function(){
      //导航条加载动画
      wx.showNavigationBarLoading()
      //loading 提示框
      wx.showLoading({
        title: 'Loading...',
      })
      this.getUserInfo();
      setTimeout(function () {
        wx.hideLoading();
        wx.hideNavigationBarLoading();
        //停止下拉刷新
        wx.stopPullDownRefresh();
      }, 1500)
    },
  
})