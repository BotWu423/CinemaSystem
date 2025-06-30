<template>
  <div class="add-movie">
    <h2>添加新电影</h2>
    <form @submit.prevent="submitMovie">
      <label>电影名称：<input v-model="movie.title" required /></label><br />
      <label>导演：<input v-model="movie.director" required /></label><br />
      <label>演员：<input v-model="movie.actors" /></label><br />
      <label>类型：<input v-model="movie.type" /></label><br />
      <label>时长（分钟）：<input v-model.number="movie.duration" type="number" /></label><br />
      <label>上映日期：<input v-model="movie.releaseDate" type="date" /></label><br />
      <label>评分：<input v-model.number="movie.rating" type="number" step="0.1" /></label><br />
      <label>简介：<textarea v-model="movie.description"></textarea></label><br />
      <button type="submit">提交</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      movie: {
        title: '',
        director: '',
        actors: '',
        type: '',
        duration: 0,
        releaseDate: '',
        rating: null,
        description: ''
      }
    };
  },
  methods: {
    async submitMovie() {
      const token = localStorage.getItem('token');
      try {
        await axios.post('http://localhost:9000/api/admin/movie-management/add', this.movie, {
          headers: { Authorization: 'Bearer ' + token }
        });
        alert('电影添加成功！');
        this.$router.back(); // 返回上一页或刷新列表
      } catch (error) {
        console.error('添加电影失败:', error);
        alert('添加电影失败，请重试');
      }
    }
  }
};
</script>
