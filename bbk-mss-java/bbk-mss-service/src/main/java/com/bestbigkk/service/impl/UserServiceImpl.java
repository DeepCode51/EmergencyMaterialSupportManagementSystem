package com.bestbigkk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bestbigkk.persistence.dao.UserDao;
import com.bestbigkk.persistence.entity.UserPO;
import com.bestbigkk.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xugongkai
 * @since 2020-04-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserPO> implements IUserService {

}
