package com.bestbigkk.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bestbigkk.common.Pagination;
import com.bestbigkk.common.enums.Identity;
import com.bestbigkk.common.exception.BusinessException;
import com.bestbigkk.common.utils.QueryWrapperUtils;
import com.bestbigkk.persistence.entity.UserPO;
import com.bestbigkk.service.IUserService;
import com.bestbigkk.common.ListResponse;
import com.bestbigkk.web.response.annotation.RW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author: 开
 * @date: 2020-04-19 18:57:11
 * @describe: 用户控制器
 */
@RW
@Api(tags = "用户接口")
@RequestMapping(value = "/dev/user", produces = {"application/json;charset=UTF-8"})
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private QueryWrapperUtils queryWrapperUtils;

    @PostMapping
    @ApiOperation(value = "新增一个用户")
    public UserPO add(UserPO user) {
        boolean registered = userService.save(user);
        if (registered) {
            return user;
        }
        throw new BusinessException("注册失败");
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "按照ID删除一个用户")
    public Boolean delete(@PathVariable("id") Long id) {

        final UserPO beDelete = userService.getById(id);
        if (Objects.isNull(beDelete)) {
            throw new BusinessException("被删除用户不存在");
        }

        if (Identity.SYSTEM_OPERATOR.identityCode.equals(beDelete.getIdentityCode())) {
            throw new BusinessException("无法删除该身份的用户");
        }

        final boolean remove = userService.removeById(id);
        if (remove) {
            return true;
        }

        throw new BusinessException("删除失败，请检查用户ID");
    }

    @PutMapping
    @ApiOperation(value = "按照ID更新用户信息，以ID确定被更新用户")
    public UserPO update(UserPO user) {

        if (Objects.isNull(user.getId())) {
            throw new BusinessException("必须指定更新用户的ID");
        }

        final UserPO beUpdate = userService.getById(user.getId());
        if (Objects.isNull(beUpdate)) {
            throw new BusinessException("被更新用户不存在");
        }

        final boolean update = userService.updateById(user);
        if (update) {
            return query(user.getId());
        }
        throw new BusinessException("更新失败");
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "按照ID查询一个用户")
    public UserPO query(@PathVariable("id") Long id) {
        final UserPO user = userService.getById(id);
        if (Objects.nonNull(user)) {
            return user;
        }
        throw new BusinessException("用户不存在");
    }

    @GetMapping("/list")
    @ApiOperation(value = "按照条件查询用户列表")
    public ListResponse<UserPO> querys(UserPO user, Pagination<UserPO> page) {
        final QueryWrapper<UserPO> query = queryWrapperUtils.buildNotNullEqualsWrapper(user);
        final IPage<UserPO> record = userService.page(page.toPage("create_time"), query);
        return new ListResponse<>(record.getRecords(), new Pagination<UserPO>().toPagination(record));
    }

}
