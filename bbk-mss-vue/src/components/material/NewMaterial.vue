<template>
  <div>
    <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
      <el-form-item label-width="150px" label="创建人ID" prop="createUserId">
        <el-input v-model="formData.createUserId" readonly :disabled='true' clearable
          :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="物资批次" prop="materialBatchNo">
        <el-input v-model="formData.materialBatchNo" placeholder="请输入物资批次" :maxlength="50" show-word-limit
          clearable prefix-icon='el-icon-menu' :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="物资名称" prop="materialName">
        <el-input v-model="formData.materialName" placeholder="请输入物资名称" clearable
          prefix-icon='el-icon-takeaway-box' :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="物资数量" prop="materialNum">
        <el-input-number v-model="formData.materialNum" :step='1'></el-input-number>
      </el-form-item>
      <el-form-item label-width="150px" label="所属分类" prop="materialCategoryCode">
        <el-select v-model="formData.materialCategoryCode" placeholder="请选择所属分类" clearable
          :style="{width: '100%'}">
          <el-option v-for="(item, index) in materialCategoryCodeOptions" :key="index" :label="item.label"
            :value="item.value" :disabled="item.disabled"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label-width="150px" label="购进来源" prop="materialFrom">
        <el-input v-model="formData.materialFrom" placeholder="请输入购进来源" clearable :style="{width: '100%'}">
        </el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="购进方联系人" prop="materialContactName">
        <el-input v-model="formData.materialContactName" placeholder="请输入购进方联系人" clearable
          :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="购进方联系方式" prop="materialContactNo">
        <el-input v-model="formData.materialContactNo" placeholder="请输入购进方联系方式" clearable
          :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="合同编号" prop="materialContractNo">
        <el-input v-model="formData.materialContractNo" placeholder="请输入合同编号" clearable
          :style="{width: '100%'}"></el-input>
      </el-form-item>
      <el-form-item label-width="150px" label="配送策略" prop="enable" required>
        <el-switch v-model="formData.enable" active-text="允许配送" inactive-text="禁止配送"></el-switch>
      </el-form-item>
      <el-form-item size="large">
        <el-button type="primary" @click="submitForm('elForm', newMaterial)">提 交</el-button>
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
  name:"NewMaterial",
  props: [],
  data() {
    return {
      formData: {
        createUserId: undefined,
        materialBatchNo: undefined,
        materialName: undefined,
        materialNum: 0,
        materialCategoryCode: undefined,
        materialFrom: undefined,
        materialContactName: undefined,
        materialContactNo: undefined,
        materialContractNo: undefined,
        enable: false,
      },
      rules: {
        createUserId: [{
          required: true,
          message: '',
          trigger: 'blur'
        }],
        materialBatchNo: [{
          required: true,
          message: '请输入物资批次',
          trigger: 'blur'
        }],
        materialName: [{
          required: true,
          message: '请输入物资名称',
          trigger: 'blur'
        }],
        materialNum: [{
          required: true,
          message: '',
          trigger: 'blur'
        }],
        materialCategoryCode: [{
          required: true,
          message: '请选择所属分类',
          trigger: 'change'
        }],
        materialFrom: [{
          required: true,
          message: '请输入购进来源',
          trigger: 'blur'
        }],
        materialContactName: [{
          required: true,
          message: '请输入购进方联系人',
          trigger: 'blur'
        }],
        materialContactNo: [{
          required: true,
          message: '请输入购进方联系方式',
          trigger: 'blur'
        }],
        materialContractNo: [{
          required: true,
          message: '请输入合同编号',
          trigger: 'blur'
        }],
      },
      materialCategoryCodeOptions: [],
    }
  },
  mounted() {
    this.formData.createUserId = this.$user.id;
    this.loadCategory();
  },
  methods: {
    resetForm,submitForm,
    async newMaterial(){
      let res = await API.newMaterial(this.formData);
      if (res.status) {
        this.$message({message:"新建物资完成", type: 'success'});
      }
    },
    async loadCategory(){
      let res = await API.listMaterialCategory({currentPage:1, pageSize:100});
      if(res.status){
        const list = res.data.list;
        this.$message({message:"物资分类加载完成, 数量："+list.length , type: 'success'});
        list.forEach((v)=>{
          this.materialCategoryCodeOptions.push({
            'label': v.categoryName,
            'value': v.categoryCode
          });
        });
      }
    },
  }
}

</script>
<style>
</style>
