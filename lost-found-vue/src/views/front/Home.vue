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
                        <el-button type="info"  @click="showContent(item.content)">查看详情</el-button>
                        <el-button type="success">联系失主</el-button>
                      </div>
                    </div>
                  </el-col>




                </el-row>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="card">
              <div style="display: flex">
                <div style="flex: 1;color: #725b04;font-size: 18px;font-weight: 550">最新发布招领</div>
                <div style="flex: 1;text-align: right;color: #666666">查看更多</div>
              </div>
              <div style="margin-top: 40px">
                <el-row :gutter="20" style="display: flex">
                  <el-col :span="12">
                    <div class="transition card" style="margin-left: 3px">
                      <div style="display: flex">
                        <img src="@/assets/imgs/logo.png" alt=""
                             style="height: 90px;width: 90px;border: 1px solid #eeeeee;border-radius: 10px">
                        <div style="margin-left: 10px">
                          <div style="font-weight: 550;color:#ef5d26;margin-top: 5px">南瓜头</div>
                          <div style="margin-top: 10px;color: #666666">发布人：张三</div>
                          <div style="margin-top: 10px;color: #666666">时间：2025-4-10</div>
                        </div>
                      </div>
                      <div style="margin-top: 20px">
                        <el-button type="info">查看详情</el-button>
                        <el-button type="success">联系失主</el-button>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="12">
                    <div class="transition card" style="margin-left: 3px">
                      <div style="display: flex">
                        <img src="@/assets/imgs/logo.png" alt=""
                             style="height: 90px;width: 90px;border: 1px solid #eeeeee;border-radius: 10px">
                        <div style="margin-left: 10px">
                          <div style="font-weight: 550;color:#ef5d26;margin-top: 5px">南瓜头</div>
                          <div style="margin-top: 10px;color: #666666">发布人：张三</div>
                          <div style="margin-top: 10px;color: #666666">时间：2025-4-10</div>
                        </div>
                      </div>
                      <div style="margin-top: 20px">
                        <el-button type="info">查看详情</el-button>
                        <el-button type="success">联系失主</el-button>
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
  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      carouselData: [
        // require('@/assets/imgs/carousel1.jpg'),
        require('@/assets/imgs/carousel2.png'),
        require('@/assets/imgs/carousel3.jpeg')
      ],
      lostData: [],
      foundData: [],
      formVisibleContent:false,
      content:''
    }
  },
  mounted() {
    this.getLostData();
  },
  methods: {
    //用弹窗显示内容
    showContent(content) {
      this.content = content
      this.formVisibleContent = true
    },

    getLostData() {
      this.$request.get('/lostInfo/selectNew4').then(res=>{
        if (res.code==='200'){
          this.lostData = res.data;
        }else{
          this.$message.error(res.msg)
        }
      }).catch(res=>res)
    }
  }
}
</script>

<style scoped>
/*设置弹窗位置*/
::v-deep .el-dialog__header{
  margin-top: 300px !important;
}
</style>
