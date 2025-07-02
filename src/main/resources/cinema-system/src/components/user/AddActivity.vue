<template>
  <div class="add-activity-page">
    <h1>添加新活动</h1>
    <div class="form">
      <input v-model="activity.name" placeholder="活动名称" />
      <input v-model="activity.quota" type="number" min="1" placeholder="名额" />
      <textarea v-model="activity.description" placeholder="活动描述"></textarea>
      <select v-model="activity.cinemaId">
        <option disabled value="">请选择所属影院</option>
        <option v-for="cinema in cinemas" :key="cinema.id" :value="cinema.id">{{ cinema.name }}</option>
      </select>
      <div class="button-container">
        <button @click="submitActivity" :disabled="!activity.name || !activity.quota">提交</button>
        <button @click="$router.back()" style="margin-left:10px;">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'AddActivity',
  data() {
    return {
      activity: {name: '', description: '', quota: 1, cinemaId: ''},
      cinemas: []
    };
  },
  mounted() {
    this.fetchCinemas();
  },
  methods: {
    async fetchCinemas() {
      const token = localStorage.getItem('token');
      const headers = token ? { Authorization: 'Bearer ' + token } : {};
      const res = await axios.get('http://localhost:9000/api/cinemas/all', { headers });
      this.cinemas = res.data;
    },
    async submitActivity() {
      const token = localStorage.getItem('token');
      const headers = token ? {Authorization: 'Bearer ' + token} : {};
      await axios.post('http://localhost:9000/api/activities', this.activity, {headers});
      this.$router.push({path: '/activity'});
    }
  }
};
</script>

<style scoped>
.add-activity-page {
  padding: 30px;
}

.form {
  display: flex;
  flex-direction: column;
  max-width: 400px;
}

.form input, .form textarea {
  margin-bottom: 12px;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.button-container {
  display: flex;
  justify-content: space-between;
}

.button-container button {
  width: calc(50% - 5px);
  padding: 8px 20px;
  background-color: #42b983;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.button-container button:hover {
  background-color: #369d6b;
}
</style>