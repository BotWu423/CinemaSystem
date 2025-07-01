<template>
  <div class="work-log-page">
    <h1>每日工作记录</h1>
    <div class="log-form">
      <textarea v-model="newLogContent" placeholder="请输入今日工作内容..." rows="4"></textarea>
      <button @click="submitLog">提交记录</button>
    </div>
    <h2>历史记录</h2>
    <ul class="log-list">
      <li v-for="log in logs" :key="log.id" class="log-item">
        <div class="log-content">{{ log.content }}</div>
        <div class="log-meta">
          <span>记录人：{{ log.username }}</span>
          <span>日期：{{ formatTime(log.createTime) }}</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'WorkLog',
  data() {
    return {
      newLogContent: '',
      logs: []
    };
  },
  mounted() {
    this.fetchLogs();
  },
  methods: {
    async fetchLogs() {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get('http://localhost:9000/api/worklogs', {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.logs = res.data;
      } catch (e) {
        console.error('获取工作记录失败:', e);
      }
    },
    async submitLog() {
      if (!this.newLogContent) {
        alert('请输入工作内容');
        return;
      }
      try {
        const token = localStorage.getItem('token');
        await axios.post('http://localhost:9000/api/worklogs', {
          content: this.newLogContent
        }, {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.newLogContent = '';
        this.fetchLogs();
      } catch (e) {
        alert('提交失败');
      }
    },
    formatTime(time) {
      if (!time) return '';
      return new Date(time).toLocaleString();
    }
  }
};
</script>

<style scoped>
.work-log-page {
  padding: 30px;
}
.log-form {
  margin-bottom: 24px;
}
textarea {
  width: 100%;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 16px;
  margin-bottom: 10px;
}
button {
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-weight: bold;
}
button:hover {
  background-color: #1976d2;
}
.log-list {
  list-style: none;
  padding: 0;
}
.log-item {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  background-color: #f9f9f9;
}
.log-content {
  font-size: 16px;
  margin-bottom: 8px;
}
.log-meta {
  font-size: 14px;
  color: #888;
  display: flex;
  gap: 20px;
}
</style> 