import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import request from '@/utils/request'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'highlight.js/styles/monokai-sublime.css'
import '@/assets/css/global.css'
import '@/assets/iconfont/iconfont.css'


Vue.use(ElementUI)
Vue.prototype.$request = request
Vue.prototype.$baseUrl = process.env.VUE_APP_BASE_URL
Vue.config.productionTip = false


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
