<template>
  <div class="home-container">
    <!-- Search Bar -->
    <div class="search-bar">
      <div class="search-input-wrapper">
        <svg class="search-icon" viewBox="0 0 24 24" fill="none">
          <circle cx="11" cy="11" r="8" stroke="currentColor" stroke-width="2"/>
          <path d="M21 21l-4.35-4.35" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
        </svg>
        <input
          v-model="searchKeyword"
          type="text"
          class="search-input"
          placeholder="搜索名称或备注..."
          clearable
        />
        <button v-if="searchKeyword" class="search-clear" @click="searchKeyword = ''">
          <svg viewBox="0 0 24 24" fill="none">
            <path d="M18 6L6 18M6 6l12 12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
        </button>
      </div>
    </div>

    <!-- Tabs -->
    <div class="tabs-container">
      <el-tabs v-model="editableTabsValue" class="custom-tabs">
        <el-tab-pane :label="item.name" :name="item.id" v-for="item in editableTabs" :key="item.id">
          <div class="cards-grid">
            <transition-group name="card-list">
              <div
                class="card-wrapper"
                v-for="(url, index) in filterData(item.data)"
                :key="url.id"
                :style="{ animationDelay: `${index * 0.05}s` }"
              >
                <Card
                  :name="url.urlName"
                  :remark="url.remark"
                  :url="url.url"
                  :id="url.id"
                  :orderNum="url.orderNum"
                  @refresh-parent-page="getData"
                />
              </div>
            </transition-group>
          </div>

          <!-- Empty State -->
          <div v-if="!filterData(item.data) || filterData(item.data).length === 0" class="empty-state">
            <div class="empty-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path v-if="searchKeyword" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                <path v-else d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <p class="empty-text">{{ searchKeyword ? '未找到匹配结果' : '暂无书签' }}</p>
            <p class="empty-hint">{{ searchKeyword ? '请尝试其他关键词' : '点击右下角按钮添加新卡片' }}</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- Floating Action Button -->
    <div class="fab-container">
      <el-dropdown trigger="click" placement="top-end" :hide-on-click="true">
        <button class="fab-button">
          <span class="fab-icon">+</span>
        </button>
        <template #dropdown>
          <el-dropdown-menu class="fab-menu">
            <el-dropdown-item @click="addCardDialogVisible = true" class="fab-item">
              <span class="fab-item-icon">
                <svg viewBox="0 0 24 24" fill="none">
                  <rect x="3" y="3" width="18" height="18" rx="2" stroke="currentColor" stroke-width="2"/>
                  <path d="M12 8v8m-4-4h8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                </svg>
              </span>
              <span>新增卡片</span>
            </el-dropdown-item>
            <el-dropdown-item @click="addTabDialogVisible = true" class="fab-item">
              <span class="fab-item-icon">
                <svg viewBox="0 0 24 24" fill="none">
                  <path d="M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2h-6l-2-2H5a2 2 0 00-2 2z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>新增环境</span>
            </el-dropdown-item>
            <el-dropdown-item @click="delTabDialogVisible = true" class="fab-item">
              <span class="fab-item-icon danger">
                <svg viewBox="0 0 24 24" fill="none">
                  <path d="M3 6h18M8 6V4a1 1 0 011-1h6a1 1 0 011 1v2m3 0v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6h14z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>删除环境</span>
            </el-dropdown-item>
            <el-dropdown-item divided @click="downloadAllByExcel" class="fab-item">
              <span class="fab-item-icon">
                <svg viewBox="0 0 24 24" fill="none">
                  <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4M7 10l5 5 5-5M12 15V3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>备份数据</span>
            </el-dropdown-item>
            <el-dropdown-item @click="uploadBackupRecoverByExcelDialogVisible = true" class="fab-item">
              <span class="fab-item-icon">
                <svg viewBox="0 0 24 24" fill="none">
                  <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4M17 8l-5-5-5 5M12 3v12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </span>
              <span>恢复数据</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

    <!-- Add Card Dialog -->
    <el-dialog
      v-model="addCardDialogVisible"
      title="新增卡片"
      width="480px"
      class="custom-dialog"
      :close-on-click-modal="false"
    >
      <el-form label-width="60px" class="custom-form">
        <el-form-item label="链接">
          <el-input v-model="addCardForm.url" placeholder="https://example.com" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="addCardForm.urlName" placeholder="书签名称" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="addCardForm.remark" type="textarea" :rows="3" placeholder="添加备注信息..." />
        </el-form-item>
        <el-form-item label="排序">
          <el-input v-model="addCardForm.orderNum" type="number" placeholder="数字越小越靠前" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="addCardDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addCard">确认添加</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Add Tab Dialog -->
    <el-dialog
      v-model="addTabDialogVisible"
      title="新增环境"
      width="400px"
      class="custom-dialog"
      :close-on-click-modal="false"
    >
      <el-form label-width="60px" class="custom-form">
        <el-form-item label="名称">
          <el-input v-model="addTabForm.name" placeholder="环境名称，如：开发、测试、生产" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="addTabDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="addTab">确认添加</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Delete Tab Dialog -->
    <el-dialog
      v-model="delTabDialogVisible"
      title="删除环境"
      width="400px"
      class="custom-dialog"
      :close-on-click-modal="false"
    >
      <div class="delete-warning">
        <div class="warning-icon">
          <svg viewBox="0 0 24 24" fill="none">
            <path d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          </svg>
        </div>
        <p class="warning-text">确定要删除环境吗？</p>
        <p class="warning-env-name">{{ editableTabs.find(tab => tab.id == editableTabsValue)?.name }}</p>
        <p class="warning-hint">此操作将删除该环境下的所有卡片</p>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="delTabDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="delTab">确认删除</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- Upload Dialog -->
    <el-dialog
      v-model="uploadBackupRecoverByExcelDialogVisible"
      title="恢复备份"
      width="480px"
      class="custom-dialog"
      :close-on-click-modal="false"
    >
      <div class="upload-area">
        <el-upload
          ref="uploadBackupRecover"
          class="upload-dragger"
          :http-request="uploadBackupRecoverByExcelAction"
          :limit="1"
          accept=".xlsx"
          :auto-upload="false"
          drag
        >
          <div class="upload-content">
            <div class="upload-icon">
              <svg viewBox="0 0 24 24" fill="none">
                <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4M17 8l-5-5-5 5M12 3v12" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
            <p class="upload-text">拖拽文件到此处，或<em>点击上传</em></p>
            <p class="upload-hint">仅支持 .xlsx 格式的备份文件</p>
          </div>
        </el-upload>
        <div class="upload-warning">
          <svg viewBox="0 0 24 24" fill="none">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <path d="M12 8v4m0 4h.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          </svg>
          <span>注意：上传备份文件将会先清空现有数据</span>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="uploadBackupRecoverByExcelDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="uploadBackupRecoverByExcel()">确认恢复</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import moment from 'moment';
