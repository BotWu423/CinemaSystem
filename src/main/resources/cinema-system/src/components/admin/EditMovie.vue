<template>
  <div class="add-movie">
    <h2>编辑电影</h2>
    <div v-if="loading">加载中...</div>
    <form v-else @submit.prevent="submitForm">
      <div class="form-group">
        <label for="title">电影名称:</label>
        <input type="text" id="title" v-model="movie.title" required>
      </div>

      <div class="form-group">
        <label for="director">导演:</label>
        <input type="text" id="director" v-model="movie.director">
      </div>

      <div class="form-group">
        <label for="actors">演员:</label>
        <input type="text" id="actors" v-model="movie.actors">
      </div>

      <div class="form-group">
        <label for="type">类型:</label>
        <input type="text" id="type" v-model="movie.type">
      </div>

      <div class="form-group">
        <label for="duration">时长(分钟):</label>
        <input type="number" id="duration"  v-model.number="movie.duration" min="0">
      </div>

      <div class="form-group">
        <label for="rating">评分:</label>
        <input type="number" id="rating" v-model.number="movie.rating" step="0.1" min="0" max="10">
      </div>

      <div class="form-group">
        <label for="releaseDate">上映日期:</label>
        <input type="date" id="releaseDate" v-model="movie.releaseDate">
      </div>

      <div class="form-group">
        <label for="description">简介:</label>
        <textarea id="description" v-model="movie.description"></textarea>
      </div>

      <div class="form-group">
        <label for="posterUrl">海报链接:</label>
        <input type="url" id="posterUrl" v-model="movie.posterUrl">
      </div>

      <div class="form-actions">
        <button type="submit">保存更改</button>
        <button type="button" @click="$router.back()" style="margin-left: 10px;">取消</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const router = useRouter();
    return { router };
  },
  data() {
    return {
      loading: true,
      movie: {
        id: null,
        title: '',
        director: '',
        actors: '',
        type: '',
        duration: null,
        rating: null,
        releaseDate: '',
        description: '',
        posterUrl: ''
      }
    };
  },
  async mounted() {
    this.loading = true;
    try {
      const movieId = this.$route.query.movieId;
      const token = localStorage.getItem('token');

      const response = await axios.get(`http://localhost:9000/api/movies/${movieId}`, {
        headers: {
          'Authorization': 'Bearer ' + token
        }
      });

      this.movie = response.data;
    } catch (error) {
      console.error('获取电影详情失败:', error);
      alert('获取电影详情失败');
    } finally {
      this.loading = false;
    }
  },
  methods: {
    async submitForm() {
      try {
        const token = localStorage.getItem('token');
        await axios.put('http://localhost:9000/api/movies', this.movie, {
          headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
          }
        });

        alert('电影信息更新成功');
        this.router.back();
      } catch (error) {
        console.error('更新电影失败:', error);
        alert('更新电影失败，请查看控制台日志');
      }
    }
  }
};
</script>

<style scoped>

</style>
