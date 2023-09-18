<template>
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
        <el-tab-pane label="测试" name="first">
            <el-row :gutter="20">
                <el-col :span="6" v-for="item in urls">
                    <Card :name="item.name" :remark="item.remark" :url="item.url" />
                </el-col>
            </el-row>
        </el-tab-pane>
        <el-tab-pane label="模板" name="second">     <el-row :gutter="20">
                <el-col :span="6" v-for="item in urls2">
                    <Card :name="item.name" :remark="item.remark" :url="item.url" />
                </el-col>
            </el-row></el-tab-pane>
        <el-tab-pane label="生产" name="third">     <el-row :gutter="20">
                <el-col :span="6" v-for="item in urls3">
                    <Card :name="item.name" :remark="item.remark" :url="item.url" />
                </el-col>
            </el-row></el-tab-pane>
    </el-tabs>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'
import Card from "./Card.vue"

const activeName = ref('first')
const handleClick = (tab: TabsPaneContext, event: Event) => {
    console.log(tab, event)
}
let urls = ref([])
let urls2 = ref([])
let urls3 = ref([])

fetch('/testing.json')  // 替换为你的 JSON 文件路径
        .then(response => response.json())
        .then(data => {
            urls.value = data;  // 将获取到的 JSON 数据保存到组件数据中
        })
        .catch(error => {
          console.error('Error:', error);
        });

        fetch('/release.json')  // 替换为你的 JSON 文件路径
        .then(response => response.json())
        .then(data => {
            urls2.value = data;  // 将获取到的 JSON 数据保存到组件数据中
        })
        .catch(error => {
          console.error('Error:', error);
        });
        
        
        fetch('/product.json')  // 替换为你的 JSON 文件路径
        .then(response => response.json())
        .then(data => {
            urls3.value = data;  // 将获取到的 JSON 数据保存到组件数据中
        })
        .catch(error => {
          console.error('Error:', error);
        });
</script>
<style>
.demo-tabs>.el-tabs__content {
    padding: 10px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
}
</style>
  