<template>
  <div>
    <!--查询框-->
    <div>
      <el-input style="width: 200px" placeholder="查询账号" clearable v-model="username"></el-input>
      <el-input style="width: 200px;margin: 0 5px" placeholder="查询姓名" clearable v-model="name"></el-input>
      <el-button type="primary" @click="getData(1)">查询</el-button>
      <el-button type="info" @click="reset">重置</el-button>
    </div>
    <!--新增删除框-->
    <div style="margin:10px 0">
      <el-button type="primary" plain @click="handleAddDialog">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>
    <el-table :data="tableData" stripe
              :header-cell-style="{backgroundColor:'aliceblue',color:'#666'}"
              @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="ID" min-width="30"></el-table-column>
      <el-table-column prop="username" label="账号"></el-table-column>
      <el-table-column prop="name" label="姓名"></el-table-column>
      <el-table-column prop="role" label="角色"></el-table-column>
      <el-table-column label="头像" align="center">
        <template v-slot="scope">
          <el-image v-if="scope.row.avatar" :src="scope.row.avatar"
                    alt="" style="width: 50px;height: 50px;border-radius: 50%"
                    :preview-src-list="[scope.row.avatar]"
          />
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column label="操作" width="180" min-width="120%" align="center">
        <template v-slot="scope">
          <el-button size="mini" type="primary" plain @click="handleEdiUser(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" plain @click="delOne(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="margin: 10px 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[10, 20, 30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>
    <!--弹窗嵌套表单-->
    <el-dialog :title="title" :visible.sync="formVisible" width="30%">
      <el-form :model="form" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <div style="margin-bottom:5px;text-align: center">
          <!--头像上传框-->
          <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/file/upload"
            :headers="{token:user.token}"
            :show-file-list="false"
            :on-success="handleFromAvaSuccess"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div slot="tip" class="el-upload__text">请上传相应头像</div>
          </el-upload>
        </div>
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="用户" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdateUser">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'User',
  title:'',
  data () {
    return {
      //所有数据
      tableData: [],
      //当前页码
      pageNum: 1,
      //每页显示条数
      pageSize: 10,
      username: '',
      name: '',
      total: 0,
      formVisible: false,
      form:{},
      user: JSON.parse(localStorage.getItem('user')||'{}'),
      title:'',
      ids:[] ,
      rules: {
        username: [
          {
            required: true,
            message: '请输入账号',
            trigger: 'blur'
          },
          {
            min: 3,
            max: 10,
            message: '长度在 3 到 10 个字符',
            trigger: 'blur'
          }
        ],
      }
    }
  },
  mounted () {
    this.getData()
  },
  methods: {
    //批量删除
    delBatch(){
      if (!this.ids.length){
        this.$message({
          type: 'error',
          message: '请选择要删除的数据!'
        });
        return
      }
      this.$confirm('是否要删除这些用户吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete('/user/del/batchByIds', {data:this.ids}).then(res=>{
          if (res.code === '200'){
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.getData(1)
          }else {
            this.$message({
              type: 'error',
              message: res.data.msg
            });
          }
        }).catch(res=>res)
      })
    },

    //批量选中 rows:当前选中所有行数据
    handleSelectionChange(rows){
     // 将对象数组中id 放入数组中
     this.ids = rows.map(v =>v.id)
    },

    //删除单个用户
    delOne(id){
      this.$confirm('是否要删除该用户?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete('/user/'+id).then(res=>{
          if (res.code === '200'){
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.getData(1)
          }else {
            this.$message({
              type: 'error',
              message: res.data.msg
            });
          }
        }).catch(res=>res)
      })
      this.getData(1)
    },
    handleEdiUser(row){
      this.formVisible = true
      this.title = '编辑用户'
      //给form对象赋值，要深度拷贝
      this.form = JSON.parse(JSON.stringify(row))
    },
    //
    saveOrUpdateUser(){
      //校验规则
      this.$refs.formRef.validate(valid => {
        if (valid) {
          this.$request.post('/user/saveOrUpdate', this.form).then(res => {
            if (res.code === '200'){
              this.$message.success('保存成功')
              this.getData(1)
              this.formVisible = false
            }else if (res.code === '416'){
              this.$message.error('账号已存在')
            }else{
              this.$message.error(res.data.msg)
            }
          }).catch(res=>{
            console.log(res.message)
          })
        }
      })
    },
    handleAddDialog(){
      //打开新增窗口前，清空上次残留数据
      this.form = {role:'USER',avatar:'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'}
      this.title = '新增用户'
      this.formVisible = true;
    },
    handleFromAvaSuccess(response,file,fileList){
      console.log('response=>',response)
       this.form.avatar = response.data
      console.log('this.form=>',this.form)
    },

    reset () {
      this.username = ''
      this.name = ''
    },

    //从后端获取数据
    getData (pageNum) {
      // 解决在第二页点击查询后，数据在第一页，而显示的是第二页
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/user/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    handleSizeChange (val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange (val) {
      this.getData(val)
    }
  }

}
</script>

<style scoped>

/*设置表单padding*/
::v-deep .el-dialog__body{
  padding: 20px 10px;
}

/*下面几个css的是头像上传框的样式*/
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
  width: 130px;
  height: 130px;
  line-height: 130px;
  text-align: center;
}
.avatar {
  width: 130px;
  height: 130px;
  display: block;
  border-radius: 2%;
}
</style>
