<template>
  <div class="layout">
    <SidebarMenu />
    <div class="main-content">
      <HeaderBar />
      <div class="content">
        <h1>Welcome to OpsPanel Admin Dashboard 🎉</h1>
        <p>This dashboard integrates your system APIs dynamically.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import HeaderBar from '@/components/HeaderBar.vue'
import SidebarMenu from '@/components/SidebarMenu.vue'
</script>

<style scoped>
.layout {
  display: flex;
  height: 100vh;
}
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.content {
  flex: 1;
  background: #f6f8fa;
  padding: 30px;
}
</style>
