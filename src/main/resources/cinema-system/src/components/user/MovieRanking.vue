<template>
  <div class="add-movie">
    <h2>电影榜单</h2>

    <!-- 票房榜 -->
    <div class="chart-container">
      <h3>票房榜（柱状图）</h3>
      <div ref="barChart" class="echarts-container"></div>
    </div>

    <!-- 热度榜 -->
    <div class="chart-container">
      <h3>热度榜（折线图）</h3>
      <div ref="lineChart" class="echarts-container"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import axios from 'axios';

export default {
  name: 'MovieRanking',
  data() {
    return {
      movies: []
    };
  },
  async mounted() {
    await this.fetchMovies();
  },
  methods: {
    async fetchMovies() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:9000/api/movies', {
          headers: { Authorization: 'Bearer ' + token }
        });
        this.movies = response.data;
        this.initBarChart();
        this.initLineChart();
      } catch (e) {
        this.movies = [];
      }
    },
    initBarChart() {
      const chartDom = this.$refs.barChart;
      const myChart = echarts.init(chartDom);
      const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: {
          left: '7%',
          right: '7%',
          bottom: '15%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.movies.map(m => m.title),
          axisTick: { alignWithLabel: true },
          axisLabel: {
            interval: 0,
            rotate: 45,
            margin: 8,
            fontSize: 12
          },
          axisPointer: {
            type: 'shadow'
          }
        },
        yAxis: { type: 'value', name: '票房' },
        dataZoom: [
          {
            type: 'slider',
            show: true,
            xAxisIndex: [0],
            start: 0,
            end: 100
          }
        ],
        series: [
          {
            name: '票房',
            type: 'bar',
            barWidth: '60%',
            data: this.movies.map(m => m.boxOffice)
          }
        ]
      };
      myChart.setOption(option);
    },
    initLineChart() {
      const chartDom = this.$refs.lineChart;
      const myChart = echarts.init(chartDom);
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '7%',
          right: '7%',
          bottom: '20%', // 增加底部间距
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.movies.map(m => m.title),
          axisLabel: {
            interval: 0, // 显示所有标签
            rotate: 45, // 标签倾斜角度
            margin: 8,
            fontSize: 12
          }
        },
        yAxis: {
          type: 'value',
          name: '热度'
        },
        series: [
          {
            name: '热度',
            data: this.movies.map(m => m.popularity),
            type: 'line',
            smooth: true
          }
        ],
        // 添加滑动条配置
        dataZoom: [
          {
            type: 'slider', // 滑动条类型
            show: true, // 显示滑动条，默认不显示
            xAxisIndex: [0], // 应用于x轴
            start: 0, // 默认显示区域起点百分比
            end: 50, // 默认显示区域终点百分比
            height: 30 // 设置滑动条的高度
          },
          {
            type: 'inside', // 支持鼠标滚轮和触控板缩放和平移
            xAxisIndex: [0],
            start: 0,
            end: 50
          }
        ]
      };
      myChart.setOption(option);
    }
  }
};
</script>
<style scoped>
.add-movie {
  padding: 20px;
}

.chart-container {
  margin-top: 40px;
  background-color: #2e2e2e;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
}

.echarts-container {
  width: 100%;
  height: 400px;
  background-color: #1e1e1e;
  border-radius: 6px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
</style>
