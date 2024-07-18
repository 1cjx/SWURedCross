// pages/sign/sign_release/sign_release.js
import {
    request
} from '../../../utils/request';
Page({

    /**
     * 页面的初始数据
     */
    data: {
        pageNum:1,
        pageSize:10,
        SignList:[],
        SignDetailList:[],
        isShowSignDetail:[[]],
        dialogShow: false,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
      console.log(options)
    },
    
    openDialog: function () {
      console.log(this.data.dialogShow)
      this.setData({
        dialogShow: true
      })
    },
    closeDialog(e) {
      console.log(11)
      this.setData({
        dialogShow: false
      })
    },
    async getSignInList() {
        const result = await request({
            url: '/signIn/getSignInList',
            data: {
                pageNum: 1,
                pageSize: this.data.pageSize
            },
            method: "GET"
        });
        
        const length = result.data.rows.length
        this.setData({
            SignList: result.data.rows,
            isShowSignDetail:new Array(length).fill(0).map(() => new Array(2).fill(0)),
        });

        for(let i=0;i<length;i++){
            let id=this.data.SignList[i].activityAssignmentId,
            result = await request({
                url: '/signIn/getSignDetail',
                data: {
                    activityAssignmentId: id
                },
                method: "GET"
            });
            console.log(result.data)
            this.setData({
                ['isShowSignDetail['+i+'][1]']: result.data,
            });
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
      this.getSignInList()
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

    /**
     * 点击显示对应活动签到类型
     */
    showSignDetail:function(e){
        let Listindex=e.currentTarget.dataset.index
        this.setData({
            ['isShowSignDetail['+Listindex+'][0]']:!this.data.isShowSignDetail[Listindex][0],
        })
    },


    formatDate(d) {
        return d.slice(11,16)
    },
})