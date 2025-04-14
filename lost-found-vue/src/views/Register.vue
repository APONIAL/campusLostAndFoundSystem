<template>
  <div style="height: 100vh;width: 100%;display: flex;
  align-items: center;justify-content: center;background-color: #658dd5">
    <div style="display: flex;background-color: white;width: 50%;border-radius: 5px;overflow: hidden">
      <!--左边图片框-->
      <div style="flex: 1;">
        <img src="@/assets/imgs/logo.png" alt="" style="width: 100%">
      </div>
      <!--右边登录框-->
      <div style="flex: 1;display: flex;align-items: center;justify-content: left">
        <el-form :model="user" style="width: 90%" :rules="rules" ref="registerRef">
          <div style="font-size: 20px;font-weight: bold;text-align: center;margin-bottom: 20px">
            欢迎注冊校园失物招领系统
          </div>
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入账号"
                      v-model="user.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请输入密码"
                      v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input prefix-icon="el-icon-lock" size="medium" show-password placeholder="请确认密码"
                      v-model="user.confirmPassword"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="success" style="width: 100%;margin-top: 10px" @click="register">注 册</el-button>
          </el-form-item>
          <div style="display:flex; ">
            <div style="flex: 1">已有账号？请 <span style="color: #409EFF;cursor: pointer" @click="$router.push('/login')">登陆</span></div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Login',

  data () {
    //二次密码校验
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'))
      } else if(confirmPass !== this.user.password){
        callback(new Error('两次密码不一致'))
      }else{
        //通过
        callback()
      }
    };
    return {
      user: {
        username: '',
        password: '',
        confirmPassword:''
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
        confirmPassword: [
          {validator: validatePassword, trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['registerRef'].validate((valid) => {
        if (valid) {
          this.$request.post('/user/register', this.user).then(res => {
            if (res.code === '200') {
              this.$message.success("注册成功")
              this.$router.push('/login')
            } else {
              this.$message.error(res.data.msg)
            }
          }).catch(res=>{
            //强制刷新当前页面
            this.$router.go(0)
          })
        }
      })

    }
  },

}
</script>

<style scoped>

</style>
