// pages/identify/index.js
import {
    request,
    baseUrl,
    getWxLogin
} from '../../utils/request';
Page({

    /**
     * 页面的初始数据
     */
    data: {

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
    async wxlogin(loginParam) {
        const result = await request({
            url: "/weixin/login",
            data: loginParam,
            method: "post"
        });
        if (result.code === 200) {
            const token = result.data;
            wx.setStorageSync('token', token);
        }
        //微信用户与角色绑定
        if (Object.keys(loginParam).length == 1 && result.code === 200) {
            this.setData({
                isbinding: 1
            })
        }
        return result
    },
    async onBind(){
        const{
            userId,
            userName,
        } = this.data;
        await getWxLogin().then(async (res)=>{
            let code = res.code;
            let loginParam={
            code:res.code,
            id:userId,
            name:userName,
            // collegeName:college
            }
            res = await this.wxlogin(loginParam);
            if(res.code == 200){
                wx.switchTab({
                  url: '/pages/home/index',
                })
            }
            if(res.code == 501){
                wx.navigateTo({
                  url: '/pages/register/index?code='+code,
                })
            }
        }) 
    //     "code":501,
	// "msg":"用户不存在"
    }
    ,
    getId(e) {
        var value = e.detail.value;
        if (!value.trim()) {
            this.setData({
                isUserIdFocus: false,
            })
            return
        }
        this.setData({
            isUserIdFocus: true,
        })
    },
    getUserName(e) {
        var value = e.detail.value;
        if (!value.trim()) {
            this.setData({
                isUserNameFocus: false,
            })
            return
        }
        this.setData({
            isUserNameFocus: true,
        })
    },
    getCollege(e) {
        var value = e.detail.value;
        if (!value.trim()) {
            this.setData({
                isCollegeFocus: false,
            })
            return
        }
        this.setData({
            isCollegeFocus: true,
        })
    },
    handleItemTap(e) {
        const {
            index
        } = e.currentTarget.dataset;
        console.log(index);
        if (index == "0") {
            this.setData({
                userId: "",
                isUserIdFocus: false,
            })
        }
        if (index == "1") {
            this.setData({
                userName: "",
                isUserNameFocus: false,
            })
        }
    },
    onIdent(){
        wx.navigateTo({
          url: '/pages/register/index',
        })
    }
})