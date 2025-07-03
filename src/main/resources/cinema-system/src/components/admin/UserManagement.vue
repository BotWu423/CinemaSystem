<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const users = ref([]);
const loading = ref(true);

const fetchUsers = async () => {
  loading.value = true;
  const token = localStorage.getItem('token');
  const res = await axios.get('http://localhost:9000/api/users/all', {
    headers: { Authorization: 'Bearer ' + token }
  });
  users.value = res.data;
  loading.value = false;
};

const resetPassword = async (user) => {
  if (!confirm(`确定要重置用户【${user.username}】的密码为123456吗？`)) return;
  const token = localStorage.getItem('token');
  await axios.post(`http://localhost:9000/api/users/${user.id}/reset-password`, {}, {
    headers: { Authorization: 'Bearer ' + token }
  });
  alert('密码已重置为123456');
};

const changeRole = async (user, event) => {
  const newRole = event.target.value;
  if (user.role === newRole) return;
  if (!confirm(`确定要将用户【${user.username}】的权限更改为${newRole === 'ADMIN' ? '管理员' : '普通用户'}吗？`)) {
    // 恢复下拉框显示原角色
    event.target.value = user.role;
    return;
  }
  const token = localStorage.getItem('token');
  try {
    await axios.post(`http://localhost:9000/api/users/${user.id}/change-role`, { role: newRole }, {
      headers: { Authorization: 'Bearer ' + token }
    });
    alert('权限已更改');
    await fetchUsers(); // 重新拉取用户列表，确保数据一致
  } catch (e) {
    alert('更改权限失败：' + (e.response?.data || e.message));
    event.target.value = user.role; // 恢复下拉框显示原角色
  }
};

onMounted(fetchUsers);
</script>

<template>
  <div class="add-movie">
    <h1>用户管理</h1>
    <div v-if="loading">加载中...</div>
    <table v-else class="user-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>用户名</th>
          <th>邮箱</th>
          <th>电话</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id">
          <td>{{ user.id }}</td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.phone }}</td>
          <td>
            <button @click="resetPassword(user)">重置密码</button>
            <select :value="user.role" @change="changeRole(user, $event)" style="margin-left:10px;">
              <option value="ADMIN">管理员</option>
              <option value="NORMAL">普通用户</option>
            </select>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>

</style>