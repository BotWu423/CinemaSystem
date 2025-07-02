<script setup>

</script>

<template>
  <!-- 查看员工按钮 -->
  <button @click="fetchEmployees" class="view-employees-btn">查看员工</button>
  <!-- 新增员工按钮（仅当已显示员工列表时出现） -->
  <div v-if="showEmployees && isAdmin">
    <button @click="addEmployeeForm" class="add-employee-btn">新增员工</button>
  </div>

  <!-- 新增员工表单 -->
  <div v-if="isAddingEmployee" class="add-employee-form">
    <h3>新增员工</h3>
    <form @submit.prevent="addEmployee">
      <div>
        <label>姓名:</label>
        <input type="text" v-model="newEmployee.name" required />
      </div>
      <div>
        <label>电话:</label>
        <input type="text" v-model="newEmployee.phone" />
      </div>
      <button type="submit">提交</button>
      <button @click="isAddingEmployee = false" type="button">取消</button>
    </form>
  </div>
  <!-- 修改员工表单 -->
  <div v-if="isEditingEmployee" class="edit-employee-form">
    <h3>修改员工信息</h3>
    <form @submit.prevent="updateEmployee">
      <div>
        <label>姓名:</label>
        <input type="text" v-model="editingEmployee.name" required />
      </div>
      <div>
        <label>电话:</label>
        <input type="text" v-model="editingEmployee.phone" />
      </div>
      <button type="submit">保存</button>
      <button @click="isEditingEmployee = false" type="button">取消</button>
    </form>
  </div>

  <!-- 员工列表 -->
  <div v-if="showEmployees" class="employee-list">
    <h3>影院员工</h3>
    <div v-if="employees.length === 0">暂无员工信息</div>
    <!-- CinemaDetail.vue -->
    <ul v-else>
      <li v-for="employee in employees" :key="employee.id" class="employee-item">
        <p><strong>姓名:</strong> {{ employee.name }}</p>
        <p><strong>电话:</strong> {{ employee.phone }}</p>
        <button v-if="isAdmin" @click="deleteEmployee(employee.id)" class="delete-employee-btn">删除</button>
        <button v-if="isAdmin" @click="editEmployee(employee)" class="edit-employee-btn">修改</button>
      </li>
    </ul>
  </div>

  <div class="cinema-detail">
    <h1>{{ cinema.name }}</h1>

    <div class="cinema-info">
      <div class="poster-section">
        <img :src="cinema.posterUrl || 'https://img.picui.cn/free/2025/07/01/686387241876a.jpg'"
             alt="影院海报"
             class="cinema-poster">
      </div>

      <div class="basic-info">
        <h2>基本信息</h2>
        <p><strong>地址:</strong> {{ cinema.address }}</p>
        <p><strong>联系方式:</strong> {{ cinema.contact }}</p>
        <p><strong>简介:</strong> {{ cinema.description }}</p>
      </div>
    </div>

    <div class="movies-section">
      <h2>当前热映</h2>
      <div v-if="loading">加载中...</div>
      <div v-else-if="error">{{ error }}</div>
      <div v-else>
        <div v-if="movies.length === 0">暂无上映电影</div>
        <ul class="movie-list">
          <li v-for="movie in movies" :key="movie.id" class="movie-item">
            <div class="movie-poster">
              <img :src="movie.posterUrl" :alt="movie.title + ' 海报'">
            </div>
            <div class="movie-details">
              <h3>{{ movie.title }}</h3>
              <p><strong>导演:</strong> {{ movie.director }}</p>
              <p><strong>演员:</strong> {{ movie.actors }}</p>
              <p><strong>类型:</strong> {{ movie.type }}</p>
              <p><strong>时长:</strong> {{ movie.duration }} 分钟</p>
              <p><strong>评分:</strong> {{ movie.rating ? movie.rating : '暂无' }}</p>
              <p><strong>上映日期:</strong> {{ movie.releaseDate }}</p>
              <p><strong>简介:</strong> {{ movie.description }}</p>
              <button @click="goToScreenings(movie.id)">查看场次</button>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="comment-section">
      <h2>评价区</h2>
      <div v-if="comments.length === 0">暂无评论</div>
      <ul>
        <li v-for="comment in comments" :key="comment.id" class="comment-item">
          <span class="comment-user">{{ comment.user.username }}</span>：
          <span class="comment-content">{{ comment.content }}</span>
          <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
          <button v-if="canDelete(comment)" @click="deleteComment(comment.id)">删除</button>
        </li>
      </ul>
      <div class="add-comment">
        <textarea v-model="newComment" placeholder="写下你的评价..." rows="2"></textarea>
        <button @click="submitComment" :disabled="!newComment.trim()">发表评论</button>
      </div>
    </div>
  </div>
