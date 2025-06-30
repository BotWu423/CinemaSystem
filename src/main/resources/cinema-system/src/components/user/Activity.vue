<template>
  <div class="activity-page">
    <h1>近期活动</h1>

    <h2>活动列表</h2>
    <ul>
      <li v-for="activity in activities" :key="activity.id" class="activity-item">
        <div>
          <strong>{{ activity.name }}</strong>
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
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.currentUser = { id: payload.id, username: payload.sub };
      this.isAdmin = payload.roles && payload.roles.includes('ADMIN');
    },
    async fetchActivities() {
      const token = localStorage.getItem('token');
      const headers = token ? { Authorization: 'Bearer ' + token } : {};
      const res = await axios.get('http://localhost:9000/api/activities', { headers });
      this.activities = res.data;
    },
    async publishActivity() {
      const token = localStorage.getItem('token');
      const headers = token ? { Authorization: 'Bearer ' + token } : {};
      await axios.post('http://localhost:9000/api/activities', this.newActivity, { headers });
      this.newActivity = { name: '', description: '', quota: 1 };
      this.fetchActivities();
    },
    async joinActivity(activityId) {
      const token = localStorage.getItem('token');
      const headers = token ? { Authorization: 'Bearer ' + token } : {};
      await axios.post(`http://localhost:9000/api/activities/${activityId}/join`, {}, { headers });
      this.fetchActivities();
    },
    async deleteActivity(activityId) {
      const token = localStorage.getItem('token');
      const headers = token ? { Authorization: 'Bearer ' + token } : {};
      await axios.delete(`http://localhost:9000/api/activities/${activityId}`, { headers });
      this.fetchActivities();
    },
    goToAddActivity() {
      this.$router.push({ path: '/add-activity' });
    }
  }
};
</script>

<style scoped>
.activity-page { padding: 30px; }
.publish-form { margin-bottom: 20px; }
.activity-item { margin-bottom: 16px; border-bottom: 1px solid #eee; padding-bottom: 10px; }
.add-activity-btn {
  display: flex;
  justify-content: flex-end;
  margin-top: 30px;
}
.add-activity-btn button {
  padding: 8px 20px;
  background-color: #42b983;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.add-activity-btn button:hover {
  background-color: #369d6b;
}
</style> 