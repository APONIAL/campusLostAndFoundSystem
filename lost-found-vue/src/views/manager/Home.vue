<template>
  <div>
    <div style="box-shadow: 0 0 10px rgba(0,0,0,.1);padding: 10px 20px;
          border-radius: 5px;margin-bottom: 10px">
      早安，{{user.name}}，欢迎使用校园失物招领系统
    </div>
    <div style="display: flex">
      <el-card style="width: 40%">
        <div slot="header" class="clearfix"><span>系统公告</span></div>
        <div>
          2025毕设
          <div style="margin-top: 20px">
            <div style="margin: 10px 0"><strong>主题色</strong></div>
            <el-button type="primary">按钮</el-button>
            <el-button type="success">按钮</el-button>
            <el-button type="warning">按钮</el-button>
            <el-button type="danger">按钮</el-button>
            <el-button type="info">按钮</el-button>
          </div>
        </div>
      </el-card>
    </div>
      <div style="display: flex;margin: 15px 0">
        <el-card style="width: 50%;margin-right: 10px">
          <div style="margin-bottom: 15px;font-size: 20px;font-weight: bold">系统公告</div>
          <el-timeline style="padding: 0">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time" placement="top">
              <el-card>
                <h4>{{item.title}}</h4>
                <p>{{item.content}}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>
        <el-card style="width: 50%">
          <div style="margin-bottom: 15px;font-size: 20px;font-weight: bold">系统公告</div>
          <el-collapse v-model="activeName" accordion>
            <el-collapse-item v-for="(item,index) in notices "
                              :key="item.id" :title="item.title" :name="index+''">
              <template slot="title">
                <h4>{{item.title}}</h4>
              </template>
              <div>{{item.content}}</div>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data(){
    return{
      user:JSON.parse(localStorage.getItem('user')||'{}'),
      notices:[],
      activeName:'0'
    }
  },
  mounted(){
    this.loadNotice()
  },
  methods:{
    loadNotice(){
      this.$request.get('/notice/selectOpenNotice').then(res=>{
        this.notices = res.data
      })

    }

  }

}
</script>

<style scoped>

</style>
