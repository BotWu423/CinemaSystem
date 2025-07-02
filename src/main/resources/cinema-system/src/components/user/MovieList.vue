<template>
  <div class="movie-list">
    <div class="top-buttons">
      <button @click="goToActivity">近期活动</button>
      <button @click="goToProfile" style="margin-left: 10px;">个人中心</button>
      <button v-if="isAdmin" @click="goToUserManagement" style="margin-left: 10px; background-color: #e67e22;">用户管理</button>
      <button v-if="isAdmin" @click="goToOrderManagement" style="margin-left: 10px; background-color: #e67e22;">订单管理</button>
      <button v-if="isAdmin" @click="goToWorkLog" style="margin-left: 10px; background-color: #f5a623;">每日工作记录</button>
      <button v-if="isAdmin" @click="goToAddMovie" style="margin-left: auto; background-color: #369d6b;">添加新电影</button>
    </div>
    <!-- 轮播图 -->
    <div class="carousel-wrapper">
      <MovieCarousel />
    </div>

    <h1>电影列表</h1>
    <div v-if="loading">加载中...</div>
    <ul v-else>
      <li v-for="movie in movies" :key="movie.id" class="movie-item">
        <img :src="require('@/assets/image/default-poster.jpg')" alt="海报" width="100"/>
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
          <button @click="goToMovieDetail(movie.id)" style="margin-left: 10px;">查看详情</button>
        </div>
      </li>
    </ul>
    <!-- 在 </ul> 结束标签后添加如下代码 -->
    <div class="cinema-list-section">
      <h2>热门影院</h2>
      <div v-if="loadingCinemas">加载中...</div>
      <ul v-else>
        <li v-for="cinema in cinemas" :key="cinema.id" class="cinema-item">
          <img :src="cinema.posterUrl || 'https://img.picui.cn/free/2025/07/01/686387241876a.jpg'" alt="影院图片" width="150" />
          <div class="cinema-info">
            <h4>{{ cinema.name }}</h4>
            <p>地址: {{ cinema.address }}</p>
            <button @click="goToCinemaDetail(cinema.id)">查看详情</button>
          </div>
        </li>
      </ul>
    </div>

  </div>
</template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router'; // 引入 Vue Router
import MovieCarousel from "@/components/common/MovieCarousel.vue";

export default {
  components: {
    MovieCarousel
  },
  setup() {
    const router = useRouter(); // 获取路由实例

    return {
      router
    };
  },
  data() {
    return {
      movies: [],
      loading: true,
      currentUser: null,
      isAdmin: false,
      cinemas: [],
      loadingCinemas: true
    };
  },

  mounted() {
    this.getCurrentUser();
    this.fetchMovies();
    this.fetchCinemas();
  },
  methods: {
    getCurrentUser() {
      const token = localStorage.getItem('token');
      console.log('token:', token);
      if (!token) return;
      try {
        const payload = JSON.parse(atob(token.split('.')[1])); // 解析 JWT payload
        this.currentUser = { id: payload.id, username: payload.sub };
        this.isAdmin = payload.roles && payload.roles.includes('ADMIN');
        console.log('isAdmin:', this.isAdmin);
        console.log('currentUser:', this.currentUser);
        console.log('roles:', payload.roles);
      } catch (e) {
        console.error('解析 token 失败:', e);
      }
    },
    async fetchMovies() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:9000/api/movies', {
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
    async fetchCinemas() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:9000/api/cinemas/all', {
          headers: { Authorization: 'Bearer ' + token }
        });
        this.cinemas = response.data;
      } catch (error) {
        console.error('获取影院失败:', error);
      } finally {
        this.loadingCinemas = false;
      }
    },
    goToCinemas(movieId) {
      // 跳转到 /cinemas 页面，并携带 movieId 参数
      this.router.push({ path: '/cinemas', query: { movieId: movieId } });
    },
    goToMovieDetail(movieId) {
      // 跳转到 /movie-detail 页面，并携带 movieId 参数
      this.router.push({ path: '/movie-detail', query: { movieId: movieId } });
    },
    goToActivity() {
      this.router.push({ path: '/activity' });
    },
    goToProfile() {
      this.router.push({ path: '/profile' });
    },
    goToUserManagement() {
      this.router.push({ path: '/user-management' });
    },
    goToAddMovie() {
      this.router.push({ path: '/movie-management/add' });
    },
    goToWorkLog() {
      this.router.push({ path: '/work-log' });
    },
    goToOrderManagement() {
      this.router.push({ path: '/order-management' });
    },
    goToCinemaDetail(cinemaId) {
      this.router.push({
        path: '/cinema-detail',
        query: { cinemaId: cinemaId }
      });
    },
  }
};
</script>

<style scoped>
.carousel-wrapper {
  margin-bottom: 30px;
}
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

.top-buttons {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.top-buttons button {
  padding: 6px 12px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.top-buttons button:hover {
  background-color: #369d6b;
}
.cinema-list-section {
  margin-top: 40px;
}

.cinema-item {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #eee;
  padding: 10px 0;
}

.cinema-item img {
  margin-right: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.cinema-info {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.cinema-info h4 {
  margin: 0;
}

</style>