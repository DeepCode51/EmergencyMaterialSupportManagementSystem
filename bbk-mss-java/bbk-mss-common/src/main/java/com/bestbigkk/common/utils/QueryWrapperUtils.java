package com.bestbigkk.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;

/**
* @author: 开
* @date: 2020-04-19 19:16:29
* @describe: mybatis-plus query wrapper 条件构造器工具
*/

@Component
public class QueryWrapperUtils {

    /**
     * 传递一个对象，将该对象中所有非空（null）的属性构建为一个querywrapper查询器的条件。
     * @param t 对象
     * @param ignorePropertiesName 忽略的属性，这些属性不会进入构建为条件
     * @param <T> 构件好的条件查询器。
     */
    public <T> QueryWrapper<T> buildNotNullEqualsWrapper(T t, String...ignorePropertiesName){
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        HashSet<String> ignore = new HashSet<>(Arrays.asList(ignorePropertiesName));
        for (Field field : t.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            final String filedName = field.getName();
            if ("serialVersionUID".equals(filedName) || ignore.contains(filedName)) {
                continue;
            }
            try {
                if(field.get(t) == null){
                    continue;
                }
                TableId tableId = field.getAnnotation(TableId.class);
                if (tableId != null){
                    wrapper.eq(tableId.value(),field.get(t));
                    continue;
                }
                TableField tableField = field.getAnnotation(TableField.class);
                if(tableField != null && tableField.exist()){
                    wrapper.eq(tableField.value(),field.get(t));
                    continue;
                }
                wrapper.eq(filedName.replaceAll("[A-Z]", "_$0").toLowerCase(),field.get(t));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return wrapper;
    }

}
