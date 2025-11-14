<template>
    <div class="page">
      <el-form :inline="true" :model="query" class="mb-12">
        <el-form-item label="Role Name">
          <el-input v-model="query.name" placeholder="name" clearable />
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="query.status" clearable>
            <el-option label="Enabled" :value="0" />
            <el-option label="Disabled" :value="1" />
          </el-select>
        </el-form-item>
        <el-button type="primary" @click="loadTable">Search</el-button>
        <el-button @click="resetQuery">Reset</el-button>
      </el-form>
  
      <div class="mb-12">
        <el-button type="primary" @click="openCreate">Add Role</el-button>
        <el-button type="danger" :disabled="!selection.length" @click="batchRemove">Delete Selected</el-button>
      </div>
  
      <el-table v-loading="loading" :data="tableData" @selection-change="selection=$event">
        <el-table-column type="selection" width="48" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Role Name" />
        <el-table-column prop="key" label="Role Key" />
        <el-table-column prop="status" label="Status" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status===0?'success':'info'">{{ row.status===0?'Enabled':'Disabled' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Created At" width="180" />
        <el-table-column label="Actions" width="220" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">Edit</el-button>
            <el-button link type="danger" @click="remove(row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <div class="mt-12 flex-end">
        <el-pagination
          background layout="prev, pager, next, jumper, ->, total"
          :total="total" :page-size="pageSize" :current-page="pageNum"
          @current-change="(p)=>{pageNum=p;loadTable()}"
        />
      </div>
  
      <el-dialog v-model="dialog.visible" :title="dialog.mode==='create'?'Create Role':'Edit Role'">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
          <el-form-item label="Role Name" prop="name"><el-input v-model="form.name" /></el-form-item>
          <el-form-item label="Role Key" prop="key"><el-input v-model="form.key" /></el-form-item>
          <el-form-item label="Status">
            <el-select v-model="form.status">
              <el-option label="Enabled" :value="0" />
              <el-option label="Disabled" :value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="Remark"><el-input v-model="form.remark" type="textarea" /></el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialog.visible=false">Cancel</el-button>
          <el-button type="primary" @click="submit">Submit</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import { ref, reactive, onMounted } from 'vue'
  import request from '@/utils/request'
  import { ElMessage, ElMessageBox } from 'element-plus'
  
  const loading = ref(false)
  const tableData = ref([])
  const selection = ref([])
  const total = ref(0)
  const pageNum = ref(1)
  const pageSize = ref(10)
  
  const query = reactive({ name: '', status: null })
  
  function resetQuery() {
    query.name = ''
    query.status = null
    pageNum.value = 1
    loadTable()
  }
  
  async function loadTable() {
    loading.value = true
    try {
      const res = await request({ url: '/api/system/role/list', method: 'get',
        params: { pageNum: pageNum.value, pageSize: pageSize.value, ...query } })
      tableData.value = res.records || res.rows || res.data || []
      total.value = res.total ?? 0
    } finally { loading.value = false }
  }
  
  const dialog = reactive({ visible: false, mode: 'create' })
  const formRef = ref()
  const form = reactive({ id: null, name: '', key: '', status: 0, remark: '' })
  const rules = {
    name: [{ required: true, message: 'Role name is required', trigger: 'blur' }],
    key: [{ required: true, message: 'Role key is required', trigger: 'blur' }]
  }
  
  function openCreate() {
    dialog.mode='create'
    Object.assign(form, { id:null, name:'', key:'', status:0, remark:'' })
    dialog.visible = true
  }
  
  function openEdit(row) {
    dialog.mode='edit'
    Object.assign(form, row)
    dialog.visible = true
  }
  
  async function submit() {
    await formRef.value.validate()
    if (dialog.mode==='create') {
      await request({ url:'/api/system/role', method:'post', data: form })
      ElMessage.success('Created')
    } else {
      await request({ url:'/api/system/role', method:'put', data: form })
      ElMessage.success('Updated')
    }
    dialog.visible=false
    loadTable()
  }
  
  async function remove(id) {
    await ElMessageBox.confirm('Confirm to delete this role?', 'Warning', { type:'warning' })
    await request({ url:`/api/system/role/${id}`, method:'delete' })
    ElMessage.success('Deleted')
    loadTable()
  }
  
  async function batchRemove() {
    await ElMessageBox.confirm('Confirm to delete selected roles?', 'Warning', { type:'warning' })
    await Promise.all(selection.value.map(r => request({ url:`/api/system/role/${r.id}`, method:'delete' })))
    ElMessage.success('Deleted')
    loadTable()
  }
  
  onMounted(loadTable)
  </script>
  
  <style scoped>
  .page { padding: 16px; }
  .mb-12 { margin-bottom: 12px; }
  .mt-12 { margin-top: 12px; }
  .flex-end { display: flex; justify-content: flex-end; }
  </style>
  