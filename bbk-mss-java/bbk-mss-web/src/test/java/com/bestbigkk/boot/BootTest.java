package com.bestbigkk.boot;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bestbigkk.BBKApplication;
import com.bestbigkk.common.Pagination;
import com.bestbigkk.common.utils.encryption.AESUtils;
import com.bestbigkk.persistence.dao.UserDao;
import com.bestbigkk.persistence.entity.UserPO;
import com.bestbigkk.service.IUserService;
import com.bestbigkk.web.config.WebSocketConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
* @author: 开
* @date: 2020-03-24 16:03:20
* @describe: SpringBoot单元测试
*/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BBKApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BootTest {

    @Autowired
    IUserService userService;
    @Autowired
    UserDao userDao;

    @org.junit.Test
    public void test() {
        final IPage<UserPO> page = userService.page(new Pagination<UserPO>(1, 1).toPage());
        log.info("{}", page.getRecords());

        final IPage<UserPO> page1 = userService.page(new Page<UserPO>(1, 1));
        log.info("{}", page1.getRecords());

        final IPage<UserPO> page2 = userDao.selectPage(new Pagination<UserPO>(1,1).toPage(), null);
        log.info("{}", page2.getRecords());

    }

}
