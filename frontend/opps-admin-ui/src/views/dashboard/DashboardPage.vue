<template>
  <div class="page">
    <!-- ======================= Page Selector / Toolbar ======================= -->
    <div class="toolbar mb-16">
      <div class="toolbar-left">
        <span class="label">Dashboard:</span>
        <el-select
          v-model="currentPageId"
          placeholder="Select page"
          style="min-width: 220px"
          @change="onPageChange"
        >
          <el-option
            v-for="page in pages"
            :key="page.id"
            :label="page.pageName || page.page_name || page.pageCode || page.page_code || `Page #${page.id}`"
            :value="page.id"
          />
        </el-select>

        <!-- Home star button -->
        <el-button
          v-if="currentPageId"
          class="ml-8"
          text
          @click="markAsHome"
        >
          <span
            class="star-icon"
            :class="{ active: isHome }"
          >
            {{ isHome ? '★' : '☆' }}
          </span>
          <span class="star-text">
            {{ isHome ? 'Home Page' : 'Set as Home' }}
          </span>
        </el-button>
      </div>

      <div class="toolbar-right">
        <el-button
          v-if="currentPageId"
          type="primary"
          text
          @click="reloadCurrentPage"
        >
          Refresh
        </el-button>

        <!-- AI Insight toggle button -->
        <el-button
          v-if="currentPageId"
          text
          class="ml-8"
          @click="toggleInsight"
        >
          <el-icon class="insight-icon">
            <ChatDotRound />
          </el-icon>
          <span class="insight-btn-text">
            {{ showInsight ? 'Hide Insight' : 'AI Insight' }}
          </span>
        </el-button>
      </div>
    </div>

    <!-- ======================= Widgets + Insight Layout ======================= -->
    <div v-if="!currentPageId" class="empty-tip">
      No dashboard page found. Please create a dashboard page in backend.
    </div>

    <div v-else class="dashboard-layout">
      <!-- Left: Dashboard widgets -->
      <div class="dashboard-main">
        <el-row :gutter="16">
          <el-col
            v-for="widget in widgets"
            :key="widget.id"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="8"
            class="mb-16"
          >
            <el-card shadow="hover" class="widget-card">
              <template #header>
                <div class="widget-header">
                  <div class="widget-title">
                    {{ getWidgetTitle(widget) }}
                    <span class="widget-datasource" v-if="widget.dataSource">
                      {{ widget.dataSource }}
                    </span>
                  </div>
                  <el-button
                    size="small"
                    text
                    @click="reloadWidget(widget)"
                  >
                    Refresh
                  </el-button>
                </div>
              </template>

              <div class="widget-body">
                <template v-if="widgetStates[widget.id]?.loading">
                  <div class="widget-loading">
                    Loading...
                  </div>
                </template>

                <template v-else-if="widgetStates[widget.id]?.error">
                  <div class="widget-error">
                    {{ widgetStates[widget.id].error }}
                  </div>
                </template>

                <template v-else-if="widgetStates[widget.id]?.data">
                  <div class="widget-view">
                    <div
                      v-if="widgetStates[widget.id].data.value !== undefined &&
                            widgetStates[widget.id].data.value !== null"
                      class="stat-block"
                    >
                      <div class="stat-value">
                        {{ widgetStates[widget.id].data.value }}
                        <span
                          v-if="widgetStates[widget.id].data.unit"
                          class="stat-unit"
                        >
                          {{ widgetStates[widget.id].data.unit }}
                        </span>
                      </div>

                      <div
                        v-if="widgetStates[widget.id].data.title ||
                             widgetStates[widget.id].data.subtitle"
                        class="stat-title"
                      >
                        {{ widgetStates[widget.id].data.title || widgetStates[widget.id].data.subtitle }}
                      </div>

                      <div
                        v-if="widgetStates[widget.id].data.description"
                        class="stat-desc"
                      >
                        {{ widgetStates[widget.id].data.description }}
                      </div>
                    </div>
                  </div>
                </template>

                <template v-else>
                  <div class="widget-empty">No data</div>
                </template>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- Right: AI Insight panel (toggle) -->
      <div v-if="showInsight" class="dashboard-insight">
        <InsightChatPanel
          source-type="DASHBOARD"
          :source-ref="currentPageId"
          :context-json="dashboardContext"
          :show-session-toolbar="true"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, defineComponent, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound } from '@element-plus/icons-vue'

