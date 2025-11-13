import { createRouter, createWebHistory } from 'vue-router'

import Dashboard from '@/views/dashboard/index.vue'
import Login from '@/views/login/index.vue'
import User from '@/views/system/user/index.vue'
import Role from '@/views/system/role/index.vue'
import Menu from '@/views/system/menu/index.vue'
import Dept from '@/views/system/dept/index.vue'
import UserRole from '@/views/system/userRole/index.vue'
import RoleMenu from '@/views/system/roleMenu/index.vue'
import Log from '@/views/system/log/index.vue'

const routes = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/system',
    name: 'System',
    redirect: '/system/user',
    children: [
      { path: 'user', name: 'User', component: User },
      { path: 'role', name: 'Role', component: Role },
      { path: 'menu', name: 'Menu', component: Menu },
      { path: 'dept', name: 'Dept', component: Dept },
      { path: 'userRole', name: 'UserRole', component: UserRole },
      { path: 'roleMenu', name: 'RoleMenu', component: RoleMenu },
      { path: 'log', name: 'Log', component: Log },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
