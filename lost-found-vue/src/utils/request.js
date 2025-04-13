import axios from 'axios'
import router from '@/router'
//按需导入element-ui
import {
  Message,
  MessageBox,
} from 'element-ui'
//创建一个新的axios实例
const request = axios.create({
  baseURL: 'http://localhost:9090',
  timeout: 5000
})

//request拦截器
//可以在请求发出去之前做一些处理
//比如统一的token，对请求参数统一加密
request.interceptors.request.use(config => {
  config.headers['Content-Type'] = 'application/json;charset=UTF-8'
  //拿到token
  let user = JSON.parse(localStorage.getItem('user') || '{}')
  //设置请求头
  config.headers['token'] = user.token
  return config
}, error => {
  //for debug
  console.error('request error: ' + error)
  return Promise.reject(error)
})

//response拦截器
//可以在请求发出去之后做一些处理
//比如统一处理错误，对返回的数据进行统一处理

request.interceptors.response.use(response => {
  let res = response.data
  // 兼容服务器端返回的字符串数据
  if (typeof res === 'string') {
    res = res ? JSON.parse(res) : res
  }
  if (res.code === '200') {
    return res
  } else if (res.code === '401') {
    MessageBox.confirm(res.msg, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      window.sessionStorage.clear()
      router.push('/login')
    }).catch(res => res)
  }else if(res.code === '401'){
    Message({
      message: res.msg,
      type: 'error',
      duration: 3000
    })
    router.push('/login')
  } else {
    Message({
      message: res.msg,
      type: 'error',
      duration: 3000
    })
    return Promise.reject(res)
  }
}, error => {
  // 处理响应错误
  if (error.response) {
    // 根据响应状态码进行错误处理
    switch (error.response.status) {
      case "401":
        console.error('Unauthorized access, redirecting to login')
        // 可以在此处跳转到登录页或其他处理逻辑
        break
      case 404:
        console.error('Resource not found')
        break
      case 400:
        console.error('params error')
        Message({
          message: 'params error',
          type: 'error',
          duration: 3000
        })
        break
      default:
        console.error('An unexpected error occurred')
    }
  } else {
    console.error('Network error, please try again later')
  }
  return Promise.reject(error)
})

export default request
