<template>
    <div id="loginPage">
        <el-card shadow="always">
            <h1 class="slogan bc">应急物资保障系统</h1>
            <el-form :model="login" status-icon ref="loginForm" label-width="50px">
                <el-form-item label="账户" prop="account"  :rules="{ required: true, message: '请输入账户', trigger: 'blur' }">
                    <el-input placeholder="请输入账户" v-model="login.account"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password" :rules="{ required: true, message: '请输入密码', trigger: 'blur' }">
                    <el-input type="password" v-model="login.password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('loginForm', requestLogin)" size="medium">提交</el-button>
                    <el-button @click="resetForm('loginForm')" size="medium">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script>
    import {resetForm, submitForm} from "@/base/ele-base";
    import {toLogin} from '@/axios/api.js'

    export default {
        name: "Login",
        data(){
            return{
               login:{
                   account: "",
                   password:""
               }
            }
        },
        methods: {
            resetForm, submitForm,
            async requestLogin(){
                let res = await toLogin(this.login);
                if(res.status){
                    console.log("登录反馈..");
                    window.localStorage.setItem("token", res.data.token);
                    window.localStorage.setItem("user_json", res.data.userJson);
                    await this.$router.push("/dashboard");
                }
            }
        }
    }
</script>

<style scoped>
    .slogan{
        margin-bottom: 20px;
        text-align: center;
    }
    .el-card {
        width: 350px;
        height: auto;
        position: absolute;
        transform: translate(-50%, -50%);
        left: 50%;
        top: 30%;
    }
</style>