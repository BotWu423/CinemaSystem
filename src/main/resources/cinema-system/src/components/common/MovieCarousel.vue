<template>
  <div class="carousel-container">
    <div class="carousel-track" :style="{ transform: 'translateX(' + offset + 'px)' }">
      <!-- 循环显示海报 -->
      <div v-for="movie in movies" :key="movie.id" class="carousel-item">
        <img :src="movie.posterUrl" :alt="movie.title" @click="goToDetail(movie.id)" style="cursor: pointer;" />
      </div>
      <!-- 重复一次内容以实现无缝滚动 -->
      <div v-for="movie in movies" :key="'dup' + movie.id" class="carousel-item">
        <img :src="movie.posterUrl" :alt="movie.title" @click="goToDetail(movie.id)" style="cursor: pointer;" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();

const movies = ref([]);
const offset = ref(0);
let animationFrameId = null;
const speed = -0.5; // 滚动速度（负值表示向左）

// 获取推荐电影数据
async function fetchFeaturedMovies() {
  try {
    const token = localStorage.getItem('token');
    const res = await axios.get('http://localhost:9000/api/movies/featured', {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });
    console.log("获取电影链接:"+ res);
    movies.value = res.data;
  } catch (error) {
    console.error('获取轮播电影失败:', error);
  }
}

// 动画函数
function animate() {
  const containerWidth = document.querySelector('.carousel-container')?.offsetWidth || 0;
  offset.value += speed;

  // 当滚动过一张图片宽度时，重置位置，实现无限滚动
  if (offset.value <= -containerWidth) {
    offset.value = 0;
  }

  animationFrameId = requestAnimationFrame(animate);
}

// 点击跳转至电影详情页
function goToDetail(movieId) {
  router.push({ path: '/movie-detail', query: { movieId: movieId } });
}

onMounted(async () => {
  await fetchFeaturedMovies();
  animate();
});

onUnmounted(() => {
  cancelAnimationFrame(animationFrameId);
});
</script>

<!-- 单独定义组件名称 -->
<script>
export default {
  name: 'MovieCarousel'
}
</script>

<style scoped>
.carousel-container {
  width: 100%;
  overflow: hidden;
  position: relative;
  height: 300px;
  margin: 20px 0;
}

.carousel-track {
  display: flex;
  transition: transform 0.5s linear;
}

.carousel-item {
  flex: 0 0 300px;
  margin-right: 20px;
}

.carousel-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
</style>
