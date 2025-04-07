<template>
  <div style="height: 100vh;width: 100%;display: flex;
  align-items: center;justify-content: center;background-color: #0f9876">
    <div style="display: flex;background-color: white;width: 50%;border-radius: 5px;overflow: hidden">
      <!--左边图片框-->
      <div style="flex: 1;">
        <img src="@/assets/logo.png" alt="" style="width: 100%">
      </div>
      <!--右边登录框-->
      <div style="flex: 1;display: flex;align-items: center;justify-content: left">
        <el-form :model="user" style="width: 90%" :rules="rules" ref="loginRef">
          <div style="font-size: 20px;font-weight: bold;text-align: center;margin-bottom: 20px">
            欢迎登录校园失物招领系统
          </div>
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号"
                      v-model="user.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请输入密码"
                      v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item prop="checkCode">
            <div style="display: flex">
              <el-input prefix-icon="el-icon-circle-check" size="medium" style="flex: 1"
                        v-model="user.checkCode" placeholder="请输入验证码"></el-input>
              <!--验证码框 左右不齐的时候设置相同高度-->
              <div style="flex: 1;height: 36px">
                <valid-code @update:value="getCode"/>
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%;margin-top: 10px" @click="login">登 录</el-button>
          </el-form-item>
          <div style="display:flex;">
            <div style="flex: 1">还没有账号？请 <span style="color: #409EFF;
            cursor: pointer" @click="$router.push('/register')">注册</span></div>
            <div style="flex: 1;text-align: right"><span style="color: #409EFF;cursor: pointer">忘记密码</span></div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import ValidCode from '@/components/ValidCode'

export default {
  name: 'Login',
  components: {
    ValidCode
  },
  data () {
    return {
      code: '', // 验证码组件传递过来的code
      user: {
        checkCode: '',
        username: '',
        password: ''
      },
      rules: {
        username: [
          {
            required: true,
            message: '请输入账号',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 10,
            message: '长度在 3 到 10 个字符',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 10,
            message: '长度在 3 到 10 个字符',
            trigger: 'blur'
          },

        ],
      }
    }
  },
  methods: {
    getCode (code) {
      this.code = code.toLowerCase()
    },
    login () {
      this.$refs['loginRef'].validate((valid) => {
        if (valid) {
          //验证通过
          if (this.code === this.user.checkCode.toLowerCase()) {
            //验证码正确
            this.$request.post('/user/login', this.user).then(res => {
              if (res.code === '200') {
                this.$router.push('/')
                this.$message.success("登录成功")
                //存儲用户信息
                localStorage.setItem('user', JSON.stringify(res.data))
              } else {
                this.$message.error(res.data.msg)
              }
            }).catch(res=>res)
          } else if(!this.user.checkCode){
            //验证码为空
            this.$message.error('请输入验证码')
         }else{
            this.$message.error('验证码错误')
          }
        }
      })

    }
  },

}
</script>

<style scoped>

</style>
