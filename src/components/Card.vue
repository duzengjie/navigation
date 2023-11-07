<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span
          ><el-text truncated>{{ name }}</el-text></span
        >
        <div>
          <el-button class="button" type="primary" text @click="jump"
            >前往</el-button
          >
          <el-button class="button" type="success" text @click="updateCard"
            >更新</el-button
          >
        </div>
      </div>
    </template>
    <div style="height: 50px" @click="open(remark)">
      <span class="text item">
        <el-text truncated>{{ remark }}</el-text>
      </span>
    </div>
  </el-card>

  <el-dialog v-model="updateCardDialogVisible" title="修改地址">
    <el-form label-width="120px">
      <el-form-item label="链接">
        <el-input v-model="props.url" />
      </el-form-item>

      <el-form-item label="姓名">
        <el-input v-model="props.name" />
      </el-form-item>

      <el-form-item label="备注">
        <el-input v-model="props.remark" />
      </el-form-item>
    </el-form>
  </el-dialog>
</template>


<script setup>
import { defineComponent, ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
let updateCardDialogVisible = ref(false)

const props = defineProps({
  url: String,
  name: String,
  remark: String,
});
const jump = () => {
  window.open(this.url, "_blank")
};
const open = (message) => {
  ElMessageBox.alert(message, "备注", { confirmButtonText: "OK" })
};
const updateCard = () => {
  this.updateCardDialogVisible = true
}
</script>

<style>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 20px;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  margin-top: 20px;
  width: 400px;
}
</style>
  