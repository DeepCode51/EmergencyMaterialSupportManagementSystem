package com.bestbigkk.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
* @author: 开
* @date: 2020-04-19 20:14:58
* @describe: 分页对象
*/
@Data
@NoArgsConstructor
public class Pagination<T> {

    @ApiModelProperty("当前页数")
    private Long currentPage;

    @ApiModelProperty("页容量")
    private Long pageSize;

    @ApiModelProperty("记录总数")
    private Long total;

    public Pagination(long currentPage, long pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Page<T> toPage(String...descColumnNames) {
        Page<T> page = new Page<>();
        page.setCurrent(this.currentPage==null ? 1 : this.currentPage);
        page.setSize(this.pageSize==null ? 10 : this.pageSize);
        page.setTotal(this.total == null ? 0 : this.total);
        page.setDescs(Arrays.asList(descColumnNames));
        return page;
    }

    public Pagination<T> toPagination(IPage<T> page) {
        if (Objects.isNull(page)) {
            return new Pagination<>(1, 10);
        }

        Pagination<T> pagination = new Pagination<>(page.getCurrent(), page.getSize());
        pagination.setTotal(page.getTotal());
        return pagination;

    }
}
