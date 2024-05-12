// pages/register/index.js
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
        sex:''
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        if (options.code != "") {
            console.log(options.code)
            this.setData({
                code: options.code
            })
        };
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
    getPhone(e) {
        var value = e.detail.value;
        if (!value.trim()) {
            this.setData({
                isPhoneFocus: false,
            })
            return
        }
        this.setData({
            isPhoneFocus: true,
        })
    },
    getQ(e) {
        var value = e.detail.value;
        if (!value.trim()) {
            this.setData({
                isQFocus: false,
            })
            return
        }
        this.setData({
            isQFocus: true,
        })
    },
    getEmail(e) {
        var value = e.detail.value;
        if (!value.trim()) {
            this.setData({
                isEmailFocus: false,
            })
            return
        }
        this.setData({
            isEmailFocus: true,
        })
    },
    handleChange(e) {
        this.setData({
            sex: e.detail.value == 'male' ? '1' : '2'
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
        if (index == "6") {
            console.log("1111");
            this.setData({
                userName: "",
                isUserNameFocus: false,
            })
        }
        // if (index == "2") {
        //     this.setData({
        //         sex: "",
        //         isSexFocus: false,
        //     })
        // }
        if (index == "3") {
            this.setData({
                phone: "",
                isPhoneFocus: false,
            })
        }
        if (index == "4") {
            this.setData({
                qq: "",
                isQFocus: false,
            })
        }
        if (index == "5") {
            this.setData({
                email: "",
                isEmailFocus: false,
            })
        }
    },
    async Register() {
        const {
            userId,
            userName,
            sex,
            phone,
            qq,
            email,
            
        } = this.data;
        let code = this.data.code;
        if (code == undefined) {
            console.log("getcode")
            await getWxLogin().then(async (res) => {
                code = res.code;
            });
        }
        console.log(code)
        const result = await request({
            url: "/weixin/register",
            data: {
                id: userId,
                name: userName,
                phoneNumber: phone,
                qq: qq,
                email: email,
                sex: sex,
                code: code
            },
            method: "post"
        });
        if(result.code==200){
            console.log("???")
            wx.switchTab({
              url: '/pages/home/index',
            })
        }else{
            wx.showToast({
                icon:'error',
              title: result.msg
            })
        }
    },
    onIdent() {
        // if(this.checkid()){
        //     console.log("校验")
        // }
        this.Register()
    },
    // async checkid(){
    //     const id  = this.data.userId
    //     if(length(id) != 15){
    //         wx.showToast({
    //           title: '学号',
    //         })
    //         return false
    //     }else{
    //         return true
    //     }
    // }
})