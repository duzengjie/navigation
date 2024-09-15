<template>
  <el-tabs v-model="editableTabsValue" class="tabs">
    <el-tab-pane :label="item.name" :name="item.id" v-for="item in editableTabs" :key="item.id">
      <el-row :gutter="20">
        <el-col :span="6" v-for="url in item.data" :key="url.name">
          <Card :name="url.urlName" :remark="url.remark" :url="url.url" :id="url.id" :orderNum="url.orderNum" @refresh-parent-page="getData" />
        </el-col>
      </el-row>
    </el-tab-pane>
  </el-tabs>

  <el-dropdown class="float-button" split-button type="primary" @click="addCardDialogVisible = true">
    新增卡片
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="addTabDialogVisible = true">新增环境</el-dropdown-item>
        <el-dropdown-item @click="delTabDialogVisible = true">删除环境</el-dropdown-item>
        <el-dropdown-item @click="downloadAllByExcel">备份数据</el-dropdown-item>
        <el-dropdown-item @click="uploadBackupRecoverByExcelDialogVisible = true">恢复数据</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>

  <el-dialog v-model="addCardDialogVisible" title="新增卡片">
    <el-form label-width="50px">
      <el-form-item label="链接">
        <el-input v-model="addCardForm.url" />
      </el-form-item>

      <el-form-item label="名称">
        <el-input v-model="addCardForm.urlName" />
      </el-form-item>

      <el-form-item label="备注">
        <el-input v-model="addCardForm.remark" type="textarea" />
      </el-form-item>

      <el-form-item label="排序">
        <el-input v-model="addCardForm.orderNum" type="number" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addCardDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addCard">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>

  <el-dialog v-model="addTabDialogVisible" title="新增环境">
    <el-form label-width="50px">
      <el-form-item label="名称">
        <el-input v-model="addTabForm.name" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="addTabDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addTab">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>

  <el-dialog v-model="delTabDialogVisible" title="删除环境">
    <el-form label-width="50px">
      <el-form-item label="名称">
        <el-text>{{ editableTabs.find(tab => { return tab.id == editableTabsValue }).name }}</el-text>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="delTabDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="delTab">
          确认
        </el-button>
      </span>
    </template>
  </el-dialog>

  <el-dialog v-model="uploadBackupRecoverByExcelDialogVisible" title="还原备份">
    <el-upload
    ref="uploadBackupRecover"
    class="upload"
    :http-request="uploadBackupRecoverByExcelAction"
    :limit="1"
    accept=".xlsx"
    :auto-upload="false"
  >
    <template #trigger>
      <el-button type="primary">文件选择</el-button>
    </template>

    <template #tip>
      <div class="el-upload__tip text-red">
        注意上传备份文件会将数据先清空
      </div>
    </template>
  </el-upload>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="uploadBackupRecoverByExcelDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="uploadBackupRecoverByExcel()">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script  setup>
import {reactive, ref} from "vue";
import moment from 'moment';
import {ElMessage} from "element-plus";
import Card from "./Card.vue";
import requestService from "../http/request";
//默认选择第一个
const editableTabsValue = ref(1)
//标签
const editableTabs = ref([])
//新增卡片弹窗
const addCardDialogVisible = ref(false)
//新增环境弹窗
const addTabDialogVisible = ref(false)
//删除环境弹窗
const delTabDialogVisible = ref(false)
//上传备份文件弹窗
const uploadBackupRecoverByExcelDialogVisible = ref(false)
//新增卡片表单
const addCardForm = reactive({ url: "http://" })
//新增TAB
const addTabForm = reactive({})
//备份文件上传类
const uploadBackupRecover = ref({})
/**
 * 获取数据方法
 */
const getData = (initFlag) => {
  requestService({
    url: "/env/api/list",
    method: 'get'
  }).then((res) => {
    editableTabs.value = res.data.data;
    if (initFlag) {
      if(res.data.data[0]){
        //console.info('初始化的时候',res.data.data[0].id)
        editableTabsValue.value = res.data.data[0].id
      }
    }
  });
}
/**
 * 新增卡片
 */
const addCard = () => {
  requestService({
    url: "/url/api/add",
    method: 'post',
    data: { environmentId: editableTabsValue.value, 
    url: addCardForm.url, 
    remark: addCardForm.remark, 
    urlName: addCardForm.urlName,
    orderNum: addCardForm.orderNum }
  }).then((res) => {
    if (res.data.data == true) {
      ElMessage({ message: '新增成功', type: 'success' })
    } else {
      ElMessage(res.data.msg)
    }
    //关闭弹窗
    addCardDialogVisible.value = false
    //重置
    addCardForm.url = "http://"
    addCardForm.remark = null
    addCardForm.urlName = null
    addCardForm.orderNum = null
    //刷新页面
    getData();
  });
}
/**
 * 新增环境
 */
const addTab = () => {
  requestService({
    url: "/env/api/add",
    method: 'post',
    data: { name: addTabForm.name }
  }).then((res) => {
    if (res.data.data == true) {
      ElMessage({ message: '新增成功', type: 'success' })
    } else {
      ElMessage(res.data.msg)
    }
    //关闭弹窗
    addTabDialogVisible.value = false
    //重置
    addTabForm.name = null
    //刷新页面
    getData(true);
  });
}
/**
 * 删除环境
 */
const delTab = () => {
  requestService({
    url: "/env/api/delete?id=" + editableTabsValue.value,
    method: 'get',
  }).then((res) => {
    if (res.data.data == true) {
      ElMessage({ message: '删除成功', type: 'success' })
      delTabDialogVisible.value = false
      //刷新页面
      getData(true);
    } else {
      ElMessage(res.data.msg)
      //关闭弹窗
      delTabDialogVisible.value = false
    }
  });
}
/**
 * 下载文件
 */
const downloadAllByExcel = () => {
  requestService({
    url: "/env/api/downloadAllByExcel",
    method: 'get',
    responseType: 'blob'
  }).then((res) => {
    // 地址转换
    let url = window.URL.createObjectURL(res.data);
    // 文件名
    let fileName = moment(new Date()).format('YYYY-MM-DD')+".xlsx";
    const a = document.createElement("a");
    a.setAttribute("href", url);
    a.setAttribute("download", fileName);
    document.body.append(a);
    a.click();
    document.body.removeChild(a);
  });
}
/**
 * 确认上传
 */
const uploadBackupRecoverByExcel = () =>{
  console.log("uploadBackupRecover.value",uploadBackupRecover.value)
  uploadBackupRecover.value.submit()
}
/**
 * 上传
 * @param {*} param 
 */
const uploadBackupRecoverByExcelAction = (param) =>{
  console.log('param',param)
  let formData = new FormData()
  formData.append('file', param.file)
  requestService({
    url: "/env/api/backupRecoverByExcel",
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: formData,
    timeout: 300000
  }).then(res => {
    uploadBackupRecoverByExcelDialogVisible.value = false
    uploadBackupRecover.value.clearFiles()
    if(res.data.code == 200){
      //ElMessage.success('请求成功');
      getData(true);
    }else{
      ElMessage.error(res.data.msg);
    }
  }).catch((err) => {
    console.log(2222)
    ElMessage.error('异常:'+err);
    uploadBackupRecoverByExcelDialogVisible.value = false
    uploadBackupRecover.value.clearFiles()
    getData(true);
    console.log(err);
  });
}
/**
 * 初始化数据
 */
getData(true);
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

.float-button {
  position: fixed;
  bottom: 20px;
  right: 20px;
}
</style>
  