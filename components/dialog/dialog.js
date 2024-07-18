const app = getApp()
let id = ''
let name = ''
let phone = ''
let product = ''
let type = ''
let address = ''
let description = ''
import {
  request,
} from '../../utils/request'
Component({
  data: {
    showModalStatus: false,
    ActivityList: [],
    assignmentId: -1,
    signId: -1,
    showpost: false,
    showId: "0",
    activityName: "",
    signName: "",
    expireTime: "",
    atleast: "",
    signType: [{
      label: "签到",
      id: 1
    }, {
      label: "考勤",
      id: 2
    }, {
      label: "签退",
      id: 3
    }],
  },
  properties: {
    // 这里定义了innerText属性，属性值可以在组件使用时指定
    show: {
      type: String,
      value: "close",
    },
    activityAssignmentId:{
      type: Number,
      value:null,
    }
  },
  ready() {
    const that = this;
    console.log(that.properties.show)

    if (that.properties.show) {
      that.util(this.properties.show)
    }
    this.setData({
      assignmentId:that.properties.activityAssignmentId
    })
  },
  methods: {
    //信息修改
    //获取用户输入信息
    name(event) { //获取报修人员姓名
      name = event.detail.value
      console.log("name", name)
    },
    phone(event) { //获取手机号
      phone = event.detail.value
      console.log("phone", phone)
    },
    product(event) { //维修产品
      product = event.detail.value
      console.log("produc", product)
    },
    type(event) { //故障类型
      type = event.detail.value
      console.log("type", type)
    },
    address(event) { //地址
      address = event.detail.value
      console.log("address", address)
    },
    description(event) { //描述
      description = event.detail.value
      console.log("description", description)
    },
    update: function (e) {
      var currentStatu = e.currentTarget.dataset.statu;
      this.util(currentStatu)
    },
    util: function (currentStatu) {
      
      /* 动画部分 */
      // 第1步：创建动画实例  
      var animation = wx.createAnimation({
        duration: 200, //动画时长 
        timingFunction: "linear", //线性 
        delay: 0 //0则不延迟 
      });
      // 第2步：这个动画实例赋给当前的动画实例 
      this.animation = animation;

      // 第3步：执行第一组动画 
      animation.opacity(0).rotateX(-100).step();

      // 第4步：导出动画对象赋给数据对象储存 
      this.setData({
        animationData: animation.export()
      })

      // 第5步：设置定时器到指定时候后，执行第二组动画 
      setTimeout(function () {
        // 执行第二组动画 
        animation.opacity(1).rotateX(0).step();
        // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象 
        this.setData({
          animationData: animation
        })
        //关闭 
        if (currentStatu == "close") {
          this.setData({
            showModalStatus: false
          });
          this.triggerEvent('refresh');
        }
      }.bind(this), 200)
      // 显示 
      if (currentStatu == "open") {
        var date = new Date();
        var hour = date.getHours();
        var minute = date.getMinutes();
        this.setData({
          showModalStatus: true,
          signId:-1,
          expireTime:"",
          atleast: hour + ":" + minute
        });
      }
    },
    /**
     * 用户选择签到类型
     */
    selectType: function (e) {
      console.log(e)
      this.setData({
        signId:Number(e.detail.value)
      })
      console.log(this.data.signType[this.data.signId])
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
    async addSignIn() {
      let ass = this.data.assignmentId;
      let exp = this.data.expireTime;
      let sign = this.data.signId
      console.log(ass,exp,sign)
      if (ass != -1 && exp != "" && sign != -1) {
        const {
          assignmentId,
          expireTime,
          signId
        } = this.data;
        let date = new Date().toISOString().substring(0, 10)
        const result = await request({
          url: '/signIn/addSignIn',
          method: "POST",
          data: {
            assignmentId: assignmentId,
            expireTime: date + " " + expireTime + ":00",
            type: signId +1
          }
        });
        if (result.code == 200) {
          wx.showToast({
            icon: "success",
            title: result.msg,
          })
        this.util("close")
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
        } else {
          wx.showToast({
            icon: "error",
            title: "尚未选择过期时间",
          })
        }
      }
    },
  },
 
})