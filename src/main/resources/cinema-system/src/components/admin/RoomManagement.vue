<template>
  <div class="room-management">
    <h1>放映室管理</h1>

    <div class="add-room-form">
      <h3>添加新放映室</h3>
      <form @submit.prevent="addRoom">
        <select v-model="newRoom.cinemaId" required>
          <option v-for="cinema in cinemas" :key="cinema.id" :value="cinema.id">
            {{ cinema.name }}
          </option>
        </select>
        <input v-model="newRoom.name" placeholder="放映室名称" required>
        <input v-model.number="newRoom.totalSeats" placeholder="总座位数" type="number" required>
        <textarea v-model="newRoom.layout" placeholder="座位布局JSON"></textarea>
        <button type="submit">添加放映室</button>
      </form>
    </div>

    <div class="room-list">
      <h3>现有放映室</h3>
      <table>
        <thead>
        <tr>
          <th>影院</th>
          <th>放映室名称</th>
          <th>总座位数</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="room in rooms" :key="room.id">
          <td>{{ getCinemaName(room.cinemaId) }}</td>
          <td>{{ room.name }}</td>
          <td>{{ room.totalSeats }}</td>
          <td>
            <button @click="editLayout(room)">编辑布局</button>
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

const rooms = ref([]);
const cinemas = ref([]);
const newRoom = ref({
  cinemaId: null,
  name: '',
  totalSeats: 0,
  layout: ''
});

const fetchRooms = async () => {
  try {
    const response = await axios.get('/api/admin/rooms');
    rooms.value = response.data;
  } catch (error) {
    console.error('获取放映室列表失败:', error);
  }
};

const fetchCinemas = async () => {
  try {
    const response = await axios.get('/api/cinemas');
    cinemas.value = response.data;
  } catch (error) {
    console.error('获取影院列表失败:', error);
  }
};

const addRoom = async () => {
  try {
    await axios.post('/api/admin/rooms', newRoom.value);
    newRoom.value = {
      cinemaId: null,
      name: '',
      totalSeats: 0,
      layout: ''
    };
    fetchRooms();
  } catch (error) {
    console.error('添加放映室失败:', error);
  }
};

const editLayout = async (room) => {
  try {
    await axios.put(`/api/admin/rooms/${room.id}/layout`, {
      layoutJson: room.layout
    });
    alert('布局更新成功');
  } catch (error) {
    console.error('更新布局失败:', error);
  }
};

const deleteRoom = async (roomId) => {
  if (confirm('确定要删除这个放映室吗？')) {
    try {
      await axios.delete(`/api/admin/rooms/${roomId}`);
      fetchRooms();
    } catch (error) {
      console.error('删除放映室失败:', error);
    }
  }
};

const getCinemaName = (cinemaId) => {
  const cinema = cinemas.value.find(c => c.id === cinemaId);
  return cinema ? cinema.name : '未知影院';
};

onMounted(async () => {
  await Promise.all([fetchRooms(), fetchCinemas()]);
});
</script>