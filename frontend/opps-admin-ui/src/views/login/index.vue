<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">OpsPanel Admin</h2>
      <form @submit.prevent="handleLogin">
        <input
          v-model="username"
          placeholder="Username"
          required
          class="input"
        />
        <input
          type="password"
          v-model="password"
          placeholder="Password"
          required
          class="input"
        />
        <button type="submit" class="login-btn">Login</button>
      </form>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { setToken } from '@/utils/auth'
import request from '@/utils/request'

const router = useRouter()
const username = ref('')
const password = ref('')
const errorMessage = ref('')

const handleLogin = async () => {
  try {
    const res = await request.post('/api/auth/login', {
      username: username.value,
      password: password.value
    })
    if (res.code === 200 && res.data.token) {
      setToken(res.data.token)
      router.push('/dashboard')
    } else {
      errorMessage.value = res.msg || 'Login failed'
    }
  } catch (err) {
    errorMessage.value = err.message || 'Network error'
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #f0f2f5;
}
.login-box {
  background: #fff;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
  width: 320px;
  text-align: center;
}
.title {
  margin-bottom: 20px;
}
.input {
  width: 100%;
  padding: 10px;
  margin-bottom: 16px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.login-btn {
  width: 100%;
  padding: 10px;
  background: #409eff;
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}
.error {
  color: #ff4d4f;
  margin-top: 10px;
}
</style>
