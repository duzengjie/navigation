<template>
  <el-tabs v-model="editableTabsValue" class="tabs">
    <el-tab-pane :label="item.name" :name="item.id" v-for="item in editableTabs" :key="item.id">
      <el-row :gutter="20">
        <el-col :span="6" v-for="url in item.data" :key="url.name">
          <Card :name="url.urlName" :remark="url.remark" :url="url.url" :id="url.id" @refresh-parent-page="getData" />
        </el-col>
      </el-row>
    </el-tab-pane>
  </el-tabs>
</template>
<script  setup>
import { ref } from "vue";
import Card from "./Card.vue";
import requestService from "../http/request";
//默认选择第一个
const editableTabsValue = ref(1)
//标签
const editableTabs = ref([])
/**
 * 获取数据方法
 */
const getData = () => {
  requestService({
    url: "/env/api/list",
    method: 'get'
  }).then((res) => {
    editableTabs.value = res.data.data;
    //防止id1删除之后 无法默认选择到
    editableTabsValue.value = res.data.data[0].id
  });
}
/**
 * 初始化数据
 */
getData();
</script>
<style>
.tabs>.el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
</style>
  