import { listDashboardPages } from '@/api/dashboard/page'
import { listFavorites, setHomePage } from '@/api/dashboard/favorite'
import { listWidgetsByPage } from '@/api/dashboard/widget'
import { queryDashboardData } from '@/api/dashboard/data'
import InsightChatPanel from '@/views/insight/InsightChatPanel.vue'

const DashboardWidgetView = defineComponent({
  name: 'DashboardWidgetView',
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  setup(props) {
    const hasRows = computed(
      () => Array.isArray(props.data.rows) && props.data.rows.length > 0
    )

    const tableColumns = computed(() => {
      if (!hasRows.value) return []
      const firstRow = props.data.rows[0] || {}
      return Object.keys(firstRow)
    })

    return {
      hasRows,
      tableColumns
    }
  },
  template: `
    <div class="widget-view">
      <div
        v-if="data.value !== undefined && data.value !== null"
        class="stat-block"
      >
        <div class="stat-value">
          {{ data.value }}
          <span v-if="data.unit" class="stat-unit">{{ data.unit }}</span>
        </div>
        <div v-if="data.title || data.subtitle" class="stat-title">
          {{ data.title || data.subtitle }}
        </div>
        <div v-if="data.description" class="stat-desc">
          {{ data.description }}
        </div>
      </div>

      <el-table
        v-if="hasRows"
        :data="data.rows"
        size="small"
        border
        class="mt-8"
      >
        <el-table-column
          v-for="col in tableColumns"
          :key="col"
          :prop="col"
          :label="col"
          :min-width="120"
        />
      </el-table>

      <pre
        v-else-if="data.series"
        class="series-json mt-8"
      >{{ JSON.stringify(data.series, null, 2) }}</pre>
    </div>
  `
})

const pages = ref([])
const currentPageId = ref(null)
const homePageId = ref(null)

const widgets = ref([])
const widgetStates = reactive({})

const showInsight = ref(false)

const isHome = computed(() => {
  return currentPageId.value != null && homePageId.value === currentPageId.value
})

const dashboardContext = computed(() => {
  return {
    pageId: currentPageId.value,
    widgets: widgets.value.map(w => ({
      id: w.id,
      title: w.title || w.widgetTitle || w.name || `Widget #${w.id}`,
      dataSource: w.dataSource || w.dataSourceCode
    })),
    data: widgetStates
  }
})

function getWidgetTitle(widget) {
  return (
    widget.title ||
    widget.widgetTitle ||
    widget.name ||
    `Widget #${widget.id}`
  )
}

async function loadPagesAndHome() {
  const resPages = await listDashboardPages()
  const list = resPages.data || resPages || []
  pages.value = list

  try {
    const resFav = await listFavorites()
    const favList = resFav.data || resFav || []
    const home = favList.find(f => f.isHome === 1)
    homePageId.value = home ? home.pageId : null
  } catch (e) {
    console.warn('Failed to load dashboard favorites:', e)
  }

  if (homePageId.value) {
    currentPageId.value = homePageId.value
  } else if (pages.value.length) {
    currentPageId.value = pages.value[0].id
  } else {
    currentPageId.value = null
  }

  if (currentPageId.value) {
    await loadWidgetsForPage(currentPageId.value)
  }
}

async function loadWidgetsForPage(pageId) {
  widgets.value = []
  Object.keys(widgetStates).forEach(key => delete widgetStates[key])

  if (!pageId) return

  const res = await listWidgetsByPage(pageId)
  const list = res.data || res || []
  widgets.value = list

  for (const widget of widgets.value) {
    await loadWidgetData(widget)
  }
}

async function loadWidgetData(widget) {
  const id = widget.id
  widgetStates[id] = { loading: true }

  try {
    const payload = {
      dataSource: widget.dataSource || widget.dataSourceCode,
      params: {}
    }

    const res = await queryDashboardData(payload)
    const api = res?.data ?? res
    const vo = api?.data ?? api

    widgetStates[id] = {
      loading: false,
      data: vo
    }
  } catch (e) {
    widgetStates[id] = {
      loading: false,
      error: e?.message || 'Failed to load widget data'
    }
  }
}

