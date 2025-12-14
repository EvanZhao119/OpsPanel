<template>
    <div class="page">
      <!-- ======================= Search Bar ======================= -->
      <el-form :inline="true" :model="query" class="mb-12">
        <el-form-item label="Job Name">
          <el-input
            v-model="query.jobName"
            placeholder="job name"
            clearable
          />
        </el-form-item>
  
        <el-form-item label="Status" class="status-form-item">
          <el-select v-model="query.status" placeholder="status" clearable>
            <el-option label="Running" value="0" />
            <el-option label="Paused" value="1" />
          </el-select>
        </el-form-item>
  
        <el-form-item label="Job Type">
          <el-select v-model="query.jobType" placeholder="type" clearable>
            <el-option label="JAVA" value="JAVA" />
            <el-option label="HTTP" value="HTTP" />
          </el-select>
        </el-form-item>
  
        <el-form-item>
          <el-button type="primary" @click="loadTable">Search</el-button>
          <el-button @click="resetQuery">Reset</el-button>
        </el-form-item>
      </el-form>
  
      <!-- ======================= Toolbar ======================= -->
      <div class="mb-12">
        <el-button type="primary" @click="openCreate">Add Job</el-button>
  
        <el-button
          type="danger"
          :disabled="selection.length === 0"
          @click="batchRemove()"
        >
          Delete Selected
        </el-button>
      </div>
  
      <!-- ======================= Job Table ======================= -->
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="onSelectionChange"
      >
        <el-table-column type="selection" width="48" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="jobName" label="Job Name" min-width="180" />
        <el-table-column prop="jobType" label="Type" min-width="90" />
        <el-table-column prop="invokeTarget" label="Invoke Target" min-width="260" />
        <el-table-column prop="cronExpression" label="Cron" min-width="160" />
  
        <el-table-column label="Status" min-width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === '0' ? 'success' : 'info'">
              {{ row.status === '0' ? 'Running' : 'Paused' }}
            </el-tag>
          </template>
        </el-table-column>
  
        <!-- Action Column -->
        <el-table-column label="Actions" min-width="320" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">Edit</el-button>
            <el-button
              link
              type="warning"
              @click="changeStatus(row)"
            >
              {{ row.status === '0' ? 'Pause' : 'Resume' }}
            </el-button>
            <el-button link type="success" @click="runOnce(row)">Run Once</el-button>
            <el-button link type="danger" @click="batchRemove([row.id])">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- ======================= Create / Edit Dialog ======================= -->
      <el-dialog v-model="dialog.visible" :title="dialogTitle" width="720px">
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="130px"
        >
          <!-- Job name -->
          <el-form-item label="Job Name" prop="jobName">
            <el-input v-model="form.jobName" placeholder="job name" />
          </el-form-item>
  
          <!-- Job type -->
          <el-form-item label="Job Type" prop="jobType">
            <el-select v-model="form.jobType" placeholder="job type">
              <el-option label="JAVA" value="JAVA" />
              <el-option label="HTTP" value="HTTP" />
            </el-select>
          </el-form-item>
  
          <!-- ======================= JAVA job fields ======================= -->
          <template v-if="form.jobType === 'JAVA'">
            <el-form-item label="Bean Name">
              <el-input
                v-model="javaForm.beanName"
                placeholder="e.g. sampleTimeTask"
              />
            </el-form-item>
  
            <el-form-item label="Method Name">
              <el-input
                v-model="javaForm.methodName"
                placeholder="e.g. printCurrentTime"
              />
            </el-form-item>
          </template>
  
          <!-- ======================= HTTP job fields ======================= -->
          <template v-else-if="form.jobType === 'HTTP'">
            <el-form-item label="HTTP Method">
              <el-select v-model="httpForm.method" style="width: 120px">
                <el-option label="GET" value="GET" />
                <el-option label="POST" value="POST" />
                <el-option label="PUT" value="PUT" />
                <el-option label="DELETE" value="DELETE" />
              </el-select>
            </el-form-item>
  
            <el-form-item label="Request URL">
              <el-input
                v-model="httpForm.url"
                placeholder="e.g. https://webhook.site/xxxx"
              />
            </el-form-item>
          </template>
  
          <!-- Cron expression -->
          <el-form-item label="Cron Expression" prop="cronExpression">
            <el-input
              v-model="form.cronExpression"
              placeholder="e.g. 0/20 * * * * ?"
            />
          </el-form-item>
  
          <!-- Status -->
          <el-form-item label="Status">
            <el-select v-model="form.status" style="width: 140px">
              <el-option label="Running" value="0" />
              <el-option label="Paused" value="1" />
            </el-select>
          </el-form-item>
  
          <!-- Remark -->
          <el-form-item label="Remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              :rows="3"
              placeholder="optional remark"
            />
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
   * Task Job Management Page (new model)
   * ------------------------------------------------------------
   * Back-end entity: TaskJob
   *  - id, jobName, jobGroup, jobType
   *  - invokeTarget
   *  - cronExpression
   *  - status: "0" = running, "1" = paused
   *  - remark
   * ------------------------------------------------------------ */
  
  import { ref, reactive, computed, onMounted } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import {
    listJobs,
    getJob,
    createJob,
    updateJob,
    deleteJobs,
    changeStatus as apiChangeStatus,
    runOnce as apiRunOnce
  } from '@/api/task/job'
  
  /* ---------------------- Table State ---------------------- */
  const loading = ref(false)
  const tableData = ref([])
  const selection = ref([])
  
  /* ---------------------- Search Form ---------------------- */
  const query = reactive({
    jobName: '',
    status: null,
    jobType: null
  })
  
  function resetQuery() {
    query.jobName = ''
    query.status = null
    query.jobType = null
    loadTable()
  }
  
  /* ---------------------- Load Table ---------------------- */
  async function loadTable() {
    loading.value = true
    try {
      const res = await listJobs(query)
      // ApiResponse<T> wrapper
      const data = res.data || res || []
      tableData.value = data.records || data
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
    dialog.mode === 'create' ? 'Add Job' : 'Edit Job'
  )
  
  /* ---------------------- Form Models ---------------------- */
  const formRef = ref()
  
  // Main job form (matches TaskJob entity)
  const form = reactive({
    id: null,
    jobName: '',
    jobGroup: 'DEFAULT',
    jobType: 'JAVA',
    invokeTarget: '',
    cronExpression: '',
    misfirePolicy: '1',
    concurrent: '1',
    status: '0',
    remark: ''
  })
  
  // Sub-form for JAVA type
  const javaForm = reactive({
    beanName: '',
    methodName: ''
  })
  
  // Sub-form for HTTP type
  const httpForm = reactive({
    method: 'GET',
    url: ''
  })
  
  /* ---------------------- Validation rules ---------------------- */
  const rules = {
    jobName: [{ required: true, message: 'Job name is required', trigger: 'blur' }],
    jobType: [{ required: true, message: 'Job type is required', trigger: 'change' }],
    cronExpression: [
      { required: true, message: 'Cron expression is required', trigger: 'blur' }
    ]
  }
  
  /* ---------------------- Helper: reset sub-forms ---------------------- */
  function resetSubForms() {
    javaForm.beanName = ''
    javaForm.methodName = ''
    httpForm.method = 'GET'
    httpForm.url = ''
  }
  
  /* ---------------------- Open Create ---------------------- */
  function openCreate() {
    dialog.mode = 'create'
    Object.assign(form, {
      id: null,
      jobName: '',
      jobGroup: 'DEFAULT',
      jobType: 'JAVA',
      invokeTarget: '',
      cronExpression: '',
      misfirePolicy: '1',
      concurrent: '1',
      status: '0',
      remark: ''
    })
    resetSubForms()
    dialog.visible = true
  }
  
  /* ---------------------- Open Edit ---------------------- */
  function openEdit(row) {
    dialog.mode = 'edit'
    Object.assign(form, row)
    resetSubForms()
  
    // Parse invokeTarget into sub-forms based on jobType
    if (row.jobType === 'HTTP' && row.invokeTarget) {
      // Format: METHOD:URL
      const idx = row.invokeTarget.indexOf(':')
      if (idx > 0) {
        httpForm.method = row.invokeTarget.substring(0, idx)
        httpForm.url = row.invokeTarget.substring(idx + 1)
      }
    } else if (row.jobType === 'JAVA' && row.invokeTarget) {
      // Simple format: bean.method (ignore arguments for now)
      const idx = row.invokeTarget.indexOf('.')
      if (idx > 0) {
        javaForm.beanName = row.invokeTarget.substring(0, idx)
        javaForm.methodName = row.invokeTarget.substring(idx + 1)
      }
    }
  
    dialog.visible = true
  }
  
  /* ---------------------- Submit (create / update) ---------------------- */
  async function submit() {
    await formRef.value.validate()
  
    // Build invokeTarget from sub-forms
    if (form.jobType === 'HTTP') {
      if (!httpForm.url) {
        ElMessage.error('Request URL is required for HTTP job')
        return
      }
      form.invokeTarget = `${httpForm.method}:${httpForm.url}`
    } else if (form.jobType === 'JAVA') {
      if (!javaForm.beanName || !javaForm.methodName) {
        ElMessage.error('Bean name and method name are required for JAVA job')
        return
      }
      form.invokeTarget = `${javaForm.beanName.trim()}.${javaForm.methodName.trim()}`
    }
  
    if (dialog.mode === 'create') {
      await createJob(form)
      ElMessage.success('Job created successfully')
    } else {
      await updateJob(form)
      ElMessage.success('Job updated successfully')
    }
  
    dialog.visible = false
    loadTable()
  }
  
  /* ---------------------- Delete Job(s) ---------------------- */
  async function batchRemove(idList = null) {
    const ids = idList || selection.value.map(r => r.id)
  
    if (!ids || !ids.length) {
      ElMessage.warning('Please select at least one job.')
      return
    }
  
    await ElMessageBox.confirm(
      `Confirm delete ${ids.length} job(s)?`,
      'Warning'
    )
  
    await deleteJobs(ids)
    ElMessage.success('Deleted successfully')
    loadTable()
  }
  
  /* ---------------------- Change Status ---------------------- */
  async function changeStatus(row) {
    const newStatus = row.status === '0' ? '1' : '0'
    await apiChangeStatus(row.id, newStatus)
    ElMessage.success('Status updated')
    loadTable()
  }
  
  /* ---------------------- Run Once ---------------------- */
  async function runOnce(row) {
    await ElMessageBox.confirm(
      `Run job "${row.jobName}" once now?`,
      'Confirm'
    )
  
    await apiRunOnce(row.id)
    ElMessage.success('Triggered successfully')
  }
  
  /* ---------------------- Init ---------------------- */
  onMounted(loadTable)
  </script>
  
  <style scoped>
  .page {
    padding: 16px;
  }
  
  .mb-12 {
    margin-bottom: 12px;
  }
  
  .status-form-item {
    min-width: 160px;
  }
  </style>
  