import { ElMessage } from "element-plus";
import Card from "./Card.vue";
import requestService from "../http/request";

// Default select first tab
const editableTabsValue = ref(1)
// Tabs data
const editableTabs = ref([])
// Search keyword
const searchKeyword = ref('')
// Add card dialog
const addCardDialogVisible = ref(false)
// Add tab dialog
const addTabDialogVisible = ref(false)
// Delete tab dialog
const delTabDialogVisible = ref(false)
// Upload backup dialog
const uploadBackupRecoverByExcelDialogVisible = ref(false)
// Add card form
const addCardForm = reactive({ url: "https://" })
// Add tab form
const addTabForm = reactive({})
// Upload ref
const uploadBackupRecover = ref({})

/**
 * Get data
 */
const getData = (initFlag) => {
  requestService({
    url: "/env/api/list",
    method: 'get'
  }).then((res) => {
    editableTabs.value = res.data.data;
    if (initFlag) {
      if (res.data.data[0]) {
        editableTabsValue.value = res.data.data[0].id
      }
    }
  });
}

/**
 * Filter data by search keyword
 */
const filterData = (data) => {
  if (!data) return [];
  if (!searchKeyword.value) return data;

  const keyword = searchKeyword.value.toLowerCase();
  return data.filter(item =>
    (item.urlName && item.urlName.toLowerCase().includes(keyword)) ||
    (item.remark && item.remark.toLowerCase().includes(keyword))
  );
}

/**
 * Add card
 */
