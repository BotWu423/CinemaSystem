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
            occupied: seat.status !== 'AVAILABLE'
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
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

// 接收参数
const screeningId = ref(route.query.screeningId);
const screeningRoomId = ref(null);
const pricePerSeat = ref(0);

// 响应式数据
const seats = ref([]);
const selectedSeats = ref([]);
const loading = ref(true);
const error = ref(null);

// 请求头配置
const token = localStorage.getItem('token');
const apiBase = 'http://localhost:8080/api';

// 获取座位数据
const fetchSeats = async () => {
  try {
    const res = await axios.get(`${apiBase}/seats/by-screening-room?screeningRoomId=${screeningRoomId.value}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    seats.value = res.data;
    loading.value = false;
  } catch (err) {
    console.error('获取座位失败', err);
    error.value = '无法加载座位信息，请稍后再试';
    loading.value = false;
  }
};

// 获取场次详细信息（含价格、放映厅ID）
const fetchScreeningDetails = async () => {
  try {
    const res = await axios.get(`${apiBase}/screenings/${screeningId.value}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    pricePerSeat.value = res.data.price;
    screeningRoomId.value = res.data.screeningRoom.id;
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
    await axios.post(
        `${apiBase}/orders`,
        {
          screeningId: screeningId.value,
          seatIds: selectedSeats.value
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
    );
    alert('下单成功！');
    router.push('/orders'); // 跳转至订单页
  } catch (err) {
    console.error('下单失败', err);
    alert('下单失败，请重试');
  }
};

// 生命周期钩子
onMounted(async () => {
  if (!screeningId.value) {
    alert('缺少必要参数 screeningId');
    router.back();
    return;
  }
  await fetchScreeningDetails();
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
  background-color: #e0ffe0;
}

.available:hover {
  background-color: #aaffaa;
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