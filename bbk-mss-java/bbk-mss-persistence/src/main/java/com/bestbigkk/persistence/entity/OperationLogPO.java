package com.bestbigkk.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author xugongkai
 * @since 2020-03-24
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_operation_log")
@ApiModel(value="OperationLogPO对象", description="操作日志表")
public class OperationLogPO extends Model<OperationLogPO> {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "请求者IP")
    private String ip;

    @ApiModelProperty(value = "模块名称")
    private String modelName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "日志行为简述")
    private String logDescription;

    @ApiModelProperty(value = "携带参数")
    private String actionArgs;

    @ApiModelProperty(value = "日志产生地方【ClassName:methodName】")
    private String logLocation;

    @ApiModelProperty(value = "异常简述")
    private String errorMsg;

    @ApiModelProperty(value = "堆栈信息")
    private String stackInfo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
