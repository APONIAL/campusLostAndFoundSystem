<template>
  <div>
    <!--查询框-->
    <div>
      <el-input style="width: 200px" placeholder="查询发布人" clearable v-model="founderName"></el-input>
      <el-input style="width: 200px;margin-left: 10px" placeholder="查询名称" clearable
                v-model="foundName"></el-input>
      <el-select  placeholder="请选择类别" v-model="category" style="margin-left: 10px">
        <el-option v-for="(item,index) in categoryList " :key="index" :label="item.label" :value="item.value"></el-option>
      </el-select>
      <el-input style="width: 200px;margin-left: 10px" placeholder="查询地点" clearable
                v-model="foundLocation"></el-input>
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
      <el-table-column prop="name" label="物品名称"></el-table-column>
      <el-table-column prop="img" label="物品图片">
        <template v-slot="scope">
          <img :src="scope.row.img" alt="" style="width: 60px;height: 60px; border-radius: 10px">
        </template>
      </el-table-column>
      <el-table-column prop="founderName" label="发布人"></el-table-column>
      <el-table-column prop="content" label="失物描述">
        <template v-slot="scope">
          <el-button type="text" @click="showContent(scope.row.content)">点击查看</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template v-slot="scope">
          <el-tag v-if="scope.row.status === true" type="success">已找到失主</el-tag>
          <el-tag v-if="scope.row.status === false" type="danger">未找到失主</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="类别"></el-table-column>
      <el-table-column prop="foundLocation" label="发现地点"></el-table-column>
      <el-table-column prop="foundTime" label="发现时间"></el-table-column>
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

    <el-dialog title="编辑招领信息" :visible.sync="formVisible"
               width="40%" :close-on-click-modal="false" @close="closeDialog">
      <el-form :model="form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="物品图片" prop="img">
          <el-upload
            class="upload-demo"
            action="http://localhost:9090/file/upload"
            :headers="{token:loginUser.token}"
            :on-success="handleUploadSuccess"
            list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="物品名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="物品类别" prop="category">
          <el-select  placeholder="请选择类别" v-model="form.category">
            <el-option v-for="(item,index) in categoryList " :key="index" :label="item.label" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="物品状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="未找到失主" :value="false"></el-option>
            <el-option label="已找到失主" :value="true"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发现地点" prop="foundLocation">
          <el-input v-model="form.foundLocation"></el-input>
        </el-form-item>
        <el-form-item label="物品描述" prop="content">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="sendSaveOrUpdateRequest">确 定</el-button>
      </div>
    </el-dialog>

    <!--弹窗显示富文本内容-->
    <el-dialog title="内容" :visible.sync="formVisibleContent" width="60%">
      <div class="w-e-text">
        <div v-html="content"></div>
      </div>
    </el-dialog>

  </div>
</template>

<script>

import E from "wangeditor";
import hljs from "highlight.js";

export default {
  name: 'LostInfoList',
  data() {
    return {
      //所有数据
      tableData: [],
      form: '',
      //当前页码
      pageNum: 1,
      //每页显示条数
      pageSize: 6,
      username: '',
      founderName: '',
      foundName:'',
      total: 0,
      loginUser: JSON.parse(localStorage.getItem('user') || '{}'),
      ids: [],
      content: '',
      formVisible: false,
      editor: null,
      formVisibleContent: false,
      category:'',
      categoryList: [{label:'学习用品',value:'学习用品'},{label:'电子设备',value:'电子设备'},{label:'个人用品',value:'个人用品'},{label:'生活用品',value:'生活用品'},{label:'贵重物品',value:'贵重物品'},{value:'其他'}],
      foundLocation:''
    }
  },
  mounted() {
    this.getData(1)
  },
  methods: {
//用弹窗显示内容
    showContent(content) {
      this.content = content
      this.formVisibleContent = true
    },

    //设置富文本
    setRichText() {
      this.$nextTick(() => {
        this.editor = new E(`#editor`)
        this.editor.highlight = hljs
        this.editor.config.uploadImgServer = this.$baseUrl + '/file/editor/upload'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgHeaders = {
          token: this.loginUser.token
        }
        this.editor.config.uploadImgParams = {
          type: 'img'
        }
        this.editor.config.uploadVideoServer = this.$baseUrl + '/file/editor/upload'
        this.editor.config.uploadVideoName = 'file'
        this.editor.config.uploadVideoHeaders = {
          token: this.loginUser.token
        }
        this.editor.config.uploadVideoParams = {
          type: 'video'
        }
        this.editor.create()
      })
    },
    //关闭弹窗的回调
    closeDialog() {
      //销毁编辑器
      this.editor.destroy()
      this.editor = null
    },

    //上传成功后的回调函数
    handleUploadSuccess(res, file) {
      this.$set(this.form, 'img', res.data)
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
        this.$request.delete('lostInfo/del/batchByIds', {data: this.ids}).then(res => {
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
        this.$request.delete('found-info/' + id).then(res => {
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
      this.founderName = ''
      this.foundName = ''
      this.category =''
      this.foundLocation =''
    },
    handleEdit(row) {
      this.formVisible = true
      //给form对象赋值，要深度拷贝
      this.form = JSON.parse(JSON.stringify(row))
      //创建富文本容器
      this.setRichText()
      //设置编辑框的内容 延迟加载
      setTimeout(() => {
        this.editor.txt.html(this.form.content)
      })
    },
    handleAddLost() {
      //打开新增窗口前，清空上次残留数据
      this.form = {}
      this.formVisible = true
      //等div 渲染完毕，再创建富文本编辑器
      this.setRichText()
    },
    sendSaveOrUpdateRequest() {
      //获取编辑框的内容并发送
      this.form.content = this.editor.txt.html()
      this.$request.post('/found-info/saveOrUpdate', this.form).then(res => {
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
      this.$request.get('/found-info/FoundSquarePage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          founderName: this.founderName,
          name: this.foundName,
          category:this.category,
          foundLocation:this.foundLocation
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
