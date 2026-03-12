<template>
  <div class="bookmark-card" @mouseenter="isHovered = true" @mouseleave="isHovered = false">
    <!-- Card Glow Effect -->
    <div class="card-glow" :class="{ active: isHovered }"></div>

    <!-- Card Content -->
    <div class="card-content">
      <!-- Header -->
      <div class="card-header">
        <div class="card-title">
          <div class="card-favicon" v-if="faviconUrl">
            <img :src="faviconUrl" :alt="name" @error="faviconUrl = null" />
          </div>
          <div class="card-favicon placeholder" v-else>
            <svg viewBox="0 0 24 24" fill="none">
              <path d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <span class="card-name">{{ name }}</span>
        </div>
        <div class="card-actions" :class="{ visible: isHovered }">
          <button class="action-btn primary" @click.stop="jump" title="前往链接">
            <svg viewBox="0 0 24 24" fill="none">
              <path d="M18 13v6a2 2 0 01-2 2H5a2 2 0 01-2-2V8a2 2 0 012-2h6M15 3h6v6M10 14L21 3" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
          <button class="action-btn success" @click.stop="openUpdateCarddDialog" title="编辑卡片">
            <svg viewBox="0 0 24 24" fill="none">
              <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </button>
        </div>
      </div>

      <!-- Body -->
      <div class="card-body" @click="open(remark)">
        <p class="card-remark" :class="{ empty: !remark }">
          {{ remark || '点击添加备注...' }}
        </p>
      </div>

      <!-- Footer -->
      <div class="card-footer">
        <div class="card-url">
          <svg viewBox="0 0 24 24" fill="none" class="url-icon">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <path d="M2 12h20M12 2a15.3 15.3 0 014 10 15.3 15.3 0 01-4 10 15.3 15.3 0 01-4-10 15.3 15.3 0 014-10z" stroke="currentColor" stroke-width="2"/>
          </svg>
          <span class="url-text">{{ displayUrl }}</span>
        </div>
      </div>
    </div>

    <!-- Gradient Border -->
    <div class="card-border"></div>
  </div>

  <!-- Update Dialog -->
  <el-dialog
    v-model="updateCardDialogVisible"
    title="编辑卡片"
    width="480px"
    class="custom-dialog"
    :close-on-click-modal="false"
  >
    <el-form label-width="60px" class="custom-form">
      <el-form-item label="链接">
        <el-input v-model="form.url" placeholder="https://example.com" />
      </el-form-item>
      <el-form-item label="名称">
        <el-input v-model="form.urlName" placeholder="书签名称" />
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="添加备注信息..." />
      </el-form-item>
      <el-form-item label="排序">
        <el-input v-model="form.orderNum" type="number" placeholder="数字越小越靠前" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="danger" @click="deleteCard">
          <span class="btn-icon">
            <svg viewBox="0 0 24 24" fill="none">
              <path d="M3 6h18M8 6V4a1 1 0 011-1h6a1 1 0 011 1v2m3 0v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6h14zM10 11v6M14 11v6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </span>
          删除
        </el-button>
        <div class="footer-right">
          <el-button @click="updateCardDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="updateCard">保存</el-button>
        </div>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, computed, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import requestService from "../http/request";

let updateCardDialogVisible = ref(false);
let isHovered = ref(false);
let faviconUrl = ref(null);

const emits = defineEmits(['refreshParentPage']);

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
});

// Display URL (truncated)
const displayUrl = computed(() => {
  try {
    const urlObj = new URL(props.url);
    return urlObj.hostname;
  } catch {
    return props.url?.substring(0, 30) || '未知链接';
  }
});

// Get favicon
onMounted(() => {
  try {
    const urlObj = new URL(props.url);
    faviconUrl.value = `https://www.google.com/s2/favicons?domain=${urlObj.hostname}&sz=32`;
  } catch {
    faviconUrl.value = null;
  }
});

/**
 * Jump to link
 */
const jump = () => {
  requestService({
    url: "/url/api/useNumIncrease",
    method: 'post',
    data: { id: form.id }
  }).then(() => {
    window.open(props.url, "_blank");
  });
};

/**
 * Show remark alert
 */
const open = (message) => {
  if (!message) return;
  ElMessageBox.alert(message, "备注", {
    confirmButtonText: "确定",
    customClass: 'remark-alert'
  });
};

/**
 * Open update dialog
 */
const openUpdateCarddDialog = () => {
  // Reset form with latest props
  form.id = props.id;
  form.url = props.url;
  form.remark = props.remark;
  form.urlName = props.name;
  form.orderNum = props.orderNum;
  updateCardDialogVisible.value = true;
};

