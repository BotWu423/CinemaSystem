<template>
  <div class="add-movie">
    <h1>订单管理</h1>
    <div v-if="loading">加载中...</div>
    <table v-else class="order-table">
      <thead>
        <tr>
          <th>订单编号</th>
          <th>用户</th>
          <th>电影</th>
          <th>影院名称</th>
          <th>影厅</th>
          <th>放映时间</th>
          <th>座位</th>
          <th>总价</th>
          <th>下单时间</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.id">
          <td>{{ order.id }}</td>
          <td>{{ order.username || '-' }}</td>
          <td>{{ order.movieTitle || '-' }}</td>
          <td>{{ order.cinemaName || '-' }}</td>
          <td>{{ order.screeningRoomName || '-' }}</td>
          <td>{{ formatTime(order.screeningTime) }}</td>
          <td>{{ order.seatInfo || '-' }}</td>
          <td>¥{{ order.totalPrice }}</td>
          <td>{{ formatTime(order.createTime) }}</td>
          <td>{{ getOrderStatusText(order.status) }}</td>
          <td><button class="delete-btn" @click="deleteOrder(order.id)">删除</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'OrderManagement',
  data() {
    return {
      orders: [],
      loading: true
    };
  },
  mounted() {
    this.fetchOrders();
  },
  methods: {
    async fetchOrders() {
      try {
        const token = localStorage.getItem('token');
        const res = await axios.get('http://localhost:9000/api/orders/all', {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.orders = res.data;
      } catch (e) {
        alert('获取订单失败');
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
    async deleteOrder(orderId) {
      if (!confirm('确定要删除该订单吗？')) return;
      try {
        const token = localStorage.getItem('token');
        await axios.delete(`http://localhost:9000/api/orders/${orderId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.fetchOrders();
      } catch (e) {
        alert('删除订单失败');
      }
    }
  }
};
</script>

<style scoped>

</style> 