// pages/sign/sign_activity_assignment/sign_activity_assignment.js
import {
  request,
} from '../../../utils/request';
Page({

    /**
     * 页面的初始数据
     */
    data: {
      signInList:[],
      showAddSignIn:false,
      activityAssignmentId:null
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
      let activityAssignmentId = options.assignmentId;
      this.setData({
        activityAssignmentId,
      })
    },
    async getSignInDetail(id){
      let result = await request({
        url: '/signIn/getSignDetail',
        data: {
            activityAssignmentId: id
        },
        method: "GET"
    });
    let length = result.data.length
    let showAddSignIn = length>0?result.data[length-1].type!=="3":true;
    this.setData({
      signInList:result.data,
      showAddSignIn,
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
      this.getSignInDetail(this.data.activityAssignmentId);
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