<template>
    <el-card shadow="hover">
        <el-dialog title="选择具体位置" :visible.sync="showLoc" width="70%">
            <BaiDuSelect :keyword="newEvent.address" @cancel="cancel" @map-confirm="confirm"></BaiDuSelect>
        </el-dialog>
        <h1 class="title">新建应急事件：</h1>
        <el-divider></el-divider>
        <el-form  label-width="120px" :model="newEvent" ref="newEventForm" class="form">
            <el-form-item label="事件标题" :rules="{required: true, message:'必须输入标题', trigger: 'blur'}">
                <el-input v-model="newEvent.title" placeholder="请输入事件标题"></el-input>
            </el-form-item>
            <el-form-item label="发生地点" :rules="{required: true, message:'必须输入标题', trigger: 'blur'}">
                <el-input v-model="newEvent.address"></el-input>
            </el-form-item>
            <el-form-item label="位置坐标" :rules="{required:true, message:'必须选择坐标', trigger:'blur'}">
                <el-input v-model="newEvent.loc" readonly style="width: 30%"></el-input>
                <el-divider direction="vertical"></el-divider>
                <el-button @click="cancel" round>清空</el-button>
                <el-divider direction="vertical"></el-divider>
                <el-button @click="showLoc=true" type="primary" round>选择坐标</el-button>
            </el-form-item>
            <el-form-item label="详细说明" :rules="{required: true, message:'必须输入详细说明信息', trigger: 'blur'}">
                <el-input type="textarea" :autosize="{ minRows: 5, maxRows: 12 }" v-model="newEvent.detail"></el-input>
            </el-form-item>
            <el-form-item label="事件影响范围">
                <el-radio-group v-model="newEvent.level">
                    <el-radio-button label="轻微"></el-radio-button>
                    <el-radio-button label="一般"></el-radio-button>
                    <el-radio-button label="严重"></el-radio-button>
                    <el-radio-button label="紧急"></el-radio-button>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="现场图片">
                <el-image style="max-height: 200px; width: 200px; overflow: auto; border-radius: 5px;" v-show="!isNull(newEvent.picture)" :src="newEvent.picture" fir="contain" :preview-src-list="[newEvent.picture]"></el-image>
                <el-button @click="showUpload=true" type="success" round v-show="isNull(newEvent.picture)">上传</el-button>
            </el-form-item>
            <el-form-item class="btn">
                <el-button type="primary" @click="submitForm('newEventForm', requestSubmitEvent)" size="medium">提 交</el-button>
                <el-divider direction="vertical"></el-divider>
                <el-button @click="resetForm('newEventForm')" size="medium">重 置</el-button>
            </el-form-item>
        </el-form>
        <el-dialog title="上传文件" :visible.sync="showUpload" width="30%" >
            <UploadFile @success="getPath"></UploadFile>
        </el-dialog>
    </el-card>
</template>

<script>
    import {resetForm, submitForm} from "@/base/ele-base";
    import BaiDuSelect from "@/components/BaiDuSelect";
    import UploadFile from "@/components/UploadFile";
    import { eventApi } from '@/axios/api'
    import {isNull} from "@/base/utils";
    export default {
        name: "NewEvent",
        components:{
            BaiDuSelect,
            UploadFile
        },
        data(){
            return{
                showLoc: false,
                showUpload: false,
                newEvent: {
                    loc:'',
                    title: '',
                    address: '河南',
                    detail:'',
                    level: '一般',
                    createUserId: '',
                    picture:"",
                }
            }
        },
        mounted(){
           this.newEvent.createUserId = this.$user.id;
        },
        methods:{
            resetForm, submitForm,isNull,
            getPath(path) {
                this.showUpload = false;
                this.newEvent.picture = path;
                console.log("接收到路径：" + path);
            },
            confirm(loc){
                console.log("确定地点："+loc);
                this.newEvent.loc = loc;
                this.showLoc = false;
            },
            cancel(){
               this.$set(this.newEvent, 'loc', "");
               this.showLoc =false;
            },
            async requestSubmitEvent(){
                let res = await eventApi.newEvent(this.newEvent);
                if(res.status){
                    this.$message({message: '建立完成', type: 'success', duration: 4000});
                }
            }
        }
    }
</script>

<style scoped>
    .title{
        text-align: left;
        padding: 3px 5px;
        border-left: rgba(44, 62, 80, 0.87) 5px solid;
    }
    .form{
        text-align: left;
    }
    .btn{
        text-align: left;
    }
</style>