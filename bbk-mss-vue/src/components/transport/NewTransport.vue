<template>
    <div>
        <el-row :gutter="11">
            <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
                <el-col :span="24">
                    <el-form-item label-width="150px" label="创建人ID" prop="createUserId">
                        <el-input v-model="formData.createUserId" readonly placeholder="请输入单行文本创建人ID" clearable
                                  :style="{width: '100%'}"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label-width="150px" label="关联审批标题" prop="approvalId">
                        <el-select v-model="formData.approvalId" placeholder="审批关联关联审批标题" clearable @change="loadDataFromApproval"
                                   :style="{width: '100%'}">
                            <el-option v-for="(item, index) in approvalList" :key="index" :label="item.title"
                                       :value="item.id" :disabled="item.disabled"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label-width="150px" label="接收人ID" prop="receiverUserId">
                        <el-input v-model="formData.receiverUserId" placeholder="ID接收人接收人ID" readonly clearable
                                  :style="{width: '100%'}"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="11">
                    <el-form-item label-width="150px" label="物资出库日期" prop="createTime">
                        <el-date-picker v-model="formData.createTime" format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss"
                                        :style="{width: '100%'}" placeholder="物资出库日期物资出库日期" clearable></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="13">
                    <el-form-item label-width="150px" label="预期到达时间" prop="expectTime">
                        <el-date-picker v-model="formData.expectTime" format="yyyy-MM-dd" value-format="yyyy-MM-dd HH:mm:ss"
                                        :style="{width: '100%'}" placeholder="预期到达时间预期到达时间" clearable></el-date-picker>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label-width="150px" label="运输方式" prop="transportType">
                        <el-radio-group v-model="formData.transportType" size="medium">
                            <el-radio-button v-for="(item, index) in transportTypeOptions" :key="index" :label="item.value"
                                             :disabled="item.disabled">{{item.label}}</el-radio-button>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item label-width="150px" label="物资列表">
                        <div v-for="(i , index) in materialApplyList" :key="index + Date.now()">
                            <el-tag style="display: inline-block; margin: 5px;">
                                物资名称：
                                <b>{{i.name}}</b>
                                <el-divider direction="vertical"></el-divider>
                                数量：<b>{{i.num}}</b>
                            </el-tag>
                        </div>
                    </el-form-item>
                </el-col>
                <el-col :span="24">
                    <el-form-item size="large">
                        <el-button type="primary" @click="submitForm('elForm', newTransport)">提 交</el-button>
                        <el-button @click="resetForm('elForm')">重 置</el-button>
                    </el-form-item>
                </el-col>
            </el-form>
        </el-row>
    </div>
</template>
<script>
    import {resetForm, submitForm} from "@/base/ele-base";
    import { transportApi , approvalApi} from '@/axios/api'
    import entity from '@/base/entity'
    import {mergeBean} from '@/base/utils'
    export default {
        components: {},
        props: [],
        data() {
            return {
                entity,
                approvalList:[],
                materialApplyList:[],
                formData: {
                    createUserId: undefined,
                    approvalId: undefined,
                    receiverUserId: undefined,
                    createTime: null,
                    expectTime: null,
                    transportType: "飞机",
                },
                rules: {
                    createUserId: [{
                        required: true,
                        message: '请输入单行文本创建人ID',
                        trigger: 'blur'
                    }],
                    approvalId: [{
                        required: true,
                        message: '审批关联关联审批ID',
                        trigger: 'change'
                    }],
                    receiverUserId: [{
                        required: true,
                        message: 'ID接收人接收人ID',
                        trigger: 'blur'
                    }],
                    createTime: [{
                        required: true,
                        message: '物资出库日期物资出库日期',
                        trigger: 'change'
                    }],
                    expectTime: [{
                        required: true,
                        message: '预期到达时间预期到达时间',
                        trigger: 'change'
                    }],
                    transportType: [{
                        required: true,
                        message: '运输方式不能为空',
                        trigger: 'change'
                    }],
                },
                transportTypeOptions: [{
                    "label": "汽车",
                    "value": "汽车"
                }, {
                    "label": "火车",
                    "value": "火车"
                }, {
                    "label": "轮船",
                    "value": "轮船"
                }, {
                    "label": "飞机",
                    "value": "飞机"
                }],
            }
        },
        methods: {
            resetForm,submitForm,mergeBean,
            async newTransport(){
                this.formData.status = "配送中";
                let res = await transportApi.newTransport(this.formData);
                if(res.status){
                    this.$message({message:'新建物资运输单完成', type:'success'});
                    this.updateApproval(this.formData.approvalId);
                }
            },
            updateApproval(id){
                console.log("流转状态, ID = " + id);
                approvalApi.updateApproval({id: id, status:'配送中'}).then(res=>{
                    this.$message({message: '审批' + id + '已经流转至配送中状态!', type:'success'});
                });
            },
            loadList(){
                approvalApi.listApproval({pageSize:1000,currentPage:1, status:"待配送"}).then(res=>{
                    this.approvalList = res.data.list;
                });
            },
            loadDataFromApproval(nV){
                for (const index in this.approvalList) {
                    if(this.approvalList[index].id===nV){
                        this.formData.receiverUserId = this.approvalList[index].createUserId;
                        this.formData.materialRecord = this.approvalList[index].materialApply;
                        const detail = this.formData.materialRecord;
                        this.materialApplyList = [];
                        const arr = detail.split("@");
                        for(const a in arr){
                            if (arr[a] !== undefined && arr[a] !== "") {
                                const aa = arr[a].split("#");
                                this.materialApplyList.push({
                                    name: aa[0],
                                    num: aa[1]
                                });
                            }
                        }
                    }
                }
            }
        },
        mounted(){
            this.formData.createUserId = this.$user.id;
            this.loadList();
            console.log("挂载新增物资运输单...")
        }
    }

</script>
<style>
</style>
