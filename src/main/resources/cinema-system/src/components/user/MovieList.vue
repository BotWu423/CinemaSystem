<template>
  <div class="movie-list">
    <div class="top-Buttons">
      <button @click="goToMovieRanking">电影榜单</button>
      <button @click="goToActivity" style="margin-left: 10px;">近期活动</button>
      <button @click="goToProfile" style="margin-left: 10px;">个人中心</button>
      <button v-if="isAdmin" @click="goToUserManagement" style="margin-left: 10px;">用户管理</button>
      <button v-if="isAdmin" @click="goToOrderManagement" style="margin-left: 10px;">订单管理</button>
      <button v-if="isAdmin" @click="goToWorkLog" style="margin-left: 10px;">每日工作记录</button>
      <button v-if="isAdmin" @click="goToAddMovie" style="margin-left: auto;">添加新电影</button>
    </div>
    <!-- 轮播图 -->
    <div class="carousel-wrapper">
      <MovieCarousel />
    </div>

    <h1>电影列表</h1>
    <div v-if="loading">加载中...</div>
    <div class="movies-container">
      <el-row :gutter="20">
        <el-col
            v-for="movie in movies"
            :key="movie.id"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
            :xl="4"
        >
          <el-card class="movie-card" shadow="hover">
            <div @click="goToMovieDetail(movie.id)" style="cursor: pointer;">
              <img
                  :src="movie.posterUrl || require('@/assets/image/default-poster.jpg')"
                  alt="海报"
                  class="movie-poster"
              />
            </div>
            <div class="movie-info">
              <h3>{{ movie.title }}</h3>
              <p><strong>类型:</strong> {{ movie.type }}</p>
              <p><strong>时长:</strong> {{ movie.duration }} 分钟</p>
              <p><strong>评分:</strong> {{ movie.rating ? movie.rating : '暂无' }}</p>
              <!-- 操作按钮 -->
              <p>
                <button @click="goToCinemas(movie.id)" style="margin-left: 10px;">选择影院</button>
                <button @click="goToMovieDetail(movie.id)" style="margin-left: 10px;">查看详情</button>
              </p>
              <p>
                <button v-if="isAdmin" @click="deleteMovie(movie.id)" style="margin-left: 10px;">删除电影</button>
                <button v-if="isAdmin" @click="goToEditMovie(movie.id)" style="margin-left: 10px;">编辑电影</button>
              </p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
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
    async deleteMovie(movieId) {
      const confirmDelete = confirm("确定要删除这部电影及其所有关联数据吗？");
      if (!confirmDelete) return;

      try {
        const token = localStorage.getItem('token');
        await axios.delete(`http://localhost:9000/api/movies/${movieId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });
        alert("删除成功！");
        this.fetchMovies(); // 刷新电影列表
      } catch (error) {
        console.error('删除失败:', error);
        alert("删除失败，请查看控制台日志。");
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
    goToEditMovie(movieId) {
      this.router.push({
        path: '/movie-management/edit',
        query: { movieId: movieId }
      });
    },
    goToMovieRanking() {
      this.router.push({ path: '/movie-ranking' });
    },
  }
};
</script>

<style scoped>
.carousel-wrapper {
  margin-bottom: 30px;
}
.movie-poster {
  width: 100%;             /* 占满卡片宽度 */
  height: 300px;            /* 固定高度 */
  object-fit: cover;        /* 图片等比缩放并裁剪 */
  border-radius: 8px;       /* 可选圆角 */
  transition: transform 0.3s ease;
}

.movie-poster:hover {
  transform: scale(1.02);   /* 可选悬停放大效果 */
}

</style>