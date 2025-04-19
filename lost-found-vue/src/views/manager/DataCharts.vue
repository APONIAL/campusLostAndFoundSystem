<template>
  <div>
    <div id="heatmapChart" style="width: 600px; height: 400px;"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
const hours = ['12a', '1a', '2a', '3a', '4a', '5a', '6a', '7a', '8a', '9a', '10a', '11a', '12p', '1p', '2p', '3p', '4p', '5p', '6p', '7p', '8p', '9p', '10p', '11p'];
const days = ['周六', '周五', '周四', '周三', '周二', '周一', '周日'];
const data = [[0, 0, 5], [0, 1, 1], [0, 2, 0], [0, 3, 0], [0, 4, 0], [0, 5, 0], [0, 6, 0]];
const option = {
  title: { text: '丢失物品热力图' },
  tooltip: { position: 'top' },
  grid: { height: '50%', top: '10%' },
  xAxis: { type: 'category', data: hours },
  yAxis: { type: 'category', data: days },
  visualMap: { min: 0, max: 10, calculable: true, orient: 'horizontal', left: 'center' },
  series: [{
    name: '丢失频率',
    type: 'heatmap',
    data: data,
    label: { show: true },
    emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0, 0, 0, 0.5)' } }
  }]
};
export default {
  name: "DataCharts",
  data(){
    return {

    }
  },
  mounted() {
    let chartDom = document.getElementById('heatmapChart');
    let myChart = echarts.init(chartDom);
    myChart.setOption(option);
  }

}
</script>

<style scoped>

</style>
