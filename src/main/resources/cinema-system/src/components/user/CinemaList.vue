<template>
  <div class="cinema-list">
    <h1>电影院列表</h1>
    <div v-if="loading">加载中...</div>
    <ul v-else>
      <li v-for="cinema in cinemas" :key="cinema.id" class="cinema-item">
        <div class="info">
          <h3>{{ cinema.name }}</h3>
          <p>地址: {{ cinema.address }}</p>
          <p>联系方式: {{ cinema.contact }}</p>
          <p>描述: {{ cinema.description }}</p>
          <button @click="gotoScreeningList(cinema.id, cinema.name)">查看场次</button>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      cinemas: [],
      loading: true
    };
  },
  mounted() {
    const movieId = this.$route.query.movieId;

    if (movieId) {
      this.fetchCinemas(movieId);
    } else {
      console.error('缺少 movieId 参数');
      this.loading = false;
    }
  },
  methods: {
    async fetchCinemas(movieId) {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`http://localhost:9000/api/cinemas?movieId=${movieId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        this.cinemas = response.data;
        this.loading = false;
      } catch (error) {
        console.error('获取电影院失败:', error.response ? error.response.data : error.message);
        this.loading = false;
      }
    },
    gotoScreeningList(cinemaId, cinemaName) {
      const movieId = this.$route.query.movieId;
      this.$router.push({
        path: '/screenings',
        query: {cinemaId: cinemaId, movieId: movieId, cinemaName: cinemaName}
      });
    }
  }
};
</script>

<style scoped>
.cinema-list {
  padding: 20px;
}

.cinema-item {
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  padding-bottom: 10px;
}
</style>