// pages/my_activity/my_activity_detail/my_activity_detail.js
import {
  request,
} from '../../../utils/request.js';
Page({

    /**
     * 页面的初始数据
     */
    data: {
      userActivityDetail:[],
      activityInfo:'',
      isLeader:false,
      isshowVolunteer:[],
      tempshowVolunteer:[[]]
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
      let isLeader = options.isLeader=="false"?false:true;
      let activityId = options.id;
      this.getUserActivityDetail(activityId,isLeader)
      this.setData({
        isLeader,
      })
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
    async getUserActivityDetail(activityId,type) {
      const result = await request({
          url: '/activity/userActivityDetail',
          data: {
              activityId,
              type,
          },
          method: "GET"
      });
      if(result.code==200){
        this.setData({
          userActivityDetail:result.data.userActivityVos,
          activityInfo:result.data.activityVo
        })
      }
      if(result.code==200&&type){
        let list = result.data.userActivityVos;
        let length=result.data.userActivityVos.length;
        if(result.data!=null){
            this.setData({
                tempshowVolunteer:new Array(length).fill(0).map(() => new Array(2).fill(false)),
            })
            for(let i=0;i<length;i++){
                this.setData({
                    ['tempshowVolunteer['+i+'][1]']: new Array(list[i].volunteersInfo.length).fill(false),
                })
            }
            let temp=this.data.tempshowVolunteer
            this.setData({
                isshowVolunteer:this.data.isshowVolunteer.concat(temp)
            })
      }
  }
},
toMakeSignIn(e){
  let assignmentId = e.currentTarget.dataset.assignmentid;
  wx.navigateTo({
    url: '/pages/sign/sign_activity_assignment/sign_activity_assignment?assignmentId=' + assignmentId
    })
},
  showVolunteerPost:function(e){
    let index = e.currentTarget.dataset.index;
    let length=this.data.isshowVolunteer[index][1].length;
    let bool=this.data.isshowVolunteer[index][0];
    console.log(length);
    if(bool){
        for(let i=0;i<length;i++){
            this.setData({
                ['isshowVolunteer['+index+'][1]['+i+']']:false,
            })
        }
        this.setData({
            ['isshowVolunteer['+index+'][0]']:!this.data.isshowVolunteer[index][0],
        })
    }else{
        this.setData({
            ['isshowVolunteer['+index+'][0]']:!this.data.isshowVolunteer[index][0],
        })
    }
},

showVolunteerInfo:function(e){
    let index = e.currentTarget.dataset.index;
    let childindex = e.currentTarget.dataset.childindex;
    this.setData({
        ['isshowVolunteer['+index+'][1]['+childindex+']']:!this.data.isshowVolunteer[index][1][childindex],
    })
},
})