const addCard = () => {
  requestService({
    url: "/url/api/add",
    method: 'post',
    data: {
      environmentId: editableTabsValue.value,
      url: addCardForm.url,
      remark: addCardForm.remark,
      urlName: addCardForm.urlName,
      orderNum: addCardForm.orderNum
    }
  }).then((res) => {
    if (res.data.data === true) {
      ElMessage({ message: '新增成功', type: 'success' })
    } else {
      ElMessage(res.data.msg)
    }
    addCardDialogVisible.value = false
    addCardForm.url = "https://"
    addCardForm.remark = null
    addCardForm.urlName = null
    addCardForm.orderNum = null
    getData();
  });
}

/**
 * Add tab
 */
const addTab = () => {
  requestService({
    url: "/env/api/add",
    method: 'post',
    data: { name: addTabForm.name }
  }).then((res) => {
    if (res.data.data === true) {
      ElMessage({ message: '新增成功', type: 'success' })
    } else {
      ElMessage(res.data.msg)
    }
    addTabDialogVisible.value = false
    addTabForm.name = null
    getData(true);
  });
}

/**
 * Delete tab
 */
const delTab = () => {
  requestService({
    url: "/env/api/delete?id=" + editableTabsValue.value,
    method: 'get',
  }).then((res) => {
    if (res.data.data === true) {
      ElMessage({ message: '删除成功', type: 'success' })
      delTabDialogVisible.value = false
      getData(true);
    } else {
      ElMessage({ message: res.data.msg, type: 'error' })
      delTabDialogVisible.value = false
    }
  });
}

/**
 * Download backup
 */
const downloadAllByExcel = () => {
  requestService({
    url: "/env/api/downloadAllByExcel",
    method: 'get',
    responseType: 'blob'
  }).then((res) => {
    let url = window.URL.createObjectURL(res.data);
    let fileName = moment(new Date()).format('YYYY-MM-DD') + "_navigation_backup.xlsx";
    const a = document.createElement("a");
    a.setAttribute("href", url);
    a.setAttribute("download", fileName);
    document.body.append(a);
    a.click();
    document.body.removeChild(a);
  });
}

/**
 * Confirm upload
 */
const uploadBackupRecoverByExcel = () => {
  uploadBackupRecover.value.submit()
}

/**
 * Upload action
 */
const uploadBackupRecoverByExcelAction = (param) => {
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
    if (res.data.code === 200) {
      getData(true);
    } else {
      ElMessage.error(res.data.msg);
    }
  }).catch((err) => {
    ElMessage.error('异常:' + err);
    uploadBackupRecoverByExcelDialogVisible.value = false
    uploadBackupRecover.value.clearFiles()
    getData(true);
  });
}

// Initialize
getData(true);
</script>

<style scoped>
.home-container {
  max-width: 1800px;
  margin: 0 auto;
  animation: fadeIn 0.5s ease;
}

/* Search Bar */
.search-bar {
  margin-bottom: var(--space-md);
}

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 12px;
  width: 18px;
  height: 18px;
  color: var(--color-text-muted);
  pointer-events: none;
}

.search-input {
  width: 100%;
  max-width: 320px;
  height: 40px;
  padding: 0 36px;
  font-size: 0.875rem;
  font-family: var(--font-body);
  color: var(--color-text-primary);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  outline: none;
  transition: all var(--transition-base);
}

.search-input::placeholder {
  color: var(--color-text-muted);
}

.search-input:hover {
  border-color: var(--color-border-hover);
}

.search-input:focus {
  border-color: var(--color-accent-primary);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.search-clear {
  position: absolute;
  left: 290px;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  color: var(--color-text-muted);
  transition: all var(--transition-fast);
}

.search-clear:hover {
  color: var(--color-text-primary);
  background: var(--color-surface-hover);
}

.search-clear svg {
  width: 14px;
  height: 14px;
}

/* Tabs Container */
.tabs-container {
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-xl);
  padding: var(--space-md);
  box-shadow: var(--shadow-sm);
}

/* Cards Grid */
.cards-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: var(--space-sm);
}

@media (max-width: 1600px) {
  .cards-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 1200px) {
  .cards-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .cards-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 600px) {
  .cards-grid {
    grid-template-columns: 1fr;
  }
}

.card-wrapper {
  animation: slideUp 0.5s ease backwards;
}

/* Card list transition */
.card-list-enter-active,
.card-list-leave-active {
  transition: all 0.4s ease;
}

.card-list-enter-from,
.card-list-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

/* Empty State */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  animation: fadeIn 0.5s ease;
}

