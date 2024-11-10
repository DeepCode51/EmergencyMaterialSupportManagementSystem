<template>
    <div>
        <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
            <el-form-item label-width="120px" label="创建用户ID" prop="createUserId" disabled="">
                <el-input v-model="formData.createUserId" readonly placeholder="请输入创建用户ID" clearable :style="{width: '100%'}">
                </el-input>
            </el-form-item>
            <el-form-item label-width="120px" label="关联事件ID" prop="eventId">
                <el-select v-model="formData.eventId" placeholder="请选择关联事件" clearable :style="{width: '100%'}">
                    <el-option v-for="(item, index) in eventList" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label-width="120px" label="指定审批人" prop="approvalUserId">
                <el-select v-model="formData.approvalUserId" placeholder="请选择指定审批人" clearable :style="{width: '100%'}">
                    <el-option v-for="(item, index) in userList" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label-width="120px" label="申请标题" prop="title">
                <el-input v-model="formData.title" placeholder="请输入审批标题" clearable :style="{width: '100%'}">
                </el-input>
            </el-form-item>
            <el-form-item label-width="120px" label="申请优先级" prop="level">
                <el-radio-group v-model="formData.level" size="medium">
                    <el-radio-button v-for="(item, index) in levelOptions" :key="index" :label="item.value"
                                     :disabled="item.disabled">{{item.label}}</el-radio-button>
                </el-radio-group>
            </el-form-item>
            <el-form-item label-width="120px" label="申请内容" prop="content">
                <el-input v-model="formData.content" type="textarea" placeholder="请输入申请内容" :maxlength="4096"
                          show-word-limit :autosize="{minRows: 6, maxRows: 11}" :style="{width: '100%'}"></el-input>
            </el-form-item>
            <el-form-item label-width="120px" v-for="(material, index) in materialList" :key="index + Date.now()" :label="'待申请物资'">
                <el-card shadow="hover">
                    <el-select v-model="material.name" placeholder="请选择物资" clearable :style="{width: '100%'}" size="mini">
                        <el-option v-for="(item, index) in allMaterialList" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                    </el-select>
                    <el-input-number v-model="material.num" :min="1" :max="100"  size="mini"></el-input-number>
                    <el-divider direction="vertical"></el-divider>
                    <el-button @click.prevent="removeMaterial(material)" size="mini">移 除</el-button>
                </el-card>
            </el-form-item>
            <el-form-item label="选择申请物资" label-width="120px">
                <el-button @click="addMaterial" type="info" size="mini">新增物资</el-button>
            </el-form-item>

            <el-form-item size="large">
                <el-button type="primary" @click="submitForm('elForm', addApproval)">提 交</el-button>
                <el-button @click="resetForm('elForm')">重 置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script>
    import {resetForm, submitForm} from "@/base/ele-base";
    import {approvalApi, eventApi, userApi, materialApi} from "@/axios/api";
    export default {
        name:'NewApproval',
        components: {},
        props: [],
        data() {
            return {
                formData: {
                    createUserId: undefined,
                    title: undefined,
                    level: '中',
                    content: undefined,
                    approvalUserId: undefined,
                    status: undefined,
                    eventId: undefined,
                    materialApply: ""
                },
                rules: {
                    createUserId: [{
                        required: true,
                        message: '请输入创建用户ID',
                        trigger: 'blur'
                    }],
                    eventId: [{
                        required: true,
                        message: '必须设置关联事件',
                        trigger: 'blur'
                    }],
                    title: [{
                        required: true,
                        message: '请输入审批标题',
                        trigger: 'blur'
                    }],
                    level: [{
                        required: true,
                        message: '申请优先级不能为空',
                        trigger: 'change'
                    }],
                    content: [{
                        required: true,
                        message: '请输入申请内容',
                        trigger: 'blur'
                    }],
                    approvalUserId: [{
                        required: true,
                        message: '请选择指定审批人',
                        trigger: 'change'
                    }],
                },
                levelOptions: [
                    {
                    "label": "低",
                    "value": "低"
                }, {
                    "label": "中",
                    "value": "中"
                }, {
                    "label": "高",
                    "value": "高"
                }, {
                    "label": "紧急",
                    "value": "紧急"
                }],
                approvalUserIdOptions: [
                    {
                    "label": "选项一",
                    "value": 1
                }, {
                    "label": "选项二",
                    "value": 2
                }],
                eventList:[],
                userList:[],
                allMaterialList:[],
                materialList:[]
            }
        },
        methods: {
            submitForm,resetForm,
            removeMaterial(item) {
                let index = this.materialList.indexOf(item)
                if (index !== -1) {
                    this.materialList.splice(index, 1)
                }
            },
            addMaterial() {
                this.materialList.push({
                    name: '',
                    num: 0,
                    flag: Date.now()
                });
            },
            async addApproval(){
                this.formData.status = "待审核";
                this.materialList.forEach(m=>{
                    this.formData.materialApply += m.name + "#" + m.num + "@";
                });
                let res = await approvalApi.newApproval(this.formData);
                if(res.status){
                    this.$message({message:'提交申请成功', type:'success'});
                }
            },
            async loadEvent(){
                let res = await eventApi.listEvent({currentPage:1, pageSize:100});
                if (res.status) {
                    const list = res.data.list;
                    this.$message({message:"应急事件加载完成, 数量："+list.length , type: 'success'});
                    list.forEach((v)=>{
                        this.eventList.push({
                            'label': v.title,
                            'value': v.id
                        });
                    });
                }
            },
            async loadMaterial(){
                let res = await materialApi.listMaterial({currentPage: 1, pageSize: 1000, materialEnable: true});
                if(res.status){
                    const list = res.data.list;
                    this.$message({message:"可调配物资加载完成, 数量："+list.length , type: 'success'});
                    list.forEach((v)=>{
                        this.allMaterialList.push({
                            'label': v.materialName,
                            'value': v.materialName
                        });
                    });
                }
            },
            async loadUser(){
                let res = await userApi.listUser({currentPage: 1, pageSize: 100, identityName:'审批人员'});
                if(res.status){
                    const list = res.data.list;
                    this.$message({message:"应急审批部门人员完成, 数量："+list.length , type: 'success'});
                    list.forEach((v)=>{
                        this.userList.push({
                            'label': v.nickName,
                            'value': v.id
                        });
                    });
                }
            }
        },
        mounted() {
            this.formData.createUserId = this.$user.id;
            this.loadEvent();
            this.loadUser();
            this.loadMaterial();
        }
    }

</script>
<style>
</style>
