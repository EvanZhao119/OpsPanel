// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router'

// Top-level pages
import LoginPage from '@/views/login/LoginPage.vue'
import DashboardPage from '@/views/dashboard/DashboardPage.vue'

// System module pages
import UserPage from '@/views/system/user/UserPage.vue'
import RolePage from '@/views/system/role/RolePage.vue'
import MenuPage from '@/views/system/menu/MenuPage.vue'
import DeptPage from '@/views/system/dept/DeptPage.vue'
import UserRolePage from '@/views/system/userRole/UserRolePage.vue'
import RoleMenuPage from '@/views/system/roleMenu/RoleMenuPage.vue'
import LogPage from '@/views/system/log/LogPage.vue'
import TaskJobPage from '@/views/task/TaskJobPage.vue'
import InsightPage from '@/views/insight/InsightPage.vue'


const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: DashboardPage
  },
  {
    path: '/insight',
    name: 'Insight',
    component: InsightPage
  },  
  {
    path: '/system',
    name: 'System',
    redirect: '/system/user',
    children: [
      { path: 'user', name: 'User', component: UserPage },
      { path: 'role', name: 'Role', component: RolePage },
      { path: 'menu', name: 'Menu', component: MenuPage },
      { path: 'dept', name: 'Dept', component: DeptPage },
      { path: 'userRole', name: 'UserRole', component: UserRolePage },
      { path: 'roleMenu', name: 'RoleMenu', component: RoleMenuPage },
      { path: 'log', name: 'Log', component: LogPage },
      { path: 'task', name: 'Task', component: TaskJobPage }
    ]
  }
]

const router = createRouter({
  // history: createWebHistory(),
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
