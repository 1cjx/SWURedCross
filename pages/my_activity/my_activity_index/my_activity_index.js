// pages//my_activity/my_activity.js
import {
    request,
} from '../../../utils/request.js';
Page({

    /**
     * 页面的初始数据
     */
    data: {
        activeIndex:1,
        tempshowVolunteer:[[]],
        showPostIndex: 0,
        showInfoIndex: 0,
        showContactIndex:0,
        userActivityList:[],
        pageNum:1,
        isloading:false,
        totalPage:0,
        pageSize:10,
        isLeader:false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
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
      this.setData({
        pageNum:0,
        userActivityList:[],
        isshowVolunteer:[],
        tempshowVolunteer:[[]],
      })
      this.data.activeIndex==1?this.getUserActivityList(0,false):this.getUserActivityList(0,true);
      if (typeof this.getTabBar === 'function' && this.getTabBar()) {
        this.getTabBar().setData({
            selected: 3
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
        
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        if(this.data.pageNum<this.data.totalPage && !this.data.isloading &&this.data.activeIndex==1){
            this.setData({
                isloading:true
            })
            this.getUserActivityList(this.data.pageNum,false)
        }
        if(this.data.pageNum<this.data.totalPage && !this.data.isloading &&this.data.activeIndex==0){
            this.setData({
                isloading:true
            })
            this.getUserActivityList(this.data.pageNum,true)
        }
        if(this.data.pageNum==this.data.totalPage&&this.data.activeIndex==1){
            wx.showToast({
                icon: "none",
                title: '暂无更多数据',
            })
        }
        if(this.data.pageNum==this.data.totalPage&&this.data.activeIndex==0){
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

    handleItemTap(e) {
        const {
            index
        } = e.currentTarget.dataset;
        if(index==0){
          this.setData({
            activeIndex: index,
            pageNum:0,
            userActivityList:[],
            isLeader:true
        })
            this.getUserActivityList(0,true)
        }else{
          this.setData({
            activeIndex: index,
            pageNum:0,
            userActivityList:[],
            isLeader:false
        })
            this.getUserActivityList(0,false)
        }
    },
    async getUserActivityList(p,type) {
        let page = p+1;
        const result = await request({
            url: '/activity/userActivityList',
            data: {
                pageNum:page,
                pageSize:this.data.pageSize,
                type
            },
            method: "GET"
        });
        let rows = result.data.rows
        let totalPage = Math.ceil(parseInt(result.data.total) / this.data.pageSize);
        if(result.data!=null){
            this.setData({
                userActivityList: this.data.userActivityList.concat(rows),
                totalPage:totalPage,
                pageNum:page,
                isloading:false
            })
        }
    },
})