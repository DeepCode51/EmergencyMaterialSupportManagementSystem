package ${package.Controller};

import com.dacheng.info.bim.business.base.common.JsonBean;
import com.dacheng.info.bim.business.base.common.ResultCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.web.bind.annotation.*;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

import javax.annotation.Resource;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * ${table.comment!} 前端控制器
 *
 * @author ${author}
 * @data ${date}
 * @describe:
 */
<#if restControllerStyle>
@Api(tags = {"${table.comment!}API接口"})
@RestController
<#else>
@Controller
@Sl4j
</#if>@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>class ${table.controllerName}<#if superControllerClass??>:${superControllerClass}()</#if><#else><#if superControllerClass??>public class ${table.controllerName} extends ${superControllerClass}{<#else>public class ${table.controllerName} {</#if>

    @Resource
    private ${table.serviceName} ${(table.serviceName?substring(1))?uncap_first};

    @ApiOperation(value = "新增一个对象")
    @PostMapping
    public ${entity} add( ${entity} ${entity?uncap_first}){
       return null;
    }

    @ApiOperation(value = "删除数据")
    @DeleteMapping("{id}")
    public Boolean delete(@RequestParam("id") Long id){
        if(Objects.isNull(id)){
            throw new BusinessException("必须提供要删除对象的ID");
        }
        return null;
    }

    @ApiOperation(value = "更新数据")
    @PutMapping()
    public ${entity} updateById( ${entity} ${entity?uncap_first}){
        if(Objects.isNull(${entity?uncap_first})){
            throw new BusinessException("必须提供要更新对象的ID");
        }
        return null;
    }

    @ApiOperation(value = "查询单一对象")
    @GetMapping(value="/{id}")
    public ${entity} findById(@PathVariable("id") Long id ){
        return null;
    }

    @ApiOperation(value = "查询多个对象")
    @GetMapping(value="/list")
    public ListResponse<${entity}> findListByPagination(${entity} ${entity?uncap_first}, Pagination<${entity}}> pagination){
        return null;
    }

}
</#if>