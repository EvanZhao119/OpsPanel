<!-- src/views/insight/InsightPage.vue -->
<template>
    <div class="insight-page">
      <el-card shadow="never" class="insight-card">
        <template #header>
          <div class="header">
            <span class="title">AI Insight Center</span>
          </div>
        </template>
  
        <InsightChatPanel
          source-type="INSIGHT"
          :source-ref="null"
          :context-json="null"
          :show-session-toolbar="true"
        />
      </el-card>
    </div>
  </template>
  
  <script setup>
  import InsightChatPanel from './InsightChatPanel.vue'
  </script>
  
  <style scoped>
  .insight-page {
    padding: 16px;
    height: 100%;
    box-sizing: border-box;
  }
  
  .insight-card {
    height: calc(100vh - 120px);
  }
  
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  .title {
    font-weight: 600;
  }
  </style>
  