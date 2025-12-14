<template>
  <div id="app">
    <!-- Login page -->
    <template v-if="isLoginPage">
      <router-view />
    </template>

    <!-- Main layout -->
    <template v-else>
      <div class="layout">
        <SidebarMenu class="sidebar" />

        <div class="main">
          <header class="header">
            <h2 class="title">OpsPanel Admin</h2>

            <div class="user-area">
              <el-dropdown>
                <span class="el-dropdown-link">
                  {{ username }}
                  <el-icon><ArrowDown /></el-icon>
                </span>

                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="logout">Logout</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </header>

          <main class="content">
            <router-view />
          </main>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import SidebarMenu from '@/components/SidebarMenu.vue'
import { clearTokens } from '@/utils/auth'
import { ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

import { useUserStore } from '@/store/user'
import { getUserInfo } from '@/api/system/login'

/* Current route and router instance */
const route = useRoute()
const router = useRouter()

/* Pinia user store */
const userStore = useUserStore()

/* Determine if current page is the login page */
const isLoginPage = computed(() => route.path === '/login')

/* Read username from Pinia store */
const username = computed(() => userStore.username || 'User')

/* Auto-load user info after page refresh (if token exists) */
onMounted(async () => {
  // Only fetch user info when token exists and username is still empty
  if (userStore.token && !userStore.username) {
    const res = await getUserInfo()
    if (res.code === 200) {
      userStore.setUserInfo(res.data)
    }
  }
})

/* Logout logic */
const logout = async () => {
  await ElMessageBox.confirm('Confirm logout?', 'Logout', { type: 'warning' })
  clearTokens()
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}

/* Sidebar */
.sidebar {
  width: 220px;
  background-color: #001529;
  color: white;
  flex-shrink: 0;
  overflow-y: auto;
}

/* Main section */
.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #f5f6fa;
}

/* Header */
.header {
  background: #fff;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  border-bottom: 1px solid #e4e4e4;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.header .title {
  font-size: 20px;
  font-weight: 600;
}

.user-area {
  display: flex;
  align-items: center;
  font-weight: 500;
  color: #333;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}

/* Content area */
.content {
  flex: 1;
  padding: 20px;
  overflow: auto;
  background: #f0f2f5;
}
</style>
