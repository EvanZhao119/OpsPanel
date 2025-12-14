<template>
  <div class="page">
    <!-- ======================= Search Bar ======================= -->
    <el-form :inline="true" :model="query" class="mb-12">
      <el-form-item label="Menu Name">
        <el-input v-model="query.menuName" placeholder="menu name" clearable />
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
      <el-button type="primary" @click="openCreate">Add Menu</el-button>

      <el-button
        type="danger"
        :disabled="selection.length === 0"
        @click="batchRemove"
      >
        Delete Selected
      </el-button>
    </div>

    <!-- ======================= Menu Table ======================= -->
    <el-table
      v-loading="loading"
      :data="tableData"
      row-key="menuId"
      default-expand-all
      :tree-props="{ children: 'children' }"
      @selection-change="onSelectionChange"
    >
      <el-table-column type="selection" width="48" />

      <el-table-column prop="menuId" label="ID" width="80" />
      <el-table-column prop="menuName" label="Menu Name" />
      <el-table-column prop="path" label="Path" />
      <el-table-column prop="component" label="Component" />

      <el-table-column label="Type" width="120">
        <template #default="{ row }">
          <el-tag type="info">
            {{ typeText(row.menuType) }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="perms" label="Perms" />
      <el-table-column prop="orderNum" label="Sort" width="80" />

      <el-table-column prop="status" label="Status" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? 'Enabled' : 'Disabled' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="Actions" width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">Edit</el-button>
          <el-button link type="danger" @click="remove(row.menuId)">
            Delete
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- ======================= Create / Edit Dialog ======================= -->
    <el-dialog v-model="dialog.visible" :title="dialogTitle">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">

        <el-form-item label="Parent ID">
          <el-input-number v-model="form.parentId" :min="0" />
        </el-form-item>

        <el-form-item label="Menu Name" prop="menuName">
          <el-input v-model="form.menuName" />
        </el-form-item>

        <el-form-item label="Path">
          <el-input v-model="form.path" />
        </el-form-item>

        <el-form-item label="Component">
          <el-input v-model="form.component" />
        </el-form-item>

        <el-form-item label="Menu Type">
          <el-select v-model="form.menuType">
            <el-option label="Directory" :value="0" />
            <el-option label="Menu" :value="1" />
            <el-option label="Button" :value="2" />
          </el-select>
        </el-form-item>

        <el-form-item label="Perms">
          <el-input v-model="form.perms" />
        </el-form-item>

        <el-form-item label="Sort">
          <el-input-number v-model="form.orderNum" :min="0" />
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listMenus,
  createMenu,
  updateMenu,
  batchDeleteRole,
} from '@/api/system/menu'

/* ---------------------- State ---------------------- */
const loading = ref(false)
const tableData = ref([])
const selection = ref([])

const query = reactive({
  menuName: '',
  status: null,
})

const dialog = reactive({
  visible: false,
  mode: 'create',
})

const dialogTitle = computed(() =>
  dialog.mode === 'create' ? 'Create Menu' : 'Edit Menu'
)

const formRef = ref()
const form = reactive({
  menuId: null,
  parentId: 0,
  menuName: '',
  path: '',
  component: '',
  menuType: 1,
  perms: '',
  orderNum: 0,
  status: 1,
})

const rules = {
  menuName: [{ required: true, message: 'Menu name is required', trigger: 'blur' }],
}

/* ---------------------- Utils ---------------------- */
function typeText(t) {
  return t === 0 ? 'Directory' : t === 1 ? 'Menu' : 'Button'
}

/* ---------------------- Select Events ---------------------- */
function onSelectionChange(rows) {
  selection.value = rows
}

/* ---------------------- Load List ---------------------- */
async function loadTable() {
  loading.value = true
  try {
    const res = await listMenus(query)
    tableData.value = res.data?.records || []
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.menuName = ''
  query.status = null
  loadTable()
}

/* ---------------------- Create ---------------------- */
function openCreate() {
  dialog.mode = 'create'
  Object.assign(form, {
    menuId: null,
    parentId: 0,
    menuName: '',
    path: '',
    component: '',
    menuType: 1,
    perms: '',
    orderNum: 0,
    status: 1,
  })
  dialog.visible = true
}

/* ---------------------- Edit ---------------------- */
function openEdit(row) {
  dialog.mode = 'edit'
  Object.assign(form, row)
  dialog.visible = true
}

/* ---------------------- Submit ---------------------- */
async function submit() {
  await formRef.value.validate()

  if (dialog.mode === 'create') {
    await createMenu(form)
    ElMessage.success('Menu created successfully')
  } else {
    await updateMenu(form)
    ElMessage.success('Menu updated successfully')
  }

  dialog.visible = false
  loadTable()
}

/* ---------------------- Delete ---------------------- */
async function remove(id) {
  await ElMessageBox.confirm('Confirm delete this menu?', 'Warning')
  await batchDeleteRole([id])
  ElMessage.success('Deleted successfully')
  loadTable()
}

async function batchRemove() {
  const ids = selection.value.map(r => r.menuId)
  if (!ids.length) return

  await ElMessageBox.confirm(`Confirm delete ${ids.length} menu(s)?`, 'Warning')
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
.status-form-item { min-width: 160px; }
</style>
