<template>
    <aside class="sidebar">
      <ul>
        <li
          v-for="item in menuList"
          :key="item.id"
          @click="navigate(item.path)"
          class="menu-item"
        >
          {{ item.name }}
        </li>
      </ul>
    </aside>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import request from '@/utils/request'
  
  const router = useRouter()
  const menuList = ref([])
  
  const fetchMenus = async () => {
    const res = await request.get('/api/system/menu/list')
    if (res.code === 200) {
      menuList.value = res.data
    }
  }
  
  onMounted(fetchMenus)
  
  const navigate = (path) => {
    router.push(path)
  }
  </script>
  
  <style scoped>
  .sidebar {
    width: 200px;
    background: #001529;
    color: #fff;
    padding-top: 20px;
  }
  .menu-item {
    padding: 10px 20px;
    cursor: pointer;
  }
  .menu-item:hover {
    background: #1890ff;
  }
  </style>
  