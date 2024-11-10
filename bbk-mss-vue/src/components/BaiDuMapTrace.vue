<template>
    <el-card>
        <baidu-map class="map" @ready="handler" :center="keyword" :map-click="true">
            <bml-marker-clusterer :averageCenter="true">
                <div v-for="(marker, i) of markers" :key="Date.now() + i">
                    <bm-marker :dragging="false" animation="BMAP_ANIMATION_BOUNCE"
                               :icon="{url: 'http://oss.bestbigkk.com/halo/loc_icon_1587780493004.png', size: {width: 37, height: 50}}"
                               :position="{lng: marker.lng, lat: marker.lat}"
                               @click="infoWindowOpen(marker)">
                    </bm-marker>
                </div>
            </bml-marker-clusterer>
        </baidu-map>
    </el-card>
</template>

<script>
    import { BmlMarkerClusterer } from 'vue-baidu-map'
    import BmMarker from 'vue-baidu-map/components/overlays/Marker'

    export default {
        name: 'BaiDuMapTrace',
        components: {
            BmlMarkerClusterer,
            BmMarker
        },
        props: {
            mapHeight: {
                type: Number,
                default: 500
            },
            markers:{
                type: Array,
                default(){
                    return []
                }
            },
            keyword:{
                type:String,
                default: "河南"
            }
        },
        data () {
            return {
                BMap: '',
                map: ''
            }
        },
        methods: {
            handler ({ BMap, map }) {
                map.enableScrollWheelZoom(true);
                this.BMap = BMap;
                this.map = map
            },
            infoWindowOpen (marker) {
                this.$set(marker, 'showInfo', true);
                console.log(marker);
            }
        }
    }
</script>
<style scoped>
    .map {
        height:800px;
    }
</style>