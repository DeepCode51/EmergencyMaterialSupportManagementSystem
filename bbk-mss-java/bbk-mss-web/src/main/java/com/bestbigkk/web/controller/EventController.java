package com.bestbigkk.web.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bestbigkk.common.ListResponse;
import com.bestbigkk.common.Pagination;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.utils.QueryWrapperUtils;
import com.bestbigkk.persistence.entity.EventPO;
import com.bestbigkk.service.EventService;
import com.bestbigkk.web.response.annotation.RW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xugongkai
 * @data 2020-04-21
 * @describe: API接口
 */
@RW
@Api(tags = {"应急事件接口"})
@Slf4j
@RequestMapping(value = "/dev/event", produces = {"application/json;charset=UTF-8"})
public class EventController {

    /**
     * 单次最大操作数量
     */
    private final Integer OPERATE_MAX_BATCH = 1000;

    @Autowired
    private EventService eventService;
    @Autowired
    private QueryWrapperUtils queryWrapperUtils;

    @PostMapping
    @ApiOperation(value = "新增一个对象")
    public EventPO add(EventPO eventPO) {
        eventPO.setId(null);
        boolean registered = eventService.save(eventPO);
        if (registered) {
            return eventPO;
        }
        throw new BusinessException("新增失败");
    }

    @PostMapping("/batch")
    @ApiOperation(value = "新增一批对象，最多允许：1000个对象/次")
    public Integer adds(String jsonList) {
        List<EventPO> eventPOList = parseJson2Obj(jsonList, EventPO.class);
        List<EventPO> collects = eventPOList.stream().peek(obj -> obj.setId(null)).collect(Collectors.toList());
        boolean addBatch = eventService.saveBatch(collects);
        if (addBatch) {
            return collects.size();
        }
        throw new BusinessException("批量插入失败");
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "按照ID删除一个对象")
    public Boolean delete(@PathVariable("id") Long id) {
        idsCheck(Collections.singletonList(id));
        final boolean remove = eventService.removeById(id);
        if (remove) {
            return true;
        }
        throw new BusinessException("删除失败，请检查对象 [ID = " + id + "]");
    }

    @DeleteMapping("/batch")
    @ApiOperation(value = "批量删除对象")
    public Boolean deletes(Long[] ids) {
        idsCheck(ids);
        boolean removeBatch = eventService.removeByIds(Arrays.asList(ids));
        if (removeBatch) {
            return true;
        }
        throw new BusinessException("批量删除失败");
    }

    @PutMapping
    @ApiOperation(value = "按照ID更新对象信息，以ID确定被更新对象")
    public EventPO update(EventPO eventPO) {
        idsCheck(Collections.singletonList(eventPO.getId()));
        final boolean update = eventService.updateById(eventPO);
        if (update) {
            return query(eventPO.getId());
        }
        throw new BusinessException("更新失败");
    }

    @PutMapping("/batch")
    @ApiOperation(value = "批量更新对象")
    public Boolean updates(String jsonList) {
        List<EventPO> eventPOList = parseJson2Obj(jsonList, EventPO.class);
        List<Long> ids = eventPOList.stream().filter(u -> Objects.nonNull(u.getId())).map(EventPO::getId).collect(Collectors.toList());
        idsCheck(ids);
        boolean updateBatch = eventService.updateBatchById(eventPOList);
        if (updateBatch) {
            return true;
        }
        throw new BusinessException("批量更新失败");
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "按照ID查询一个对象")
    public EventPO query(@PathVariable("id") Long id) {
        idsCheck(Collections.singletonList(id));
        return eventService.getById(id);
    }

    @GetMapping("/list")
    @ApiOperation(value = "按照条件查询对象列表")
    public ListResponse<EventPO> list(EventPO eventPO, Pagination<EventPO> page) {
        final QueryWrapper<EventPO> query = queryWrapperUtils.buildNotNullEqualsWrapper(eventPO);
        final IPage<EventPO> record = eventService.page(page.toPage("create_time"), query);
        return new ListResponse<>(record.getRecords(), new Pagination<EventPO>().toPagination(record));
    }

    /**
     * 判断一批id在数据库是否都存在。
     *
     * @param ids 被判断id集合
     * @return 是否都存在
     */
    private <T> void idsCheck(List<T> ids) {
        if (Objects.isNull(ids) || ids.size() == 0) {
            throw new BusinessException("未指定对象ID");
        }
        int dbCount = eventService.count(new QueryWrapper<EventPO>().lambda().in(EventPO::getId, ids));
        if (dbCount != ids.size()) {
            throw new BusinessException("请求操作的ID数量：" + ids.size() + "，系统得到的实际可操作ID数量：" + dbCount + ", 数量不对等，部分ID可能不存在，请确认！");
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
     *
     * @param jsonString Json字符串
     * @param clazz      对象
     * @param <T>        类型
     * @return 数组
     */
    private <T> List<T> parseJson2Obj(String jsonString, Class<T> clazz) {
        List<T> list;
        try {
            list = JSON.parseArray(jsonString, clazz);
        } catch (Exception e) {
            throw new BusinessException("Json解析失败：" + e.getMessage());
        }
        if (list.size() > OPERATE_MAX_BATCH) {
            throw new BusinessException("超出限制，批量插入最多允许：1000个对象/次");
        }
        return list;
    }

}
