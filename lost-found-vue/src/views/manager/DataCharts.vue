<template>
  <div>
    <el-row :gutter="10" style="margin: 10px 0">
      <el-col :span="15">
        <el-card>
          <div class="chart-container">
            <h3 style="font-size: 20px;margin-bottom: 10px;text-align: center">近一周用户互动与找回成功率</h3>
            <div ref="chart" class="chart"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="9">
        <el-card>
          <div id="pie" style="width: 100%;height: 575px"></div>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";

const option2 = {
  title: {
    text: '已发布招领种类统计图',
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
      name: '个数',
      type: 'pie',
      //设置水平与垂直居中
      center: ['60%', '60%'],
      radius: '40%',
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
  name: "DataCharts",
  data(){
    return {
      interactionRate: [], // 用户互动率数据
      successRate: [],     // 找回成功率数据
    }
  },
  mounted() {

    let pieDom = document.getElementById('pie');
    let pieChart = echarts.init(pieDom);
    this.$request.get('/analysis/getPieByFoundCategory').then(res => {
      //饼图
      option2.series[0].data = res.data || []
      pieChart.setOption(option2)
    })
    this.fetchData();
  },
    methods: {
      fetchData() {
        // 模拟从后端获取数据
        const mockData = {
          interactionData: [
            { date: "2025-04-13", post: 24, claim: 15, comment: 15 },
            { date: "2025-04-14", post: 25, claim: 20, comment: 15 },
            { date: "2025-04-15", post: 30, claim: 25, comment: 15 },
            { date: "2025-04-16", post: 35, claim: 30, comment: 10 },
            { date: "2025-04-17", post: 40, claim: 35, comment: 25 },
            { date: "2025-04-18", post: 42, claim: 30, comment: 30 },
            { date: "2025-04-19", post: 35, claim: 35, comment: 20 },
          ],
          successRate: [
            { date: "2025-04-13", rate: 0.6 },
            { date: "2025-04-14", rate: 0.65 },
            { date: "2025-04-15", rate: 0.7 },
            { date: "2025-04-16", rate: 0.51 },
            { date: "2025-04-17", rate: 0.66 },
            { date: "2025-04-18", rate: 0.73 },
            { date: "2025-04-19", rate: 0.59 },
          ],
        };

        // 将数据赋值给组件状态
        this.interactionData = mockData.interactionData;
        this.successRate = mockData.successRate;

        // 渲染图表
        this.renderChart();
      },
      renderChart() {
        if (!this.$refs.chart) {
          console.error("Chart DOM element not found!");
          return;
        }

        const chartDom = this.$refs.chart;
        const myChart = echarts.init(chartDom);

        // 提取日期、互动数据和成功率数据
        const dates = this.interactionData.map((item) => item.date);
        const posts = this.interactionData.map((item) => item.post);
        const claims = this.interactionData.map((item) => item.claim);
        const comments = this.interactionData.map((item) => item.comment);
        const successRates = this.successRate.map((item) => (item.rate * 100).toFixed(2));

        // 配置 ECharts 图表
        const option = {
          backgroundColor: "#1e1e2f", // 设置背景颜色
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "cross",
            },
            formatter: function (params) {
              let result = `<div style="color: #fff;">${params[0].name}</div>`;
              params.forEach((item) => {
                result += `<div style="color: ${item.color};">${item.seriesName}: ${item.value}</div>`;
              });
              return result;
            },
          },
          legend: {
            data: ["发布失物信息", "认领物品", "评论", "找回成功率"],
            textStyle: {
              color: "#fff",
            },
          },
          xAxis: {
            type: "category",
            data: dates,
            axisLine: { lineStyle: { color: "#8392a5" } },
            axisLabel: { color: "#fff" },
          },
          yAxis: [
            {
              type: "value",
              name: "互动次数",
              min: 0,
              max: Math.max(...posts) + Math.max(...claims) + Math.max(...comments) + 10,
              axisLine: { lineStyle: { color: "#8392a5" } },
              axisLabel: { color: "#fff" },
              splitLine: { lineStyle: { color: "#3e4b61" } },
            },
            {
              type: "value",
              name: "成功率 (%)",
              min: 0,
              max: 100,
              axisLine: { lineStyle: { color: "#8392a5" } },
              axisLabel: { color: "#fff" },
              splitLine: { lineStyle: { color: "#3e4b61" } },
            },
          ],
          series: [
            {
              name: "发布失物信息",
              type: "bar",
              stack: "总量",
              data: posts,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: "#ff7f50" },
                  { offset: 1, color: "#ff4500" },
                ]),
              },
            },
            {
              name: "认领物品",
              type: "bar",
              stack: "总量",
              data: claims,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: "#87cefa" },
                  { offset: 1, color: "#1e90ff" },
                ]),
              },
            },
            {
              name: "评论",
              type: "bar",
              stack: "总量",
              data: comments,
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: "#98fb98" },
                  { offset: 1, color: "#32cd32" },
                ]),
              },
            },
            {
              name: "找回成功率",
              type: "line",
              data: successRates,
              yAxisIndex: 1,
              symbol: "circle",
              symbolSize: 8,
              lineStyle: {
                width: 3,
                color: "#ffd700",
              },
              itemStyle: {
                color: "#ffd700",
                borderColor: "#ffffff",
                borderWidth: 2,
              },
            },
          ],
        };

        // 显示加载动画
        myChart.showLoading();

        // 模拟数据加载延迟
        setTimeout(() => {
          myChart.hideLoading(); // 隐藏加载动画
          myChart.setOption(option); // 设置图表选项
        }, 1000);
      },
    },

}
</script>

<style scoped>
.chart-container {
  padding: 20px;
}

.chart {
  width: 100%;
  height: 500px; /* 确保高度足够 */
}
</style>
