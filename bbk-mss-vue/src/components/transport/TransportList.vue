<template>
    <div id="list">
        <el-dialog title="选择具体位置" :visible.sync="showLoc" width="70%">
            <BaiDuSelect :keyword="newTransport.target" @cancel="cancel" @map-confirm="confirm"></BaiDuSelect>
        </el-dialog>
        <el-dialog title="流转坐标位置" :visible.sync="viewLocVisible" width="70%">
            <BaiDuMapTrace  :markers="beView"></BaiDuMapTrace>
        </el-dialog>
        <h1 class="title">正在配送的物资：</h1>
        <el-divider></el-divider>
        <el-card class="filter-bar" shadow="never">
            <el-form :inline="true" :model="filterCondition"  ref="transportFilter">
                <el-form-item label="接收人">
                    <el-select v-model="filterCondition.receiverUserId" placeholder="请选择指定审批人" clearable :style="{width: '100%'}">
                        <el-option v-for="(item, index) in userList" :key="index" :label="item.label" :value="item.value" :disabled="item.disabled"></el-option>
                    </el-select>
                </el-form-item>
<!--                <el-form-item label="关联事件">-->
<!--                    <el-select v-model="filterCondition.status" placeholder="关联事件">-->
<!--                        <el-option label="已拒绝" value="已拒绝"></el-option>-->
<!--                    </el-select>-->
<!--                </el-form-item>-->
                <el-form-item>
                    <el-button type="info" size="medium" @click="resetForm('transportFilter', resetFilter)">清 空</el-button>
                    <el-button type="primary" size="medium" @click="submitForm('transportFilter', loadList)">筛 选</el-button>
                </el-form-item>
            </el-form>
        </el-card>
        <el-table :data="list" border style="width: 100%" :highlight-current-row="true" :stripe="true">
            <el-table-column fixed prop="id" label="ID" width="180" align="center"></el-table-column>
            <el-table-column  prop="approvalId" label="关联事件ID" width="180" align="center"></el-table-column>
            <el-table-column  prop="createTime" label="创建时间" width="150" align="center"></el-table-column>
            <el-table-column  prop="expectTime" label="预期到达时间" width="150" align="center"></el-table-column>
            <el-table-column prop="createUserId" label="创建人ID" width="150" align="center"></el-table-column>
            <el-table-column prop="status" label="申请状态" width="100" align="center"></el-table-column>
            <el-table-column prop="receiverUserId" label="接收人ID" width="150" align="center"></el-table-column>
            <el-table-column prop="transportType" label="运输方式" width="150" align="center"></el-table-column>
            <el-table-column fixed="right" label="操作" align="center">
                <template slot-scope="scope">
                    <el-button type="text" round size="mini" @click="showMaterial(scope.row.materialRecord)">查看物资</el-button>
                    <el-divider direction="vertical"></el-divider>
                    <el-button type="text" round size="mini" @click="showTransport(scope.row)">流转记录</el-button>
                    <el-divider direction="vertical"></el-divider>
                    <el-popconfirm confirmButtonText='确认' cancelButtonText='取消' icon="el-icon-info" iconColor="red" title="确定结束流转吗?" @onConfirm="updateStatus(scope.row)">
                        <el-button type="danger" round size="mini" slot="reference" :disabled="isNotIdentity(1)">结束流转</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination @current-change="loadList" background
                       :current-page.sync="filterCondition.currentPage"
                       :page-size="filterCondition.pageSize"
                       layout="total, prev, pager, next"
                       :total="filterCondition.total">
        </el-pagination>
        <el-drawer title="运送列表：" :visible.sync="showMaterialApplyList" direction="ltr">
            <el-card id="detail" shadow="never">
                <div v-for="(i , index) in materialApplyList" :key="index + Date.now()">
                    <el-tag>
                        物资名称：
                        <b>{{i.name}}</b>
                        <el-divider direction="vertical"></el-divider>
                        数量：<b>{{i.num}}</b>
                    </el-tag>
                </div>
            </el-card>
        </el-drawer>
        <el-drawer title="流转状态" :visible.sync="showTransportRecord" direction="rtl">
            <div style="margin: 10px 20px;">
                <div v-if="transportRecord.length>0">
                    <div  v-for="(r, index) in transportRecord" :key=Date.now()+index style="margin: 10px auto">
                        <h4 style="color: #009fcd">
                            <i class="el-icon-s-flag"></i>
                            {{index+1}}.流转时间：<el-tag type="primary" size="small">{{r.time}}</el-tag>
                            <el-divider direction="vertical"></el-divider>
                            <el-button type="text" size="mini" @click="viewLoc(r)">查看坐标</el-button>
                        </h4>
                        <p style="padding: 0 8px; font-size: 13px; border-left: lightblue 2px solid; line-height: 20px"><b>流转详情：</b>{{r.address}}</p>
                    </div>
                </div>
                <p v-else  style="text-align: center; margin: 100px 0;"><b>暂无流转记录</b></p>
            </div>
            <div style="text-align: center">
                <el-button type="primary" @click="newTransportVisible=true" :disabled="isNotIdentity(3)">+ 新增流转状态</el-button>
            </div>
        </el-drawer>
        <el-dialog title="输入新的流转地址及坐标：" :visible.sync="newTransportVisible" width="40%">
            <el-card style="margin: 0 0 10px" shadow="never">
                <el-input v-model="newTransport.loc" readonly style="width: 30%"></el-input>
                <el-divider direction="vertical"></el-divider>
                <el-button @click="cancel" round>清空</el-button>
                <el-divider direction="vertical"></el-divider>
                <el-button @click="showLoc=true" type="primary" round>选择坐标</el-button>
            </el-card>
            <el-input type="textarea" :autosize="{minRows: 5, maxRows:10}" count v-model="newTransport.address" clearable size="large"></el-input>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="updateTransport">确 定</el-button>
              </span>
        </el-dialog>
    </div>
