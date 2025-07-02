<template>
  <div class="screening-list">
    <h1>{{ cinemaName }} 的场次信息</h1>
    <div v-if="loading">加载中...</div>
    <div v-else-if="error" style="color: red;">{{ error }}</div>
    <ul v-else>
      <li v-for="showtime in showtimes" :key="showtime.id" class="showtime-item">
        <h4>放映厅: {{ showtime.screeningRoom.name }}</h4>
        <p>开始时间: {{ formatTime(showtime.startTime) }}</p>
        <p>票价: ¥{{ showtime.price }}</p>
        <button @click="bookSeats(showtime.id, showtime.screeningRoom.id)">选座购票</button>
      </li>
    </ul>
    <div v-if="isAdmin" class="admin-actions">
      <button @click="goToAddScreening">添加场次</button>
      <button @click="goToAddScreeningRoom">添加放映室</button>
    </div>
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
      showtimes: [],
      cinemaName: '',
      loading: true,
      error: null,
      isAdmin: false
    };
  },
  mounted() {
    this.getCurrentUser();
    const cinemaId = this.$route.query.cinemaId;
    this.cinemaName = this.$route.query.cinemaName;
    const movieId = this.$route.query.movieId;

    if (cinemaId && movieId) {
      this.fetchShowtimes(cinemaId, movieId);
    } else {
      this.error = '缺少必要参数';
      this.loading = false;
    }
  },
  methods: {
    getCurrentUser() {
      const token = localStorage.getItem('token');
      if (!token) return;
      try {
        const payload = JSON.parse(atob(token.split('.')[1])); // 解析 JWT payload
        this.isAdmin = payload.roles && payload.roles.includes('ADMIN');
      } catch (e) {
        console.error('解析 token 失败:', e);
      }
    },
    async fetchShowtimes(cinemaId, movieId) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`http://localhost:9000/api/screenings?cinemaId=${cinemaId}&movieId=${movieId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        this.showtimes = response.data;
        this.loading = false;
      } catch (error) {
        console.error('获取场次失败:', error);
        this.error = '无法加载场次信息，请稍后再试';
        this.loading = false;
      }
    },
    formatTime(dateTime) {
      return new Date(dateTime).toLocaleString();
    },
    bookSeats(screeningId, screeningRoomId) {
      this.router.push({ path: '/seats', query: { screeningId, screeningRoomId } });
    },
    goToAddScreening() {
      this.$router.push({
        path: '/admin/screening-schedule'
      });
    },
    goToAddScreeningRoom() {
      this.$router.push({
        path: '/admin/RoomManagement'
      });
    }
  }

};
</script>

<style scoped>
.showtime-list {
  padding: 20px;
}

.showtime-item {
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  padding-bottom: 10px;
}
</style>