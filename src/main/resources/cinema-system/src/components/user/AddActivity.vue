<template>
  <div class="add-movie">
    <h2>添加新活动</h2>

    <form @submit.prevent="submitActivity">
      <div class="form-group">
        <label>活动名称：
          <input type="text" v-model="activity.name" placeholder="请输入活动名称" />
        </label>
      </div>

      <div class="form-group">
        <label>名额：
          <input v-model.number="activity.quota" type="number" min="1" />
        </label>
      </div>

      <div class="form-group">
        <label>所属影院：
          <select v-model="activity.cinemaId">
            <option disabled value="">请选择影院</option>
            <option v-for="cinema in cinemas" :key="cinema.id" :value="cinema.id">
              {{ cinema.name }}
            </option>
          </select>
        </label>
      </div>

      <div class="form-group">
        <label>活动描述：
          <textarea v-model="activity.description"></textarea>
        </label>
      </div>

      <button type="submit" :disabled="!activity.name || !activity.quota">提交</button>
      <button type="button" @click="$router.back()">取消</button>
    </form>
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

</style>