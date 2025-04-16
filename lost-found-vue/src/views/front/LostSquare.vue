<template>
  <div class="main-content">
    <div style="color: #8b4515;font-size: 16px;font-weight: 550;">这里是失物广场</div>
    <div style="margin-top: 30px">
      <el-input placeholder="请输入标题查询" style="width: 200px" v-model="title"></el-input>
      <el-button type="primary" @click="getData(1)" style="margin: 0 10px">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
    <!--一行4个，设置:span为6，即24/4=6-->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="6" v-for="item in tableData" style="margin-bottom: 20px" :key="item.id">
        <div class="transition card" style="margin-left: 3px">
          <div style="display: flex">
            <img :src="item.img" alt=""
                 style="height: 90px;width: 90px;border: 1px solid #eeeeee;border-radius: 10px">
            <div style="margin-left: 10px">
              <div style="font-weight: 550;color:#ef5d26;margin-top: 5px">{{ item.name }}</div>
              <div style="margin-top: 10px;color: #666666">发布人：{{ item.user }}</div>
              <div style="margin-top: 10px;color: #666666">时间：{{ item.time }}</div>
            </div>
          </div>
          <div style="margin-top: 20px">
            <el-button type="info" @click="showContent(item.content)">查看详情</el-button>
            <el-button type="success">联系失主</el-button>
          </div>
        </div>
      </el-col>


    </el-row>

    <div class="block" style="margin: 10px 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :page-sizes="[8, 16, 32]"
        layout="total,sizes, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>


    <!--弹窗显示富文本内容-->
    <el-dialog title="内容" :visible.sync="formVisibleContent" width="60%">
      <div class="w-e-text">
        <div v-html="content"></div>
      </div>
    </el-dialog>
    <!--弹窗显示联系失主-->
    <el-dialog title="内容" :visible.sync="formVisibleContent" width="60%">

    </el-dialog>
  </div>
</template>

<script>
import E from "wangeditor";
import hljs from "highlight.js";

export default {
  name: "LostSquare",
  data() {
    return {
      tableData: [],
      form: [],
      formVisible: false,
      //当前页码
      pageNum: 1,
      //每页显示条数
      pageSize: 8,
      total: 0,
      loginUser: JSON.parse(localStorage.getItem('user') || '{}'),
      title: '',
      formVisibleContent: '',
      content: ''
    }
  },
  mounted() {
    this.getData();
  },
  methods: {

    //用弹窗显示内容
    showContent(content) {
      this.content = content
      this.formVisibleContent = true
    },



    //删除单条数据
    delOne(id) {
      this.$confirm('是否要删除该数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete('/notice/' + id).then(res => {
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
    handleAddLost() {
      //打开新增窗口前，清空上次残留数据
      this.form = {}
      this.formVisible = true
    },
    sendSaveOrUpdateRequest() {
      //获取编辑框的内容并发送
      this.$request.post('/lostInfo/saveOrUpdate', this.form).then(res => {
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

    //从后端获取数据
    getData(pageNum) {
      // 解决在第二页点击查询后，数据在第一页，而显示的是第二页
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/lostInfo/lostSquarePage', {
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
    }
  }
}
</script>

<style scoped>
</style>
