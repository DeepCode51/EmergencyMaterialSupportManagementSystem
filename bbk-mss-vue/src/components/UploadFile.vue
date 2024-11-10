<template>
    <div style="text-align: center">
        <el-upload drag action="#" :http-request="requestUpload" ref="uploader">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
    </div>
</template>

<script>
    import {ossApi} from "@/axios/api";
    export default {
        name: "UploadFile",
        data(){
            return{

            }
        },
        methods:{
            async requestUpload(file){
                console.log("请求上传");
                console.log(file);
                let res = await ossApi.upload({file: file.file});
                console.log(res);
                if (res.status) {
                    this.$message({message:"上传完成", type:"success"})
                    this.clear();
                    this.$emit('success', res.data);
                }
            },
            clear(){
                this.$refs.uploader.clearFiles();
            }
        }
    }
</script>

<style scoped>

</style>