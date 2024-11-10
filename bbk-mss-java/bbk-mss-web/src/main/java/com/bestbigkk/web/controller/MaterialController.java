package com.bestbigkk.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bestbigkk.common.ListResponse;
import com.bestbigkk.common.Pagination;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.utils.QueryWrapperUtils;
import com.bestbigkk.persistence.entity.MaterialCategoryPO;
import com.bestbigkk.persistence.entity.MaterialPO;
import com.bestbigkk.service.IMaterialCategoryService;
import com.bestbigkk.service.IMaterialService;
import com.bestbigkk.web.response.annotation.RW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author: 开
 * @date: 2020-04-19 18:57:11
 * @describe: 用户控制器
 */
@RW
@Api(tags = "物资接口")
@RequestMapping(value = "/dev/material", produces = {"application/json;charset=UTF-8"})
public class MaterialController {

    @Autowired
    private IMaterialService materialService;
    @Autowired
    private IMaterialCategoryService materialCategoryService;
    @Autowired
    private QueryWrapperUtils queryWrapperUtils;

    @PostMapping
    @ApiOperation(value = "新增一个物资")
    public MaterialPO add(MaterialPO material) {
        final String categoryCode = material.getMaterialCategoryCode();
        if (Objects.isNull(categoryCode)) {
            throw new BusinessException("必须指定隶属于的物资分类");
        }
        final int count = materialCategoryService.count(new QueryWrapper<MaterialCategoryPO>().lambda().eq(MaterialCategoryPO::getCategoryCode, categoryCode));
        if (count == 0) {
            throw new BusinessException("未找到物资分类编码为：" + categoryCode + "的物资");
        }

        boolean save = materialService.save(material);
        if (save) {
            return material;
        }
        throw new BusinessException("新增物资失败");
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "按照ID删除一个物资")
    public Boolean delete(@PathVariable("id") String id) {

        final MaterialPO beDelete = materialService.getById(id);
        if (Objects.isNull(beDelete)) {
            throw new BusinessException("被删除物资不存在");
        }

        final boolean remove = materialService.removeById(id);
        if (remove) {
            return true;
        }

        throw new BusinessException("删除失败，请检查物资ID");
    }

    @PutMapping
    @ApiOperation(value = "按照ID更新物资信息，以ID确定被更新物资")
    public MaterialPO update(MaterialPO material) {

        if (Objects.isNull(material.getId())) {
            throw new BusinessException("必须指定更新物资的ID");
        }

        final String materialCategoryCode = material.getMaterialCategoryCode();
        if (Objects.isNull(materialCategoryCode)) {
            throw new BusinessException("必须指定隶属于的物资分类");
        }
        final int count = materialCategoryService.count(new QueryWrapper<MaterialCategoryPO>().lambda().eq(MaterialCategoryPO::getCategoryCode, materialCategoryCode));
        if (count == 0) {
            throw new BusinessException("未找到物资分类编码为：" + materialCategoryCode + "的物资");
        }


        final MaterialPO beUpdate = materialService.getById(material.getId());
        if (Objects.isNull(beUpdate)) {
            throw new BusinessException("被更新物资不存在");
        }

        final boolean update = materialService.updateById(material);
        if (update) {
            return query(material.getId());
        }
        throw new BusinessException("更新失败");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "按照ID查询一个位物资")
    public MaterialPO query(@PathVariable("id") Long id) {
        final MaterialPO user = materialService.getById(id);
        if (Objects.nonNull(user)) {
            return user;
        }
        throw new BusinessException("物资不存在");
    }

    @GetMapping("/list")
    @ApiOperation(value = "按照条件查询物资列表")
    public ListResponse<MaterialPO> querys(MaterialPO user, Pagination<MaterialPO> page) {
        final QueryWrapper<MaterialPO> query = queryWrapperUtils.buildNotNullEqualsWrapper(user);
        final IPage<MaterialPO> record = materialService.page(page.toPage("create_time"), query);
        return new ListResponse<>(record.getRecords(), new Pagination<MaterialPO>().toPagination(record));
    }

}
