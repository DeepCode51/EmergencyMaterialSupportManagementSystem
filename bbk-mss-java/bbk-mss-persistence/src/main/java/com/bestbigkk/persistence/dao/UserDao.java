package com.bestbigkk.persistence.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bestbigkk.persistence.entity.UserPO;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xugongkai
 * @since 2020-04-19
 */
@Repository
public interface UserDao extends BaseMapper<UserPO> {

}
