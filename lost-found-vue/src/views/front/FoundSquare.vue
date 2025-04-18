<template>
  <div class="main-content">
    <div style="color: #8b4515;font-size: 16px;font-weight: 550;">这里是招领广场</div>
    <div style="margin-top: 30px">
      <el-input style="width: 200px" placeholder="查询发布人" clearable v-model="founderName"></el-input>
      <el-input style="width: 200px;margin-left: 10px" placeholder="查询名称" clearable
                v-model="foundName"></el-input>
      <el-select  placeholder="请选择类别" v-model="category" style="margin-left: 10px">
        <el-option v-for="(item,index) in categoryList " :key="index" :label="item.label" :value="item.value"></el-option>
      </el-select>
      <el-input style="width: 200px;margin-left: 10px" placeholder="查询地点" clearable
                v-model="foundLocation"></el-input>
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
              <div style="margin-top: 10px;color: #666666">发布人：{{ item.founderName }}</div>
              <div style="margin-top: 10px;color: #666666">地点：{{ item.foundLocation }}</div>
              <div style="margin-top: 10px;color: #666666">时间：{{ item.foundTime }}</div>
            </div>
          </div>
          <div style="margin-top: 20px">
            <el-button type="info" @click="showContent(item.content)">查看详情</el-button>
            <el-button type="success" @click="handleAddContact(item.userId,item.id)">联系拾主</el-button>
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
    <el-dialog title="联系失主" :visible.sync="formVisibleContact"
               width="40%">
      <el-form :model="contactForm" label-width="80px" style="padding-right: 20px" ref="formRef">
        <el-form-item label="联系方式" prop="contactMethod">
          <el-input v-model="contactForm.contactMethod"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="contactForm.content"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="formVisibleContact = false">取 消</el-button>
        <el-button type="primary" @click="saveContact">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "LostSquare",
  data() {
    return {
      tableData: [],
      contactForm: [],
      //当前页码
      pageNum: 1,
      //每页显示条数
      pageSize: 8,
      total: 0,
      loginUser: JSON.parse(localStorage.getItem('user') || '{}'),
      title: '',
      formVisibleContent: false,
      content: '',
      formVisibleContact: false,
      founderName: '',
      foundName:'',
      category:'',
      categoryList: [{label:'学习用品',value:'学习用品'},{label:'电子设备',value:'电子设备'},{label:'个人用品',value:'个人用品'},{label:'生活用品',value:'生活用品'},{label:'贵重物品',value:'贵重物品'},{value:'其他'}],
      foundLocation:''
    }
  },
  mounted() {
    this.getData();
  },
  methods: {
    handleAddContact(contactedId,itemId) {
      //打开新增窗口前，清空上次残留数据
      this.contactForm = {}
      this.contactForm.contactedId = contactedId
      this.contactForm.itemId = itemId
      //区分当前contact是失物还是招领 1：失物，0：招领
      this.contactForm.itemType = 0
      this.formVisibleContact = true
    },

    saveContact() {
      this.$request.post('/lost-contact/save', this.contactForm).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.getData(1)
          this.formVisibleContact = false
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(res => {
        console.log(res.message)
      })
    },


    //删除单条数据
    delOne(id) {
      this.$confirm('是否要删除该数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$request.delete('/found-info/' + id).then(res => {
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
    },
    //用弹窗显示内容
    showContent(content) {
      this.content = content
      this.formVisibleContent = true
    },
  }
}
</script>

<style scoped>
</style>
