<template>
  <div>
    <el-table :data="tableData" stripe>
      <el-table-column prop="id" label="ID" min-width="30"></el-table-column>
      <el-table-column prop="username" label="账号"></el-table-column>
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="role" label="角色"></el-table-column>
      <el-table-column label="头像" align="center" >
        <template v-slot="scope">
          <el-image v-if="scope.row.avatar" :src="scope.row.avatar"
               alt="" style="width: 50px;height: 50px;border-radius: 50%"
               :preview-src-list="[scope.row.avatar]"
          />
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column label="操作" width="auto" min-width="120%" align="center">
        <template v-slot="scope" >
          <el-button size="mini" type="primary" plain>编辑</el-button>
          <el-button size="mini" type="danger" plain>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: 'User',
  data () {
    return {
      tableData: []
    }
  },
  mounted () {
    this.$request({
      url: '/user',
      method: 'get'
    }).then(res => {
      this.tableData = res.data
    })
  },
  methods:{
  }

}
</script>

<style scoped>

</style>
