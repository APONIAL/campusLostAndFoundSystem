<template>
  <div class="main-content">
    <div>
      <el-table :data="tableData" stripe
                :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}">
        <el-table-column prop="id" label="序号" min-width="30"></el-table-column>
        <el-table-column prop="img" label="物品主图">
          <template v-slot="scope">
            <el-image :src="scope.row.img" style="width: 50px; height: 50px;border-radius: 15px"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="itemName" label="物品名称"></el-table-column>
        <el-table-column prop="contactedName" label="联系人"></el-table-column>
        <el-table-column prop="contactMethod" label="联系方式"></el-table-column>
        <el-table-column prop="content" label="留言内容"></el-table-column>

        <el-table-column label="操作" width="180" min-width="120%" align="center">
          <template v-slot="scope">
            <el-button size="mini" type="danger" plain @click="delOne(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--分页插件-->
      <div class="block" style="margin: 10px 0">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[6, 12, 24]"
          :page-size="pageSize"
          layout="total,sizes, prev, pager, next"
          :total="total">
        </el-pagination>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  name: "MyContact",
  data(){
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 6,
      total: 0,
      formVisible: false,
     }
  },
  mounted() {
    this.getData(1)
  },
  methods:{
    //删除单条数据
    delOne(id) {
      this.$confirm('是否要删除该数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete('/lost-contact/' + id).then(res => {
          if (res.code === '200') {
            this.$message({
              type: 'success',
              message: '删除成功!'
            })
            this.getData(1)
          } else {
            this.$message({
              type: 'error',
              message: res.data.msg
            })
          }
        }).catch(res => res)
      })
      this.getData(1)
    },
    //从后端获取数据
    getData(pageNum) {
      // 解决在第二页点击查询后，数据在第一页，而显示的是第二页
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/lost-contact/myContact', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(res => res)
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.getData(val)
    },
  }

}
</script>

<style scoped>

</style>
