<template>
  <div class="room-management">
    <!-- 添加放映室 -->
    <form @submit.prevent="addRoom" class="add-form">
      <h3>添加新放映室</h3>
      <label>所属影院：
        <select v-model.number="newRoom.cinemaId" required>
          <option v-for="cinema in cinemas" :key="cinema.id" :value="cinema.id">
            {{ cinema.name }}
          </option>
        </select>
      </label>

      <label>名称：
        <input v-model="newRoom.name" required />
      </label>
      <label>座位布局（格式如 "5x8"）：
        <input v-model="newRoom.layout" placeholder="如 5x8" required />
      </label>
      <button type="submit">提交</button>
    </form>

    <!-- 放映室列表 -->
    <div class="room-list">
      <h3>现有放映室</h3>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>名称</th>
          <th>影院</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="room in rooms" :key="room.id">
          <td>{{ room.id }}</td>
          <td>{{ room.name }}</td>
          <td>{{ room.cinemaName }}</td>
          <td>
            <button @click="deleteRoom(room.id)">删除</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// 获取 Token
const getToken = () => localStorage.getItem('token');
console.log(getToken())
// 表单数据
const newRoom = ref({
  cinemaId: null,
  name: '',
  totalSeats: 0,
  layout: ''
});

// 放映室列表数据
const rooms = ref([]);
const cinemas = ref([]);
// 提交新增放映室
const addRoom = async () => {
  try {
    const token = getToken();
    await axios.post('http://localhost:9000/api/admin/rooms', newRoom.value, {
      headers: {
        'Authorization': 'Bearer ' + token,
        'Content-Type': 'application/json'
      }
    });
    alert('放映室添加成功');
    await fetchRooms(); // 刷新列表
  } catch (error) {
    console.error('添加失败:', error);
    alert('添加失败，请检查输入或网络');
  }
};

// 获取所有放映室
const fetchRooms = async () => {
  try {
    const token = getToken();
    const res = await axios.get('http://localhost:9000/api/screening-rooms/all', {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });
    rooms.value = res.data;
    console.log('Rooms:', rooms.value);
  } catch (error) {
    console.error('获取放映室失败:', error);
  }
};

// 获取所有影院（用于下拉框）
const fetchCinemas = async () => {
  try {
    const token = getToken();
    const res = await axios.get('http://localhost:9000/api/cinemas/all', {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });
    cinemas.value = res.data;
    console.log('cinemas:', cinemas.value);
  } catch (error) {
    console.error('获取影院失败:', error);
  }
};
const deleteRoom = async (roomId) => {
  try {
    const token = getToken();
    await axios.delete(`http://localhost:9000/api/screening-rooms/${roomId}`, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });
    alert('放映室删除成功');
    await fetchRooms(); // 刷新列表
  } catch (error) {
    console.error('删除失败:', error);
    alert('删除失败，请检查网络或权限');
  }
};

onMounted(async () => {
  await Promise.all([fetchRooms(), fetchCinemas()]);
});
</script>

<style scoped>
.room-management {
  padding: 20px;
}

.add-form {
  margin-bottom: 30px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.add-form label {
  display: flex;
  flex-direction: column;
}

.add-form input {
  padding: 6px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.add-form button {
  width: fit-content;
  background-color: #42b983;
  color: white;
  border: none;
  padding: 6px 12px;
  cursor: pointer;
  border-radius: 4px;
}

.room-list table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.room-list th,
.room-list td {
  border: 1px solid #ccc;
  text-align: center;
  padding: 8px;
}
</style>