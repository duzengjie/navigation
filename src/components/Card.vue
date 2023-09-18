<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span><el-text truncated>{{name}}</el-text></span>
                <el-button class="button" text @click="jump">前往</el-button>
            </div>
        </template>
       <div style="height:50px" @click="open(remark)" > <span class="text item">
              <el-text truncated>{{remark}}</el-text>
        </span></div>
    </el-card>
</template>


<script >
import { defineComponent } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus'
export default defineComponent({
  props: {
    url: {
      type: String
    },
    name: {
      type: String
    },
    remark: {
      type: String
    }
  },
  methods: {
    jump() {
      console.log(this.url); // 在方法内部使用 this.url
      //alert('Hello!');
      //const newTab = window.open();
        //newTab.location.href = this.url;
      window.open(this.url, '_blank')
    },
   open(message) {
   ElMessageBox.alert(message, '备注', {confirmButtonText: 'OK'})
}
  }
});


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
  