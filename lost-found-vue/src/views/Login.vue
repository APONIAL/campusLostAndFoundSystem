<template>
  <div style="height: 100vh;width: 100%;display: flex;
  align-items: center;justify-content: center;background-color: #0f9876">
    <div style="display: flex;background-color: white;width: 50%;border-radius: 5px;overflow: hidden">
      <!--左边图片框-->
      <div style="flex: 1;">
        <img src="@/assets/imgs/logo.png" alt="" style="width: 100%">
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
          <!--<el-form-item prop="role">-->
          <!--  <el-select v-model="user.role" placeholder="请选择">-->
          <!--    <el-option label="管理员" value="ADMIN"></el-option>-->
          <!--    <el-option label="用户" value="USER"></el-option>-->
          <!--  </el-select>-->
          <!--</el-form-item>-->


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
            <div style="flex: 1;text-align: right"><span style="color: #409EFF;cursor: pointer"
                                                         @click="handleForgetPass">忘记密码</span></div>
          </div>
        </el-form>
      </div>
    </div>
    <!--忘记密码 对话框-->
    <el-dialog title="忘记密码" :visible.sync="forgetPassDialogVis" width="30%">
      <el-form :model="forgetUserForm" label-width="80px" style="padding-right: 20px">
        <el-form-item label="账号">
          <el-input v-model="forgetUserForm.username" autocomplete="off" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="forgetUserForm.phone" autocomplete="off" placeholder="请输入手机号"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="forgetPassDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="resetPassword">确 定</el-button>
      </div>
    </el-dialog>
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
      forgetUserForm: {}, //忘记密码表单数据
      forgetPassDialogVis: false,
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
    //初始化表单
    handleForgetPass () {
      this.forgetUserForm = {}
      this.forgetPassDialogVis = true
    },
    //重置密码
    resetPassword () {
      this.$request.post('/user/forgetPassword', this.forgetUserForm).then(res => {
        if (res.code ==='200'){
          this.$message.success('重置密码成功，初试密码为123456')
          this.forgetUserForm = {}
          this.forgetPassDialogVis = false
        }else{
          this.$message.error(res.data.msg)
        }
      }).catch(res=> {
        this.handleForgetPass();
      })
    },
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
                if (res.data.role === 'USER'){
                  this.$router.push('/front')
                }else{
                  this.$router.push('/')
                }
                this.$message.success('登录成功')
                //存儲用户信息
                localStorage.setItem('user', JSON.stringify(res.data))
              } else {
                this.$message.error(res.data.msg)
              }
            }).catch(res => res)
          } else if (!this.user.checkCode) {
            //验证码为空
            this.$message.error('请输入验证码')
          } else {
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
