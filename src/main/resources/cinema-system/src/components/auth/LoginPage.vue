<template>
  <el-container style="height: 100vh; background-color: #f5f7fa">
    <el-main style="display: flex; justify-content: center; align-items: center">
      <el-card class="box-card" style="width: 400px">
        <h2 style="text-align: center">用户登录</h2>
        <el-form :model="form" label-position="top">
          <el-form-item label="用户名">
            <el-input v-model="form.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
          </el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%">登录</el-button>
        </el-form>
      </el-card>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const form = ref({
  username: '',
  password: ''
})

const router = useRouter()

const handleLogin = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/auth/login', {
      username: form.value.username,
      password: form.value.password
    }, {
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token') // 如果需要 token 认证
      }
    })

    const token = response.data.token

    // 存储 token 到 localStorage
    localStorage.setItem('token', token)

    // 登录成功后跳转到主页或其他页面
    await router.push('/movies')
  } catch (error) {
    console.error('登录失败:', error.response?.data || error.message)
    alert('登录失败，请检查用户名或密码')
  }
}
</script>