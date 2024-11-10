package com.bestbigkk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bestbigkk.persistence.dao.MaterialDao;
import com.bestbigkk.persistence.entity.MaterialPO;
import com.bestbigkk.service.IMaterialService;
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
public class MaterialServiceImpl extends ServiceImpl<MaterialDao, MaterialPO> implements IMaterialService {

}
