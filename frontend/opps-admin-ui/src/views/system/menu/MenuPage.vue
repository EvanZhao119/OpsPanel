<template>
    <div class="page">
      <el-form :inline="true" :model="query" class="mb-12">
        <el-form-item label="Menu Name"><el-input v-model="query.name" clearable/></el-form-item>
        <el-form-item label="Status"  class="status-form-item">
          <el-select v-model="query.status" clearable>
            <el-option label="Enabled" :value="0" />
            <el-option label="Disabled" :value="1" />
          </el-select>
        </el-form-item>
        <el-button type="primary" @click="loadTable">Search</el-button>
        <el-button @click="resetQuery">Reset</el-button>
      </el-form>
  
      <div class="mb-12">
        <el-button type="primary" @click="openCreate">Add Menu</el-button>
      </div>
  
      <el-table v-loading="loading" :data="tableData" row-key="id" default-expand-all :tree-props="{ children: 'children' }">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="Menu Name" />
        <el-table-column prop="path" label="Path" />
        <el-table-column prop="component" label="Component" />
        <el-table-column prop="type" label="Type" width="100" />
        <el-table-column prop="perms" label="Perms" />
        <el-table-column prop="sort" label="Sort" width="80" />
        <el-table-column prop="status" label="Status" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status===0?'success':'info'">{{ row.status===0?'Enabled':'Disabled' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">Edit</el-button>
            <el-button link type="danger" @click="remove(row.id)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog v-model="dialog.visible" :title="dialog.mode==='create'?'Create Menu':'Edit Menu'">
        <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
          <el-form-item label="Parent ID"><el-input-number v-model="form.parentId" :min="0"/></el-form-item>
          <el-form-item label="Name" prop="name"><el-input v-model="form.name"/></el-form-item>
          <el-form-item label="Path"><el-input v-model="form.path"/></el-form-item>
          <el-form-item label="Component"><el-input v-model="form.component"/></el-form-item>
          <el-form-item label="Type"><el-select v-model="form.type"><el-option label="Dir" :value="0"/><el-option label="Menu" :value="1"/><el-option label="Button" :value="2"/></el-select></el-form-item>
          <el-form-item label="Perms"><el-input v-model="form.perms"/></el-form-item>
          <el-form-item label="Sort"><el-input-number v-model="form.sort" :min="0"/></el-form-item>
          <el-form-item label="Status"><el-select v-model="form.status"><el-option label="Enabled" :value="0"/><el-option label="Disabled" :value="1"/></el-select></el-form-item>
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
  const query = reactive({ name: '', status: null })
  
  function resetQuery() { query.name=''; query.status=null; loadTable() }
  
  async function loadTable() {
    loading.value = true
    try {
      const res = await request({ url:'/api/system/menu/list', method:'get', params: query })
      // Expect tree structure or flat list; if flat, you can convert to tree by yourself.
      tableData.value = res.records || res.data || res || []
    } finally { loading.value = false }
  }
  
  const dialog = reactive({ visible:false, mode:'create' })
  const formRef = ref()
  const form = reactive({ id:null, parentId:0, name:'', path:'', component:'', type:1, perms:'', sort:0, status:0 })
  const rules = { name:[{ required:true, message:'Name is required', trigger:'blur' }] }
  
  function openCreate(){ dialog.mode='create'; Object.assign(form,{ id:null,parentId:0,name:'',path:'',component:'',type:1,perms:'',sort:0,status:0 }); dialog.visible=true }
  function openEdit(row){ dialog.mode='edit'; Object.assign(form,row); dialog.visible=true }
  
  async function submit(){
    await formRef.value.validate()
    if(dialog.mode==='create'){ await request({ url:'/api/system/menu', method:'post', data: form }); ElMessage.success('Created') }
    else { await request({ url:'/api/system/menu', method:'put', data: form }); ElMessage.success('Updated') }
    dialog.visible=false; loadTable()
  }
  
  async function remove(id){
    await ElMessageBox.confirm('Confirm to delete this menu?', 'Warning', { type:'warning' })
    await request({ url:`/api/system/menu/${id}`, method:'delete' })
    ElMessage.success('Deleted'); loadTable()
  }
  
  onMounted(loadTable)
  </script>
  
  <style scoped>
  .page{ padding:16px; }
  .mb-12{ margin-bottom:12px; }
  .status-form-item { min-width: 160px; }
  </style>
  