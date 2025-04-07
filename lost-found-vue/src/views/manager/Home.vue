<template>
  <div>
    <div style="box-shadow: 0 0 10px rgba(0,0,0,.1);padding: 10px 20px;
          border-radius: 5px;margin-bottom: 10px">
      早安，{{user.name}}，欢迎使用校园失物招领系统
    </div>
    <div style="display: flex">
      <el-card style="width: 40%">
        <div slot="header" class="clearfix"><span>系统公告</span></div>
        <div>
          2025毕设
          <div style="margin-top: 20px">
            <div style="margin: 10px 0"><strong>主题色</strong></div>
            <el-button type="primary">按钮</el-button>
            <el-button type="success">按钮</el-button>
            <el-button type="warning">按钮</el-button>
            <el-button type="danger">按钮</el-button>
            <el-button type="info">按钮</el-button>
          </div>
        </div>
      </el-card>

    </div>

  </div>
</template>

<script>
export default {
  name: 'Home',
  data(){
    return{
      user:JSON.parse(localStorage.getItem('user')||'{}')
    }
  }
}
</script>

<style scoped>

</style>
