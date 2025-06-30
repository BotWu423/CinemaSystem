<template>
  <div class="profile-page">
    <h1>个人中心</h1>

    <!-- 订单部分 -->
    <div v-if="loading">加载中...</div>
    <div v-else>
      <h2>我的订单</h2>
      <div v-if="orders.length === 0">暂无订单记录</div>
      <ul class="order-list" v-else>
        <li v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-header">
            <strong>订单编号：</strong>{{ order.id }}
          </div>
          <div class="order-info">
            <p><strong>电影名称：</strong>{{ order.screening.movie.title }}</p>
            <p><strong>放映时间：</strong>{{ formatTime(order.screening.startTime) }}</p>
            <p><strong>座位信息：</strong>
              {{ order.details.map(d => `${d.seat.rowNumber}-${d.seat.seatNumber}`).join(', ') }}
            </p>
            <p><strong>总价：</strong>¥{{ order.totalPrice }}</p>
            <p><strong>下单时间：</strong>{{ formatTime(order.createTime) }}</p>
            <p><strong>状态：</strong>{{ getOrderStatusText(order.status) }}</p>
          </div>
        </li>
      </ul>
    </div>

    <!-- 评论部分 -->
    <div v-if="comments.length > 0">
      <h2>我的评论</h2>
      <ul class="comment-list">
        <li v-for="comment in comments" :key="comment.id" class="comment-item">
          <p><strong>电影名称：</strong>{{ comment.movie.title }}</p>
          <p><strong>评论内容：</strong>{{ comment.content }}</p>
          <p><strong>评论时间：</strong>{{ formatTime(comment.createTime) }}</p>
          <button @click="deleteComment(comment.id)">删除</button>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>暂无评论记录</p>
    </div>

    <!-- 活动部分 -->
    <div v-if="activities.length > 0">
      <h2>我参与的活动</h2>
      <ul class="activity-list">
        <li v-for="activity in activities" :key="activity.id" class="activity-item">
          <p><strong>活动名称：</strong>{{ activity.name }}</p>
          <p><strong>活动描述：</strong>{{ activity.description }}</p>
          <p><strong>人数限制：</strong>{{activity.quota}}</p>
          <p><strong>当前人数：</strong>{{activity.quota-activity.participants.length}}</p>
          <button @click="leaveActivity(activity.id)">退出活动</button>
        </li>
      </ul>
    </div>
    <div v-else>
      <p>暂无参与的活动</p>
    </div>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  name: 'UserProfile',
  data() {
    return {
      loading: true,
      orders: [],
      comments: [],
      activities: [],
      userId: null
    };
  },
  mounted() {
    this.userId = localStorage.getItem('userId');
    if (!this.userId) {
      alert('请先登录');
      this.$router.push('/login');
      return;
    }
    this.fetchOrders();
    this.fetchComments();
    this.fetchActivities();
  },
  methods: {
    async fetchOrders() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`http://localhost:9000/api/orders/user/${this.userId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        this.orders = response.data;
      } catch (error) {
        console.error('获取订单失败:', error);
      }
    },
    async fetchComments() {
      try {
        const token = localStorage.getItem('token');
        console.log('【获取评论】Token:', token);
        const response = await axios.get(`http://localhost:9000/api/comments/user/${this.userId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        this.comments = response.data;
      } catch (error) {
        console.error('获取评论失败:', error);
      }
    },
    async fetchActivities() {
      try {
        const token = localStorage.getItem('token');
        console.log('【获取活动】Token:', token);
        const response = await axios.get(`http://localhost:9000/api/activities/user/${this.userId}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        this.activities = response.data;
      } catch (error) {
        console.error('获取活动失败:', error);
      } finally {
        this.loading = false;
      }
    },
    formatTime(time) {
      if (!time) return '';
      return new Date(time).toLocaleString();
    },
    getOrderStatusText(status) {
      switch (status) {
        case 'PENDING': return '待支付';
        case 'PAID': return '已支付';
        case 'CANCELLED': return '已取消';
        default: return status;
      }
    },
    async leaveActivity(activityId) {
      try {
        const token = localStorage.getItem('token');
        await axios.post(
            `http://localhost:9000/api/activities/${activityId}/leave`,
            {},
            {
              headers: { Authorization: `Bearer ${token}` }
            }
        );
        this.fetchActivities();
      } catch (error) {
        console.error('退出活动失败:', error);
        alert('退出活动失败');
      }
    },async deleteComment(commentId) {
      if (!confirm('确定要删除这条评论吗？')) return;

      try {
        const token = localStorage.getItem('token');
        await axios.delete(`http://localhost:9000/api/comments/${commentId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.fetchComments();
      } catch (error) {
        console.error('删除评论失败:', error);
        alert('删除失败，请重试');
      }
    }
  }
};
</script>


<style scoped>
.profile-page {
  padding: 30px;
}

.order-list, .comment-list, .activity-list {
  list-style: none;
  padding: 0;
}

.order-item, .comment-item, .activity-item {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  background-color: #f9f9f9;
}

.order-header {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}

.order-info p, .comment-item p, .activity-item p {
  margin: 4px 0;
}
</style>

