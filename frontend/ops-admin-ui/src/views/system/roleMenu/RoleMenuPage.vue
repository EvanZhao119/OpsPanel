<template>
    <div class="page">
      <el-form :inline="true" :model="query" class="mb-12">
        <el-form-item label="Role ID"><el-input-number v-model="query.roleId" :min="1" /></el-form-item>
        <el-button type="primary" @click="loadData">Load</el-button>
      </el-form>
  
      <el-card v-if="query.roleId">
        <template #header> Assign Menus to Role #{{ query.roleId }} </template>
        <el-tree
          ref="treeRef"
          :data="menuTree"
          node-key="id"
          show-checkbox
          :default-checked-keys="checkedMenuIds"
          :props="{ children:'children', label:'name' }"
          class="menu-tree"
        />
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
  
  const query = reactive({ roleId: null })
  const treeRef = ref()
  const menuTree = ref([])
  const checkedMenuIds = ref([])
  
  async function loadData() {
    if (!query.roleId) return
    // 1) current checked menu ids
    const ids = await request({ url: '/system/role-menu/list', method:'get', params:{ roleId: query.roleId } })
    checkedMenuIds.value = ids || []
  
    // 2) full menu tree
    const res = await request({ url:'/system/menu/list', method:'get' })
    menuTree.value = res.records || res.data || res || []
    // sync default checked after data set
    setTimeout(()=>{ treeRef.value.setCheckedKeys(checkedMenuIds.value) }, 0)
  }
  
  async function save(){
    const keys = treeRef.value.getCheckedKeys(true) // true: leaf only
    await request({ url:'/system/role-menu/assign', method:'post', data:{ roleId: query.roleId, menuIds: keys } })
    ElMessage.success('Saved')
  }
  
  async function removeAll(){
    await request({ url:`/system/role-menu/${query.roleId}`, method:'delete' })
    treeRef.value.setCheckedKeys([])
    ElMessage.success('Removed')
  }
  </script>
  
  <style scoped>
  .page{ padding:16px; }
  .mb-12{ margin-bottom:12px; }
  .mt-12{ margin-top:12px; }
  .menu-tree{ border:1px solid var(--el-border-color); padding:12px; border-radius:6px; }
  </style>
  