<template>
  <div id="basicLayout">
    <a-layout style="min-height: 100vh">
      <a-layout-header class="header">
        <GlobalHeader />
      </a-layout-header>
      <a-layout>
        <GlobalSider class="sider" />
        <a-layout-content class="content">
          <router-view />
        </a-layout-content>
      </a-layout>
    </a-layout>
  </div>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalSider from "@/components/GlobalSider.vue";
</script>

<style scoped>
 #basicLayout {
  min-height: 100vh;
}

/* 顶部导航栏 */
#basicLayout .header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  z-index: 1000;
  background: white;
  padding-inline: 20px;
  border-bottom: 1px solid #eee;
}

/* 固定左侧侧边栏 */
#basicLayout .sider {
  position: fixed;
  top: 64px; /* header 高度 */
  left: 0;
  height: calc(100vh - 64px);
  width: 200px;
  background: #fff;
  border-right: 1px solid #eee;
  padding-top: 20px;
  z-index: 999;
}

/* 主内容区域右移防止被侧边栏遮住 */
#basicLayout .content {
  margin-left: 200px; /* 与 sider 宽度一致 */
  margin-top: 64px;   /* 与 header 高度一致 */
  padding: 28px;
  background: linear-gradient(to right, #fefefe, #fff);
  min-height: calc(100vh - 64px - 48px); /* 减去顶部和 footer 高度 */
  box-sizing: border-box;
}

/* 修复菜单边框 */
#basicLayout :deep(.ant-menu-root) {
  border-bottom: none !important;
  border-inline-end: none !important;
}
</style>
