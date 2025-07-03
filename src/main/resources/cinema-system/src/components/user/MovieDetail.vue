<template>
  <div class="add-movie" v-if="movie">
    <h2>{{ movie.title }}</h2>

    <!-- 电影海报 -->
    <div class="movie-poster-container">
      <img
          :src="movie.posterUrl||require('@/assets/image/default-poster.jpg')"
          alt="电影海报"
          class="movie-poster"
      />
    </div>

    <!-- 电影基本信息 -->
    <div class="movie-info">
      <p><strong>导演：</strong>{{ movie.director }}</p>
      <p><strong>演员：</strong>{{ movie.actors || '暂无' }}</p>
      <p><strong>类型：</strong>{{ movie.type }}</p>
      <p><strong>时长：</strong>{{ movie.duration }} 分钟</p>
      <p><strong>评分：</strong>{{ movie.rating ? movie.rating : '暂无' }}</p>
      <p><strong>上映日期：</strong>{{ movie.releaseDate }}</p>
      <p><strong>简介：</strong>{{ movie.description }}</p>
    </div>

    <!-- 评价区 -->
    <div class="comment-section">
      <h3>用户评价</h3>

      <!-- 评论列表 -->
      <ul v-if="comments.length > 0" class="comment-list">
        <li v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-header">
            <span class="comment-user">{{ comment.user.username }}</span>
            <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
          </div>
          <p class="comment-content">{{ comment.content }}</p>
          <button v-if="canDelete(comment)" @click="deleteComment(comment.id)" class="btn-delete">删除</button>
        </li>
      </ul>
      <p v-else class="no-comment">暂无评论</p>

      <!-- 新增评论表单 -->
      <form @submit.prevent="submitComment" class="add-comment-form">
        <textarea v-model="newComment" placeholder="写下你的评价..." rows="3"></textarea>
        <button type="submit" :disabled="!newComment.trim()" class="btn-submit">发表评论</button>
      </form>
    </div>
  </div>

  <div v-else-if="loading">加载中...</div>
  <div v-else>未找到电影信息</div>
</template>


<script>
import axios from 'axios';
import { useRoute } from 'vue-router';

export default {
  setup() {
    const route = useRoute();
    return { route };
  },
  data() {
    return {
      movie: null,
      loading: true,
      comments: [],
      newComment: '',
      currentUser: null,
      isAdmin: false
    };
  },
  mounted() {
    this.fetchMovieDetail();
    this.fetchComments();
    this.getCurrentUser();
  },
  methods: {
    async fetchMovieDetail() {
      try {
        const movieId = this.route.query.movieId;
        const token = localStorage.getItem('token');
        const response = await axios.get(`http://localhost:9000/api/movies/${movieId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });
        this.movie = response.data;
        this.loading = false;
      } catch (error) {
        console.error('获取电影详情失败:', error);
        this.loading = false;
      }//111
    },
    async fetchComments() {
      try {
        const movieId = this.route.query.movieId;
        const token = localStorage.getItem('token');
        const headers = token ? { Authorization: 'Bearer ' + token } : {};
        const response = await axios.get(`http://localhost:9000/api/comments/movie/${movieId}`, { headers });
        this.comments = response.data;
      } catch (e) {
        this.comments = [];
      }
    },
    async submitComment() {
      if (!this.newComment.trim()) return;
      const token = localStorage.getItem('token');
      const movieId = this.route.query.movieId;
      try {
        await axios.post('http://localhost:9000/api/comments', {
          movieId,
          content: this.newComment
        }, {
          headers: { Authorization: 'Bearer ' + token }
        });
        this.newComment = '';
        this.fetchComments();
      } catch (e) {
        alert('发表评论失败');
      }
    },
    async deleteComment(commentId) {
      const token = localStorage.getItem('token');
      try {
        await axios.delete(`http://localhost:9000/api/comments/${commentId}`, {
          headers: { Authorization: 'Bearer ' + token }
        });
        this.fetchComments();
      } catch (e) {
        alert('删除失败');
      }
    },
    formatTime(time) {
      if (!time) return '';
      return new Date(time.replace('T', ' ')).toLocaleString();
    },
    canDelete(comment) {
      if (!this.currentUser) return false;
      return this.isAdmin || comment.user.id === this.currentUser.id;
    },
    getCurrentUser() {
      // 假设token中有用户id和角色，实际可根据后端接口调整
      const token = localStorage.getItem('token');
      if (!token) return;
      // 解析token获取用户信息（简单实现，生产建议后端提供接口）
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.currentUser = { id: payload.id, username: payload.sub };
      this.isAdmin = payload.roles && payload.roles.includes('ADMIN');
    }
  }
};
</script>

<style scoped>
/* 电影海报容器 */
.movie-poster-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

/* 电影海报样式 */
.movie-poster {
  width: 200px;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
}

/* 电影信息区域 */
.movie-info {
  margin-bottom: 30px;
  line-height: 1.8;
}

/* 评论区整体样式 */
.comment-section {
  margin-top: 30px;
  padding: 20px;
  background-color: #2e2e2e;
  border-radius: 8px;
}

/* 评论列表 */
.comment-list {
  list-style: none;
  padding: 0;
  max-height: 300px;
  overflow-y: auto;
  margin-bottom: 20px;
}

/* 单条评论样式 */
.comment-item {
  background-color: #1e1e1e;
  padding: 15px;
  border-radius: 6px;
  margin-bottom: 15px;
  position: relative;
}

/* 评论头部：用户名 + 时间 */
.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.comment-user {
  font-weight: bold;
  color: var(--primary-color);
}

.comment-time {
  font-size: 0.9em;
  color: #aaa;
}

/* 评论内容 */
.comment-content {
  white-space: pre-wrap;
  word-break: break-word;
}

/* 删除按钮 */
.btn-delete {
  margin-top: 10px;
  padding: 5px 10px;
  background-color: #ff4d4d;
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9em;
}

.btn-delete:hover {
  background-color: #cc0000;
}

/* 发表评论区域 */
.add-comment-form textarea {
  width: 100%;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid var(--primary-color);
  background-color: #2e2e2e;
  color: white;
  resize: vertical;
  min-height: 80px;
  margin-bottom: 10px;
}

.btn-submit {
  padding: 10px 15px;
  background-color: var(--primary-color);
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.btn-submit:hover {
  background-color: var(--secondary-color);
}

/* 滚动条样式 */
.comment-list::-webkit-scrollbar {
  width: 6px;
}

.comment-list::-webkit-scrollbar-thumb {
  background-color: var(--primary-color);
  border-radius: 3px;
}

.comment-list::-webkit-scrollbar-track {
  background-color: #1e1e1e;
}
</style>
