<template>
  <div>
    <!--Column 1 和 Column 2 之间的水平间距为 10px。-->
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card>
          <div id="line" style="width: 100%;height: 400px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div id="bar" style="width: 100%;height: 400px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" style="margin: 10px 0">
      <el-col :span="12">
        <el-card>
          <div id="pie" style="width: 100%;height: 400px"></div>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

const option = {
  //设置表单title
  title: {
    text: '订单金额的统计表',
    left: 'center'
  },
  //提示组件
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [

    {
      name: '订单金额',
      data: [],
      type: 'line',
      smooth: true
    },

  ]
}

const option1 = {
  //设置表单title
  title: {
    text: '订单金额的柱状图',
    left: 'center'
  },
  //提示组件
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '金额',
      data: [],
      type: 'bar',
      smooth: true
    },
    {
      name: '库存',
      data: [],
      type: 'line',
      smooth: true
    }
  ]
};
const option2 = {
  title: {
    text: '订单销售统计',
    //二级标题
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '金额',
      type: 'pie',
      //设置水平与垂直居中
      center: ['50%', '60%'],
      radius: '50%',
      data: [
      ],
      label: {
        formatter: '{b}: {c} ({d}%)'
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};
export default {
  name: "Charts",
  data() {
    return {}
  },
  //等待页面的元素全部加载完毕再初始化
  mounted() {
    let lineDom = document.getElementById('line');
    let linChart = echarts.init(lineDom);
    let barDom = document.getElementById('bar');
    let barChart = echarts.init(barDom);
    let pieDom = document.getElementById('pie');
    let pieChart = echarts.init(pieDom);

    this.$request.get('/web/charts').then(res => {
      //折线
      option.xAxis.data = res.data?.line?.map(v => v.date) || []
      option.series[0].data = res.data?.line?.map(v => v.value) || []
      linChart.setOption(option)
      //柱状图
      option1.xAxis.data = res.data?.bar?.map(v => v.name) || []
      option1.series[0].data = res.data?.bar?.map(v => v.value) || []
      option1.series[1].data = res.data?.line?.map(v => v.value) || []
      barChart.setOption(option1)
      //饼图
      option2.series[0].data = res.data?.bar || []
      pieChart.setOption(option2)
    })
  }
}
</script>

<style scoped>

</style>