</template>

<script>
    import {resetForm, submitForm} from "@/base/ele-base";
    import BaiDuSelect from "@/components/BaiDuSelect";
    import BaiDuMapTrace from "@/components/BaiDuMapTrace";
    import {approvalApi, transportApi, userApi} from '@/axios/api'
    import entity from '@/base/entity'
    import {mergeBean, isNull, dateFormat,isNotIdentity} from '@/base/utils'
    export default {
        name: "TransportList",
        components:{
           BaiDuSelect,
            BaiDuMapTrace
        },
        data(){
            return{
                entity,
                showMaterialApplyList: false,
                materialApplyList:[],
                showTransportRecord: false,
                transportRecord:[],
                activeTransport: 0,
                newTransportVisible: false,
                viewLocVisible:false,
                beView:[],
                newTransport:{
                    target:"",
                    address:"",
                    old:"",
                    loc:"",
                    id:undefined
                },
                showLoc:false,
                //筛选条件
                list: [],
                userList:[],
                filterCondition:{

                }
            }
        },
        methods:{
            mergeBean, isNull, dateFormat, resetForm,submitForm,isNotIdentity,
            confirm(loc){
                console.log("确定地点："+loc);
                this.newTransport.loc = loc;
                this.showLoc = false;
            },
            cancel(){
                this.$set(this.newTransport, 'loc', "");
                this.showLoc =false;
            },
            viewLoc(rec){
                console.log("查看坐标：");
                console.log(rec);
                if(isNull(rec.loc)){
                    this.$alert("该流转状态不包含详细坐标信息", '提示：',{type: 'info'});
                    return;
                }
                this.viewLocVisible = true;
                const arr = rec.loc.split("&");
                this.beView.push({
                    lng: arr[0],
                    lat: arr[1]
                })
            },
            showMaterial(detail){
                this.materialApplyList = [];
                console.log("显示物资详情:"+detail);
                if (isNull(detail)) {
                    this.$alert("该运输单不包含物资列表", '提示：',{type: 'info'});
                    return;
                }
                const arr = detail.split("@");
                for(const a in arr){
                    if (arr[a] !== undefined && arr[a] !== "") {
                        const aa = arr[a].split("#");
                        this.materialApplyList.push({
                            name: aa[0],
                            num: aa[1]
                        });
                    }
                }
                this.showMaterialApplyList = true;
            },
            showTransport(record){
                console.log(record);
                const r = record.transportRecord;
                this.transportRecord = [];
                this.showTransportRecord = true;
                this.newTransport.old = r;
                this.newTransport.target = isNull(record.address) ? "河南" : record.address;
                this.newTransport.id = record.id;
                if (isNull(r)) {
                    return;
                }
                const arr = r.split("@");
                for(const a in arr){
                    if (arr[a] !== undefined && arr[a] !== "") {
                        const aa = arr[a].split("#");
                        this.transportRecord.push({
                            time: aa[0],
                            address: aa[1],
                            loc: aa[2]
                        });
                    }
                }
                this.activeTransport = this.transportRecord.length;
                console.log(this.transportRecord);
            },
            updateStatus(record){
                console.log("结束流转状态："+record);
                transportApi.updateTransport({id:record.id, status:'已接收'}).then(res=>{
                    this.$message({message:'更新状态成功', type:'success'});
                    approvalApi.updateApproval({id:record.approvalId, status:'已完结'}).then(res=>{
                        this.$message({message:'审批'+record.approvalId+'已经流转到已完结状态', type:'success'});
                    });
                });
            },
            async updateTransport(){
                console.log("新增流转状态");
                let t = dateFormat(Date.now(), "yyyy-MM-dd hh:mm") + "#" + this.newTransport.address + "#" + this.newTransport.loc + "@";
                if(!isNull(this.newTransport.old)){
                    t = (this.newTransport.old + "@") + t;
                }
                transportApi.updateTransport({id:this.newTransport.id, transportRecord: t}).then(res=>{
                    this.$message({message:'新增流转状态状态完成', type:'success'});
                    this.newTransport = {};
                    this.newTransportVisible =false;
                    this.loadList();
                });
            },
            loadList(){
                transportApi.listTransport(this.filterCondition).then(res=>{
                    this.list = res.data.list;
                    this.filterCondition = this.mergeBean(res.data.pagination, this.filterCondition);
                });
            },
            resetFilter(){
                this.filterCondition.receiverUserId = undefined;
                this.filterCondition.approvalId = undefined;
                this.loadList();
            },
            async loadUser(){
                let res = await userApi.listUser({currentPage: 1, pageSize: 100, identityName:'应急事件响应人员'});
                if(res.status){
                    const list = res.data.list;
                    this.$message({message:"加载应急事件响应人员完成, 数量："+list.length , type: 'success'});
                    list.forEach((v)=>{
                        this.userList.push({
                            'label': v.nickName,
                            'value': v.id
                        });
                    });
                }
            }
        },
        mounted(){
            console.log("挂载配送列表");
            this.filterCondition = this.mergeBean(this.entity.pagination, this.filterCondition);
            this.loadList();
            this.loadUser();
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
        margin: 10px auto;
    }
    .filter-bar{
        margin: 10px auto;
        text-align: left;
    }
    #detail{
        margin: 20px;
        padding: 0 20px;
    }
    #detail div{
        display: inline-block;
        margin: 10px;
    }
</style>