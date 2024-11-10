package com.bestbigkk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bestbigkk.persistence.dao.OperationLogDao;
import com.bestbigkk.persistence.entity.OperationLogPO;
import com.bestbigkk.service.IOperationLogService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author xugongkai
 * @since 2020-03-24
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogDao, OperationLogPO> implements IOperationLogService {

    @Autowired
    private OperationLogDao operationLogDao;
}
