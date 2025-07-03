<template>
  <div class="activity-page">
    <h1>近期活动</h1>



    <h2>活动列表</h2>
    <ul>
      <li v-for="activity in activities" :key="activity.id" class="activity-item">
        <div>
          <strong>{{ activity.name }}</strong>
          <span v-if="activity.cinemaName">（所属影院：{{ activity.cinemaName }}）</span>
          （剩余名额：{{ activity.quota - activity.participants.length }} / {{ activity.quota }}）
          <p>{{ activity.description }}</p>
        </div>
        <div>
          <button v-if="!isAdmin"
                  @click="joinActivity(activity.id)"
                  :disabled="activity.participants.includes(currentUser?.id) || activity.participants.length >= activity.quota">
            {{ activity.participants.includes(currentUser?.id) ? '已报名' : '报名' }}
          </button>
          <button v-if="isAdmin" @click="deleteActivity(activity.id)">删除</button>
        </div>
      </li>
    </ul>

    <!-- 管理员底部添加活动按钮 -->
    <div v-if="isAdmin" class="add-activity-btn">
      <button @click="goToAddActivity">添加活动</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UserActivity',
  data() {
    return {
      activities: [],
      newActivity: { name: '', description: '', quota: 1 },
      currentUser: null,
      isAdmin: false
    };
  },
  mounted() {
    this.getCurrentUser();
    this.fetchActivities();
  },
  methods: {
    getCurrentUser() {
      const token = localStorage.getItem('token');
      if (!token) return;
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        this.currentUser = { id: payload.id, username: payload.sub };
        this.isAdmin = payload.roles && payload.roles.includes('ADMIN');
      }catch (e)
      {
        console.error('解析 token 失败:', e);
      }
    },
    async fetchActivities() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:9000/api/activities', {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });
        this.activities = response.data;
      } catch (error) {
        if (error.response && error.response.status === 401) {
          this.$router.push('/login');
        } else {
          console.error('获取活动失败:', error);
        }
      }
    },
    async publishActivity() {
      try {
        const token = localStorage.getItem('token');
        await axios.post('http://localhost:9000/api/activities', this.newActivity, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });
        this.newActivity = { name: '', description: '', quota: 1 };
        this.fetchActivities();
      } catch (error) {
        console.error('发布活动失败:', error);
      }
    },
    async joinActivity(activityId) {
      try {
        const token = localStorage.getItem('token');
        await axios.post(`http://localhost:9000/api/activities/${activityId}/join`, {}, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });
        this.fetchActivities();
      } catch (error) {
        console.error('报名活动失败:', error);
      }
    },
    async deleteActivity(activityId) {
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`http://localhost:9000/api/activities/${activityId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });
        this.fetchActivities();
      } catch (error) {
        console.error('删除活动失败:', error);
      }
    },
    goToAddActivity() {
      this.$router.push({ path: '/add-activity' });
    }
  }
};
</script>

<style scoped>

</style> 