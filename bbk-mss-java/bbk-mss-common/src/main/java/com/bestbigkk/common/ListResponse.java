package com.bestbigkk.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
* @author: 开
* @date: 2020-04-19 20:35:22
* @describe: 公共列表响应对象，会加装一个分页信息
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse<T> implements Serializable {

    private List<T> list;
    private Pagination<T> pagination;

}
