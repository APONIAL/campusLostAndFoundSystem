<template>
  <div>
    <div style="box-shadow: 0 0 10px rgba(0,0,0,.1);padding: 10px 20px;
          border-radius: 5px;margin-bottom: 10px">
      早安，{{user.name}}，欢迎使用校园失物招领系统
    </div>
      <div style="display: flex;margin: 15px 0">
        <el-card style="width: 50%;margin-right: 10px">
          <div slot="header" class="clearfix" style="font-weight: bold;font-size: 24px"><span>系统公告</span></div>
          <el-timeline style="padding: 0">
            <el-timeline-item v-for="item in notices" :key="item.id" :timestamp="item.time" placement="top">
              <el-card>
                <h4>{{item.title}}</h4>
                <p>{{item.content}}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
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
