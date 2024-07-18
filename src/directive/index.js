import hasPermission from './permission/hasPermission'
import defaultSelect from './select/defaultSelect'
const install = function(Vue) {
  Vue.directive('hasPermission', hasPermission)
	Vue.directive('defaultSelect',defaultSelect)
}

if (window.Vue) {
  window['hasPermission'] = hasPermission
	window['defaultSelect'] = defaultSelect
  Vue.use(install); // eslint-disable-line
}

export default install
