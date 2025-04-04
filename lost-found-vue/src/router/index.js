import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ElementView from '../views/Element'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/element',
    name: 'Element',
    component: ElementView
  },
  {
    path:'/login',
    name:'Login',
    component:()=>import('../views/Login')
  },
  {
    path:'/register',
    name:'Register',
    component:()=>import('../views/Register')
  }

]
const originalPush = VueRouter.prototype.push
// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
  routes
})

export default router
