<template>
  <div>
    <h2>电影榜单</h2>
    <h2 style="margin-top: 40px;">票房榜（柱状图）</h2>
    <div ref="barChart" style="width: 100%; height: 400px;"></div>
    <h2 style="margin-top: 40px;">热度榜（折线图）</h2>
    <div ref="lineChart" style="width: 100%; height: 400px;"></div>
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