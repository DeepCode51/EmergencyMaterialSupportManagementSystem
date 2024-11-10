package com.bestbigkk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bestbigkk.persistence.dao.EventDao;
import com.bestbigkk.persistence.entity.EventPO;
import com.bestbigkk.service.EventService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xugongkai
 * @since 2020-04-21
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventDao, EventPO> implements EventService {

}
