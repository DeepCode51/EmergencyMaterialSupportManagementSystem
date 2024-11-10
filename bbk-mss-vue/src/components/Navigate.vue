<template>
    <div class="navigate">
        <el-image id="logo" :src=pic fit="cover"></el-image>
        <el-menu default-active="100-100" :unique-opened=true>
            <el-submenu index="1">
                <template slot="title">
                    <span class="title">应急事件管理</span>
                </template>
                <el-menu-item-group>
                    <el-menu-item index="1-1">
                        <router-link :to="{name: 'event-list'}" tag="li" >已建立列表</router-link>
                    </el-menu-item>
                    <el-menu-item index="1-2" v-if="isIdentity(1)">
                        <router-link :to="{name: 'event-new'}" tag="li" >新建应急事件</router-link>
                    </el-menu-item>
                    <el-menu-item index="1-3" v-if="isIdentity(1)">
                        <router-link :to="{name: 'approval-new'}" tag="li" >发起物资申请</router-link>
                    </el-menu-item>
                </el-menu-item-group>
            </el-submenu>
            <el-submenu index="2">
                <template slot="title">
                    <span class="title">应急物资管理</span>
                </template>
                <el-menu-item-group>
                    <el-menu-item index="2-1" v-if="isIdentity(2)">
                        <router-link :to="{name:'material-category-new'}" tag="li">新建应急物资分类</router-link>
                    </el-menu-item>
                    <el-menu-item index="2-2" v-if="isIdentity(2)">
                        <router-link :to="{name: 'material-new'}" tag="li">新增物资</router-link>
                    </el-menu-item>
                    <el-menu-item index="2-3">
                        <router-link :to="{name: 'material-category-list'}" tag="li">应急物资分类列表</router-link>
                    </el-menu-item>
                    <el-menu-item index="2-4">
                        <router-link :to="{name: 'material-list'}" tag="li">物资列表</router-link>
                    </el-menu-item>
                </el-menu-item-group>
            </el-submenu>
            <el-submenu index="3">
                <template slot="title">
                    <span class="title">审批管理</span>
                </template>
                <el-menu-item-group>
                    <el-menu-item index="3-1" v-if="isIdentity(4)">
                        <router-link :to="{name:'wait-approval'}" tag="li">待审批列表</router-link>
                    </el-menu-item>
                    <el-menu-item index="3-2">
                        <router-link :to="{name: 'approval-list'}" tag="li" >已建立申请</router-link>
                    </el-menu-item>
                </el-menu-item-group>
            </el-submenu>
            <el-submenu index="4">
                <template slot="title">
                    <span class="title">运输状态管理</span>
                </template>
                <el-menu-item-group>
                    <el-menu-item index="4-1" v-if="isIdentity(3)">
                        <router-link :to="{name:'wait-transport'}" tag="li">待配送</router-link>
                    </el-menu-item>
                    <el-menu-item index="4-2" v-if="isIdentity(3)">
                        <router-link :to="{name:'new-transport'}" tag="li">新建配送单</router-link>
                    </el-menu-item>
                    <el-menu-item index="4-3">
                        <router-link :to="{name:'transport-list'}" tag="li">已建立配送单</router-link>
                    </el-menu-item>
                </el-menu-item-group>
            </el-submenu>
<!--            <el-submenu index="5">-->
<!--                <template slot="title">-->
<!--                    <span class="title">个人中心</span>-->
<!--                </template>-->
<!--                <el-menu-item-group>-->
<!--                    <el-menu-item index="5-1">选项1</el-menu-item>-->
<!--                    <el-menu-item index="5-2">选项2</el-menu-item>-->
<!--                </el-menu-item-group>-->
<!--            </el-submenu>-->
        </el-menu>
    </div>
</template>

<script>
    import {isIdentity} from "@/base/utils";
    export default {
        name: "Navigate",
        data(){
            return{
                pic: "http://oss.bestbigkk.com/halo/bestbigkk_1579275103355.svg",
                identityName:"",
                identityCode:-1,
            }
        },
        methods:{
            isIdentity
        },
        mounted() {
            this.pic = this.$user.picture;
            this.identityName = this.$user.identityName;
            this.identityCode = this.$user.identityCode;
        }
    }
</script>

<style scoped>
    .navigate{
        height: 100%;
        border-right: rgba(116, 116, 116, 0.57) 1px solid;
    }
    .el-image{
        border-radius: 50%;
        border: deepskyblue 5px double;
        overflow: hidden;
        display: block;
        margin: 20px;
    }
    #logo{
        width: 100%;
        height: 100%;
    }
    .title{
        font-weight: bold;
    }
</style>