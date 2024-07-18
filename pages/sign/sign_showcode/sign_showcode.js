// pages/sign/sign_showcode/sign_showcode.js
import drawQrcode from '../../../utils/weapp.qrcode.esm';
import {
  request
} from '../../../utils/request';
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id:"",
        // qrcodeWidth: 0
        qrcodeURL:"",
        timer: null,
        signInUserList:[]
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        this.setData({
            id:options.id
        });
        this.getQRCode();
        this.getSignInUserList();
    },
    async getQRCode(){
      const result = await request({
        url: '/signIn/getQRCode',
        responseType: 'arraybuffer',
        data: {
          signInId : this.data.id
        },
        method: "GET"
    });
    let length = result.byteLength;
    if(length<=44){
      wx.navigateBack()
    }
    let url ='data:image/jpg;base64,'+wx.arrayBufferToBase64(result)
    url = url.replace(/[\r\n]/g,"");
    this.setData({
      qrcodeURL:url
    })
    },
    async getSignInUserList(){
      const result = await request({
        url: '/signIn/getQRCodeSignInList',
        data: {
          signInId : this.data.id
        },
        method: "GET"
    });
      console.log(result);
      this.setData({
        signInUserList:result.data
      })
    },
    // draw(){
    //     drawQrcode({
            
    //         width: 200, // 必须，二维码宽度，与canvas的width保持一致
            
    //         height: 200, // 必须，二维码高度，与canvas的height保持一致
           
    //         canvasId: 'myQrcode',
            
    //         background:'#ffffff', //	非必须，二维码背景颜色，默认值白色
            
    //         foreground: '#2bb15e', // 非必须，二维码前景色，默认值黑色 	'#000000'
            
    //         // ctx: wx.createCanvasContext('myQrcode'), // 非必须，绘图上下文，可通过 wx.createCanvasContext('canvasId') 获取，v1.0.0+版本支持
            
    //         text: this.data.id,  // 必须，二维码内容
    //         // v1.0.0+版本支持在二维码上绘制图片
      
    //         image: {
    //           // imageResource: '../../images/icon.png', // 指定二维码小图标
    //           dx: 70,
    //           dy: 70,
    //           dWidth: 60,
    //           dHeight: 60
    //         }
    //       })
    // },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        // this.draw();
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
      var that = this;
      var timer = setInterval(function () {
        that.getSignInUserList();
        that.getQRCode();}, 6000);
      this.setData({
        timer
      })
    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {
      clearInterval(this.data.timer);
      this.setData({
        timer: null
      });
    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {
      clearInterval(this.data.timer);
      this.setData({
        timer: null
      });
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