<template>
  <div class="main-content">
    <!--轮播图-->
    <div style="width: 100%;height: 300px;margin: 0 auto">
      <el-carousel height="280px">
        <el-carousel-item v-for="item in carouselData" :key="item">
          <img :src="item" alt="" style="width: 100%;height: 300px">
        </el-carousel-item>
      </el-carousel>
      <div>
        <el-row :gutter="20">
          <!--一共24份，一人一半-->
          <el-col :span="12">
            <div class="card">
              <div style="display: flex">
                <div style="flex: 1;color: #725b04;font-size: 18px;font-weight: 550">最新发布失物</div>
                <div style="flex: 1;text-align: right;color: #666666">查看更多</div>
              </div>
              <div style="margin-top: 40px">
                <el-row :gutter="20">
                  <el-col :span="12" v-for="item in lostData" style="margin-bottom: 20px" :key="item.id">
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
                        <el-button type="success" @click="handleAddContact(item.userId)">联系失主</el-button>
                      </div>
                    </div>
                  </el-col>


                </el-row>
              </div>
            </div>
          </el-col>


          <!--右侧招领-->
          <el-col :span="12">
            <div class="card">
              <div style="display: flex">
                <div style="flex: 1;color: #725b04;font-size: 18px;font-weight: 550">最新发布招领</div>
                <div style="flex: 1;text-align: right;color: #666666">查看更多</div>
              </div>
              <div style="margin-top: 40px">
                <el-row :gutter="20">
                  <el-col :span="12" v-for="item in foundData" style="margin-bottom: 20px" :key="item.id">
                    <div class="transition card" st0yle="margin-left: 3px">
                      <div style="display: flex">
                        <img :src="item.img" alt=""
                             style="height: 90px;width: 90px;border: 1px solid #eeeeee;border-radius: 10px">
                        <div style="margin-left: 10px">
                          <div style="font-weight: 550;color:#ef5d26;margin-top: 5px">{{ item.name }}</div>
                          <div style="margin-top: 10px;color: #666666">发布人：{{ item.founderName }}</div>
                          <div style="margin-top: 10px;color: #666666">时间：{{ item.foundTime }}</div>
                        </div>
                      </div>
                      <div style="margin-top: 20px">
                        <el-button type="info" @click="showContent(item.content)">查看详情</el-button>
                        <el-button type="success" @click="handleAddContact(item.userId)">联系失主</el-button>
                      </div>
                    </div>
                  </el-col>


                </el-row>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!--弹窗显示富文本内容-->
      <el-dialog title="内容" :visible.sync="formVisibleContent" width="60%">
        <div class="w-e-text">
          <div v-html="content"></div>
        </div>
      </el-dialog>
    </div>
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
  name: "Home",
  data() {
    return {
      carouselData:[
        require('@/assets/imgs/carousel2.png'),
        require('@/assets/imgs/carousel3.jpeg')
      ],
      lostData: [],
      foundData: [],
      formVisibleContent: false,
      content: '',
      formVisibleContact: false,
      contactForm: ''
    }
  },
  mounted() {
    this.getLostData()
    this.getFoundData()
  },
  methods: {
    //用弹窗显示内容
    showContent(content) {
      this.content = content
      this.formVisibleContent = true
    },
    handleAddContact(contactedId) {
      //打开新增窗口前，清空上次残留数据
      this.contactForm = {}
      this.contactForm.contactedId = contactedId
      this.formVisibleContact = true
    },

    saveContact() {
      this.$request.post('/lost-contact/save', this.contactForm).then(res => {
        if (res.code === '200') {
          this.$message.success('保存成功')
          this.formVisibleContact = false
          this.getData(1)
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(res => {
        console.log(res.message)
      })
    },

    getLostData() {
      this.$request.get('/lostInfo/selectNew4').then(res => {
        if (res.code === '200') {
          this.lostData = res.data;
        } else {
          this.$message.error(res.msg)
        }
      }).catch(res => res)
    },
    getFoundData() {
      this.$request.get('/found-info/selectNew4').then(res => {
        if (res.code === '200') {
          this.foundData = res.data;
        } else {
          this.$message.error(res.msg)
        }
      }).catch(res => res)
    }
  }
}
</script>

<style scoped>
/*设置弹窗位置*/
::v-deep .el-dialog__header {
  margin-top: 300px !important;
}
</style>
