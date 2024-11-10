import Vue from 'vue';
import BaiduMap from 'vue-baidu-map'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue';
import router from '@/router/router'

Vue.use(ElementUI);
Vue.use(BaiduMap, {
  ak: "L0eY0aB9So7nEfQLkLAdG4cHuS9S68Oc"
});

new Vue({
  el: '#app',
  router,
  render: h => h(App)
});