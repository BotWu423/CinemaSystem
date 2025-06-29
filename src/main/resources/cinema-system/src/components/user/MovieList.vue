<template>
  <div class="movie-list">
    <h1>电影列表</h1>
    <div v-if="loading">加载中...</div>
    <ul v-else>
      <li v-for="movie in movies" :key="movie.id" class="movie-item">
        <img :src="require('@/assets/image/default-poster.jpg')" alt="海报" width="100" />
        <div class="info">
          <h3>{{ movie.title }}</h3>
          <p>导演: {{ movie.director }}</p>
          <p>演员: {{ movie.actors }}</p>
          <p>类型: {{ movie.type }}</p>
          <p>时长: {{ movie.duration }} 分钟</p>
          <p>评分: {{ movie.rating ? movie.rating : '暂无' }}</p>
          <p>上映日期: {{ movie.releaseDate }}</p>
          <p>简介: {{ movie.description }}</p>
          <!-- 添加跳转按钮 -->
          <button @click="goToCinemas(movie.id)">选择影院</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router'; // 引入 Vue Router

export default {
  setup() {
    const router = useRouter(); // 获取路由实例

    return {
      router
    };
  },
  data() {
    return {
      movies: [],
      loading: true
    };
  },
  mounted() {
    this.fetchMovies();
  },
  methods: {
    async fetchMovies() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:8080/api/movies', {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });
        this.movies = response.data;
        this.loading = false;
      } catch (error) {
        console.error('获取电影失败:', error);
        this.loading = false;
      }
    },
    goToCinemas(movieId) {
      // 跳转到 /cinemas 页面，并携带 movieId 参数
      this.router.push({ path: '/cinemas', query: { movieId: movieId } });
    }
  }
};
</script>

<style scoped>
.movie-list {
  padding: 20px;
}

.movie-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  padding-bottom: 10px;
}

.info {
  margin-left: 20px;
}

button {
  margin-top: 10px;
  padding: 6px 12px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #369d6b;
}
</style>