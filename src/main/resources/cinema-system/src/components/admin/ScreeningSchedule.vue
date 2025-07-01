<template>
  <div class="screening-schedule">
    <h2>添加新场次</h2>
    <!-- 添加场次表单 -->
    <form @submit.prevent="addScreening" class="add-form">
      <label>
        电影:
        <select v-model.number="newScreening.movieId" required>
          <option value="">请选择电影</option>
          <option v-for="movie in movies" :key="movie.id" :value="movie.id">
            {{ movie.title }}
          </option>
        </select>
      </label>

      <label>
        放映厅:
        <select v-model.number="newScreening.roomId" required>
          <option value="">请选择放映厅</option>
          <option v-for="room in screeningRooms" :key="room.id" :value="room.id">
            [{{room.cinemaName}}] {{ room.name }}
          </option>
        </select>
      </label>


      <label>
        开始时间:
        <input v-model="newScreening.startTime" type="datetime-local" required />
      </label>

      <label>
        票价:
        <input v-model.number="newScreening.price" type="number" step="0.01" required />
      </label>

      <button type="submit">提交</button>
    </form>

    <!-- 已有场次列表 -->
    <h3>已有场次</h3>
    <table class="screening-table">
      <thead>
      <tr>
        <th>电影名称</th>
        <th>放映厅</th>
        <th>开始时间</th>
        <th>票价</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in screenings" :key="item.id">
        <td>{{ item.movie.title }}</td>
        <td>{{ item.screeningRoom.name }}</td>
        <td>{{ formatTime(item.startTime) }}</td>
        <td>¥{{ item.price }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const newScreening = ref({
  movieId: null,
  roomId: null,
  startTime: '',
  endTime: '',
  price: 0
});
const screenings = ref([]);
const movies = ref([]);
const screeningRooms = ref([]);
// 获取所有场次信息
const fetchAllScreenings = async () => {
  try {
    const token = localStorage.getItem('token');
    const res = await axios.get('http://localhost:9000/api/screenings/all', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    screenings.value = res.data;
  } catch (error) {
    console.error('获取场次失败:', error);
  }
};

// 提交新场次
const addScreening = async () => {
  try {
    const token = localStorage.getItem('token');
    const res = await axios.post('http://localhost:9000/api/admin/screenings/add', newScreening.value, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    console.log('添加场次响应:', res.data); // 使用 res
    alert('场次添加成功！');
    newScreening.value = {
      movieId: null,
      roomId: null,
      startTime: '',
      price: 0
    };
    fetchAllScreenings(); // 刷新列表
  } catch (error) {
    console.error('添加场次失败:', error);
    alert('添加场次失败，请检查输入或网络');
  }
};
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

// 获取所有放映厅
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

onMounted(() => {
  fetchAllMovies();
  fetchAllScreeningRooms();
  fetchAllScreenings();
});

// 时间格式化函数
const formatTime = (dateTime) => {
  return new Date(dateTime).toLocaleString();
};

onMounted(() => {
  fetchAllScreenings();
});
</script>
<style scoped>
.screening-schedule {
  padding: 30px;
}

.add-form {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr 1fr 1fr auto;
  gap: 10px;
  margin-bottom: 20px;
}

.add-form label {
  display: flex;
  flex-direction: column;
  font-size: 14px;
}

.add-form input {
  padding: 6px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.add-form button {
  padding: 8px 16px;
  background-color: #42b983;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.add-form button:hover {
  background-color: #369d6b;
}

.screening-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.screening-table th,
.screening-table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: center;
}
</style>

