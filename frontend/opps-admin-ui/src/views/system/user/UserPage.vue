<template>
  <div class="page">
    <!-- ======================= Search Bar ======================= -->
    <el-form :inline="true" :model="query" class="mb-12">
      <el-form-item label="Username">
        <el-input v-model="query.username" placeholder="username" clearable />
      </el-form-item>

      <el-form-item label="Status" class="status-form-item">
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

    <!-- ======================= Toolbar ======================= -->
    <div class="mb-12">
      <el-button type="primary" @click="openCreate">Add User</el-button>

      <el-button
        type="danger"
        :disabled="selection.length === 0"
        @click="batchRemove()"
      >
        Delete Selected
      </el-button>
    </div>

    <!-- ======================= User Table ======================= -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="onSelectionChange"
    >
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

      <!-- Action Column -->
      <el-table-column label="Actions" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">Edit</el-button>

          <el-button
            link
            type="danger"
            @click="batchRemove([row.userId])"
          >
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- ======================= Pagination ======================= -->
    <div class="mt-12 flex-end">
      <el-pagination
        background
        layout="prev, pager, next, jumper, ->, total"
        :total="total"
        :page-size="pageSize"
        :current-page="pageNum"
        @current-change="onPageChange"
      />
    </div>

    <!-- ======================= Create / Edit Dialog ======================= -->
    <el-dialog v-model="dialog.visible" :title="dialogTitle">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="Username" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item v-if="dialog.mode === 'create'" label="Password" prop="password">
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
        <el-button @click="dialog.visible = false">Cancel</el-button>
        <el-button type="primary" @click="submit">Submit</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/* ------------------------------------------------------------
 * User Management Page (Best Practice Version)
 * ------------------------------------------------------------
 * Features:
 *  - Paginated user list
 *  - Search form
 *  - Create / Edit user dialog
 *  - Delete single / multiple users
 *  - Clean and safe API calls
 *  - Fully reactive + error safeguarded
 * ------------------------------------------------------------ */

import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listUsers, createUser, updateUser, batchDeleteUsers } from '@/api/system/user'

/* ---------------------- Table State ---------------------- */
const loading = ref(false)
const tableData = ref([])
const selection = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

/* ---------------------- Search Form ---------------------- */
const query = reactive({
  username: '',
  status: null
})

/* ---------------------- Dialog State ---------------------- */
const dialog = reactive({
  visible: false,
  mode: 'create' // 'create' | 'edit'
})

const dialogTitle = computed(() =>
  dialog.mode === 'create' ? 'Create User' : 'Edit User'
)

/* ---------------------- Form ---------------------- */
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

const rules = computed(() => ({
  username: [{ required: true, message: 'Username is required', trigger: 'blur' }],
  password: dialog.mode === 'create'
    ? [{ required: true, message: 'Password is required', trigger: 'blur' }]
    : []
}))

/* ---------------------- Load Table ---------------------- */
async function loadTable() {
  loading.value = true
  try {
    const res = await listUsers({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...query
    })

    const page = res.data || {}
    tableData.value = page.records || []
    total.value = page.total || 0
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.username = ''
  query.status = null
  pageNum.value = 1
  loadTable()
}

/* ---------------------- Table Events ---------------------- */
function onSelectionChange(rows) {
  selection.value = rows
}

function onPageChange(p) {
  pageNum.value = p
  loadTable()
}

/* ---------------------- Create User ---------------------- */
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

/* ---------------------- Edit User ---------------------- */
function openEdit(row) {
  dialog.mode = 'edit'
  Object.assign(form, { ...row, password: '' })
  dialog.visible = true
}

/* ---------------------- Submit User ---------------------- */
async function submit() {
  await formRef.value.validate()

  if (dialog.mode === 'create') {
    await createUser(form)
    ElMessage.success('User created successfully')
  } else {
    await updateUser(form)
    ElMessage.success('User updated successfully')
  }

  dialog.visible = false
  loadTable()
}

/* ---------------------- Delete Users ---------------------- */
async function batchRemove(idList = null) {
  const ids = idList || selection.value.map(r => r.userId)

  if (!ids || ids.length === 0) {
    ElMessage.warning('Please select at least one user.')
    return
  }

  await ElMessageBox.confirm(`Confirm delete ${ids.length} user(s)?`, 'Warning')

  await batchDeleteUsers(ids)
  ElMessage.success('Deleted successfully')

  loadTable()
}

/* ---------------------- Init ---------------------- */
onMounted(loadTable)
</script>

<style scoped>
.page { padding: 16px; }
.mb-12 { margin-bottom: 12px; }
.mt-12 { margin-top: 12px; }
.flex-end { display: flex; justify-content: flex-end; }
.status-form-item { min-width: 160px; }
</style>