// pages/my/my_message/my_message.js
import {
    request,
    getBaseUrl
} from '../../../utils/request'

Page({

    /**
     * 页面的初始数据
     */
    data: {
        chosed: false,
        userInfo: {},
        avatar: 0,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
      this.getUserInfo()
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

    /**
     * 输入框获取用户输入
     */

    /**
     * radio单选框获取性别数据
     */
    async getUserInfo(){
      await request({url:"/weixin/user/getUserInfo",method:"get"}).then((res)=>{
          this.setData({
            userInfo:res.data,
            ['imgList[0].tempFilePath']: res.data.avatar
        })
      })
 },
    handleChange(e) {
        this.setData({
            ['userInfo.sex']: e.detail.value == 'male' ? '1' : '2'
        })
    },

    Inputphone: function (e) {
        this.setData({
            ['userInfo.phoneNumber']: e.detail.value
        })
    },

    Inputqq: function (e) {
        this.setData({
            ['userInfo.qq']: e.detail.value
        })
    },
    Inputemail: function (e) {
        this.setData({
            ['userInfo.email']: e.detail.value
        })
    },
    async Save() {
        if (this.submit()) {
            let avatar = await this.uploadImg();
            console.log(avatar)
            const {
                sex,
                phoneNumber,
                email,
                qq
            } = this.data.userInfo
            const result = await request({
                url: '/weixin/user/updateUserInfo',
                data: {
                    avatar: this.data.chosed ? avatar : this.data.userInfo.avatar,
                    sex: sex,
                    phoneNumber: phoneNumber,
                    qq: qq,
                    email: email
                },
                method: "PUT"
            });
            if(result.code===200){
              wx.navigateBack()
            }
            else{
              wx.showToast({
                title: result.msg,
              })
            }
        }
    },
    async uploadImg() {
        if (this.data.chosed) {
            let token = wx.getStorageSync('token')
            return new Promise((resolve, reject) => {
                wx.uploadFile({
                    url: getBaseUrl() + '/upload',
                    filePath: this.data.avatar,
                    name: "img",
                    header: {
                        "token": token
                    },
                    formData: {
                        "path": "avatar",
                    },
                    success: (result) => {
                        console.log(result)
                        let avatar = JSON.parse(result.data).data
                        console.log(avatar)
                        resolve(avatar);
                    },
                    fail: function (err) {
                        wx.showToast({
                            title: "上传失败",
                            icon: "none",
                            duration: 2000
                        })
                        reject(err);
                    },
                })
            })
        }
    },
    submit: function () {
        var phonenum = this.data.userInfo.phoneNumber;
        if(phonenum.length===0){
          wx.showToast({
            icon: 'error',
            title: '手机号不为空',
          })
          return false;
        }
        else if (phonenum.length != 11) {
            wx.showToast({
                icon: 'error',
                title: '手机号不正确',
            })
            return false;
        }
        var email = this.data.userInfo.email
        if(email.length !=0){
            if(!/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(email)) {
                wx.showToast({
                    icon:"error",
                  title: '邮箱不正确',
                })
                return false
            }
        }else{
            wx.showToast({
                icon:"error",
              title: '请输入邮箱',
            })
            return false;
        }
        var qq = this.data.userInfo.qq;
        if(qq.length===0){
          wx.showToast({
            icon: 'error',
            title: 'qq不为空',
          })
          return false;
        }
        return true
    },
    getUserAvatar(e) {
        const {
            avatarUrl
        } = e.detail
        this.setData({
            avatar:avatarUrl,
            chosed:true
        })
        wx.showModal({
            title:avatarUrl,
            content:avatar,
            cancelColor: 'cancelColor',
        })
    }
})