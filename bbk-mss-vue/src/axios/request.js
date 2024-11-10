import axios from "axios";
import Vue from 'vue';
import qs from "qs";
import { Loading } from 'element-ui';

const apiBaseURL = "http://localhost:21584/dev";
const apiTimeOut = 5000;

const service = axios.create({
    baseURL: apiBaseURL,
    timeout: apiTimeOut
});

const rawService = axios.create({
    baseURL: apiBaseURL,
    timeout: apiTimeOut,
    headers: {'Content-Type': 'multipart/form-data'}
});


let loadingIns = undefined;
const loading = {
    lock: true,
    text: '请求中...',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
};

//请求拦截器
service.interceptors.request.use(config => {
    loadingIns = Loading.service(loading);
    axios.defaults.headers['Content-Type'] = 'application/x-www-form-urlencoded';
    config.method === 'post' ? config.data = qs.stringify({...config.data}) : config.params = {...config.params};
    return config;
    }, errorHandler
);

//响应拦截器
rawService.interceptors.response.use(
    response => {
        if (loadingIns !== undefined) {
            loadingIns.close();
        }
        const res = response.data;
        if (!res.status) {
            Vue.prototype.$alert(res.data, "哇哦", {type:'info', center:true});
        }
        console.log("响应：");
        console.log(res);
        return res;
    },errorHandler
);

//响应拦截器
service.interceptors.response.use(
    response => {
        loadingIns.close();
        const res = response.data;
        if (!res.status) {
            Vue.prototype.$alert(res.data, "哇哦", {type:'info', center:true});
        }
        console.log("响应：");
        console.log(res);
        return res;
    },errorHandler
);

function errorHandler(error) {
    loadingIns.close();
    const res = error.response.data;
    Vue.prototype.$message({
        showClose: true,
        message: '错误, 请求未正常完成：'+res.msg+'，可打开控制台查看详情（F12） ',
        type: 'error',
        duration:4000
    });
    console.log(JSON.stringify(res.data));
    return Promise.reject(error)
}

export default {rawService, service};