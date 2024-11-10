package com.bestbigkk.web.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bestbigkk.common.Pagination;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.utils.QueryWrapperUtils;
import com.bestbigkk.web.response.annotation.RW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bestbigkk.common.ListResponse;
import lombok.extern.slf4j.Slf4j;

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ${author}
 * @data ${date}
 * @describe: ${table.comment!}API接口
 */
@RW
@Api(tags = {"${table.comment!}API接口"})
@Slf4j
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if superControllerClass??>public class ${table.controllerName} extends ${superControllerClass}{<#else>public class ${table.controllerName} {</#if>

    /** 单次最大操作数量*/
    private final Integer OPERATE_MAX_BATCH = 1000;

    @Autowired
    private ${table.serviceName} ${(table.serviceName?substring(0))?uncap_first};
    @Autowired
    private QueryWrapperUtils queryWrapperUtils;

    @PostMapping
    @ApiOperation(value = "新增一个对象")
    public ${entity} add( ${entity} ${entity?uncap_first}){
        ${entity?uncap_first}.setId(null);
        boolean registered = ${(table.serviceName?substring(0))?uncap_first}.save( ${entity?uncap_first});
        if (registered) {
            return  ${entity?uncap_first};
        }
        throw new BusinessException("新增失败");
    }

    @PostMapping("/batch")
    @ApiOperation(value = "新增一批对象，最多允许：1000个对象/次")
    public Integer adds(String jsonList) {
        List<${entity}> ${entity?uncap_first}List = parseJson2Obj(jsonList, ${entity}.class);
        List<${entity}> collects =  ${entity?uncap_first}List.stream().peek(obj -> obj.setId(null)).collect(Collectors.toList());
        boolean addBatch = ${(table.serviceName?substring(0))?uncap_first}.saveBatch(collects);
        if (addBatch) {
            return collects.size();
        }
        throw new BusinessException("批量插入失败");
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "按照ID删除一个对象")
    public Boolean delete(@PathVariable("id") Long id) {
        idsCheck(Collections.singletonList(id));
        final boolean remove = ${(table.serviceName?substring(0))?uncap_first}.removeById(id);
        if (remove) {
            return true;
        }
        throw new BusinessException("删除失败，请检查对象 [ID = "+ id +"]");
    }

    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除对象")
    public Boolean deletes(Long[] ids) {
        idsCheck(ids);
        boolean removeBatch = ${(table.serviceName?substring(0))?uncap_first}.removeByIds(Arrays.asList(ids));
        if (removeBatch) {
            return true;
        }
        throw new BusinessException("批量删除失败");
    }

    @PutMapping
    @ApiOperation(value = "按照ID更新对象信息，以ID确定被更新对象")
    public  ${entity} update( ${entity} ${entity?uncap_first}){
        idsCheck(Collections.singletonList(${entity?uncap_first}.getId()));
        final boolean update = ${(table.serviceName?substring(0))?uncap_first}.updateById(${entity?uncap_first});
        if (update) {
            return query(${entity?uncap_first}.getId());
        }
        throw new BusinessException("更新失败");
    }

    @PutMapping("/batch")
    @ApiOperation(value = "批量更新对象")
    public Boolean updates(String jsonList) {
        List< ${entity}> ${entity?uncap_first}List = parseJson2Obj(jsonList,  ${entity}.class);
        List<Long> ids = ${entity?uncap_first}List.stream().filter(u->Objects.nonNull(u.getId())).map(User::getId).collect(Collectors.toList());
        idsCheck(ids);
        boolean updateBatch = ${(table.serviceName?substring(0))?uncap_first}.updateBatchById(${entity?uncap_first}List);
        if (updateBatch) {
            return true;
        }
        throw new BusinessException("批量更新失败");
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "按照ID查询一个对象")
    public  ${entity} query(@PathVariable("id") Long id) {
       idsCheck(Collections.singletonList(id));
       return  ${(table.serviceName?substring(0))?uncap_first}.getById(id);
    }

    @GetMapping("/list")
    @ApiOperation(value = "按照条件查询对象列表")
    public ListResponse<${entity}> list(${entity} ${entity?uncap_first}, Pagination<${entity}> page) {
        final QueryWrapper<${entity}> query = queryWrapperUtils.buildNotNullEqualsWrapper(${entity?uncap_first});
        final IPage<${entity}> record =  ${(table.serviceName?substring(0))?uncap_first}.page(page.toPage("create_time"), query);
        return new ListResponse<>(record.getRecords(), new Pagination<${entity}>().toPagination(record));
    }

   /**
    * 判断一批id在数据库是否都存在。
    * @param ids 被判断id集合
    * @return 是否都存在
    */
   private <T> void idsCheck(List<T> ids) {
       if (Objects.isNull(ids) || ids.size() == 0) {
           throw new BusinessException("未指定对象ID");
       }
       int dbCount = userService.count(new QueryWrapper<User>().lambda().in(User::getId, ids));
       if (dbCount != ids.size()) {
           throw new BusinessException("请求操作的ID数量："+ids.size()+"，系统得到的实际可操作ID数量："+dbCount+", 数量不对等，部分ID可能不存在，请确认！");
       }
   }

   private <T> void idsCheck(T[] ids) {
       if (Objects.isNull(ids) || ids.length == 0) {
           throw new BusinessException("未指定对象ID");
       }
       idsCheck(Arrays.asList(ids));
   }

   /**
    * 解析JSON字符串为对应的对象
    * @param jsonString Json字符串
    * @param clazz 对象
    * @param <T> 类型
    * @return 数组
    */
   private <T> List<T> parseJson2Obj(String jsonString, Class<T> clazz) {
       List<T> list;
       try {
           list = JSON.parseArray(jsonString, clazz);
       } catch (Exception e) {
           throw new BusinessException("Json解析失败：" + e.getMessage());
       }
       if (list.size() > OPERATE_MAX_BATCH ) {
           throw new BusinessException("超出限制，批量插入最多允许：1000个对象/次");
       }
       return list;
   }

}
