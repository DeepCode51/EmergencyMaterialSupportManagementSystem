<template>
    <div>
            <el-card class="m">
                <baidu-map v-bind:style="mapStyle" class="bm-view" ak="L0eY0aB9So7nEfQLkLAdG4cHuS9S68Oc"
                           :center="keyword"
                           :zoom="zoom"
                           :scroll-wheel-zoom="true"
                           @ready="initMap"
                           @click="getClickInfo"
                           @moving="syncCenterAndZoom"
                           @moveend="syncCenterAndZoom"
                           @zoomend="syncCenterAndZoom">
                    <bm-view style="width: 100%; height:500px;"></bm-view>
                    <bm-marker :position="{lng: center.lng, lat: center.lat}" :dragging="true" animation="BMAP_ANIMATION_BOUNCE">
                    </bm-marker>
                    <bm-control :offset="{width: '10px', height: '10px'}">
                        <bm-auto-complete v-model="keyword" :sugStyle="{zIndex: 999999}">
                            <input type="text" placeholder="请输入搜索关键字" class="serachinput">
                        </bm-auto-complete>
                    </bm-control>
                    <bm-local-search :keyword="keyword" :auto-viewport="true" style="width:0px;height:0px;overflow: hidden;"></bm-local-search>
                </baidu-map>
            </el-card>
            <el-card shadow="never" style="text-align: right">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="confirm">确 定</el-button>
            </el-card>
    </div>
</template>
<script>
    import {BaiduMap, BmControl, BmView, BmAutoComplete, BmLocalSearch, BmMarker} from 'vue-baidu-map'
    export default {
        components: {
            BaiduMap,
            BmControl,
            BmView,
            BmAutoComplete,
            BmLocalSearch,
            BmMarker
        },
        data: function () {
            return {
                showMapComponent: this.value,
                mapStyle: {
                    width: '100%',
                    height: this.mapHeight + 'px'
                },
                center: {lat: 23.085129, lng: 113.082751},
                zoom: 18
            }
        },
        watch: {
            value: function (currentValue) {
                this.showMapComponent = currentValue;
                if (currentValue) {
                    this.keyword = ''
                }
            }
        },
        props: {
            value: Boolean,
            mapHeight: {
                type: Number,
                default: 500
            },
            keyword:{
                type:String,
                default: ""
            }
        },
        methods: {
            getClickInfo (e) {
                this.center.lng = e.point.lng;
                this.center.lat = e.point.lat
            },
            syncCenterAndZoom (e) {
                const {lng, lat} = e.target.getCenter();
                this.center.lng = lng;
                this.center.lat = lat;
                this.zoom = e.target.getZoom()
            },
            confirm: function () {
                this.showMapComponent = false;
                this.$emit('map-confirm', this.center.lng+"&"+this.center.lat);
                console.log(this.center);
            },
            cancel: function () {
                this.showMapComponent = false;
                this.$emit('cancel', this.showMapComponent);
            },
            initMap({ BMap, map }){
                this.center.lng = 113.082751;
                this.center.lat = 23.085129;
                map.centerAndZoom(this.keyword, 18);
            }
        }
    }
</script>

<style scoped>
    .m{
        padding: 0;
        margin: 0 0 10px;
    }
    .serachinput{
        margin: 5px;
        border-radius: 10px;
        border: lightskyblue 1px solid;
        font-weight: bold;
        padding: 10px;
    }
</style>