package com.bestbigkk.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bestbigkk.common.ListResponse;
import com.bestbigkk.common.Pagination;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.utils.QueryWrapperUtils;
import com.bestbigkk.persistence.entity.ApprovalPO;
import com.bestbigkk.service.IApprovalService;
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
@Api(tags = "审批接口")
@RequestMapping(value = "/dev/approval", produces = {"application/json;charset=UTF-8"})
public class ApprovalController {
    
    @Autowired
    private IApprovalService approvalService;
    @Autowired
    private QueryWrapperUtils queryWrapperUtils;

    @PostMapping
    @ApiOperation(value = "新增一个审批")
    public ApprovalPO add(ApprovalPO approvalPo) {
        boolean save = approvalService.save(approvalPo);
        if (save) {
            return approvalPo;
        }
        throw new BusinessException("新增失败");
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "按照ID删除一个审批")
    public Boolean delete(@PathVariable("id") String id) {

        final ApprovalPO beDelete = approvalService.getById(id);
        if (Objects.isNull(beDelete)) {
            throw new BusinessException("被删除审批对象不存在");
        }

        final boolean remove = approvalService.removeById(id);
        if (remove) {
            return true;
        }

        throw new BusinessException("删除失败，请检查审批对象是否存在");
    }

    @PutMapping
    @ApiOperation(value = "按照ID更新审批对象信息，以ID确定被更新审批对象")
    public ApprovalPO update(ApprovalPO approvalPo) {

        if (Objects.isNull(approvalPo.getId())) {
            throw new BusinessException("必须指定更新审批对象的ID");
        }

        final ApprovalPO beUpdate = approvalService.getById(approvalPo.getId());
        if (Objects.isNull(beUpdate)) {
            throw new BusinessException("被更新审批对象不存在");
        }

        final boolean update = approvalService.updateById(approvalPo);
        if (update) {
            return query(approvalPo.getId());
        }
        throw new BusinessException("更新失败");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "按照ID查询一个审批对象")
    public ApprovalPO query(@PathVariable("id") Long id) {
        final ApprovalPO user = approvalService.getById(id);
        if (Objects.nonNull(user)) {
            return user;
        }
        throw new BusinessException("审批对象不存在");
    }

    @GetMapping("/list")
    @ApiOperation(value = "按照条件查询审批对象列表")
    public ListResponse<ApprovalPO> querys(ApprovalPO user, Pagination<ApprovalPO> page) {
        final QueryWrapper<ApprovalPO> query = queryWrapperUtils.buildNotNullEqualsWrapper(user);
        final IPage<ApprovalPO> record = approvalService.page(page.toPage("create_time"), query);
        return new ListResponse<>(record.getRecords(), new Pagination<ApprovalPO>().toPagination(record));
    }

}
