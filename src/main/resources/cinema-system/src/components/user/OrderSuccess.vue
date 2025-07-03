<template>
  <div class="order-success">
    <h2>订单提交成功！</h2>
    <p>您的订单已成功生成，请查看以下信息：</p>

    <div v-if="order" class="order-details">
      <p><strong>订单编号：</strong>{{ order.id }}</p>
      <p><strong>用户 ID：</strong>{{ userId }}</p>
      <p><strong>场次 ID：</strong>{{ screeningId }}</p>
      <p><strong>座位编号：</strong>{{ order.details.map(d => d.seat.id).join(', ') }}</p>
      <p><strong>总价：</strong>¥{{ order.totalPrice.toFixed(2) }}</p>
    </div>

    <div v-else>
      <p>加载订单详情中...</p>
    </div>

    <button @click="$router.push('/movies')">返回我的订单</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();

const orderId = route.params.orderId;
const { userId, screeningId } = route.query;
const order = ref(null);

onMounted(async () => {
  const token = localStorage.getItem('token');
  try {
    const res = await axios.get(`http://localhost:9000/api/orders/${orderId}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    order.value = res.data;
  } catch (err) {
    console.error('获取订单详情失败', err);
    alert('无法加载订单信息');
    router.push('/movies');
  }
});
</script>

<style scoped>

</style>