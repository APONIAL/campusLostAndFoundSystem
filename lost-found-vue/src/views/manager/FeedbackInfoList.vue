<template>
  <div>
    <!--查询框-->
    <div>
      <el-input style="width: 200px" placeholder="查询用户" clearable v-model="userName"></el-input>
      <el-select  placeholder="请选择评分" v-model="rate" style="margin-left: 10px">
        <el-option v-for="(item,index) in rateList " :key="index" :label="item.label" :value="item.value"></el-option>
      </el-select>
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
      <el-table-column prop="userName" label="用户名" min-width="30"></el-table-column>
      <el-table-column prop="rate" label="评分">
        <template v-slot="scope">
          <el-tag :type="getRateColor(scope.row.rate)">{{ getRate(scope.row.rate) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="反馈建议" show-overflow-tooltip></el-table-column>
      <el-table-column prop="createTime" label="创建时间"></el-table-column>
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
  name: 'FeedBackInfoList',
  title: '',
  data() {
    return {
      //所有数据
      tableData: [],
      //当前页码
      pageNum: 1,
      //每页显示条数
      pageSize: 6,
      userName: '',
      rate: null,
      total: 0,
      loginUser: JSON.parse(localStorage.getItem('user') || '{}'),
      ids: [],
      rateList: [
        {
          value: 1,
          label: '差'
        },
        {
          value: 2,
          label: '中'
        },
        {
          value: 3,
          label: '好'
        },
        {
          value: 4,
          label: '非常好'
        },
        {
          value: 5,
          label: '极好'
        }
      ]
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    getRate(rate) {
      const rates = {
        1: "差",
        2: "中",
        3: "好",
        4: "非常好",
        5: "极好"
      };
      return rates[rate] || "无";
    },
    getRateColor(rate) {
      const rateColors = {
        1: "danger",
        2: "warning",
        3: "info",
        4: "",
        5: "success",
      };
      return rateColors[rate] || " ";
    },

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
        this.$request.delete('/feedback/del/batchByIds', {data: this.ids}).then(res => {
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
        this.$request.delete('/feedback/' + id).then(res => {
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
      this.userName= ''
      this.rate = null
    },

    //从后端获取数据
    getData(pageNum) {
      // 解决在第二页点击查询后，数据在第一页，而显示的是第二页
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/feedback/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userName: this.userName,
          rate: this.rate,
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
