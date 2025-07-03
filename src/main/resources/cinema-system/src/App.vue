<template>
  <div class="background-overlay"></div>
  <router-view></router-view>
</template>

<script setup>
// 这里可以全局初始化一些逻辑（可选）
</script>

<style>
:root {
  --btn-bg-color: #6a5acd; /* 主色调：紫色 */
  --btn-hover-color: #4b0082; /* 深紫色 */
  --btn-text-color: white;
  --btn-padding: 10px 20px;
  --btn-border-radius: 6px;
  --table-bg-color: #282828; /* 表格背景色 */
  --table-header-bg-color: #333; /* 表头背景色 */
  --table-header-text-color: #fff; /* 表头文字颜色 */
  --table-border-color: #444; /* 边框颜色 */
  --table-hover-bg-color: #3c3c3c; /* 悬停行背景色 */
}
#app {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
}
.background-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  background-image: url('@/assets/image/background-image.jpg');
  background-size: cover;
  background-position: center;
  filter: blur(5px);
  opacity: 0.7;
}
table {
  width: 100%;
  border-collapse: collapse;
  background-color: var(--table-bg-color);
  color: #fff;
  font-size: 14px;
  border-radius: 6px;
  overflow: hidden;
}
th,
td {
  padding: 12px 16px;
  text-align: left;
  border: 1px solid var(--table-border-color);
}

thead {
  background-color: var(--table-header-bg-color);
  color: var(--table-header-text-color);
}

tbody tr:hover {
  background-color: var(--table-hover-bg-color);
}

/* Element Plus el-table 兼容样式 */
.el-table {
  background-color: var(--table-bg-color);
  color: #fff;
  border-radius: 6px;
  overflow: hidden;
}

.el-table th,
.el-table td {
  background-color: transparent !important;
  border-color: var(--table-border-color);
  padding: 10px 16px;
}

.el-table thead {
  background-color: var(--table-header-bg-color);
  color: var(--table-header-text-color);
}

.el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: var(--table-hover-bg-color) !important;
}

/* 分页器样式（Element Plus） */
.el-pagination {
  margin-top: 20px;
  text-align: center;
}

.el-pager li.active {
  background-color: var(--btn-bg-color) !important;
  color: var(--btn-text-color) !important;
}

.el-pager li:hover {
  color: var(--btn-bg-color) !important;
}
button,
.el-button, /* Element Plus 按钮兼容 */
.Button,    /* 自定义组件 Button 兼容 */
[type="button"],
[type="submit"],
[type="reset"] {
  display: inline-block;
  padding: var(--btn-padding);
  background-color: var(--btn-bg-color);
  color: var(--btn-text-color);
  border: none;
  border-radius: var(--btn-border-radius);
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
  outline: none;
}

button:hover,
.el-button:hover,
.Button:hover,
[type="button"]:hover,
[type="submit"]:hover,
[type="reset"]:hover {
  background-color: var(--btn-hover-color);
}
button:disabled,
.el-button.is-disabled,
.Button[disabled],
[type="button"]:disabled,
[type="submit"]:disabled,
[type="reset"]:disabled {
  background-color: #ccc;
  cursor: not-allowed;
  opacity: 0.8;
}
/* 全局样式 */
body {
  margin: 0;
  padding: 0;
  font-family: Avenir, Helvetica, Arial, sans-serif;
  background-color: #1e1e1e;
  color: #ffffff;
}
</style>