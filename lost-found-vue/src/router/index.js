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
      {path: 'person', name: 'Person', meta: {title: '个人信息'}, component: () => import('../views/manager/Person')},
      {path: '403', name: 'NoPermission', meta: {title: '无权限'}, component: () => import('../views/manager/NoPermission')},
      {path: 'home', name: 'Home', meta: {title: '首页'}, component: () => import('../views/manager/Home')},
      {path: 'user', name: 'User', meta: {title: '用户管理'}, component: () => import('../views/manager/User')},
      {path: 'password', name: 'Password', meta: {title: '修改密码'}, component: () => import('../views/manager/Password')},
      {path: 'news', name: 'News', meta: {title: '新闻信息'}, component: () => import('../views/manager/News')},
      {path: 'newsDetail', name: 'NewsDetail', meta: {title: '新闻详情'}, component: () => import('../views/manager/NewsDetail')},
      {path: 'notice', name: 'Notice', meta: {title: '系统公告'}, component: () => import('../views/manager/Notice')},
      {path: 'logs', name: 'logs', meta: {title: '系统日志'}, component: () => import('../views/manager/Logs')},
      {path: 'charts', name: 'Charts', meta: {title: '数据统计'}, component: () => import('../views/manager/Charts')},
      {path: 'orders', name: 'Orders', meta: {title: '订单管理'}, component: () => import('../views/manager/Orders')},
      {path: 'lostContact', name: 'LostContact', meta: {title: '留言管理'}, component: () => import('../views/manager/LostContact')},
      {path: 'lostInfoList', name: 'LostInfoList', meta: {title: '失物管理'}, component: () => import('../views/manager/LostInfoList')},
      {path: 'foundInfoList', name: 'FoundInfoList', meta: {title: '招领管理'}, component: () => import('../views/manager/FoundInfoList')},
      {path: 'dataCharts', name: 'DataCharts', meta: {title: '数据分析'}, component: () => import('../views/manager/DataCharts')},
    ]
  },
  {

    path: '/front',
    name: 'Front',
    redirect: '/front/home',
    component: () => import('../views/Front'),
    children: [
      {path: 'home', name: 'FrontHome', meta: {title: '首页'}, component: () => import('../views/front/Home')},
      {path: 'person', name: 'FontPerson', meta: {title: '个人信息'}, component: () => import('../views/front/Person')},
      {path: 'myLost', name: 'MyLost', meta: {title: '我的失物'}, component: () => import('../views/front/MyLost')},
      {path: 'myFound', name: 'MyFound', meta: {title: '我的招领'}, component: () => import('../views/front/MyFound')},
      {path: 'lostSquare', name: 'LostSquare', meta: {title: '失物广场'}, component: () => import('../views/front/LostSquare')},
      {path: 'foundSquare', name: 'FoundSquare', meta: {title: '招领广场'}, component: () => import('../views/front/FoundSquare')},
      {path: 'contactMe', name: 'ContactMe', meta: {title: '联系我的'}, component: () => import('../views/front/ContactMe')},
      {path: 'myContact', name: 'MyContact', meta: {title: '我联系的'}, component: () => import('../views/front/MyContact')},
      {path: 'feedback', name: 'FeedBack', meta: {title: '反馈建议'}, component: () => import('../views/front/Feedback')},
    ]
  },

  {
    path: '/login',
    name: 'Login',
    meta: {
      title: '登录'
    },
    component: () => import('../views/Login')
  },
  {
    path: '/register',
    name: 'Register',
    meta: {
      title: '注册'
    },
    component: () => import('../views/Register')
  },
  {
    path: '*',
    name: '404',
    meta: {
      title: '404'
    },
    component: () => import('../views/404')
  },


]
const originalPush = VueRouter.prototype.push
// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
  //to是要进入的目标路由对象，from是当前导航正要离开的路由，next是一个函数，表示放行
  let adminPaths = ['/user']
  //如果当前登录的用户不是管理，并且要to的路径是管理员路径，则跳转到无权限页面
  let user = JSON.parse(localStorage.getItem('user') || '{}')
  if (user.role !== 'ADMIN' && adminPaths.includes(to.path)) {
    next('/403')
  } else {
    next()
  }
})

export default router
