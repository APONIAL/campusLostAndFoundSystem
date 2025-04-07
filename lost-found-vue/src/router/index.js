import Vue from 'vue'
import VueRouter from 'vue-router'
import Manager from '../views/Manager.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: Manager,
    redirect: '/home',
    children: [
      {
        path: '/403',
        name: 'NoPermission',
        component: () => import('../views/manager/NoPermission')
      },
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/manager/Home')
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/manager/User')
      }
      ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/manager/Login')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register')
  },
  {
    path: '*',
    name: '404',
    component: () => import('../views/404')
  }


]
const originalPush = VueRouter.prototype.push
// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  //to是要进入的目标路由对象，from是当前导航正要离开的路由，next是一个函数，表示放行
  let adminPaths = ['/user']
  //如果当前登录的用户不是管理，并且要to的路径是管理员路径，则跳转到无权限页面
  let user = JSON.parse(localStorage.getItem('user')||'{}')
  if (user.role !== 'ADMIN' && adminPaths.includes(to.path)){
    next('/403')
  }else{
    next()
  }
})

export default router
