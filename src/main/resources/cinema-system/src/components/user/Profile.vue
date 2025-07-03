<template>
  <div class="add-movie">
    <h2>个人中心</h2>

    <!-- 统一使用表格布局 -->
    <table class="profile-table">
      <tbody>
      <!-- 个人信息 -->
      <tr>
        <td colspan="2" class="section-header">个人信息</td>
      </tr>
      <tr>
        <td class="label">用户名：</td>
        <td>{{ userInfo.username }}</td>
      </tr>
      <tr>
        <td class="label">邮箱：</td>
        <td>{{ userInfo.email }}</td>
      </tr>
      <tr>
        <td class="label">手机号：</td>
        <td>{{ userInfo.phone }}</td>
      </tr>
      <tr>
        <td></td>
        <td>
          <button @click="showChangePwd = true">修改密码</button>
        </td>
      </tr>

      <!-- 修改密码弹窗 -->
      <tr v-if="showChangePwd">
        <td colspan="2" class="modal-cell">
          <div class="change-pwd-modal">
            <label>原密码：
              <input type="password" v-model="oldPassword" placeholder="请输入原密码" />
            </label>
            <label>新密码：
              <input type="password" v-model="newPassword" placeholder="请输入新密码" />
            </label>
            <div class="modal-actions">
              <button @click="changePassword">提交</button>
              <button @click="showChangePwd = false">取消</button>
            </div>
          </div>
        </td>
      </tr>

      <!-- 我的订单 -->
      <tr>
        <td colspan="2" class="section-header">我的订单</td>
      </tr>
      <tr>
        <td colspan="2">
          <div v-if="loading">加载中...</div>
          <div v-else-if="orders.length === 0">暂无订单记录</div>
          <ul v-else class="order-list">
            <li v-for="order in orders" :key="order.id" class="order-item">
              <strong>订单编号：</strong>{{ order.id }} &nbsp;&nbsp;
              <strong>电影名称：</strong>{{ order.screening.movie.title }} &nbsp;&nbsp;
              <strong>放映时间：</strong>{{ formatTime(order.screening.startTime) }} &nbsp;&nbsp;
              <strong>总价：</strong>¥{{ order.totalPrice }} &nbsp;&nbsp;
              <strong>状态：</strong>{{ getOrderStatusText(order.status) }}
              <button class="action-btn" @click="cancelOrder(order.id)" v-if="order.status !== 'CANCELLED'">取消订单</button>
            </li>
          </ul>
        </td>
      </tr>

      <!-- 我的评论 -->
      <tr>
        <td colspan="2" class="section-header">我的评论</td>
      </tr>
      <tr>
        <td colspan="2">
          <div v-if="comments.length === 0">暂无评论记录</div>
          <ul v-else class="comment-list">
            <li v-for="comment in comments" :key="comment.id" class="comment-item">
              <strong>电影名称：</strong>{{ comment.movie.title }} &nbsp;&nbsp;
              <strong>评论内容：</strong>{{ comment.content }} &nbsp;&nbsp;
              <strong>评论时间：</strong>{{ formatTime(comment.createTime) }}
              <button class="action-btn" @click="deleteComment(comment.id)">删除</button>
            </li>
          </ul>
        </td>
      </tr>

      <!-- 参与的活动 -->
      <tr>
        <td colspan="2" class="section-header">我参与的活动</td>
      </tr>
      <tr>
        <td colspan="2">
          <div v-if="activities.length === 0">暂无参与的活动</div>
          <ul v-else class="activity-list">
            <li v-for="activity in activities" :key="activity.id" class="activity-item">
              <strong>活动名称：</strong>{{ activity.name }} &nbsp;&nbsp;
              <strong>当前人数：</strong>{{ activity.quota - activity.participants.length }} / {{ activity.quota }} &nbsp;&nbsp;
              <button class="action-btn" @click="leaveActivity(activity.id)">退出活动</button>
            </li>
          </ul>
        </td>
      </tr>
      </tbody>
    </table>
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
      userId: null,
      userInfo: {
        username: '',
        email: '',
        phone: ''
      },
      showChangePwd: false,
      newPassword: '',
      oldPassword: ''
    };
  },
  mounted() {
    this.userId = localStorage.getItem('userId');
    if (!this.userId) {
      alert('请先登录');
      this.$router.push('/login');
      return;
    }
    this.fetchUserInfo();
    this.fetchOrders();
    this.fetchComments();
    this.fetchActivities();
  },
  methods: {
    async fetchUserInfo() {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get(`http://localhost:9000/api/users/${this.userId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.userInfo = res.data;
      } catch (e) {
        console.error('获取用户信息失败:', e);
      }
    },
    async changePassword() {
      if (!this.oldPassword) {
        alert('原密码不能为空');
        return;
      }
      if (!this.newPassword) {
        alert('新密码不能为空');
        return;
      }
      try {
        const token = localStorage.getItem('token');
        await axios.post(`http://localhost:9000/api/users/${this.userId}/change-password`, {
          oldPassword: this.oldPassword,
          newPassword: this.newPassword
        }, {
          headers: { Authorization: `Bearer ${token}` }
        });
        alert('密码修改成功，请重新登录');
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        this.$router.push('/login');
      } catch (e) {
        if (e.response && e.response.data && e.response.data === '原密码错误') {
          alert('原密码错误');
        } else {
          alert('修改密码失败');
        }
      }
    },
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
    },
    async deleteComment(commentId) {
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
    },
    async cancelOrder(orderId) {
      if (!confirm('确定要取消该订单吗？')) return;
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`http://localhost:9000/api/orders/${orderId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.fetchOrders();
      } catch (error) {
        console.error('取消订单失败:', error);
        alert('取消订单失败，请重试');
      }
    }
  }
};
</script>


<style scoped>
.profile-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.section-header {
  background-color: #333;
  color: var(--primary-color);
  font-size: 18px;
  text-align: left;
  padding: 15px;
}

.label {
  width: 150px;
  padding: 10px;
  vertical-align: top;
  font-weight: bold;
  background-color: #2e2e2e;
}

.modal-cell {
  padding: 10px;
  background-color: #2e2e2e;
}

.change-pwd-modal {
  display: flex;
  flex-direction: column;
  gap: 10px;
  background-color: #1e1e1e;
  padding: 15px;
  border-radius: 6px;
}

.change-pwd-modal input {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid var(--primary-color);
  background-color: #2e2e2e;
  color: var(--text-color);
}

.modal-actions button {
  margin-right: 10px;
}

/* 订单列表 */
.order-list {
  list-style: none;
  padding: 0;
  max-height: 200px;
  overflow-y: auto;
}
.order-item {
  padding: 10px;
  border-bottom: 1px solid #444;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #2e2e2e;
  margin-bottom: 5px;
  border-radius: 4px;
}

/* 评论列表 */
.comment-list {
  list-style: none;
  padding: 0;
  max-height: 200px;
  overflow-y: auto;
}
.comment-item {
  padding: 10px;
  border-bottom: 1px solid #444;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #2e2e2e;
  margin-bottom: 5px;
  border-radius: 4px;
}

/* 活动列表 */
.activity-list {
  list-style: none;
  padding: 0;
  max-height: 200px;
  overflow-y: auto;
}
.activity-item {
  padding: 10px;
  border-bottom: 1px solid #444;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #2e2e2e;
  margin-bottom: 5px;
  border-radius: 4px;
}

/* 按钮通用样式 */
.action-btn {
  margin-left: 10px;
  padding: 6px 12px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.action-btn:hover {
  background-color: var(--secondary-color);
}
</style>


