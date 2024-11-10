<template>
    <el-card shadow="always">
        <h1>5 0 0</h1>
        <div class="content" v-html="errorMsg"></div>
    </el-card>
</template>

<script>
    export default {
        name: "_50X",
        props:{
            errorMsg: {
                type: String,
                require:false,
                default: "服务器发生了错误..."
            }
        }
    }
</script>

<style scoped>
    h1 {
        color: darkred;
        font-size: 25px;
    }
</style>