<template>
    <header class="header-bar">
      <div class="left">OpsPanel Admin</div>
      <div class="right">
        <span>{{ username }}</span>
        <button @click="logout">Logout</button>
      </div>
    </header>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { clearTokens } from '@/utils/auth'
  
  const router = useRouter()
  const username = ref('admin') // TODO: 从后端userInfo接口加载
  
  const logout = () => {
    clearTokens()
    router.push('/login')
  }
  </script>
  
  <style scoped>
  .header-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    height: 60px;
    background: #fff;
    border-bottom: 1px solid #eee;
  }
  .right button {
    margin-left: 10px;
    padding: 5px 12px;
    background: #409eff;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  </style>
  