/**
 * Update card
 */
const updateCard = () => {
  requestService({
    url: "/url/api/update",
    method: 'post',
    data: {
      id: form.id,
      url: form.url,
      remark: form.remark,
      urlName: form.urlName,
      orderNum: form.orderNum
    }
  }).then((res) => {
    if (res.data.data === true) {
      ElMessage({ message: '更新成功', type: 'success' });
    } else {
      ElMessage(res.data.msg);
    }
    updateCardDialogVisible.value = false;
    emits('refreshParentPage');
  });
};

/**
 * Delete card
 */
const deleteCard = () => {
  ElMessageBox.confirm(
    '确定要删除这个卡片吗？此操作不可恢复。',
    '删除确认',
    {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    requestService({
      url: "/url/api/delete?id=" + form.id,
      method: 'get'
    }).then((res) => {
      if (res.data.data === true) {
        ElMessage({ message: '删除成功', type: 'success' });
      } else {
        ElMessage(res.data.msg);
      }
      updateCardDialogVisible.value = false;
      emits('refreshParentPage');
    });
  }).catch(() => {
    // User cancelled
  });
};
</script>

<style scoped>
/* Bookmark Card */
.bookmark-card {
  position: relative;
  border-radius: var(--radius-lg);
  background: var(--color-bg-secondary);
  border: 1px solid var(--color-border);
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
}

.bookmark-card:hover {
  border-color: rgba(59, 130, 246, 0.3);
  box-shadow: var(--shadow-md);
}

/* Card Glow */
.card-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(
    ellipse at 50% 0%,
    rgba(59, 130, 246, 0.06) 0%,
    transparent 70%
  );
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;
}

.card-glow.active {
  opacity: 1;
}

/* Card Content */
.card-content {
  position: relative;
  z-index: 1;
  padding: var(--space-md);
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
  height: 100%;
}

/* Card Header */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: var(--space-sm);
}

.card-title {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  flex: 1;
  min-width: 0;
}

.card-favicon {
  width: 24px;
  height: 24px;
  border-radius: var(--radius-sm);
  overflow: hidden;
  flex-shrink: 0;
  background: var(--color-surface);
  display: flex;
  align-items: center;
  justify-content: center;
}

.card-favicon img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.card-favicon.placeholder {
  color: var(--color-text-muted);
}

.card-favicon.placeholder svg {
  width: 14px;
  height: 14px;
}

.card-name {
  font-family: var(--font-display);
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--color-text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Card Actions */
.card-actions {
  display: flex;
  gap: 4px;
  opacity: 0;
  transform: translateX(8px);
  transition: all 0.2s ease;
}

.card-actions.visible {
  opacity: 1;
  transform: translateX(0);
}

.action-btn {
  width: 28px;
  height: 28px;
  border: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

.action-btn svg {
  width: 14px;
  height: 14px;
}

.action-btn.primary {
  background: rgba(59, 130, 246, 0.1);
  color: var(--color-accent-primary);
}

.action-btn.primary:hover {
  background: var(--color-accent-primary);
  color: white;
}

.action-btn.success {
  background: rgba(16, 185, 129, 0.1);
  color: var(--color-accent-success);
}

.action-btn.success:hover {
  background: var(--color-accent-success);
  color: white;
}

/* Card Body */
.card-body {
  flex: 1;
  min-height: 40px;
}

.card-remark {
  font-size: 0.8rem;
  color: var(--color-text-secondary);
  margin: 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-remark.empty {
  color: var(--color-text-muted);
  font-style: italic;
}

/* Card Footer */
.card-footer {
  padding-top: var(--space-sm);
  border-top: 1px solid var(--color-border);
}

.card-url {
  display: flex;
  align-items: center;
  gap: 6px;
}

.url-icon {
  width: 12px;
  height: 12px;
  color: var(--color-text-muted);
  flex-shrink: 0;
}

.url-text {
  font-size: 0.7rem;
  color: var(--color-text-tertiary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Card Border */
.card-border {
  display: none;
}

/* Dialog Footer */
.dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.footer-right {
  display: flex;
  gap: var(--space-md);
}

.btn-icon {
  display: flex;
  align-items: center;
  margin-right: 4px;
}

.btn-icon svg {
  width: 14px;
  height: 14px;
}

/* Custom Dialog */
:deep(.custom-dialog) {
  background: var(--color-bg-secondary);
}

/* Responsive */
@media (max-width: 768px) {
  .card-content {
    padding: var(--space-sm);
  }

  .card-actions {
    opacity: 1;
    transform: none;
  }
}
</style>
