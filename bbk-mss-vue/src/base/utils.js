//合并source对象的属性到target
export function mergeBean(source, target) {
    if(source===undefined){
        return target;
    }
    if(target===undefined){
        return source;
    }
    for(const i in source){
        target[i] = source[i];
    }
    return target;
}

export function dateFormat(value, format) {
    if (typeof(value) == "undefined" || value == null || value == '') {
        return value;
    }
    const date = new Date(value);
    format = format || 'yyyy-MM-dd';
    const o = {
        "M+": date.getMonth() + 1, //month
        "d+": date.getDate(), //day
        "h+": date.getHours(), //hour
        "m+": date.getMinutes(), //minute
        "s+": date.getSeconds(), //second
        "q+": Math.floor((date.getMonth() + 3) / 3), //quarter
        "S": date.getMilliseconds() //millisecond
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (const k in o){
        if (new RegExp("(" + k + ")").test(format)){
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

export function isNull(o) {
    return o === undefined || o === null || o === "";
}

export function isIdentity(code) {
    return this.$user.identityCode === code;
}

export function isNotIdentity(code) {
    return this.$user.identityCode !== code;
}