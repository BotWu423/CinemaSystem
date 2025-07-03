<template>
  <div class="add-movie">
    <h2>编辑场次</h2>
    <form @submit.prevent="submitForm">
      <label>
        电影:
        <select v-model.number="screening.movieId" required>
          <option value="">请选择电影</option>
          <option v-for="movie in movies" :key="movie.id" :value="movie.id">
            {{ movie.title }}
          </option>
        </select>
      </label>

      <label>
        放映厅:
        <select v-model.number="screening.roomId" required>
          <option value="">请选择放映厅</option>
          <option v-for="room in screeningRooms" :key="room.id" :value="room.id">
            [{{ room.cinemaName }}] {{ room.name }}
          </option>
        </select>
      </label>

      <label>
        开始时间:
        <input v-model="screening.startTime" type="datetime-local" required />
      </label>

      <label>
        票价:
        <input v-model.number="screening.price" type="number" step="0.01" required />
      </label>

      <div class="form-actions">
        <button type="submit">保存更改</button>
        <button type="button" @click="$router.back()" style="margin-left: 10px;">取消</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRouter, useRoute } from 'vue-router';

const router = useRouter();
const route = useRoute();

const screening = ref({
  id: null,
  movieId: null,
  roomId: null,
  startTime: '',
  price: 0
});
const movies = ref([]);
const screeningRooms = ref([]);

// 获取当前场次详情
const fetchScreeningDetails = async () => {
  const screeningId = route.query.screeningId;
  try {
    const token = localStorage.getItem('token');
    const res = await axios.get(`http://localhost:9000/api/screenings/${screeningId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    const data = res.data;
    screening.value = {
      id: data.id,
      movieId: data.movie.id,
      roomId: data.screeningRoom.id,
      startTime: new Date(data.startTime).toISOString().slice(0, 16),
      price: data.price
    };
  } catch (error) {
    console.error('获取场次详情失败:', error);
    alert('获取场次详情失败');
    router.back();
  }
};

// 获取所有电影用于选择
const fetchAllMovies = async () => {
  try {
    const token = localStorage.getItem('token');
    const res = await axios.get('http://localhost:9000/api/movies', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    movies.value = res.data;
  } catch (error) {
    console.error('获取电影失败:', error);
  }
};

// 获取所有放映厅用于选择
const fetchAllScreeningRooms = async () => {
  try {
    const token = localStorage.getItem('token');
    const res = await axios.get('http://localhost:9000/api/screening-rooms/all', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    screeningRooms.value = res.data;
  } catch (error) {
    console.error('获取放映厅失败:', error);
  }
};

// 提交更新
const submitForm = async () => {
  try {
    const token = localStorage.getItem('token');
    await axios.put('http://localhost:9000/api/screenings', screening.value, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    alert('场次信息更新成功');
    router.back();
  } catch (error) {
    console.error('更新场次失败:', error);
    alert('更新失败，请重试');
  }
};

onMounted(() => {
  fetchAllMovies();
  fetchAllScreeningRooms();
  fetchScreeningDetails();
});
</script>

<style scoped>

</style>