async function onPageChange(pageId) {
  if (pageId) {
    await loadWidgetsForPage(pageId)
  }
}

async function reloadCurrentPage() {
  if (!currentPageId.value) return
  await loadWidgetsForPage(currentPageId.value)
}

async function reloadWidget(widget) {
  await loadWidgetData(widget)
}

async function markAsHome() {
  if (!currentPageId.value) return

  try {
    await setHomePage(currentPageId.value)

    if (homePageId.value === currentPageId.value) {
      homePageId.value = null
      ElMessage.success('Home page unset')
    } else {
      homePageId.value = currentPageId.value
      ElMessage.success('Home page updated')
    }
  } catch (e) {
    console.error('Failed to update home dashboard:', e)
    ElMessage.error('Failed to update home page. Please try again.')
  }
}

function toggleInsight() {
  showInsight.value = !showInsight.value
}

onMounted(() => {
  loadPagesAndHome().catch(err => {
    console.error('Failed to load dashboard pages:', err)
  })
})
</script>

<style scoped>
.page {
  padding: 16px;
}

/* toolbar */
.toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.toolbar-left {
  display: flex;
  align-items: center;
}

.label {
  margin-right: 8px;
  font-weight: 500;
}

.ml-8 {
  margin-left: 8px;
}

.mb-16 {
  margin-bottom: 16px;
}

/* star button */
.star-icon {
  font-size: 16px;
  margin-right: 4px;
  transition: color 0.2s;
}

.star-icon.active {
  color: #f5a623;
}

.star-text {
  font-size: 13px;
}

/* widgets */
.widget-card {
  min-height: 150px;
}

.widget-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.widget-title {
  font-weight: 600;
  display: flex;
  align-items: center;
}

.widget-datasource {
  margin-left: 8px;
  font-size: 12px;
  color: #999;
}

.widget-body {
  min-height: 80px;
}

.widget-loading,
.widget-error,
.widget-empty {
  font-size: 13px;
  color: #666;
}

.widget-error {
  color: #f56c6c;
}

.empty-tip {
  margin-top: 40px;
  text-align: center;
  color: #999;
}

/* widget view */
.widget-view {
  font-size: 14px;
}

.stat-block {
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
}

.stat-unit {
  font-size: 14px;
  margin-left: 4px;
}

.stat-title {
  margin-top: 4px;
  color: #666;
}

.stat-desc {
  margin-top: 2px;
  color: #999;
}

.series-json {
  font-size: 12px;
  background: #fafafa;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #eee;
  max-height: 220px;
  overflow: auto;
}

.mt-8 {
  margin-top: 8px;
}

/* dashboard + insight layout */
.dashboard-layout {
  display: flex;
  align-items: stretch;
}

.dashboard-main {
  flex: 1 1 auto;
  padding-right: 12px;
}

.dashboard-insight {
  width: 360px;
  flex: 0 0 360px;
  padding-left: 12px;
  padding-top: 4px;
}


.dashboard-insight :deep(.insight-panel) {
  width: 100%;
  height: 100%;
  background: #ffffff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(15, 23, 42, 0.08);
  display: flex;
  flex-direction: column;
}

.dashboard-insight :deep(.insight-header) {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border-bottom: 1px solid #e5e7eb;
}

.dashboard-insight :deep(.insight-header-left) {
  display: flex;
  align-items: center;
  gap: 8px;
}

.dashboard-insight :deep(.insight-title) {
  font-size: 13px;
  font-weight: 600;
  margin-right: 4px;
}

.dashboard-insight :deep(.insight-header-right) {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 12px;
}

.dashboard-insight :deep(.insight-body) {
  flex: 1 1 auto;
  padding: 8px 12px;
  overflow: auto;
}

.dashboard-insight :deep(.insight-footer) {
  padding: 8px 12px 10px 12px;
  border-top: 1px solid #e5e7eb;
}

.insight-icon {
  margin-right: 4px;
}

.insight-btn-text {
  font-size: 13px;
}
</style>
