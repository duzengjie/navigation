<template>
  <el-card class="box-card">
    <template #header>
      <div class="card-header">
        <span><el-text truncated>{{ name }}</el-text></span>
        <div>
          <el-button class="button" type="primary" text @click="jump">前往</el-button>
          <el-button class="button" type="success" text @click="openUpdateCarddDialog">更新</el-button>
        </div>
      </div>
    </template>
    <div style="height: 50px" @click="open(remark)">
      <span class="text item">
        <el-text truncated size="small" type="info">{{ remark }}</el-text>
      </span>
    </div>
  </el-card>

  <el-dialog v-model="updateCardDialogVisible" title="修改">
    <el-form label-width="50px">
      <el-form-item label="链接">
        <el-input v-model="form.url" />
      </el-form-item>

      <el-form-item label="名称">
        <el-input v-model="form.urlName" />
      </el-form-item>

      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" />
      </el-form-item>

      <el-form-item label="排序">
        <el-input v-model="form.orderNum" type="number" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-row class="row-bg" justify="space-between">
          <el-col :span="3"> 
            <el-button type="danger" @click="deleteCard">删除</el-button>
          </el-col>

          <el-col :span="6"> 
            <el-button @click="updateCardDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="updateCard">确认</el-button>
          </el-col>
        </el-row>
    </template>
  </el-dialog>
</template>


<script setup>
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import requestService from "../http/request";

let updateCardDialogVisible = ref(false)
const emits = defineEmits(['refreshParentPage'])

const props = defineProps({
  id: Number,
  url: String,
  name: String,
  remark: String,
  orderNum: Number
});

const form = reactive({ 
  id: props.id, 
  url: props.url, 
  remark: props.remark, 
  urlName: props.name,
  orderNum: props.orderNum
})

/**
 * 跳转链接
 */
const jump = () => {
  //请求记录下次数
  requestService({
    url: "/url/api/useNumIncrease",
    method: 'post',
    data: { id: form.id }
  }).then((res) => {
    window.open(props.url, "_blank")
  });
};
/**
 * 备注弹出提示
 * @param {备注} message 
 */
const open = (message) => {
  ElMessageBox.alert(message, "备注", { confirmButtonText: "OK" })
};
/**
 * 打开更新卡片的窗口
 */
const openUpdateCarddDialog = () => {
  updateCardDialogVisible.value = true
}
/**
 * 更新卡片
 */
const updateCard = () => {
  requestService({
    url: "/url/api/update",
    method: 'post',
    data: { id: form.id, url: form.url, remark: form.remark, urlName: form.urlName,orderNum:form.orderNum }
  }).then((res) => {
    if (res.data.data == true) {
      ElMessage({ message: '更新成功', type: 'success' })
    } else {
      ElMessage(res.data.msg)
    }
    updateCardDialogVisible.value = false
    emits('refreshParentPage')
  });
}
/**
 * 更新卡片
 */
const deleteCard = () => {
  requestService({
    url: "/url/api/delete?id=" + form.id,
    method: 'get'
  }).then((res) => {
    if (res.data.data == true) {
      ElMessage({ message: '删除成功', type: 'success' })
    } else {
      ElMessage(res.data.msg)
    }
    updateCardDialogVisible.value = false
    emits('refreshParentPage')
  });
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
}


</style>
  