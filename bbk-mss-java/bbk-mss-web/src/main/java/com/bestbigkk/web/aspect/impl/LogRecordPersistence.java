package com.bestbigkk.web.aspect.impl;

import com.alibaba.fastjson.JSONObject;
import com.bestbigkk.common.utils.RedisUtils;
import com.bestbigkk.persistence.dao.OperationLogDao;
import com.bestbigkk.persistence.entity.OperationLogPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: 开
 * @date: 2020-03-24 21:40:34
 * @describe: 日志对象持久化异步类，异步操作需要独立出来一个类才可以。
 */
@Component
@Slf4j
public class LogRecordPersistence{

    /**缓存在Redis中的待持久化日志对象数量。*/
    @Value("${bbk.log.cache:200L}")
    public Long CACHE_LENGTH;
    public final String REDIS_KEY = "operation:log:list:cache";

    private final RedisUtils redisUtils;
    private final OperationLogDao operationLogDao;

    public LogRecordPersistence(RedisUtils redisUtils, OperationLogDao operationLogDao) {
        this.redisUtils = redisUtils;
        this.operationLogDao = operationLogDao;
    }

    @PostConstruct
    void init() {
        log.info("方法执行日志缓存数量：{}", CACHE_LENGTH);
    }

    boolean add(OperationLogPO operationLogPO) {
        final long size = redisUtils.lGetListSize(REDIS_KEY);
        if (size >= CACHE_LENGTH) {
            return false;
        }
        return redisUtils.lSet(REDIS_KEY, JSONObject.toJSONString(operationLogPO));
    }

    @Async
    public void persistence(OperationLogPO operationLogPO) {
        final List<Object> objects = redisUtils.lGet(REDIS_KEY, 0, -1);
        redisUtils.del(REDIS_KEY);
        final List<OperationLogPO> res = new LinkedList<>();
        res.add(operationLogPO);
        objects.forEach(o -> res.add(JSONObject.parseObject(o + "", OperationLogPO.class)));
        res.forEach(operationLogDao::insert);
        log.info("批量持久化到数据库完成，size = {}", res.size());
    }
}
