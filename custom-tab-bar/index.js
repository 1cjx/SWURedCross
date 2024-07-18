// custom-tab-bar/index.js
Component({
  data: {
    color: "#515151",
    selectedColor: "#DAA520",
    show: true,
    backgroundColor: "#ffffff",
    list: [{
        pagePath: "/pages/home/index",
        text: "首页",
        iconPath: "/icons/home.png",
        selectedIconPath: "/icons/_home.png"
      },
      {
        pagePath: "/pages/activity/activity_index/activity_index",
        text: "志愿服务",
        iconPath: "/icons/activity.png",
        selectedIconPath: "/icons/_activity.png"
      },
      {
        pagePath: "/pages/sign/sign_index/sign_index",
        text: "打卡",
        bulge: true,
        iconPath: "/icons/sign.png",
        selectedIconPath: "/icons/_sign.png"
      },
      {
        pagePath: "/pages/my_activity/my_activity_index/my_activity_index",
        text: "活动管理",
        iconPath: "/icons/activity_info.png",
        selectedIconPath: "/icons/_activity_info.png"
      },
      {
        pagePath: "/pages/my/my_index/my_index",
        text: "我的",
        iconPath: "/icons/my.png",
        selectedIconPath: "/icons/_my.png"
      }
    ]
  },

  attached() {
  },
  methods: {
    switchTab(e) {
      const data = e.currentTarget.dataset
      const url = data.path
      wx.switchTab({url}) 
    }
  }
})

