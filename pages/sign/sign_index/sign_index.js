// pages/sign/sign_index/sign_index.js
import {
    request
} from '../../../utils/request';
Page({

    /**
     * 页面的初始数据
     */
    data: {
        on: true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
    },
    onScan(options) {
      var that = this
      if (that.data.on) {
        wx.scanCode({
            onlyFromCamera: true,
            success: (res) => {
                const url = res.result;
                this.Signin(url)
            },
            fail: (res) => {
                wx.showToast({
                    title: '扫描失败',
                    icon: 'error',
                    duration: 2000
                })
                wx.switchTab({
                  url: '/pages/home/index',
                })
            },
        })
      }
    },
    async Signin(url) {
        const result = await request({
            url,
            method: "GET"
        });
        let code = result.code;
        let msg = result.msg;
        if (code === 200) {
            wx.showToast({
                icon:"success",
                title: msg,
                duration: 2000
            })
        }else{
            wx.showToast({
                icon:"error",
                title: msg,
                duration: 2000
            })
        }
        wx.switchTab({
          url: '/pages/home/index',
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
      // if (typeof this.getTabBar === 'function' && this.getTabBar()) {
      //   this.getTabBar().setData({
      //       selected: 3
      //   })
    // }
    this.onScan()
    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {
      var that = this
      var on = that.data.on
      that.setData({
        on: !on
      })
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