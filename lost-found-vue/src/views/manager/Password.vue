<template>
  <div>
    <el-card style="width: 50%">
      <el-form ref="formRef" :rules="rules" :model="user" label-width="80px" style="padding-right: 20px">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="update">确认</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Password',
  data () {
    //自定义校验，确认密码必须和密码一致
    const validatePassword = (rule, confirmPass, callback) => {
      if (confirmPass === '') {
        callback(new Error('请确认密码'))
      }else if (confirmPass.length < 3 || confirmPass.length > 10){
        callback(new Error('密码长度在 3 到 10 个字符'))
      } else if(confirmPass !== this.user.newPassword){
        callback(new Error('两次密码不一致'))
      }else{
        //通过
        callback()
      }
    };
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}'),
      rules: {
        password: [
          {
            required: true,
            message: '请输入原始密码',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 10,
            message: '长度在 3 到 10 个字符',
            trigger: 'blur'
          }
        ],
        newPassword: [
          {
            required: true,
            message: '请输入新密码',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 10,
            message: '长度在 3 到 10 个字符',
            trigger: 'blur'
          }
        ],
        confirmPassword: [
          {validator: validatePassword, trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    update () {
      //保存前，先校验一下规则
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          //验证成功后，做一次更新
          console.log(this.user)
          this.user.password = this.user.newPassword
          this.$request.post('/user/saveOrUpdate', this.user).then(res => {
            if (res.code === '200') {
              this.$message.success('修改成功')
              localStorage.removeItem('user')
              this.$router.push('/login')
            } else {
              this.$message.error(res.data.msg)
            }
          }).catch(res => res)
        }
      })
    },
  }

}
</script>0

<style scoped>
::v-deep .el-form-item__label {
  font-weight: bold;
}
</style>
