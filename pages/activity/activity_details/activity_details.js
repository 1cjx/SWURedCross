// pages/activity/activity_details/activity_details.js
import {
  request,
  getBaseUrl
} from '../../../utils/request';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    ActivityId: "",
    ActivityLocations: undefined,
    showpost: [
      []
    ],
    ActivityDetail: {},
    assignmentId: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    let id = options.id;
    this.setData({
      ActivityId: id,
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {
    this.getActivityDetails()
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

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

  },
  async getActivityDetails() {
    var id = new Number(this.data.ActivityId)
    const result = await request({
      url: '/activity/getActivityDetail',
      data: {
        activityId: id
      },
      method: "GET"
    });
    if (result.code !== 200) {
      wx.showToast({
        title: result.msg,
        icon: 'error',
        complete() {
          setTimeout(() => {
            wx.navigateBack();
          }, 1000)
        }
      })
    } else {
      const length = result.data.scheduled.length
      this.setData({
        ActivityDetail: result.data,
        showpost: new Array(length).fill(0).map(() => new Array(2).fill(0)),
        ActivityLocations: result.data.locations
      })
    }
  },
  cancelBaoming(e) {
    let activityAssignmentId = e.target.dataset.activityassignmentid;
    let postAssignmentId = e.target.dataset.postassignmentid;
    this.cancelSchedule(activityAssignmentId, postAssignmentId);
  },
  async cancelSchedule(activityAssignmentId, postAssignmentId) {
    const activityId = this.data.ActivityId;
    const result = await request({
      url: '/activity/cancelSchedule',
      data: {
        activityId,
        postAssignmentId,
        activityAssignmentId,
      },
      method: "POST"
    });
    if (result.code != 200) {
      wx.showToast({
        icon: 'error',
        title: result.msg,
      })
    }
    if (result.code == 200) {
      this.onReady()
      wx.showToast({
        icon: 'success',
        title: "取消成功",
      })
    }
  },
  async addSchedule(activityAssignmentId, postAssignmentId) {
    const activityId = this.data.ActivityId;
    const result = await request({
      url: '/activity/addSchedule',
      data: {
        activityId,
        postAssignmentId,
        activityAssignmentId,
      },
      method: "POST"
    });
    if (result.code != 200) {
      wx.showToast({
        icon: 'error',
        title: result.msg,
      })
    }
    if (result.code == 200) {
      this.onReady()
      wx.showToast({
        icon: 'success',
        title: "报名成功",
      })
    }
  },
  onBaoming: function (e) {
    let activityAssignmentId = e.target.dataset.activityassignmentid;
    let postAssignmentId = e.target.dataset.postassignmentid;
    this.addSchedule(activityAssignmentId, postAssignmentId);
  },

  Timeselect: function (e) {
    let index = e.currentTarget.dataset.index;
    let parentIndex = e.currentTarget.dataset.parent;
    this.setData({
      ['showpost[' + parentIndex + '][1]']: index,
    })
  },
  guideLocation: function (e) {  
    const location = e.currentTarget.dataset.location;
    wx.openLocation({
      latitude: location.latitude,	//维度
      longitude: location.longitude, //经度
      name: location.name,	//目的地定位名称
      scale: 15,	//缩放比例
      address: location.name	//导航详细地址
    })
  },

  ShowPost: function (e) {
    let index = e.currentTarget.dataset.index;
    let boolshow = this.data.showpost[index][0];
    if (boolshow) {
      this.setData({
        ['showpost[' + index + '][0]']: !this.data.showpost[index][0],
        ['showpost[' + index + '][1]']: 0,
      })
    } else {
      this.setData({
        ['showpost[' + index + '][0]']: !this.data.showpost[index][0],
      })
    }
  }
})