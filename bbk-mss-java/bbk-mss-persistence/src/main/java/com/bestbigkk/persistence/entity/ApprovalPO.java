package com.bestbigkk.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xugongkai
 * @since 2020-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_approval")
@ApiModel(value="ApprovalPO对象", description="")
public class ApprovalPO extends Model<ApprovalPO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "审批标题")
    private String title;

    @ApiModelProperty(value = "审批主体内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最近一次更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastUpdateTime;

    @ApiModelProperty(value = "审批优先级[低，中，高，紧急]")
    private String level;

    @ApiModelProperty(value = "审批当前状态[待审核，待配送，配送中，已完结]")
    private String status;

    @ApiModelProperty(value = "关联审批人ID")
    private Long approvalUserId;

    @ApiModelProperty(value = "物资申请信息：物资ID#数量@物资ID#数量")
    private String materialApply;

    @ApiModelProperty(value = "创建人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createUserId;

    @ApiModelProperty(value = "关联应急事件ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long eventId;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
