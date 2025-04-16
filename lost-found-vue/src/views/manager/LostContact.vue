<template>
  <div>
    <!--查询框-->
    <div>
      <el-input style="width: 200px" placeholder="查询联系人" clearable v-model="publisherName"></el-input>
      <el-input style="width: 200px;margin-left: 10px" placeholder="查询被联系人" clearable v-model="contactedName"></el-input>
      <el-button type="primary" style="margin-left: 10px" @click="getData(1)">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
    <!--新增删除框-->
    <div style="margin:10px 0">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>
    <el-table :data="tableData" stripe
              :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
              @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="序号" min-width="30"></el-table-column>
      <el-table-column prop="publisherName" label="联系人"></el-table-column>
      <el-table-column prop="contactedName" label="被联系人"></el-table-column>
      <el-table-column prop="contactMethod" label="联系方式"></el-table-column>
      <el-table-column prop="content" label="留言内容" show-overflow-tooltip></el-table-column>
      <el-table-column prop="createTime" label="留言时间"></el-table-column>
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
</template>

<script>

export default {
  name: 'LostContact',
  title: '',
  data() {
    return {
      //所有数据
      tableData: [],
      //当前页码
      pageNum: 1,
      //每页显示条数
      pageSize: 6,
      username: '',
      publisherName: '',
      contactedName: '',
      total: 0,
      loginUser: JSON.parse(localStorage.getItem('user') || '{}'),
      ids: [],
      content: '',
    }
  },
  mounted() {
    this.getData()
  },
  methods: {


    //批量删除
    delBatch() {
      if (!this.ids.length) {
        this.$message({
          type: 'error',
          message: '请选择要删除的数据!'
        })
        return
      }
      this.$confirm('是否要删除这些数据么?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete('/lost-contact/del/batchByIds', {data: this.ids}).then(res => {
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
    },

    //批量选中 rows:当前选中所有行数据
    handleSelectionChange(rows) {
      // 将对象数组中id 放入数组中
      this.ids = rows.map(v => v.id)
    },

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

    reset() {
      this.publisherName = ''
      this.contactedName = ''
    },

    //从后端获取数据
    getData(pageNum) {
      // 解决在第二页点击查询后，数据在第一页，而显示的是第二页
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/lost-contact/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          publisherName: this.publisherName,
          contactedName: this.contactedName,
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
    }
  }

}
</script>

<style>

/*设置表单padding*/
::v-deep .el-dialog__body {
  padding: 20px 10px;
}

.el-tooltip__popper {
  max-width: 50% !important;
  font-size: 15px;
}
</style>
