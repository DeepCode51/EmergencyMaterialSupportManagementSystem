<template>
    <el-container id="main">
        <el-container>
            <el-aside width="200px">
                <Navigate></Navigate>
            </el-aside>
            <el-main>
                <el-header>
                    <el-tag>
                        {{name}}
                    </el-tag>
                    <el-divider direction="vertical"></el-divider>
                    <el-tag type="info">
                        {{identityName}}
                    </el-tag>
                    <el-divider direction="vertical"></el-divider>
                    <el-button type="danger" size="small" round @click="logout">注 销</el-button>
                </el-header>
                <router-view></router-view>
            </el-main>
        </el-container>
        <el-dialog title="Hello :)" :visible.sync="dialogVisible" width="30%">
            你好：<el-tag type="info" size="small">{{name}}</el-tag>，欢迎使用应急物资保障系统。
            你的身份为：<el-tag type="danger" size="small">{{identityName}}</el-tag>，部分菜单或操作将根据你的身份予以禁用限制，针对此类情况。您将只能进行有限的操作。请知悉！
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="dialogVisible = false" size="mini" round>我知道了</el-button>
            </span>
        </el-dialog>
    </el-container>
</template>

<script>
    import Navigate  from "@/components/Navigate";
    export default {
        name: "Main",
        components:{
            Navigate
        },
        data(){
            return{
                dialogVisible:false,
                name:"",
                identityName:""
            }
        },
        methods:{
           logout(){
               const _this = this;
               this.$confirm('要退出登录, 是否继续?', '提示', {
                   confirmButtonText: '确定',
                   cancelButtonText: '取消',
                   type: 'warning'
               }).then(() => {
                   window.localStorage.removeItem("user_json");
                   window.localStorage.removeItem("token");
                   _this.$user = {};
                   _this.$router.push("/");
               }).catch(() => {});
           }
        },
        mounted() {
            this.name = this.$user.nickName;
            this.identityName = this.$user.identityName;
            this.dialogVisible = true;
        }
    }
</script>

<style scoped>
    #main{
        height: 100%;
    }
    .el-header{
        margin: 0;
        padding: 0;
        text-align: right;
    }
</style>