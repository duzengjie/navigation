<template>
    <el-card class="box-card">
        <template #header>
            <div class="card-header">
                <span>{{name}}</span>
                <el-button class="button" text @click="jump">前往</el-button>
            </div>
        </template>
        <span class="text item">{{remark}}</span>
    </el-card>
</template>


<script >
import { defineComponent } from 'vue';
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
    }
  }
});


</script>


<style>
.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
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
  