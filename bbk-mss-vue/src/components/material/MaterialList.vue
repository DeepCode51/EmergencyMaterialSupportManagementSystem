<template>
    <div id="list">
        <h1 class="title">已经建立物资：</h1>
        <el-divider></el-divider>
        <el-card class="filter-bar" shadow="never">
            <el-form :inline="true" :model="filterCondition"  ref="materialFilter">
                <el-form-item label="物资批次">
                    <el-input v-model="filterCondition.materialBatchNo" placeholder="输入物资批次"></el-input>
                </el-form-item>
                <el-form-item label="物资分类名称">
                    <el-select v-model="filterCondition.materialCategoryCode" placeholder="请选择所属分类" clearable :style="{width: '100%'}">
                        <el-option v-for="(item, index) in materialCategoryCodeOptions" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="物资调配策略">
                    <el-switch v-model="filterCondition.materialEnable" inactive-text="禁止调配" active-text="允许调配"></el-switch>
                </el-form-item>
                <el-form-item>
                    <el-button type="info" size="medium" @click="resetForm('materialFilter', resetFilter)">清 空</el-button>
                    <el-button type="primary" size="medium" @click="submitForm('materialFilter', loadList)">筛 选</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <el-table :data="list" border style="width: 100%" :highlight-current-row="true" :stripe="true">
            <el-table-column fixed prop="id" label="ID" width="180" align="center"></el-table-column>
            <el-table-column  prop="materialBatchNo" label="批次" width="100" align="center"></el-table-column>
            <el-table-column  prop="materialName" label="名称" width="180" align="center"></el-table-column>
            <el-table-column  prop="materialCategoryCode" label="物资分类编码" width="110" align="center"></el-table-column>
            <el-table-column  prop="materialNum" label="数量" width="100" align="center"></el-table-column>
            <el-table-column  prop="materialFrom" label="购进来源" width="100" align="center"></el-table-column>
            <el-table-column  prop="materialContactName" label="购进联系人" width="100" align="center"></el-table-column>
            <el-table-column  prop="materialContactNo" label="购进联系方式" width="180" align="center"></el-table-column>
            <el-table-column  prop="materialContractNo" label="合同编号" width="100" align="center"></el-table-column>
            <el-table-column  prop="enable" label="是否允许调配" width="110" align="center">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.enable" type="success">禁止调配</el-tag>
                    <el-tag v-else type="danger">允许调配</el-tag>
                </template>
            </el-table-column>
            <el-table-column  prop="createTime" label="创建时间" width="150" align="center"></el-table-column>
            <el-table-column  prop="createUserId" label="创建人ID" width="100" align="center"></el-table-column>
            <el-table-column fixed="right" label="操作" align="center" width="200">
                <template slot-scope="scope">
<!--                    <el-button type="primary" round size="mini" @click="showDetail(scope.row.id)">详 情</el-button>-->
<!--                    <el-divider direction="vertical"></el-divider>-->
                    <el-popconfirm confirmButtonText='确认' cancelButtonText='取消' icon="el-icon-info" iconColor="red" title="确定删除物资吗?" @onConfirm="deleteMaterial(scope.row.id)">
                        <el-button type="danger" round size="mini" slot="reference" :disabled="isNotIdentity(2)">删 除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination  @current-change="loadList" background
                       :current-page.sync="filterCondition.currentPage"
                       :page-size="filterCondition.pageSize"
                       layout="total, prev, pager, next"
                       :total="filterCondition.total">
        </el-pagination>
    </div>
</template>

<script>
    import {materialApi as API } from "@/axios/api";
    import {resetForm, submitForm} from "@/base/ele-base";
    import entity from '@/base/entity'
    import {mergeBean, isNotIdentity} from '@/base/utils'
    export default {
        name: "MaterialList",
        data(){
            return{
                entity,
                filterCondition:{

                },
                list:[],
                materialCategoryCodeOptions: [],
            }
        },
        methods:{
            mergeBean,resetForm,submitForm,isNotIdentity,
            resetFilter(){
                this.filterCondition.materialBatchNo = undefined;
                this.filterCondition.materialCategoryCode = undefined;
                this.filterCondition.materialEnable = false;
                this.loadList();
            },
            async loadList(){
                let res = await API.listMaterial(this.filterCondition);
                if(res.status){
                    this.list = res.data.list;
                    this.filterCondition = this.mergeBean(res.data.pagination, this.filterCondition);
                }
            },
            showDetail(id){

            },
            async deleteMaterial(id){
                let res = await API.deleteMaterial(id);
                if(res.status){
                    this.$message({message:'删除成功', type:'success'});
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
            }
        },
        mounted(){
            console.log("挂载应急物资列表");
            this.filterCondition = this.mergeBean(this.entity.pagination, this.filterCondition);
            this.filterCondition.pageSize = 20;
            this.loadList();
            this.loadCategory();
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
        display: block;
        margin: 10px auto;
    }
    .filter-bar{
        margin: 10px auto;
        text-align: left;
    }
</style>