<template>
  <el-tabs v-model="editableTabsValue" class="tabs" editable @edit="handleTabsEdit">
    <el-tab-pane :label="item.name" :name="item.id" v-for="item in editableTabs" :key="item.id">
      <el-row :gutter="20">
        <el-col :span="6" v-for="url in item.data" :key="url.name">
          <Card :name="url.urlName" :remark="url.remark" :url="url.url" />
        </el-col>
      </el-row>
    </el-tab-pane>
  </el-tabs>
</template>
<script  setup>
import { ref } from "vue";
import Card from "./Card.vue";
import requestService from "../http/request";
let tabIndex = 2
//默认选择第一个
const editableTabsValue = ref(1)
//标签
const editableTabs = ref([])

/**
 * 初始化数据
 */
requestService({
  url: "/env/api/list",
  method: 'get'
}).then((res) => {
  editableTabs.value = res.data.data;
  //防止id1删除之后 无法默认选择到
  editableTabsValue.value = res.data.data[0].id
});

const handleTabsEdit = (
  targetName,
  action
) => {
  if (action === 'add') {
    const newTabName = `${++tabIndex}`
    editableTabs.value.push({
      title: 'New Tab',
      name: newTabName,
      content: 'New Tab content',
    })
    editableTabsValue.value = newTabName
  } else if (action === 'remove') {
    const tabs = editableTabs.value
    let activeName = editableTabsValue.value
    if (activeName === targetName) {
      tabs.forEach((tab, index) => {
        if (tab.name === targetName) {
          const nextTab = tabs[index + 1] || tabs[index - 1]
          if (nextTab) {
            activeName = nextTab.name
          }
        }
      })
    }

    editableTabsValue.value = activeName
    editableTabs.value = tabs.filter((tab) => tab.name !== targetName)
  }
}
</script>
<style>
.tabs>.el-tabs__content {
  padding: 32px;
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
  