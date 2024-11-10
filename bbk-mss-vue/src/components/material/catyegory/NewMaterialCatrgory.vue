<template>
  <div>
    <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="150px">
      <el-form-item label-width="150px" label="创建人ID" prop="createUserId">
        <el-input v-model="formData.createUserId" readonly :disabled='true' clearable
          :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="物资分类编码" prop="categoryCode">
        <el-input v-model="formData.categoryCode" placeholder="请输入物资分类编码" :maxlength="50" show-word-limit
          clearable prefix-icon='el-icon-menu' :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="物资分类名称" prop="categoryName">
        <el-input v-model="formData.categoryName" placeholder="请输入物资分类名称" clearable
          prefix-icon='el-icon-takeaway-box' :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="配送策略" prop="enable" required>
        <el-switch v-model="formData.enable" active-text="允许配送" inactive-text="禁止配送"></el-switch>
      </el-form-item>
      <el-form-item size="large">
        <el-button type="primary" @click="submitForm('elForm', newCategory)">提 交</el-button>
        <el-button @click="resetForm('elForm')">重 置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import {resetForm, submitForm} from "@/base/ele-base";
import {materialApi as API} from "@/axios/api";
export default {
  components: {},
  name:'NewMaterialCategory',
  props: [],
  data() {
    return {
      formData: {
        createUserId: undefined,
        categoryCode: undefined,
        categoryName: undefined,
        enable: false,
      },
      rules: {
        createUserId: [{
          required: true,
          message: '',
          trigger: 'blur'
        }],
        categoryCode: [{
          required: true,
          message: '请输入物资分类编码',
          trigger: 'blur'
        }],
        category_name: [{
          required: true,
          message: '请输入物资分类名称',
          trigger: 'blur'
        }],
      },
    }
  },
  mounted() {
    this.formData.createUserId = this.$user.id;
  },
  methods: {
    resetForm,submitForm,
    async newCategory(){
      let res = await API.newMaterialCategory(this.formData);
      if (res.status) {
        this.$message({message:"新建物资分类完成", type: 'success'});
      }
    }
  }
}

</script>
<style>
</style>
