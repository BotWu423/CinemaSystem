<script setup>

</script>

<template>
  <div class="cinema-detail">
    <h1>{{ cinema.name }}</h1>

    <div class="cinema-info">
      <div class="poster-section">
        <img :src="cinema.posterUrl || 'https://img.picui.cn/free/2025/07/01/686387241876a.jpg'"
             alt="影院海报"
             class="cinema-poster">
      </div>

      <div class="basic-info">
        <h2>基本信息</h2>
        <p><strong>地址:</strong> {{ cinema.address }}</p>
        <p><strong>联系方式:</strong> {{ cinema.contact }}</p>
        <p><strong>简介:</strong> {{ cinema.description }}</p>
      </div>
    </div>

    <div class="movies-section">
      <h2>当前热映</h2>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error">{{ error }}</div>
      <div v-else>
        <div v-if="movies.length === 0">暂无上映电影</div>
        <ul class="movie-list">
          <li v-for="movie in movies" :key="movie.id" class="movie-item">
            <div class="movie-poster">
              <img :src="movie.posterUrl" :alt="movie.title + ' 海报'">
            </div>
            <div class="movie-details">
              <h3>{{ movie.title }}</h3>
              <p><strong>导演:</strong> {{ movie.director }}</p>
              <p><strong>演员:</strong> {{ movie.actors }}</p>
              <p><strong>类型:</strong> {{ movie.type }}</p>
              <p><strong>时长:</strong> {{ movie.duration }} 分钟</p>
              <p><strong>评分:</strong> {{ movie.rating ? movie.rating : '暂无' }}</p>
              <p><strong>上映日期:</strong> {{ movie.releaseDate }}</p>
              <p><strong>简介:</strong> {{ movie.description }}</p>
              <button @click="goToScreenings(movie.id)">查看场次</button>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>
<script>import axios from 'axios';
export default {
  data() {
    return {
      cinema: {
        name: '',
        address: '',
        contact: '',
        description: '',
        posterUrl: ''
      },
      movies: [],
      loading: true,
      error: null
    };
  },
  created() {
    this.fetchCinemaDetails();
    this.fetchCurrentMovies();
  },
  methods: {
    async fetchCinemaDetails() {
      try {
        const cinemaId = this.$route.query.cinemaId;
        const token = localStorage.getItem('token');

        const response = await axios.get(`http://localhost:9000/api/cinemas/${cinemaId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        this.cinema = response.data;
      } catch (error) {
        console.error('获取影院详情失败:', error);
        this.error = '无法加载影院详情';
      }
    },

    async fetchCurrentMovies() {
      try {
        const cinemaId = this.$route.query.cinemaId;
        const token = localStorage.getItem('token');

        // 获取该影院正在上映的电影列表
        const response = await axios.get(`http://localhost:9000/api/movies?cinemaId=${cinemaId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        this.movies = response.data;
        this.loading = false;
      } catch (error) {
        console.error('获取电影列表失败:', error);
        this.error = '无法加载电影信息';
        this.loading = false;
      }
    },

    goToScreenings(movieId) {
      this.$router.push({
        path: '/screenings',
        query: {
          cinemaId: this.$route.query.cinemaId,
          movieId: movieId,
          cinemaName: this.cinema.name
        }
      });
    }
  }
};
</script>
<style scoped>.cinema-detail {
  padding: 20px;
}

.cinema-info {
  display: flex;
  margin-bottom: 30px;
}

.poster-section {
  flex: 1;
  max-width: 300px;
  margin-right: 30px;
}

.cinema-poster {
  width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.basic-info {
  flex: 2;
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
}

.basic-info h2 {
  margin-top: 0;
}

.movie-list {
  list-style: none;
  padding: 0;
}

.movie-item {
  display: flex;
  border-bottom: 1px solid #eee;
  padding: 20px 0;
  margin-bottom: 20px;
}

.movie-poster {
  width: 150px;
  margin-right: 20px;
}

.movie-poster img {
  width: 100%;
  height: auto;
  border-radius: 4px;
}

.movie-details {
  flex: 1;
}
</style>