<template>
    <div id="list">
        <h1 class="title">已经建立物资分类：</h1>
        <el-divider></el-divider>
        <el-card class="filter-bar" shadow="never">
            <el-form :inline="true" :model="filterCondition"  ref="materialCategoryFilter">
                <el-form-item label="物资分类编码">
                    <el-input v-model="filterCondition.categoryCode" placeholder="输入物资分类编码"></el-input>
                </el-form-item>
                <el-form-item label="物资分类名称">
                    <el-input v-model="filterCondition.categoryName" placeholder="输入物资分类名称"></el-input>
                </el-form-item>
                <el-form-item label="物资调配策略">
                    <el-switch v-model="filterCondition.enable" inactive-text="禁止调配" active-text="允许调配"></el-switch>
                </el-form-item>
                <el-form-item>
                    <el-button type="info" size="medium" @click="resetForm('materialCategoryFilter', resetFilter)">清 空</el-button>
                    <el-button type="primary" size="medium" @click="submitForm('materialCategoryFilter', loadList)">筛 选</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <el-table :data="list" border style="width: 100%" :highlight-current-row="true" :stripe="true">
            <el-table-column fixed prop="id" label="ID" width="280" align="center"></el-table-column>
            <el-table-column fixed prop="categoryName" label="物资分类名称" align="center"></el-table-column>
            <el-table-column fixed prop="categoryCode" label="物资分类编码" width="250" align="center"></el-table-column>
            <el-table-column fixed prop="createTime" label="创建时间" width="180" align="center"></el-table-column>
            <el-table-column fixed prop="enable" label="是否允许调配" width="180" align="center">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.enable" type="success">允许调配</el-tag>
                    <el-tag v-else type="danger">禁止调配</el-tag>
                </template>
            </el-table-column>
            <el-table-column fixed prop="createUserId" label="创建人ID" width="180" align="center"></el-table-column>
            <el-table-column fixed="right" label="操作" align="center" width="120">
                <template slot-scope="scope">
<!--                    <el-button type="primary" round size="mini" @click="showDetail(scope.row.id)">详 情</el-button>-->
<!--                    <el-divider direction="vertical"></el-divider>-->
                    <el-popconfirm confirmButtonText='确认' cancelButtonText='取消' icon="el-icon-info" iconColor="red" title="确定删除物资分类吗?" @onConfirm="deleteMaterialCategory(scope.row.id)">
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
        name: "MaterialCategoryList",
        data(){
            return{
                entity,
                filterCondition:{},
                list:[]
            }
        },
        methods:{
            mergeBean,resetForm,submitForm,isNotIdentity,
            resetFilter(){
                this.filterCondition.categotyName = undefined;
                this.filterCondition.categoryCode = undefined;
                this.filterCondition.enable = false;
            },
            async loadList(){
                let res = await API.listMaterialCategory(this.filterCondition);
                if(res.status){
                    this.list = res.data.list;
                    this.filterCondition = this.mergeBean(res.data.pagination, this.filterCondition);
                }
            },
            showDetail(id){

            },
            async deleteMaterialCategory(id){
                let res = await API.deleteMaterialCategory(id);
                if(res.status){
                    this.$message({message:'删除成功', type:'success'});
                }
            }
        },
        mounted(){
            console.log("挂载应急物资分类列表");
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
        display: block;
        margin: 10px auto;
    }
    .filter-bar{
        margin: 10px auto;
        text-align: left;
    }
</style>