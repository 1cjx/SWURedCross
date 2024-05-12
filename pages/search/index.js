// pages/search/index.js
import {
    request,
    getBaseUrl
} from '../../utils/request';
Page({

    /**
     * 页面的初始数据
     */
    data: {
        inputValue: "",
        actilist:[],
        isSearchOn:false,
        isSearchOver:false,
        Err_Empty:"请输入搜索内容"
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
    onInput: function (e) {
        console.log(e);
        this.setData({
            inputValue: e.detail.value,
            isSearchOver:false
        })
        console.log(this.data.inputValue);
        let judge=this.data.isSearchOver;
        console.log('judegis='+judge);
        if(this.data.inputValue!=""&&!judge){
            clearTimeout(this.TimeoutId);
            this.TimeoutId=setTimeout(()=>{
                this.selectActivityList();
            },800)
        }else{
            console.log(111);
            this.setData({
                actilist:[],
                isSearchOn:false
            })
        }
    },
    onTapCancel() {
        this.setData({
            inputValue:'',
            actilist:[],
            isSearchOn:false
        })
    },
    async selectActivityList(){
        const input = this.data.inputValue;
        console.log(input);
        if(input!=""){
            const result =await request({
                url: "/activity/selectActivity",
                data: {
                    activityName: input,
                    pageSize:10,
                    pageNum:1
                },
                method:"GET"
            });
            this.setData({
                actilist:result.data.rows,
                isSearchOn:true,
            })
        }
    },
    
    onTapSearch() {
        console.log(this.data.inputValue);
        this.selectActivityResult();
        this.setData({
            isSearchOn:false
        })
    },
    onResultCancel() {
        this.setData({
            inputValue:'',
            actilist:[],
            isSearchOver:false
        })
    },
    ListSearch:function(e) {
        let searchvalue=e.currentTarget.dataset.value
        this.setData({
            inputValue:searchvalue,
            isSearchOn:false
        })
        this.selectActivityResult();
    },
    async selectActivityResult(){
        clearTimeout(this.TimeoutId);
        let err_msg=this.data.Err_Empty;
        const input = this.data.inputValue;
        console.log(input);
        if(input!=""){
            const result =await request({
                url: "/activity/selectActivity",
                data: {
                    activityName: input,
                    pageSize:10,
                    pageNum:1
                },
                method:"GET"
            });
            if(result.data.total!="0"){
                this.setData({
                    actilist:result.data.rows,
                    isSearchOver:true
                })
            }else{
                this.setData({
                    isSearchOver:true
                })
            }

        }else{
            console.log("111")
            wx.showToast({
                icon:"error",
                title: err_msg
            })
        }
    },
})