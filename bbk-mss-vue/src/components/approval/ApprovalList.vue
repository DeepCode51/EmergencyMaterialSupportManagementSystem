<template>
    <div id="list">
        <h1 class="title">物资申请列表：</h1>
        <el-divider></el-divider>
        <el-card class="filter-bar" shadow="never">
            <el-form :inline="true" :model="filterCondition"  ref="approvalFilter">
                <el-form-item label="申请等级">
                    <el-select v-model="filterCondition.level" placeholder="申请等级">
                        <el-option label="低" value="低"></el-option>
                        <el-option label="中" value="中"></el-option>
                        <el-option label="高" value="高"></el-option>
                        <el-option label="紧急" value="紧急"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="申请状态">
                    <el-select v-model="filterCondition.status" placeholder="申请状态">
                        <el-option label="待审核" value="待审核"></el-option>
                        <el-option label="待配送" value="待配送"></el-option>
                        <el-option label="配送中" value="配送中"></el-option>
                        <el-option label="已完结" value="已完结"></el-option>
                        <el-option label="已拒绝" value="已拒绝"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="info" size="medium" @click="resetForm('approvalFilter', resetFilter)">清 空</el-button>
                    <el-button type="primary" size="medium" @click="submitForm('approvalFilter', loadList)">筛 选</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <el-table :data="list" border style="width: 100%" :highlight-current-row="true" :stripe="true">
            <el-table-column fixed prop="id" label="ID" width="180" align="center"></el-table-column>
            <el-table-column  prop="title" label="申请标题" width="180" align="center"></el-table-column>
            <el-table-column  prop="content" label="详细内容" width="200" align="center"></el-table-column>
            <el-table-column  prop="createTime" label="创建时间" width="150" align="center"></el-table-column>
            <el-table-column  prop="lastUpdateTime" label="最后更新时间" width="150" align="center"></el-table-column>
            <el-table-column  prop="level" label="申请等级" width="100" align="center"></el-table-column>
            <el-table-column prop="status" label="申请状态" width="100" align="center"></el-table-column>
            <el-table-column prop="approvalUserId" label="关联审批人ID" width="150" align="center"></el-table-column>
            <el-table-column prop="eventId" label="关联事件ID" width="150" align="center"></el-table-column>
            <el-table-column prop="createUserId" label="创建人ID" width="150" align="center"></el-table-column>
            <el-table-column fixed="right" label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="text" round size="mini" @click="showMaterial(scope.row.materialApply)">查看物资</el-button>
                    <el-divider direction="vertical"></el-divider>
<!--                    <el-button type="text" round size="mini" @click="showDetail(scope.row.id)">详情</el-button>-->
<!--                    <el-divider direction="vertical"></el-divider>-->
                    <el-popconfirm confirmButtonText='确认' cancelButtonText='取消' icon="el-icon-info" iconColor="red" title="确定删除该申请吗?" @onConfirm="deleteApproval(scope.row.id)">
                        <el-button type="danger" round size="mini" slot="reference" :disabled="isNotIdentity(4)">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination @current-change="loadList" background
                       :current-page.sync="filterCondition.currentPage"
                       :page-size="filterCondition.pageSize"
                       layout="total, prev, pager, next"
                       :total="filterCondition.total">
        </el-pagination>
        <el-drawer title="物资申请列表" :visible.sync="showMaterialApplyList" direction="ltr">
                <el-card id="detail" shadow="never">
                    <div v-for="(i , index) in materialApplyList" :key="index + Date.now()">
                        <el-tag>
                            物资名称：
                            <b>{{i.name}}</b>
                            <el-divider direction="vertical"></el-divider>
                            数量：<b>{{i.num}}</b>
                        </el-tag>
                    </div>
                </el-card>
        </el-drawer>
    </div>
</template>

<script>
    import {resetForm, submitForm} from "@/base/ele-base";
    import { approvalApi } from '@/axios/api'
    import entity from '@/base/entity'
    import {mergeBean, isNotIdentity} from '@/base/utils'
    export default {
        name: "ApprovalList",
        data(){
            return{
                entity,
                showMaterialApplyList: false,
                materialApplyList:[],
                //筛选条件
                list: [],
                filterCondition:{

                }
            }
        },
        methods:{
            mergeBean, resetForm,submitForm,isNotIdentity,
            showDetail(id){
                console.log("显示详情："+id);
            },
            showMaterial(detail){
               this.materialApplyList = [];
               console.log("显示物资详情:"+detail);
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
               this.showMaterialApplyList = true;
            },
            deleteApproval(id){
                console.log("删除审批, ID = " + id);
                approvalApi.deleteApproval(id).then(res=>{
                    this.$message({message:'删除成功', type:'success'});
                });
            },
            loadList(){
                approvalApi.listApproval(this.filterCondition).then(res=>{
                    this.list = res.data.list;
                    this.filterCondition = this.mergeBean(res.data.pagination, this.filterCondition);
                });
            },
            resetFilter(){
                this.filterCondition.status = undefined;
                this.filterCondition.level = undefined;
                this.loadList();
            }
        },
        mounted(){
            console.log("挂载审批列表");
            this.filterCondition = this.mergeBean(this.entity.pagination, this.filterCondition);
            this.loadList();
        }
    }
</script>

<style scoped>
    .title{
        text-align: left;
        padding: 3px 5px;
        border-left: rgba(44, 62, 80, 0.87) 5px solid;
    }
    .el-pagination{
        margin: 10px auto;
    }
    .filter-bar{
        margin: 10px auto;
        text-align: left;
    }
    #detail{
        margin: 20px;
        padding: 0 20px;
    }
    #detail div{
        display: inline-block;
        margin: 10px;
    }
</style>