package com.bestbigkk.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bestbigkk.persistence.entity.OperationLogPO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 操作日志表 Mapper 接口
 * </p>
 *
 * @author xugongkai
 * @since 2020-03-24
 */
@Repository
public interface OperationLogDao extends BaseMapper<OperationLogPO> {

    int countA();

}
