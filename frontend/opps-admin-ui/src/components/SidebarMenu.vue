<template>
  <aside class="sidebar">
    <ul>
      <li
        v-for="item in menuList"
        :key="item.path"
        @click="navigate(item.path)"
        class="menu-item"
      >
        {{ item.name }}
      </li>
    </ul>
  </aside>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const menuList = ref([
  { name: 'Dashboard', path: '/dashboard' },
  { name: 'User Management', path: '/system/user' },
  { name: 'Role Management', path: '/system/role' },
  { name: 'Menu Management', path: '/system/menu' },
  { name: 'Department', path: '/system/dept' },
  { name: 'User-Role Mapping', path: '/system/userRole' },
  { name: 'Role-Menu Mapping', path: '/system/roleMenu' },
  { name: 'Task Management', path: '/system/task' },
  { name: 'Log Center', path: '/system/log' }
])

const navigate = (path) => {
  router.push(path)
}
</script>

<style scoped>
.sidebar {
  width: 220px;
  background: #001529;
  color: #fff;
  height: 100vh;
  padding-top: 20px;
}

.menu-item {
  padding: 10px 24px;
  cursor: pointer;
  list-style: none;
  transition: background 0.3s;
}

.menu-item:hover {
  background: #1890ff;
}
</style>
