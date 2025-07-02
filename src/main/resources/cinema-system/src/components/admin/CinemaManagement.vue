<template>
  <div class="cinema-management">
    <h1>管理电影院</h1>

    <div class="add-cinema-form">
      <h3>添加新电影院</h3>
      <form @submit.prevent="addCinema">
        <label>影院名称：<input v-model="newCinema.name" required /></label><br />
        <label>地址：<input v-model="newCinema.address" required /></label><br />
        <label>联系方式：<input v-model="newCinema.contact" /></label><br />
        <label>描述：<textarea v-model="newCinema.description"></textarea></label><br />
        <button type="submit">提交</button>
      </form>
    </div>

    <div class="cinema-list">
      <h3>现有电影院</h3>
      <table>
        <thead>
        <tr>
          <th>名称</th>
          <th>地址</th>
          <th>联系方式</th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="cinema in cinemas" :key="cinema.id">
          <td>{{ cinema.name }}</td>
          <td>{{ cinema.address }}</td>
          <td>{{ cinema.contact }}</td>
          <td><button @click="deleteCinema(cinema.id)">删除</button></td>
        </tr>
        </tbody>
      </table>
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
      cinemas: [],
      newCinema: {
        name: '',
        address: '',
        contact: '',
        description: ''
      }
    };
  },
  mounted() {
    this.fetchCinemas();
  },
  methods: {
    async fetchCinemas() {
      const token = localStorage.getItem('token');
      const res = await axios.get('http://localhost:9000/api/cinemas/all', {
        headers: { Authorization: 'Bearer ' + token }
      });
      this.cinemas = res.data;
    },
    async addCinema() {
      const token = localStorage.getItem('token');
      try {
        await axios.post('http://localhost:9000/api/admin/cinemas-management/add', this.newCinema, {
          headers: { Authorization: 'Bearer ' + token }
        });
        alert('电影院添加成功！');
        this.fetchCinemas();
        this.newCinema = { name: '', address: '', contact: '', description: '' };
      } catch (error) {
        console.error('添加电影院失败:', error);
        alert('添加失败，请重试');
      }
    },
    async deleteCinema(cinemaId) {
      if (!confirm('确定要删除该影院吗？')) return;
      const token = localStorage.getItem('token');
      try {
        await axios.delete(`http://localhost:9000/api/cinemas/${cinemaId}`, {
          headers: { Authorization: 'Bearer ' + token }
        });
        alert('影院删除成功');
        this.fetchCinemas(); // 刷新列表
      } catch (error) {
        console.error('删除影院失败:', error);
        alert('删除失败，请重试');
      }
    }

  }
};
</script>

<style scoped>
.add-cinema-form {
  margin-bottom: 40px;
}
label {
  display: block;
  margin-top: 10px;
}
input,
textarea {
  width: 100%;
  padding: 6px;
  margin-top: 4px;
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
table {
  width: 100%;
  border-collapse: collapse;
}
th,
td {
  border: 1px solid #ccc;
  padding: 8px;
}
</style>
