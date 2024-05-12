import {
    request
} from '../../../utils/request';
Page({
    /**
     * 页面的初始数据
     */
    data: {
        activityList: [],
        moreactivityList: "",
        activeIndex: 1,
        baseUrl: '',
        locationId: 0,
        categoryId: 0,
        pageNum: 0,
        pageSize: 6,
        selectBoxDis: false,
        selectBoxTim: false,
        chooseItemDis: '全部分类',
        chooseItemTim: '全部地点',
        locationList: [],
        categoryList: [],
        isloading:false,
        totalPage:0
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        this.getCategoryList()
        this.getLocationList()
    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    //获取活动列表
    async getActivityList() {
        const {
            activityList,
            pageNum,
            activeIndex,
            locationId,
            categoryId,
            pageSize
        } = this.data;
        const newPageNum = pageNum + 1;
        const result = await request({
            url: '/activity/getActivityList',
            data: {
                status: activeIndex,
                locationId: locationId == 0 ? "" : locationId,
                categoryId: categoryId == 0 ? "" : categoryId,
                pageNum: newPageNum,
                pageSize: pageSize
            },
            method: "GET"
        });
        if(result.data.rows!=null){
            result.data.rows.forEach(item => {
                item.beginDate = this.formatDate(item.beginDate);
                item.endDate = this.formatDate(item.endDate);
            });
        }
        const mergedData = [...activityList];
        mergedData.push(...result.data.rows);
        let totalPage = Math.ceil(parseInt(result.data.total) / this.data.pageSize);
        this.setData({
            activityList: mergedData,
            pageNum: newPageNum,
            totalPage:totalPage,
            isloading:false
        });

    },
    formatDate(d) {
        return d.slice(0, 11)
    },
    //获取地点列表
    async getLocationList() {
        const result = await request({
            url: '/location/getLocationList',
            method: "GET"
        });
        this.setData({
            locationList: result.data
        })
    },
    //获取活动分类列表
    async getCategoryList() {
        const result = await request({
            url: '/category/getCategoryList',
            method: "GET"
        });
        this.setData({
            categoryList: result.data
        })
    },
    onReady() {},
    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
      this.setData({
        activityList: [],
        pageNum:0
      })
      this.getActivityList()
      if (typeof this.getTabBar === 'function' && this.getTabBar()) {
        this.getTabBar().setData({
            selected: 1
        })
    }
    },
    //tap点击事件
    handleItemTap(e) {
        const {
            index
        } = e.currentTarget.dataset;
        this.setData({
            activeIndex: index,
            selectBoxDis: false,
            selectBoxTim: false,
            chooseItemDis: '全部分类',
            chooseItemTim: '全部地点',
            locationId: 0,
            categoryId: 0,
            pageNum:0,
            activityList:[]
        })
        this.getActivityList()
    },
    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {},
    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {},
    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {
        this.onRefresh()
    },
    onRefresh() {
        this.setData({
            activityList: [],
            moreactivityList: "",
            activeIndex: 1,
            baseUrl: '',
            locationId: 0,
            categoryId: 0,
            pageNum: 0,
            pageSize: 7,
            selectBoxDis: false,
            selectBoxTim: false,
            chooseItemDis: '全部分类',
            chooseItemTim: '全部地点',
        })
        this.getActivityList();
        setTimeout(function () {
           wx.stopPullDownRefresh()
        }, 1500)
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        if(this.data.pageNum<this.data.totalPage && !this.data.isloading){
            this.setData({
                isloading:true
            })
            this.getActivityList(this.data.pageNum)
        }
        if(this.data.pageNum==this.data.totalPage){
            console.log(this.data.pageNum,this.data.totalPage);
            wx.showToast({
                icon: "none",
                title: '暂无更多数据',
            })
        }
    },
    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {},
    //选择下拉选项
    chooseSelectItemDis: function (e) {
        console.log("下拉")
        let name = e.currentTarget.dataset.name;
        let index = e.currentTarget.id;
        console.log(index)
        this.setData({
            selectBoxDis: false,
            chooseItemDis: name,
            categoryId: index,
            activityList:[],
            pageNum:0
        })
        this.getActivityList()
    },
    chooseSelectItemTim: function (e) {
        let name = e.currentTarget.dataset.name;
        let id = e.currentTarget.id;
        this.setData({
            selectBoxTim: false,
            chooseItemTim: name,
            locationId: id,
            activityList:[],
            pageNum:0
        })
        this.getActivityList()
    },
    //显示隐藏下拉列表
    showSelectBoxDis: function () {
        this.setData({
            selectBoxDis: !this.data.selectBoxDis,
            selectBoxTim: false
        })
    },
    showSelectBoxTim: function () {
        this.setData({
            selectBoxTim: !this.data.selectBoxTim,
            selectBoxDis: false
        })
    },
    reset(){
      this.setData({
        selectBoxDis: false,
        chooseItemDis: '全部分类',
        selectBoxTim: false,
        chooseItemTim: '全部地点',
        activityList:[],
        locationId: 0,
        categoryId: 0,
        pageNum:0,
        totalPage:0
    })
    this.getActivityList()
    },
    DeleteSelectItemDis: function () {
        this.setData({
            selectBoxDis: false,
            chooseItemDis: '全部分类',
            activityList:[],
            categoryId: 0,
            pageNum:0,
            totalPage:0
        })
        this.getActivityList()
    },
    DeleteSelectItemTim: function () {
        this.setData({
            selectBoxTim: false,
            chooseItemTim: '全部地点',
            activityList:[],
            locationId: 0,
            pageNum:0,
            totalPage:0
        })
        this.getActivityList()
    },
})