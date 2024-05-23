import {
    request,
    baseUrl,
    getWxLogin
} from '../../utils/request';
// pages/authorize/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        tipNum: 1,
        loginDisabled: true,
        isbinding: 0,
        userId:'',
        userName:'',
        sex:null,
        phone:'',
        qq:'',
        email:'',
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

    checkboxChange() {
        this.setData({
            loginDisabled: !this.data.loginDisabled
        })
    },

    toProtocol() {
        wx.navigateTo({
            url: '/pages/protocol/index',
        })
    },

    toPrivate() {
        wx.navigateTo({
            url: '/pages/privatepolicy/index',
        })
    },

    cleanData() {
        this.setData({
            userId:'',
            userName:'',
            sex:null,
            phone:'',
            qq:'',
            email:'',
            isUserIdFocus:false,
            isUserNameFocus:false,
            isPhoneFocus:false,
            isQFocus:false,
            isEmailFocus:false,
            err_Id:'',
            err_Name:'',
            err_Phone:'',
            err_Q:'',
            err_Email:''
        })
    },
    checkData(){
        return this.data.err_Id!=''||this.data.err_Name!=''||this.data.err_Phone!=''||this.data.err_Q!=''||this.data.err_Email!=''
    },
    backAuth() {
        if (this.data.tipNum != 1) {
            this.cleanData()
            this.setData({
                tipNum: 1,
                loginDisabled: true,
            })
        }
    },

    backBind() {
        if (this.data.tipNum == 3) {
            this.cleanData()
            this.setData({
                tipNum: 2
            })
        }
    },

    canLogin() {
        wx.navigateBack({})
    },

    login() {
        if (!this.data.loginDisabled) {
            this.isLogin()
        }
    },

    async isLogin() {
        wx.showModal({
            title: '友情提示',
            content: '微信授权登陆',
            complete: async (res) => {
                if (res.confirm) {
                    await getWxLogin().then(async (res) => {
                        let loginParam = {
                            code: res.code,
                        }
                        console.log(res);
                        await this.wxlogin(loginParam);
                    })
                    if (this.data.isbinding === 0) {
                        wx.showModal({
                            title: '提示',
                            content: '当前微信账号未绑定志愿者，是否前往绑定',
                            complete: (res) => {
                                if (res.confirm) {
                                    this.setData({
                                        tipNum: 2
                                    })
                                }
                            }
                        })
                    } else {
                        wx.switchTab({
                            url: '/pages/home/index'
                        })
                    }
                }
            }
        })
    },

    wxlogin(loginParam) {
        return new Promise(async (resolve, reject) => {
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
            resolve(result);
        })
    },
    async onBind() {
        if(this.data.userId==''||this.data.userName==''){
            wx.showToast({
                icon:'error',
                title: '请填写完整信息',
            })
            return
        }
        if(this.data.err_Id!=''||this.data.err_Name!=''){
            wx.showToast({
                icon:'error',
                title: '请填写正确信息',
              })
              return
        }
        const {
            userId,
            userName,
        } = this.data;
        getWxLogin().then(async (res) => {
            let loginParam = {
                code: res.code,
                id: userId,
                name: userName,
            }
            this.wxlogin(loginParam).then(response => {
                console.log(response)
                if (response.code == 200) {
                    wx.switchTab({
                        url: '/pages/home/index',
                    })
                }
                if (response.code == 501) {
                    wx.showModal({
                        title: '提示',
                        content: '当前学号未注册志愿者，是否前往注册',
                        complete: (res) => {
                            if (res.confirm) {
                                this.setData({
                                    tipNum: 3
                                })
                            }
                        }
                    })
                }
            })
        })
    },
    getId(e) {
        var value = e.detail.value;
        if (!value.trim()) {
            this.setData({
                isUserIdFocus: false,
                err_Id:""
            })
            return
        }
        const pattern = /^\d{15}$/;
        if (pattern.test(value)) {
            this.setData({
                err_Id:""
            })
        } else {
            this.setData({
                err_Id:"请输入正确的学号"
            })
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
                err_Name:""
            })
            return
        }
        const pattern = /^[a-zA-Z\u4e00-\u9fa5]{2,10}$/;
        if (pattern.test(value)) {
            this.setData({
                err_Name:""
            })
        } else {
            this.setData({
                err_Name:"请输入正确的用户名"
            })
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
                err_Phone:""
            })
            return
        }
        const pattern = /^1[3-9]\d{9}$/;
        if (pattern.test(value)) {
            this.setData({
                err_Phone:""
            })
        } else {
            this.setData({
                err_Phone:"请输入正确的电话号码"
            })
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
                err_Q:""
            })
            return
        }
        const pattern = /^\d{5,12}$/;
        if (pattern.test(value)) {
            this.setData({
                err_Q:""
            })
        } else {
            this.setData({
                err_Q:"请输入正确的QQ"
            })
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
                err_Email:""
            })
            return
        }
        const pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (pattern.test(value)) {
            this.setData({
                err_Email:""
            })
        } else {
            this.setData({
                err_Email:"请输入正确的邮箱"
            })
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
        if (index == "1") {
            this.setData({
                userName: "",
                isUserNameFocus: false,
            })
        }
        if (index == "2") {
            this.setData({
                phone: "",
                isPhoneFocus: false,
            })
        }
        if (index == "3") {
            this.setData({
                qq: "",
                isQFocus: false,
            })
        }
        if (index == "4") {
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
        if(this.data.userId==''||this.data.userName==''||this.data.phone==''||this.data.qq==''||this.data.email==''){
            wx.showToast({
                icon:'error',
                title: '请填写完整信息',
            })
            return
        }
        if(this.checkData()){
            wx.showToast({
                icon:'error',
                title: '请填写正确信息',
            })
            return
        }
        this.Register()
    },
})