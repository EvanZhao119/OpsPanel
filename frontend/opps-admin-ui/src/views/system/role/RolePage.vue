<template>
  <div class="page">
    <!-- ======================= Search Bar ======================= -->
    <el-form :inline="true" :model="query" class="mb-12">
      <el-form-item label="Role Name">
        <el-input v-model="query.roleName" placeholder="role name" clearable />
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
      <el-button type="primary" @click="openCreate">Add Role</el-button>

      <el-button
        type="danger"
        :disabled="selection.length === 0"
        @click="batchRemove()"
      >
        Delete Selected
      </el-button>
    </div>

    <!-- ======================= Role Table ======================= -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="onSelectionChange"
    >
      <el-table-column type="selection" width="48" />
      <el-table-column prop="roleId" label="ID" width="80" />
      <el-table-column prop="roleName" label="Role Name" />
      <el-table-column prop="roleKey" label="Role Key" />

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
            @click="batchRemove([row.roleId])"
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

        <el-form-item label="Role Name" prop="roleName">
          <el-input v-model="form.roleName" />
        </el-form-item>

        <el-form-item label="Role Key" prop="roleKey">
          <el-input v-model="form.roleKey" />
        </el-form-item>

        <el-form-item label="Status">
          <el-select v-model="form.status">
            <el-option label="Enabled" :value="1" />
            <el-option label="Disabled" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item label="Remark">
          <el-input v-model="form.remark" type="textarea" />
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
 * Role Management Page (Referenced from UserPage Best Practices)
 * ------------------------------------------------------------ */

import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listRoles, createRole, updateRole, batchDeleteRole } from '@/api/system/role'

/* ---------------------- Table State ---------------------- */
const loading = ref(false)
const tableData = ref([])
const selection = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

/* ---------------------- Search Form ---------------------- */
const query = reactive({
  roleName: '',
  status: null
})

/* ---------------------- Dialog State ---------------------- */
const dialog = reactive({
  visible: false,
  mode: 'create'
})

const dialogTitle = computed(() =>
  dialog.mode === 'create' ? 'Create Role' : 'Edit Role'
)

/* ---------------------- Form ---------------------- */
const formRef = ref()

const form = reactive({
  roleId: null,
  roleName: '',
  roleKey: '',
  status: 1,
  remark: ''
})

const rules = computed(() => ({
  roleName: [{ required: true, message: 'Role name is required', trigger: 'blur' }],
  roleKey: [{ required: true, message: 'Role key is required', trigger: 'blur' }]
}))

/* ---------------------- Load Table ---------------------- */
async function loadTable() {
  loading.value = true
  try {
    const res = await listRoles({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      ...query
    })

    tableData.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.roleName = ''
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

/* ---------------------- Create Role ---------------------- */
function openCreate() {
  dialog.mode = 'create'
  Object.assign(form, {
    roleId: null,
    roleName: '',
    roleKey: '',
    status: 1,
    remark: ''
  })
  dialog.visible = true
}

/* ---------------------- Edit Role ---------------------- */
function openEdit(row) {
  dialog.mode = 'edit'
  Object.assign(form, row)
  dialog.visible = true
}

/* ---------------------- Submit ---------------------- */
async function submit() {
  await formRef.value.validate()

  if (dialog.mode === 'create') {
    await createRole(form)
    ElMessage.success('Role created successfully')
  } else {
    await updateRole(form)
    ElMessage.success('Role updated successfully')
  }

  dialog.visible = false
  loadTable()
}

/* ---------------------- Delete ---------------------- */
async function batchRemove(idList = null) {
  const ids = idList || selection.value.map(r => r.roleId)

  if (!ids || ids.length === 0) {
    ElMessage.warning('Please select at least one role.')
    return
  }

  await ElMessageBox.confirm(`Confirm delete ${ids.length} role(s)?`, 'Warning')

  await batchDeleteRole(ids)
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
