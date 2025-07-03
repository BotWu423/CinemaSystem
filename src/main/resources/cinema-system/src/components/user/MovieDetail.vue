<template>
  <div class="movie-detail" v-if="movie">
    <h1>{{ movie.title }}</h1>
    <img :src="require('@/assets/image/default-poster.jpg')" alt="海报" width="200" />
    <div class="info">
      <p><strong>导演:</strong> {{ movie.director }}</p>
      <p><strong>演员:</strong> {{ movie.actors }}</p>
      <p><strong>类型:</strong> {{ movie.type }}</p>
      <p><strong>时长:</strong> {{ movie.duration }} 分钟</p>
      <p><strong>评分:</strong> {{ movie.rating ? movie.rating : '暂无' }}</p>
      <p><strong>上映日期:</strong> {{ movie.releaseDate }}</p>
      <p><strong>简介:</strong> {{ movie.description }}</p>
    </div>
    <div class="comment-section">
      <h2>评价区</h2>
      <div v-if="comments.length === 0">暂无评论</div>
      <ul>
        <li v-for="comment in comments" :key="comment.id" class="comment-item">
          <span class="comment-user">{{ comment.user.username }}</span>：
          <span class="comment-content">{{ comment.content }}</span>
          <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
          <Button v-if="canDelete(comment)" @click="deleteComment(comment.id)">删除</Button>
        </li>
      </ul>
      <div class="add-comment">
        <textarea v-model="newComment" placeholder="写下你的评价..." rows="2"></textarea>
        <Button @click="submitComment" :disabled="!newComment.trim()">发表评论</Button>
      </div>
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

</style> 