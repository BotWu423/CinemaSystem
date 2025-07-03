<template>
  <div class="seat-selection">
    <h2>请选择座位</h2>

    <!-- 屏幕 -->
    <div class="screen">屏幕</div>

    <!-- 座位图 -->
    <div class="seat-map">
      <div v-for="(row, rowIndex) in totalRows" :key="rowIndex + 1" class="seat-row">
        <span
            v-for="seat in seats.filter(s => s.rowNumber === rowIndex + 1)"
            :key="seat.id"
            class="seat"
            :class="{
            available: seat.status === 'AVAILABLE',
            selected: selectedSeats.includes(seat.id),
            occupied: seat.status === 'OCCUPIED'
          }"
            @click="toggleSeat(seat)"
        >
          {{ seat.seatNumber }}
        </span>
      </div>
    </div>

    <!-- 订单摘要 -->
    <div class="summary">
      <p>已选座位数：{{ selectedSeats.length }}</p>
      <p>单价：¥{{ pricePerSeat }}</p>
      <p>总价：¥{{ totalPrice.toFixed(2) }}</p>
      <button @click="submitOrder" :disabled="selectedSeats.length === 0">确认下单</button>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue';
import axios from 'axios';
import {useRoute, useRouter} from 'vue-router';

const route = useRoute();
const router = useRouter();

// 接收参数
const screeningId = ref(route.query.screeningId);
const screeningRoomId = ref(route.query.screeningRoomId);
const pricePerSeat = ref(0);

// 响应式数据
const seats = ref([]);
const selectedSeats = ref([]);
const loading = ref(true);
const error = ref(null);

// 请求头配置
const token = localStorage.getItem('token');
const apiBase = 'http://localhost:9000/api';
// 获取座位数据
const fetchSeats = async () => {
  try {
    // 打印 token、screeningId 和 screeningRoomId 以确保它们存在并且正确
    console.log('Token:', token);
    console.log('screeningId:', screeningId.value);
    console.log('screeningRoomId:', screeningRoomId.value);

    // 获取该放映厅的所有座位，同时传递 screeningId 和 screeningRoomId 参数
    const seatRes = await axios.get(`${apiBase}/seats?screeningId=${screeningId.value}&screeningRoomId=${screeningRoomId.value}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    // 获取已预订的座位 ID
    const bookedRes = await axios.get(`${apiBase}/orders/booked-seats?screeningId=${screeningId.value}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    const bookedSeatIds = bookedRes.data;

    // 更新座位状态
    const updatedSeats = seatRes.data.map(seat => ({
      ...seat,
      status: bookedSeatIds.includes(seat.id) ? 'OCCUPIED' : 'AVAILABLE'
    }));

    seats.value = updatedSeats;
    loading.value = false;
  } catch (err) {
    console.error('获取座位失败', err);
    error.value = '无法加载座位信息，请稍后再试';
    loading.value = false;
  }
};

// 获取场次详细信息（含价格）
const fetchScreeningDetails = async () => {
  try {
    const res = await axios.get(`${apiBase}/screenings/${screeningId.value}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    pricePerSeat.value = res.data.price;
    await fetchSeats();
  } catch (err) {
    console.error('获取场次详情失败', err);
    error.value = '无法加载场次详情';
    loading.value = false;
  }
};

// 切换座位选择
const toggleSeat = (seat) => {
  if (seat.status !== 'AVAILABLE') return;
  const index = selectedSeats.value.indexOf(seat.id);
  if (index === -1) {
    selectedSeats.value.push(seat.id);
  } else {
    selectedSeats.value.splice(index, 1);
  }
};

// 总价计算
const totalPrice = computed(() => {
  return selectedSeats.value.length * pricePerSeat.value;
});

// 下单提交
const submitOrder = async () => {
  try {
    const userId = localStorage.getItem('userId');
    console.log('userId:', userId);
    console.log('screeningId:', screeningId.value);
    console.log('seatIds:', selectedSeats.value);
    console.log('totalPrice:', totalPrice.value);

    const response = await axios.post(
        `${apiBase}/orders`,
        {
          userId: userId,
          screeningId: screeningId.value,
          seatIds: selectedSeats.value,
          totalPrice: totalPrice.value
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
    );
    console.log('Response data:', response.data);
    const order = response.data;
    console.log('Order ID:', order.id);
    console.log('User ID:', userId);
    console.log('Screening ID:', screeningId.value);
    router.push({
      path: `/order-success/${order.id}`,
      query: {
        userId: userId,
        screeningId: screeningId.value
      }
    });
  } catch (err) {
    console.error('下单失败', err);
    if (err.response && err.response.data && err.response.data.message) {
      alert(`下单失败: ${err.response.data.message}`);
    } else {
      alert('下单失败，请检查网络或稍后再试');
    }
  }
};

// 生命周期钩子
onMounted(async () => {
  if (!screeningId.value || !screeningRoomId.value) {
    alert('缺少必要参数 screeningId 或 screeningRoomId');
    router.back();
    return;
  }
  await fetchScreeningDetails();
});

// 计算总排数
const totalRows = computed(() => {
  const maxRow = seats.value.reduce((max, seat) => Math.max(max, seat.rowNumber), 0);
  return Array.from({length: maxRow}, (_, i) => i + 1);
});
</script>

<style scoped>
.seat-selection {
  padding: 20px;
}

.screen {
  text-align: center;
  background-color: #333;
  color: white;
  padding: 10px;
  margin-bottom: 20px;
  font-weight: bold;
}

.seat-map {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.seat-row {
  display: flex;
  justify-content: center;
  margin: 5px 0;
}

.seat {
  width: 30px;
  height: 30px;
  margin: 2px;
  border: 1px solid #ccc;
  cursor: pointer;
  text-align: center;
  line-height: 30px;
  user-select: none;
}

.available {
  background-color: #c883ea;
}

.available:hover {
  background-color: #c910e6;
}

.selected {
  background-color: #007bff;
  color: white;
}

.occupied {
  background-color: #cccccc;
  cursor: not-allowed;
}

.summary {
  margin-top: 30px;
  text-align: center;
}
</style>