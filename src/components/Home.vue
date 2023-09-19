<template>
  <el-tabs v-model="activeName"  class="tabs"  >
    <el-tab-pane :label="item.label" :name="item.name" v-for="item in data" :key="item.name">
      <el-row :gutter="20">
        <el-col :span="6" v-for="url in item.data" :key="url.name">
          <Card :name="url.name" :remark="url.remark" :url="url.url" />
        </el-col>
      </el-row>
    </el-tab-pane>
  </el-tabs>
</template>
<script lang="ts" setup>
import { nextTick, ref } from "vue";
import type { TabsPaneContext } from "element-plus";
import Card from "./Card.vue";
import httpService from "../http/request";
let data = ref([]);
const activeName = ref('')
httpService.get("/data.json").then((res) => {
  data.value = res.data;
  activeName.value = res.data[0].name
});
</script>
<style>
.tabs > .el-tabs__content {
  padding: 10px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}

.el-row {
  flex-wrap: wrap;
}

.el-col {
  flex-basis: 0;
  flex-grow: 1;
  max-width: 100%;
}
</style>
  