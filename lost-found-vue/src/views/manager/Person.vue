<template>
  <div>
    <el-card style="width: 50%">
      <!--inline 行内输入框-->
      <!--padding-right: 20px 让输入框有边距 prop用于表单验证-->
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <div style="margin-bottom:15px;text-align: center">
          <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/file/upload"
            :headers="{token:user.token}"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="账号" prop="username">
          <el-input v-model="user.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保存</el-button>
      </div>
    </el-card>

  </div>
</template>

<script>
export default {
  name: 'Person',
  data () {
    return {
      user: JSON.parse(localStorage.getItem('user') || '{}')
    }
  },
  mounted () {

  },
  methods: {
    update () {
      //保存修改后的用户信息到数据库
      this.$request.post('/user/saveOrUpdate', this.user).then(res => {
        if (res.code === '200'){
          //  更新浏览器缓存里的用户信息
          this.$message.success("修改成功")
          localStorage.setItem('user', JSON.stringify(this.user))
          this.$emit('update:user', this.user)
        }else{
          this.$message.error(res.data.msg)
        }
      }).catch(res => res)
    },
    handleAvatarSuccess (response, file, fileList) {
      //把user的头像属性换成上传的图片地址
      this.user.avatar = response.data
    }
  }

}
</script>

<style scoped>
::v-deep .el-form-item__label {
  font-weight: bold;
}

/*要为el-upload设置avatar相同的border-radius*/
::v-deep .el-upload {
  border-radius: 2%;
}

/*element ui 自带的上传组件样式*/
::v-deep .avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 2%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 2%;
}
</style>
