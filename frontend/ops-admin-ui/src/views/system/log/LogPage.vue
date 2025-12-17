<template>
  <div class="page">
    <el-tabs v-model="active">
      <el-tab-pane label="Login Logs" name="login">
        <div>
          <el-form :model="loginQuery" inline class="mb-12">
            <el-form-item label="Username">
              <el-input v-model="loginQuery.username" placeholder="username" clearable />
            </el-form-item>
            <el-form-item label="Status">
              <el-select v-model="loginQuery.status" clearable>
                <el-option label="Success" :value="0" />
                <el-option label="Fail" :value="1" />
              </el-select>
            </el-form-item>
            <el-button type="primary" @click="loadLoginLogs">Search</el-button>
            <el-button @click="resetLoginQuery">Reset</el-button>
          </el-form>

          <el-table v-loading="loginLoading" :data="loginData">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="Username" />
            <el-table-column prop="ip" label="IP" width="140" />
            <el-table-column prop="location" label="Location" />
            <el-table-column prop="status" label="Status" width="120" />
            <el-table-column prop="method" label="Method" width="100" />
            <el-table-column prop="uri" label="URI" />
            <el-table-column prop="userAgent" label="UA" />
            <el-table-column prop="createTime" label="Time" width="180" />
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="{ row }">
                <el-button link type="danger" @click="removeLoginLog(row.id)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="mt-12 flex-end">
            <el-pagination
              background
              layout="prev, pager, next, jumper, ->, total"
              :total="loginTotal"
              :page-size="loginPageSize"
              :current-page="loginPageNum"
              @current-change="handleLoginPageChange"
            />
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Operation Logs" name="oper">
        <div>
          <el-form :model="operQuery" inline class="mb-12">
            <el-form-item label="Username">
              <el-input v-model="operQuery.username" placeholder="username" clearable />
            </el-form-item>
            <el-form-item label="Status">
              <el-select v-model="operQuery.status" clearable>
                <el-option label="Success" :value="0" />
                <el-option label="Fail" :value="1" />
              </el-select>
            </el-form-item>
            <el-button type="primary" @click="loadOperLogs">Search</el-button>
            <el-button @click="resetOperQuery">Reset</el-button>
          </el-form>

          <el-table v-loading="operLoading" :data="operData">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="username" label="Username" />
            <el-table-column prop="ip" label="IP" width="140" />
            <el-table-column prop="location" label="Location" />
            <el-table-column prop="status" label="Status" width="120" />
            <el-table-column prop="method" label="Method" width="100" />
            <el-table-column prop="uri" label="URI" />
            <el-table-column prop="userAgent" label="UA" />
            <el-table-column prop="createTime" label="Time" width="180" />
            <el-table-column label="Actions" width="120" fixed="right">
              <template #default="{ row }">
                <el-button link type="danger" @click="removeOperLog(row.id)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="mt-12 flex-end">
            <el-pagination
              background
              layout="prev, pager, next, jumper, ->, total"
              :total="operTotal"
              :page-size="operPageSize"
              :current-page="operPageNum"
              @current-change="handleOperPageChange"
            />
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const active = ref('login')

// Login Logs
const loginLoading = ref(false)
const loginData = ref([])
const loginTotal = ref(0)
const loginPageNum = ref(1)
const loginPageSize = ref(10)
const loginQuery = reactive({ username: '', status: null })

// Operation Logs
const operLoading = ref(false)
const operData = ref([])
const operTotal = ref(0)
const operPageNum = ref(1)
const operPageSize = ref(10)
const operQuery = reactive({ username: '', status: null })

const loginListApi = '/system/log/login/list'
const loginRemoveApi = '/system/log/login'
const operListApi = '/system/log/oper/list'
const operRemoveApi = '/system/log/oper'

// Login Logs functions
async function loadLoginLogs() {
  loginLoading.value = true
  try {
    const res = await request({
      url: loginListApi,
      method: 'get',
      params: { pageNum: loginPageNum.value, pageSize: loginPageSize.value, ...loginQuery }
    })
    loginData.value = res.records || res.rows || res.data || []
    loginTotal.value = res.total || 0
  } finally {
    loginLoading.value = false
  }
}

function resetLoginQuery() {
  loginQuery.username = ''
  loginQuery.status = null
  loginPageNum.value = 1
  loadLoginLogs()
}

async function removeLoginLog(id) {
  await ElMessageBox.confirm('Confirm to delete this log?', 'Warning', { type: 'warning' })
  await request({ url: `${loginRemoveApi}/${id}`, method: 'delete' })
  ElMessage.success('Deleted')
  loadLoginLogs()
}

function handleLoginPageChange(page) {
  loginPageNum.value = page
  loadLoginLogs()
}

// Operation Logs functions
async function loadOperLogs() {
  operLoading.value = true
  try {
    const res = await request({
      url: operListApi,
      method: 'get',
      params: { pageNum: operPageNum.value, pageSize: operPageSize.value, ...operQuery }
    })
    operData.value = res.records || res.rows || res.data || []
    operTotal.value = res.total || 0
  } finally {
    operLoading.value = false
  }
}

function resetOperQuery() {
  operQuery.username = ''
  operQuery.status = null
  operPageNum.value = 1
  loadOperLogs()
}

async function removeOperLog(id) {
  await ElMessageBox.confirm('Confirm to delete this log?', 'Warning', { type: 'warning' })
  await request({ url: `${operRemoveApi}/${id}`, method: 'delete' })
  ElMessage.success('Deleted')
  loadOperLogs()
}

function handleOperPageChange(page) {
  operPageNum.value = page
  loadOperLogs()
}

onMounted(() => {
  loadLoginLogs()
})
</script>

<style scoped>
.page {
  padding: 16px;
}
.mb-12 {
  margin-bottom: 12px;
}
.mt-12 {
  margin-top: 12px;
}
.flex-end {
  display: flex;
  justify-content: flex-end;
}
</style>
