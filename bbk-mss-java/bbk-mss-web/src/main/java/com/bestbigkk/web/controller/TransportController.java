package com.bestbigkk.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bestbigkk.common.ListResponse;
import com.bestbigkk.common.Pagination;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.utils.QueryWrapperUtils;
import com.bestbigkk.persistence.entity.TransportPO;
import com.bestbigkk.service.ITransportService;
import com.bestbigkk.web.response.annotation.RW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author: 开
 * @date: 2020-04-19 18:57:11
 * @describe: 用户控制器
 */
@RW
@Api(tags = "运输单接口")
@RequestMapping(value = "/dev/transport", produces = {"application/json;charset=UTF-8"})
public class TransportController {
    @Autowired
    private ITransportService transportService;
    @Autowired
    private QueryWrapperUtils queryWrapperUtils;

    @PostMapping
    @ApiOperation(value = "新增一个运输单")
    public TransportPO add(TransportPO transportPO) {
        boolean registered = transportService.save(transportPO);
        if (registered) {
            return transportPO;
        }
        throw new BusinessException("注册失败");
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "按照ID删除一个运输单")
    public Boolean delete(@PathVariable("id") Long id) {

        final TransportPO beDelete = transportService.getById(id);
        if (Objects.isNull(beDelete)) {
            throw new BusinessException("被删除运输单不存在");
        }

        final boolean remove = transportService.removeById(id);
        if (remove) {
            return true;
        }

        throw new BusinessException("删除失败，请检查运输单ID");
    }

    @PutMapping
    @ApiOperation(value = "按照ID更新运输单信息，以ID确定被更新运输单对象")
    public TransportPO update(TransportPO user) {

        if (Objects.isNull(user.getId())) {
            throw new BusinessException("必须指定更新运输单的ID");
        }

        final TransportPO beUpdate = transportService.getById(user.getId());
        if (Objects.isNull(beUpdate)) {
            throw new BusinessException("被更新运输单不存在");
        }

        final boolean update = transportService.updateById(user);
        if (update) {
            return query(user.getId());
        }
        throw new BusinessException("更新失败");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "按照ID查询一个运输单")
    public TransportPO query(@PathVariable("id") Long id) {
        final TransportPO user = transportService.getById(id);
        if (Objects.nonNull(user)) {
            return user;
        }
        throw new BusinessException("运输单不存在");
    }

    @GetMapping("/list")
    @ApiOperation(value = "按照条件查询运输单列表")
    public ListResponse<TransportPO> querys(TransportPO transport, Pagination<TransportPO> page) {
        final QueryWrapper<TransportPO> query = queryWrapperUtils.buildNotNullEqualsWrapper(transport);
        final IPage<TransportPO> record = transportService.page(page.toPage("create_time"), query);
        return new ListResponse<>(record.getRecords(), new Pagination<TransportPO>().toPagination(record));
    }
}
