<template>
  <div class="page">
    <!-- Search Bar -->
    <el-form :inline="true" :model="query" class="mb-12">
      <el-form-item label="Username">
        <el-input v-model="query.username" placeholder="username" clearable />
      </el-form-item>

      <el-form-item label="Status">
        <el-select v-model="query.status" placeholder="status" clearable>
          <el-option label="Enabled" :value="1" />
          <el-option label="Disabled" :value="0" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="loadTable">Search</el-button>
        <el-button @click="resetQuery">Reset</el-button>
      </el-form-item>
    </el-form>

    <!-- Toolbar -->
    <div class="mb-12">
      <el-button type="primary" @click="openCreate">Add User</el-button>
      <el-button type="danger" :disabled="!selection.length" @click="batchRemove">Delete Selected</el-button>
    </div>

    <!-- Table -->
    <el-table v-loading="loading" :data="tableData" @selection-change="selection = $event">
      <el-table-column type="selection" width="48" />
      <el-table-column prop="userId" label="ID" width="80" />
      <el-table-column prop="username" label="Username" />
      <el-table-column prop="nickName" label="Nickname" />
      <el-table-column prop="email" label="Email" />
      <el-table-column prop="deptId" label="Dept ID" width="100" />

      <el-table-column label="Status" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="Created At" width="180" />

      <el-table-column label="Actions" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">Edit</el-button>
          <el-button link type="danger" @click="remove(row.userId)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pager -->
    <div class="mt-12 flex-end">
      <el-pagination
        background
        layout="prev, pager, next, jumper, ->, total"
        :total="total"
        :page-size="pageSize"
        :current-page="pageNum"
        @current-change="(p)=>{pageNum=p;loadTable()}"
      />
    </div>

    <!-- Dialog -->
    <el-dialog v-model="dialog.visible" :title="dialog.mode==='create'?'Create User':'Edit User'">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">

        <el-form-item label="Username" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item v-if="dialog.mode==='create'" label="Password" prop="password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>

        <el-form-item label="Nickname">
          <el-input v-model="form.nickName" />
        </el-form-item>

        <el-form-item label="Email">
          <el-input v-model="form.email" />
        </el-form-item>

        <el-form-item label="Dept ID">
          <el-input-number v-model="form.deptId" :min="1" />
        </el-form-item>

        <el-form-item label="Status">
          <el-select v-model="form.status">
            <el-option label="Enabled" :value="1" />
            <el-option label="Disabled" :value="0" />
          </el-select>
        </el-form-item>

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

// table state
const loading = ref(false)
const tableData = ref([])
const selection = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// query
const query = reactive({ username: '', status: null })

function resetQuery() {
  query.username = ''
  query.status = null
  pageNum.value = 1
  loadTable()
}

async function loadTable() {
  loading.value = true
  try {
    const res = await request({
      url: '/api/system/user/list',
      method: 'get',
      params: { pageNum: pageNum.value, pageSize: pageSize.value, ...query }
    })

    // 正确数据结构：res.data.records / res.data.total
    const page = res.data || {}

    tableData.value = page.records || []
    total.value = page.total || 0

  } finally {
    loading.value = false
  }
}

// dialog
const dialog = reactive({ visible: false, mode: 'create' })
const formRef = ref()
const form = reactive({
  userId: null,
  username: '',
  password: '',
  nickName: '',
  email: '',
  deptId: 1,
  status: 1
})

const rules = {
  username: [{ required: true, message: 'Username is required', trigger: 'blur' }],
  password: [{ required: true, message: 'Password is required', trigger: 'blur' }]
}

function openCreate() {
  dialog.mode = 'create'
  Object.assign(form, {
    userId: null,
    username: '',
    password: '',
    nickName: '',
    email: '',
    deptId: 1,
    status: 1
  })
  dialog.visible = true
}

function openEdit(row) {
  dialog.mode = 'edit'
  Object.assign(form, { ...row, password: '' })
  dialog.visible = true
}

async function submit() {
  await formRef.value.validate()
  if (dialog.mode === 'create') {
    await request({ url: '/api/system/user', method: 'post', data: form })
    ElMessage.success('Created')
  } else {
    await request({ url: '/api/system/user', method: 'put', data: form })
    ElMessage.success('Updated')
  }
  dialog.visible = false
  loadTable()
}

async function remove(id) {
  await ElMessageBox.confirm('Confirm to delete this user?', 'Warning')
  await request({ url: `/api/system/user/${id}`, method: 'delete' })
  ElMessage.success('Deleted')
  loadTable()
}

async function batchRemove() {
  await ElMessageBox.confirm('Confirm to delete selected users?', 'Warning')
  await Promise.all(
    selection.value.map(r =>
      request({ url: `/api/system/user/${r.userId}`, method: 'delete' })
    )
  )
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
