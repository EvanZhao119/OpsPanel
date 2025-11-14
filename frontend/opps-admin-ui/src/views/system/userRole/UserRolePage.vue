<template>
    <div class="page">
      <el-form :inline="true" :model="query" class="mb-12">
        <el-form-item label="User ID"><el-input-number v-model="query.userId" :min="1" /></el-form-item>
        <el-button type="primary" @click="loadData">Load</el-button>
      </el-form>
  
      <el-card v-if="query.userId">
        <template #header> Assign Roles to User #{{ query.userId }} </template>
        <el-checkbox-group v-model="checkedRoleIds">
          <el-checkbox v-for="r in allRoles" :key="r.id" :label="r.id">{{ r.name }} ({{ r.key }})</el-checkbox>
        </el-checkbox-group>
        <div class="mt-12">
          <el-button type="primary" @click="save">Save</el-button>
          <el-button type="danger" @click="removeAll">Remove All</el-button>
        </div>
      </el-card>
    </div>
  </template>
  
  <script setup>
  import { reactive, ref } from 'vue'
  import request from '@/utils/request'
  import { ElMessage } from 'element-plus'
  
  // state
  const query = reactive({ userId: null })
  const allRoles = ref([])         // list all roles (paged fetch simple)
  const checkedRoleIds = ref([])   // selected role ids for user
  
  async function loadData() {
    if (!query.userId) return
    // 1) load user role ids
    const roleIds = await request({ url: '/api/system/user-role/list', method: 'get', params: { userId: query.userId } })
    checkedRoleIds.value = roleIds || []
  
    // 2) load all roles (first page big size)
    const res = await request({ url: '/api/system/role/list', method: 'get', params: { pageNum: 1, pageSize: 1000 } })
    allRoles.value = res.records || res.rows || res.data || []
  }
  
  async function save() {
    if (!query.userId) return
    await request({ url: '/api/system/user-role/assign', method: 'post', data: { userId: query.userId, roleIds: checkedRoleIds.value } })
    ElMessage.success('Saved')
  }
  
  async function removeAll() {
    if (!query.userId) return
    await request({ url: `/api/system/user-role/${query.userId}`, method: 'delete' })
    checkedRoleIds.value = []
    ElMessage.success('Removed')
  }
  </script>
  
  <style scoped>
  .page{ padding:16px; }
  .mb-12{ margin-bottom:12px; }
  .mt-12{ margin-top:12px; }
  </style>
  