</template>
<script>import axios from 'axios';
export default {
  data() {
    return {
      cinema: {
        name: '',
        address: '',
        contact: '',
        description: '',
        posterUrl: ''
      },
      movies: [],
      loading: true,
      error: null,
      // 新增评论相关数据
      comments: [],
      newComment: '',
      currentUser: null,
      isAdmin: false,
      // 新增员工相关数据
      employees: [],
      showEmployees: false,
      newEmployee: {
        name: '',
        phone: ''
      },
      isAddingEmployee: false, // 控制是否显示新增表单
      // 编辑员工相关
      isEditingEmployee: false,
      editingEmployee: null, // 当前正在编辑的员工对象
    };
  },
  created() {
    this.fetchCinemaDetails();
    this.fetchCurrentMovies();
    this.getCurrentUser();
    this.fetchComments();
  },
  methods: {
    editEmployee(employee) {
      this.editingEmployee = { ...employee }; // 拷贝一份用于编辑
      this.isEditingEmployee = true;
    },
    async updateEmployee() {
      const token = localStorage.getItem('token');

      try {
        await axios.put(
            `http://localhost:9000/api/employees/${this.editingEmployee.id}`,
            this.editingEmployee,
            {
              headers: {
                Authorization: 'Bearer ' + token
              }
            }
        );
        this.isEditingEmployee = false;
        this.fetchEmployees(); // 刷新员工列表
      } catch (error) {
        console.error('更新员工失败:', error);
        alert('更新员工失败');
      }
    },

    async deleteEmployee(employeeId) {
      const token = localStorage.getItem('token');

      if (!confirm('确定要删除该员工吗？')) {
        return;
      }

      try {
        await axios.delete(`http://localhost:9000/api/employees/${employeeId}`, {
          headers: {
            Authorization: 'Bearer ' + token
          }
        });
        this.fetchEmployees(); // 刷新员工列表
      } catch (error) {
        console.error('删除员工失败:', error);
        alert('删除员工失败');
      }
    },

    async fetchEmployees() {
      if (this.showEmployees) {
        this.showEmployees = false; // 如果已显示，则关闭
        return;
      }
      const cinemaId = this.$route.query.cinemaId;
      const token = localStorage.getItem('token');

      try {
        const response = await axios.get(`http://localhost:9000/api/employees/cinema/${cinemaId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });
        this.employees = response.data;
        this.showEmployees = true;
      } catch (error) {
        console.error('获取员工失败:', error);
        this.employees = [];
        this.showEmployees = false;
        alert('无法加载员工信息');
      }
    },
    addEmployeeForm() {
      this.isAddingEmployee = true;
    },
    async addEmployee() {
      const cinemaId = this.$route.query.cinemaId;
      const token = localStorage.getItem('token');

      try {
        await axios.post(
            'http://localhost:9000/api/employees',
            {
              name: this.newEmployee.name,
              phone: this.newEmployee.phone
            },
            {
              params: { cinemaId },
              headers: {
                Authorization: 'Bearer ' + token
              }
            }
        );
        this.newEmployee.name = '';
        this.newEmployee.phone = '';
        this.isAddingEmployee = false;
        this.fetchEmployees(); // 刷新员工列表
      } catch (error) {
        console.error('添加员工失败:', error);
        alert('添加员工失败');
      }
    },
    async fetchCinemaDetails() {
      try {
        const cinemaId = this.$route.query.cinemaId;
        const token = localStorage.getItem('token');

        const response = await axios.get(`http://localhost:9000/api/cinemas/${cinemaId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        this.cinema = response.data;
      } catch (error) {
        console.error('获取影院详情失败:', error);
        this.error = '无法加载影院详情';
      }
    },

    async fetchCurrentMovies() {
      try {
        const cinemaId = this.$route.query.cinemaId;
        const token = localStorage.getItem('token');

        // 获取该影院正在上映的电影列表
        const response = await axios.get(`http://localhost:9000/api/movies?cinemaId=${cinemaId}`, {
          headers: {
            'Authorization': 'Bearer ' + token
          }
        });

        this.movies = response.data;
        this.loading = false;
      } catch (error) {
        console.error('获取电影列表失败:', error);
        this.error = '无法加载电影信息';
        this.loading = false;
      }
    },

    goToScreenings(movieId) {
      this.$router.push({
        path: '/screenings',
        query: {
          cinemaId: this.$route.query.cinemaId,
          movieId: movieId,
          cinemaName: this.cinema.name
        }
      });
    },
    async fetchComments() {
      try {
        const cinemaId = this.$route.query.cinemaId;
        const token = localStorage.getItem('token');
        const headers = token ? { Authorization: 'Bearer ' + token } : {};
        const response = await axios.get(`http://localhost:9000/api/cinema-comments/cinema/${cinemaId}`, { headers });
        this.comments = response.data;
      } catch (e) {
        this.comments = [];
      }
    },

    async submitComment() {
      if (!this.newComment.trim()) return;
      const token = localStorage.getItem('token');
      const cinemaId = this.$route.query.cinemaId;

      try {
        await axios.post('http://localhost:9000/api/cinema-comments', {
          cinemaId,
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
        await axios.delete(`http://localhost:9000/api/cinema-comments/${commentId}`, {
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
      const token = localStorage.getItem('token');
      if (!token) return;
      const payload = JSON.parse(atob(token.split('.')[1]));
      this.currentUser = { id: payload.id, username: payload.sub };
      this.isAdmin = payload.roles && payload.roles.includes('ADMIN');
    },
  }
};
</script>
<style scoped>
.delete-employee-btn {
  margin-top: 5px;
  padding: 4px 8px;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.delete-employee-btn:hover {
  background-color: #c0392b;
}

.view-employees-btn {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.view-employees-btn:hover {
  background-color: #2980b9;
}

.employee-list {
  margin-top: 20px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #eee;
}

.employee-item {
  border-bottom: 1px solid #eee;
  padding: 10px 0;
}

.employee-item:last-child {
  border-bottom: none;
}

.cinema-detail {
  padding: 20px;
}

.cinema-info {
  display: flex;
  margin-bottom: 30px;
}

.poster-section {
  flex: 1;
  max-width: 300px;
  margin-right: 30px;
}

.cinema-poster {
  width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.basic-info {
  flex: 2;
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
}

.basic-info h2 {
  margin-top: 0;
}

.movie-list {
  list-style: none;
  padding: 0;
}

.movie-item {
  display: flex;
  border-bottom: 1px solid #eee;
  padding: 20px 0;
  margin-bottom: 20px;
}

.movie-poster {
  width: 150px;
  margin-right: 20px;
}

.movie-poster img {
  width: 100%;
  height: auto;
  border-radius: 4px;
}

.movie-details {
  flex: 1;
}
.comment-section {
  margin-top: 40px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}
.comment-item {
  margin-bottom: 10px;
}
.comment-user {
  font-weight: bold;
  margin-right: 8px;
}
.comment-content {
  margin-right: 8px;
}
.comment-time {
  color: #888;
  font-size: 12px;
  margin-left: 8px;
}
.add-comment {
  margin-top: 16px;
}
.add-comment textarea {
  width: 100%;
  resize: vertical;
  border-radius: 4px;
  border: 1px solid #ccc;
  padding: 6px;
}
.add-comment button {
  margin-top: 8px;
  padding: 6px 16px;
  background: #42b983;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.add-comment button:disabled {
  background: #ccc;
  cursor: not-allowed;
}
.add-employee-form {
  margin-top: 20px;
  padding: 15px;
  background-color: #f1f1f1;
  border-radius: 8px;
}

.add-employee-form div {
  margin-bottom: 10px;
}

.add-employee-form label {
  display: inline-block;
  width: 80px;
  font-weight: bold;
}

.add-employee-form input {
  padding: 6px;
  width: 200px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.add-employee-btn {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #2ecc71;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-employee-btn:hover {
  background-color: #27ae60;
}
.edit-employee-btn {
  margin-top: 5px;
  padding: 4px 8px;
  background-color: #e91e63; /* 粉色 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

</style>