// components/activitylist.js
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        list:{
            type:[],
            value:[]
        },
        flag:{//判断跳转页面
          type:Boolean,
          value:true,
        },
        isLeader:{//判断显示类型
          type:Boolean,
          value:false,
        }
    },

    /**
     * 组件的初始数据
     */
    data: {
        activityList:[]
    },

    /**
     * 组件的方法列表
     */
    methods: {

    },
    // options: {
    //     addGlobalClass: true
    // },
    
})
