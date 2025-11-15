<template>
  <div class="page">
    <!-- ======================= Search Bar ======================= -->
    <el-form :inline="true" :model="query" class="mb-12">
      <el-form-item label="Dept Name">
        <el-input v-model="query.deptName" placeholder="department name" clearable />
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
      <el-button type="primary" @click="openCreate">Add Dept</el-button>

      <el-button
        type="danger"
        :disabled="selection.length === 0"
        @click="batchRemove()"
      >
        Delete Selected
      </el-button>
    </div>

    <!-- ======================= Dept Table ======================= -->
    <el-table
      v-loading="loading"
      :data="tableData"
      @selection-change="onSelectionChange"
    >
      <el-table-column type="selection" width="48" />

      <el-table-column prop="deptId" label="ID" width="80" />

      <el-table-column prop="deptName" label="Dept Name" min-width="180" />

      <el-table-column prop="parentId" label="Parent ID" min-width="120" />

      <el-table-column label="Status" min-width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- Action Column -->
      <el-table-column label="Actions" min-width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">Edit</el-button>
          <el-button link type="danger" @click="batchRemove([row.deptId])">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- ======================= Create / Edit Dialog ======================= -->
    <el-dialog v-model="dialog.visible" :title="dialogTitle">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">

        <el-form-item label="Dept Name" prop="deptName">
          <el-input v-model="form.deptName" />
        </el-form-item>

        <el-form-item label="Parent ID">
          <el-input-number v-model="form.parentId" :min="0" />
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
 * Department Management Page (Best Practice Version)
 * ------------------------------------------------------------
 * Features:
 *  - Department list
 *  - Search form
 *  - Create / Edit dialog
 *  - Delete single / multiple departments
 *  - Fully aligned UI with UserPage.vue
 * ------------------------------------------------------------ */

import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listDepts, createDept, updateDept, deleteDept } from '@/api/system/dept'

/* ---------------------- Table State ---------------------- */
const loading = ref(false)
const tableData = ref([])
const selection = ref([])

/* ---------------------- Search Form ---------------------- */
const query = reactive({
  deptName: '',
  status: null
})

function resetQuery() {
  query.deptName = ''
  query.status = null
  loadTable()
}

/* ---------------------- Load Table ---------------------- */
async function loadTable() {
  loading.value = true
  try {
    const res = await listDepts(query)
    const page = res.data || {}
    tableData.value = page.records || []
  } finally {
    loading.value = false
  }
}

/* ---------------------- Table Events ---------------------- */
function onSelectionChange(rows) {
  selection.value = rows
}

/* ---------------------- Dialog State ---------------------- */
const dialog = reactive({
  visible: false,
  mode: 'create'
})

const dialogTitle = computed(() =>
  dialog.mode === 'create' ? 'Create Dept' : 'Edit Dept'
)

/* ---------------------- Form ---------------------- */
const formRef = ref()
const form = reactive({
  deptId: null,
  deptName: '',
  parentId: 0,
  status: 1
})

const rules = {
  deptName: [{ required: true, message: 'Department name is required', trigger: 'blur' }]
}

/* ---------------------- Create Dept ---------------------- */
function openCreate() {
  dialog.mode = 'create'
  Object.assign(form, {
    deptId: null,
    deptName: '',
    parentId: 0,
    status: 1
  })
  dialog.visible = true
}

/* ---------------------- Edit Dept ---------------------- */
function openEdit(row) {
  dialog.mode = 'edit'
  Object.assign(form, row)
  dialog.visible = true
}

/* ---------------------- Submit Dept ---------------------- */
async function submit() {
  await formRef.value.validate()

  if (dialog.mode === 'create') {
    await createDept(form)
    ElMessage.success('Department created successfully')
  } else {
    await updateDept(form)
    ElMessage.success('Department updated successfully')
  }

  dialog.visible = false
  loadTable()
}

/* ---------------------- Delete Dept ---------------------- */
async function batchRemove(idList = null) {
  const ids = idList || selection.value.map(r => r.deptId)

  if (!ids || ids.length === 0) {
    ElMessage.warning('Please select at least one department.')
    return
  }

  await ElMessageBox.confirm(
    `Confirm delete ${ids.length} department(s)?`,
    'Warning'
  )

  await deleteDept(ids)
  ElMessage.success('Deleted successfully')

  loadTable()
}

/* ---------------------- Init ---------------------- */
onMounted(loadTable)
</script>

<style scoped>
.page { padding: 16px; }
.mb-12 { margin-bottom: 12px; }
.status-form-item { min-width: 160px; }
</style>
