<template>
  <div class="add-movie">
    <h2>编辑电影院</h2>
    <form @submit.prevent="submitForm">
      <!-- 统一使用 form-group 包裹 -->
      <div class="form-group">
        <label>影院名称：<input type="text" v-model="cinema.name" required /></label>
      </div>
      <div class="form-group">
        <label>地址：<input type="text" v-model="cinema.address" required /></label>
      </div>
      <div class="form-group">
        <label>联系方式：<input type="text" v-model="cinema.contact" /></label>
      </div>
      <div class="form-group">
        <label>描述：<textarea v-model="cinema.description"></textarea></label>
      </div>

      <!-- 按钮 -->
      <button type="submit">保存更改</button>
      <button type="button" @click="$router.back()" style="margin-left: 10px;">取消</button>
    </form>
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
      cinema: {
        id: null,
        name: '',
        address: '',
        contact: '',
        description: ''
      }
    };
  },
  async mounted() {
    const cinemaId = this.$route.query.cinemaId;
    const token = localStorage.getItem('token');

    try {
      const res = await axios.get(`http://localhost:9000/api/cinemas/${cinemaId}`, {
        headers: { Authorization: 'Bearer ' + token }
      });

      this.cinema = res.data;
    } catch (error) {
      console.error('获取影院详情失败:', error);
      alert('获取影院信息失败');
      this.$router.back();
    }
  },
  methods: {
    async submitForm() {
      const token = localStorage.getItem('token');
      try {
        await axios.put('http://localhost:9000/api/cinemas', this.cinema, {
          headers: {
            Authorization: 'Bearer ' + token,
            'Content-Type': 'application/json'
          }
        });

        alert('影院信息更新成功');
        this.$router.back();
      } catch (error) {
        console.error('更新影院失败:', error);
        alert('更新失败，请重试');
      }
    }
  }
};
</script>

<style scoped>

</style>
