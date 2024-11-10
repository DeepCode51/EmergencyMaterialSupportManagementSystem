import Vue from 'vue';
import VueRouter from 'vue-router'
Vue.use(VueRouter)

// 1. 定义 (路由) 组件。
const Foo = { template: '<div>foo</div>' }

// 2. 不定义，引入
import _50X from '@/page/base/50X'
import LoginPage from '@/page/Login'
import Main from "@/page/Main";

import NewEvent from "@/components/event/NewEvent";
import ListEvent from "@/components/event/ListEvent";

import NewMaterialCategory from '@/components/material/catyegory/NewMaterialCatrgory'
import MaterialCategoryList from '@/components/material/catyegory/MaterialCategoryList'

import NewMaterial from "@/components/material/NewMaterial";
import MaterialList from "@/components/material/MaterialList";

import NewApproval from "@/components/approval/NewApproval";
import ApprovalList from "@/components/approval/ApprovalList"
import WaitApproval from "@/components/approval/WaitApproval";

import WaitTransport from "@/components/transport/WaitTransport";
import NewTransport from "@/components/transport/NewTransport";
import TransportList from "@/components/transport/TransportList";

// 3.设置具体的路由参数
const routes = [
    { path:'/', name:'LoginPage', component: LoginPage},
    { path: '/foo', name:'foo', component: Foo },
    { path: '/500', name:'_500', component: _50X},
    {
        path:'/dashboard',
        name:'Main',
        component: Main,
        meta: { // 在路由配置中加入meta:{requireAuth: true}
            requireAuth: true
        },
        children: [
            {
                path: 'event/new',
                name: 'event-new',
                component: NewEvent,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'event/list',
                name: 'event-list',
                component: ListEvent,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'material/category/new',
                name: 'material-category-new',
                component: NewMaterialCategory,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'material/category/list',
                name: 'material-category-list',
                component: MaterialCategoryList,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'material/new',
                name: 'material-new',
                component: NewMaterial,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'material/list',
                name: 'material-list',
                component: MaterialList,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'approval/new',
                name: 'approval-new',
                component: NewApproval,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'approval/list',
                name: 'approval-list',
                component: ApprovalList,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'approval',
                name: 'wait-approval',
                component:WaitApproval,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'transport',
                name: 'wait-transport',
                component: WaitTransport,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path: 'transport/new',
                name: 'new-transport',
                component: NewTransport,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            },
            {
                path:'transport/list',
                name: 'transport-list',
                component: TransportList,
                meta: { // 在路由配置中加入meta:{requireAuth: true}
                    requireAuth: true
                }
            }
        ]
    }
];

// 创建 router 实例，然后传 `routes` 配置
const router = new VueRouter({
    routes // (缩写) 相当于 routes: routes
});

router.beforeEach((to, from, next) => {
    let userJson = window.localStorage.getItem("user_json");
    const user = eval(eval('(' + userJson + ')') || {});
    Vue.prototype.$user = user;
    const isLogin = user.account !== undefined;
    console.log("---------------------------");
    console.log("From Router："+from);
    console.log("To Router："+to);
    console.log("Current User："+userJson);
    console.log("验证结果："+isLogin);
    console.log("---------------------------");

    if(to.path === "/"){
        if(isLogin){
            next("/dashboard");
        }else{
            next();
        }
    }else{
        if(to.meta.requireAuth && isLogin) {
            next();
        }else{
            Vue.prototype.$message({
                showClose: true,
                message: '检测到你还未登录，即将跳转至登录界面.',
                type: 'error',
                duration:4000
            });
            next("/");
        }
    }
})

export default router