// pages/my/my_totalactivity/my_totalactivity.js
import {
    request
} from "../../../utils/request"
Page({

    /**
     * 页面的初始数据
     */
    data: {
        isshowActivityTime:[],
        activtiyList: [],
        pageNum: 0,
        totalPage: 0,
        pageSize: 4,
        isloading:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let p = this.data.pageNum;
        this.getUserVolunteerInfo(p);
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
        if(this.data.pageNum<this.data.totalPage && !this.data.isloading){
            console.log(this.data.pageNum)
            this.setData({
                isloading:true
            })
            this.getUserVolunteerInfo(this.data.pageNum)
        }
        if(this.data.pageNum==this.data.totalPage){
            wx.showToast({
                icon: "none",
                title: '暂无更多数据',
            })
        }
            
    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    },
    async getUserVolunteerInfo(p) {
        const page = p + 1;
        const result = await request({
            url: '/activity/userVolunteerInfo',
            data: {
                pageNum: page,
                pageSize: this.data.pageSize
            },
            method: "GET"
        });
        let rows = result.data.rows;
        let totalPage = Math.ceil(parseInt(result.data.total) / this.data.pageSize);
        let length=result.data.total;
        if(p==0){
            this.setData({
                isshowActivityTime:new Array(length).fill(false)
            })
        }
        if (rows!==undefined) {
            this.setData({
                activtiyList: this.data.activtiyList.concat(rows),
                pageNum: page,
                totalPage: totalPage,
                isloading:false
            })
        }

    },

    showActivityTimes:function(e){
        let index = e.currentTarget.dataset.index;
        this.setData({
            ['isshowActivityTime['+index+']']:!this.data.isshowActivityTime[index],
        })
    }
})