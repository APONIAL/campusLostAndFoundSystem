<template>
  <div>
    <!--查询框-->
    <div>
      <el-input style="width: 200px" placeholder="查询名称" clearable v-model="name"></el-input>
      <el-button type="primary" style="margin-left: 10px" @click="getData(1)">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
    <!--新增删除框-->
    <div style="margin:10px 0">
      <el-button type="primary" plain @click="handleAddDialog">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>
    `
    <el-table :data="tableData" stripe
              :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
              @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="序号" min-width="30"></el-table-column>
      <el-table-column prop="no" label="订单编号"></el-table-column>
      <el-table-column prop="name" label="订单名称" show-overflow-tooltip></el-table-column>
      <el-table-column prop="money" label="订单金额"></el-table-column>
      <el-table-column prop="user" label="所属用户"></el-table-column>
      <el-table-column prop="date" label="创建时间"></el-table-column>
      <el-table-column label="操作" width="180" min-width="120%" align="center">
        <template v-slot="scope">
          <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
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
    <!--弹窗嵌套表单-->
    <el-dialog title="信息" :visible.sync="formVisible"
               width="40%" :close-on-click-modal="false">
      <el-form :model="form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="订单名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="订单金额" prop="money">
          <el-input v-model="form.money"></el-input>
        </el-form-item>
        <el-form-item label="订单分类" prop="category">
          <el-select style="width: 100%" v-model="form.category">
            <el-option v-for="item in ['水果','蔬菜','零食','饮料','奶制品','糕点']"
                       :key="item" :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateNews">确 定</el-button>
      </div>

    </el-dialog>

  </div>
</template>

<script>

export default {
  name: 'Orders',
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
      name: '',
      total: 0,
      formVisible: false,
      form: {},
      loginUser: JSON.parse(localStorage.getItem('user') || '{}'),
      ids: [],
      content: '',
      rules: {
        name: [
          {
            required: true,
            message: '请输入订单名称',
            trigger: 'blur'
          }
        ],
        money: [
          {
            required: true,
            message: '请输入订单金额',
            trigger: 'blur'
          }
        ],
        category: [
          {
            required: true,
            message: '请选择订单分类',
            trigger: 'blur'
          }
        ]
     }
    }
  },
  mounted() {
    this.getData()
  },
  methods: {

    //调用修改是否公开
    changeOpen(form) {
      //深拷贝0
      this.form = JSON.parse(JSON.stringify(form))
      //直接发送请求
      this.sendSaveOrUpdateRequest()
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
        this.$request.delete('/orders/del/batchByIds', {data: this.ids}).then(res => {
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
        this.$request.delete('/orders/' + id).then(res => {
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


    handleEdit(row) {
      this.formVisible = true
      //给form对象赋值，要深度拷贝
      this.form = JSON.parse(JSON.stringify(row))
    },

    sendSaveOrUpdateRequest() {
      this.$request.post('/orders/saveOrUpdate', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.getData(1)
          this.formVisible = false
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(res => {
        console.log(res.message)
      })
    },
    //validate 控制是否开启校验
    saveOrUpdateNews() {
      this.$refs.formRef.validate(valid => {
        if (valid) {
          this.sendSaveOrUpdateRequest()
        }
      })
    },
    handleAddDialog() {
      //打开新增窗口前，清空上次残留数据
      this.form = {}
      this.formVisible = true
      //等div 渲染完毕，再创建富文本编辑器
      this.setRichText()
    },

    reset() {
      this.name = ''
    },

    //从后端获取数据
    getData(pageNum) {
      // 解决在第二页点击查询后，数据在第一页，而显示的是第二页
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/orders/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
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