.empty-icon {
  width: 64px;
  height: 64px;
  margin: 0 auto var(--space-lg);
  color: var(--color-text-muted);
  opacity: 0.5;
}

.empty-icon svg {
  width: 100%;
  height: 100%;
}

.empty-text {
  font-family: var(--font-display);
  font-size: 1.125rem;
  color: var(--color-text-secondary);
  margin: 0 0 var(--space-sm) 0;
}

.empty-hint {
  font-size: 0.875rem;
  color: var(--color-text-muted);
  margin: 0;
}

/* Floating Action Button */
.fab-container {
  position: fixed;
  bottom: 32px;
  right: 32px;
  z-index: 1000;
}

.fab-button {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: var(--gradient-primary);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 16px rgba(59, 130, 246, 0.35);
  transition: all 0.3s ease;
}

.fab-button:hover {
  transform: scale(1.1) rotate(90deg);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.45);
}

.fab-icon {
  font-size: 26px;
  color: white;
  font-weight: 300;
  line-height: 1;
}

/* FAB Menu */
.fab-menu {
  min-width: 170px;
}

.fab-item {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-sm) var(--space-md) !important;
}

.fab-item-icon {
  width: 18px;
  height: 18px;
  color: var(--color-accent-primary);
  flex-shrink: 0;
}

.fab-item-icon svg {
  width: 100%;
  height: 100%;
}

.fab-item-icon.danger {
  color: var(--color-accent-danger);
}

/* Dialog */
.custom-dialog .el-dialog {
  background: var(--color-bg-secondary);
}

.custom-form {
  padding: var(--space-sm) 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-md);
}

/* Delete Warning */
.delete-warning {
  text-align: center;
  padding: var(--space-md) 0;
}

.warning-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto var(--space-md);
  color: var(--color-accent-warning);
}

.warning-icon svg {
  width: 100%;
  height: 100%;
}

.warning-text {
  font-family: var(--font-display);
  font-size: 1rem;
  color: var(--color-text-primary);
  margin: 0 0 var(--space-sm) 0;
}

.warning-env-name {
  font-size: 0.9rem;
  color: var(--color-accent-primary);
  margin: 0 0 var(--space-sm) 0;
  padding: var(--space-xs) var(--space-md);
  background: var(--color-surface);
  border-radius: var(--radius-md);
  display: inline-block;
}

.warning-hint {
  font-size: 0.8rem;
  color: var(--color-text-tertiary);
  margin: 0;
}

/* Upload Area */
.upload-area {
  padding: var(--space-sm) 0;
}

.upload-dragger :deep(.el-upload-dragger) {
  width: 100%;
  height: auto;
  padding: var(--space-xl);
  border-radius: var(--radius-lg);
  background: var(--color-bg-primary);
  border: 2px dashed var(--color-border);
  transition: all var(--transition-base);
}

.upload-dragger :deep(.el-upload-dragger:hover) {
  border-color: var(--color-accent-primary);
  background: rgba(59, 130, 246, 0.02);
}

.upload-content {
  text-align: center;
}

.upload-icon {
  width: 40px;
  height: 40px;
  margin: 0 auto var(--space-md);
  color: var(--color-accent-primary);
}

.upload-icon svg {
  width: 100%;
  height: 100%;
}

.upload-text {
  font-size: 0.9rem;
  color: var(--color-text-secondary);
  margin: 0 0 var(--space-sm) 0;
}

.upload-text em {
  color: var(--color-accent-primary);
  font-style: normal;
}

.upload-hint {
  font-size: 0.75rem;
  color: var(--color-text-muted);
  margin: 0;
}

.upload-warning {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  margin-top: var(--space-md);
  padding: var(--space-md);
  background: rgba(239, 68, 68, 0.06);
  border: 1px solid rgba(239, 68, 68, 0.15);
  border-radius: var(--radius-md);
  color: var(--color-accent-danger);
  font-size: 0.8rem;
}

.upload-warning svg {
  width: 16px;
  height: 16px;
  flex-shrink: 0;
}

/* Responsive */
@media (max-width: 768px) {
  .tabs-container {
    padding: var(--space-md);
  }

  .fab-container {
    bottom: 20px;
    right: 20px;
  }

  .fab-button {
    width: 48px;
    height: 48px;
  }

  .fab-icon {
    font-size: 22px;
  }
}
</style>
