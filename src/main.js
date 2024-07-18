import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en' // lang i18n
import directive from './directive' // directive
import '@/styles/index.scss' // global css
import App from './App'
import store from './store'
import router from './router'
import plugins from './plugins' // plugins
import './assets/icons' // icon
import '@/permission' // permission control
import * as echarts from 'echarts'
import { parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree } from '@/utils/sg'
import 'swiper/swiper.min.css'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import VueAMap from "vue-amap";
VueAMap.initAMapApiLoader({
  key: '2d0ce4ed3c455571148441f02056b42e',
  //插件集合
  plugin: [
    'AMap.Geolocation',  //定位空间，用来获取和展示用户主机所在的经纬度位置
    ' AMap.Autocomplete ',  //输入提示插件
    ' AMap.PlaceSearch ', //POI搜索插件
    ' AMap.Scale ',   //右下角缩略图插件，比例尺
    ' AMap.OverView ', //地图鹰眼插件
    ' AMap.ToolBar ',  //地图工具条
    ' AMap.MapType ',  //类别切换空间，实现默认图层与卫星图，实施交通层之间切换的控制
    ' AMap.PolyEditor ', //编辑 折线多边形
    ' AMap.CircleEditor ',
    "AMap.Geocoder"     //地图编码
  ]
});
 
// 全局方法挂载
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.handleTree = handleTree
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */

// set ElementUI lang to EN
// Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
Vue.use(VueAMap);
Vue.use(directive)
Vue.use(ElementUI)
Vue.use(plugins)
Vue.use(mavonEditor)
Vue.config.productionTip = false
import  debounceDirective from '@/directive/utils/debounceDirective.js'
// 防抖
Vue.directive('debounce', debounceDirective)
import overflowTooltip from '@/directive/utils/overflow-tooltip.js'
// 超出文本显示
Vue.use(overflowTooltip)
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
