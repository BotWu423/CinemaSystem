<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const form = ref({
  username: '',
  password: '',
  email: '',
  phone: ''
})

const router = useRouter()

const handleRegister = async () => {
  if (!form.value.username || !form.value.password) {
    alert('用户名和密码不能为空！')
    return
  }
  try {
    await axios.post('http://localhost:9000/api/users/register', {
      username: form.value.username,
      password: form.value.password,
      email: form.value.email,
      phone: form.value.phone
    })
    alert('注册成功，请登录！')
    router.push('/login')
  } catch (error) {
    alert('注册失败：' + (error.response?.data?.message || error.message))
  }
}
</script>

<script>
export default {
  name: 'UserRegister'
}
</script>

<template>
  <el-container style="height: 100vh; background-color: #f5f7fa">
    <el-main style="display: flex; justify-content: center; align-items: center">
      <el-card class="box-card" style="width: 400px">
        <h2 style="text-align: center">用户注册</h2>
        <el-form :model="form" label-position="top">
          <el-form-item label="用户名">
            <el-input v-model="form.username" placeholder="请输入用户名"/>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" type="password" show-password placeholder="请输入密码"/>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" placeholder="请输入邮箱"/>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" placeholder="请输入手机号"/>
          </el-form-item>
          <el-button type="primary" @click="handleRegister" style="width: 100%">注册</el-button>
        </el-form>
      </el-card>
    </el-main>
  </el-container>
</template>

<style scoped>
</style>
