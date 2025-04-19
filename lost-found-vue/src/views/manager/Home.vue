<template>
  <div>
    <div style="box-shadow: 0 0 10px rgba(0,0,0,.1);padding: 10px 20px;
          border-radius: 5px;margin-bottom: 10px">
      早安，{{user.name}}，欢迎使用校园失物招领系统
    </div>
      <div style="display: flex;margin: 15px 0">
        <el-card style="width: 35%;margin-right: 10px">
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
        <el-card style="width: 60%;margin-left: auto">
          <div id="main" style="height: 550px;width: 100%"></div>
        </el-card>
      </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
const cellSize = [80, 80];
const pieRadius = 30;
function getVirtualData() {
  const date = +echarts.time.parse('2025-04-01');
  const end = +echarts.time.parse('2025-05-01');
  const dayTime = 3600 * 24 * 1000;
  const data = [];
  for (let time = date; time < end; time += dayTime) {
    data.push([
      echarts.time.format(time, '{yyyy}-{MM}-{dd}', false),
      Math.floor(Math.random() * 10000)
    ]);
  }
  return data;
}
const scatterData = getVirtualData();
const pieSeries = scatterData.map(function (item, index) {
  return {
    type: 'pie',
    id: 'pie-' + index,
    center: item[0],
    radius: pieRadius,
    coordinateSystem: 'calendar',
    label: {
      formatter: '{c}',
      position: 'inside'
    },
    data: [
      { name: '发布失物次数', value: Math.round(Math.random() * 24) },
      { name: '发布招领次数', value: Math.round(Math.random() * 24) },
      { name: '完成招领次数', value: Math.round(Math.random() * 24) }
    ]
  };
});
const option = {
  title: {
    text: '本月失物招领数据统计',
    left: 'center',
    top: 5,
    textStyle: {
      fontSize: 20,
      fontWeight: 'bold'
    }
  },
  tooltip: {},
  legend: {
    data: ['发布失物次数', '发布招领次数', '完成招领次数'],
    bottom: 5,
    textStyle: {
      fontSize: 14,
      fontWeight: 'bold'
    }
  },
  calendar: {
    top: 'middle',
    left: 'center',
    orient: 'vertical',
    cellSize: cellSize,
    yearLabel: {
      show: false,
      fontSize: 30
    },
    dayLabel: {
      margin: 20,
      firstDay: 1,
      nameMap: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    monthLabel: {
      show: false
    },
    range: ['2025-04']
  },
  series: [
    {
      id: 'label',
      type: 'scatter',
      coordinateSystem: 'calendar',
      symbolSize: 0,
      label: {
        show: true,
        formatter: function (params) {
          return echarts.time.format(params.value[0], '{dd}', false);
        },
        offset: [-cellSize[0] / 2 + 10, -cellSize[1] / 2 + 10],
        fontSize: 14
      },
      data: scatterData
    },
    ...pieSeries
  ]
};
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
    let chartDom = document.getElementById('main');
    let myChart = echarts.init(chartDom);
    myChart.setOption(option);
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
