// pages/sign/sign_setsign/sign_setsign.js
import {
  request,
  getBaseUrl
} from '../../../utils/request'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    ActivityList: [],
    assignmentId: -1,
    signId: -1,
    showpost: false,
    showId: "0",
    activityName: "",
    signName: "",
    expireTime: "",
    atleast: "",
    // dropdown_Data: {},
    // dropdownHeight: 0,
    signType: [{
      name: "签到",
      id: 1
    }, {
      name: "考勤",
      id: 2
    }, {
      name: "签退",
      id: 3
    }],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

    this.getUserAsLeaderActivityList();
    // let dw_animation = wx.createAnimation({
    //     timingFunction: "ease",
    //     duration: 200
    //   })
    // this.dw_animation = dw_animation;
    // let dwheight = 3 * 70 + "rpx";
    // this.setData({
    //     dropdownHeight: dwheight
    // })
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
    var date = new Date();
    var hour = date.getHours();
    var minute = date.getMinutes();
    console.log(date)
    this.setData({
      atleast: hour + ":" + minute
    })
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
  async getUserAsLeaderActivityList() {
    const result = await request({
      url: '/activity/userAsLeaderActivityList',
      method: "GET",
      data: {
        type: 0,
        pageSize: 20,
        pageNum: 1,
      }
    });
    this.setData({
      ActivityList: result.data
    })

    // console.log(this.data.ActivityList.rows[0].activityInfo.activityName)
  },
  test(e) {
    console.log(e)
  },
  addS() {
    this.addSignIn()
  },

  async addSignIn() {
    let ass = this.data.assignmentId;
    let exp = this.data.expireTime;
    let sign = this.data.signId
    if (ass != "" && exp != "" && sign != "") {
      const {
        assignmentId,
        expireTime,
        signId
      } = this.data;
      let date = new Date().toISOString().substring(0, 10)
      console.log(date)
      const result = await request({
        url: '/signIn/addSignIn',
        method: "POST",
        data: {
          assignmentId: assignmentId,
          expireTime: date + " " + expireTime + ":00",
          type: signId
        }
      });
      if (result.code == 200) {
        wx.showToast({
          icon: "success",
          title: result.msg,
        })
        setTimeout(function () {
          wx.navigateBack()
        }, 2000)
      } else {
        wx.showToast({
          icon: "error",
          title: result.msg,
        })
      }
    } else {
      if (ass == -1) {
        wx.showToast({
          icon: "error",
          title: "尚未选择活动类型",
        })
      } else if (sign == -1) {
        wx.showToast({
          icon: "error",
          title: "尚未选择签到类型",
        })
      } else if (beg == "") {
        wx.showToast({
          icon: "error",
          title: "尚未选择开始时间",
        })
      } else {
        wx.showToast({
          icon: "error",
          title: "尚未选择结束时间",
        })
      }
    }
  },
  /**
   * 用户点击唤出下拉菜单 
   */
  showpost: function (e) {
    let id = e.currentTarget.dataset.id;
    let showid = this.data.showId;
    let boolshow = this.data.showpost;
    if (boolshow && showid != id) {
      this.setData({
        showId: id,
      })
    } else {
      this.setData({
        showId: id,
        showpost: !this.data.showpost
      })
    }
  },

  /**
   * 用户选择活动 
   */
  selectActivity: function (e) {
    console.log(e.currentTarget.dataset)
    let activity = e.currentTarget.dataset.activity;
    let id = e.currentTarget.dataset.aid
    this.setData({
      activityName: activity,
      showpost: false,
      assignmentId: id
    })
  },

  /**
   * 用户选择签到类型
   */
  selectType: function (e) {
    let sign = e.currentTarget.dataset.sign;
    let sid = e.currentTarget.dataset.sid;
    this.setData({
      signName: sign,
      showpost: false,
      signId: sid
    })
  },


  setExpireTime: function (e) {
    const selectedTime = e.detail.value;
    // 获取当前时间
    const now = new Date();
    const currentHours = now.getHours();
    const currentMinutes = now.getMinutes();
    // 将当前时间转换为字符串形式，例如 '09:30'
    const currentTimeString = `${currentHours < 10 ? '0' : ''}${currentHours}:${currentMinutes < 10 ? '0' : ''}${currentMinutes}`;

    // 如果选择的时间小于当前时间，则重置为当前时间
    if (selectedTime <= currentTimeString) {
      wx.showToast({
        title: '请选择当前时间之后的时间',
        icon: 'none'
      });

      // 重置 picker 的值为当前时间
      this.setData({
        currentTime: currentTimeString
      });

      return;
    }
    this.setData({
      expireTime: selectedTime
    })
  },

  // showtest: function () {
  //     let nowShow = this.data.showpost;
  //     let dw_animation = this.dw_animation;
  //     if (nowShow) {
  //       //下拉动画
  //       dw_animation.height(0).step();
  //       this.setData({
  //         dropdown_Data: dw_animation.export()
  //       })
  //     } else {
  //       let dropdownHeight = this.data.dropdownHeight;
  //       //下拉动画
  //       dw_animation.height(dropdownHeight).step();
  //       this.setData({
  //         dropdown_Data: dw_animation.export()
  //       })
  //     }
  //     this.setData({
  //         showpost: !nowShow
  //     })
  //   },
})