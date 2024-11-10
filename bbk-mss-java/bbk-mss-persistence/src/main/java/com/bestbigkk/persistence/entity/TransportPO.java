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
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("t_transport")
@ApiModel(value="TransportPO对象", description="")
public class TransportPO extends Model<TransportPO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "关联审批ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long approvalId;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "预期完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectTime;

    @ApiModelProperty(value = "创建人")
    private Long createUserId;

    @ApiModelProperty(value = "运输状态详情，存储格式：时间#地点@时间#地点")
    private String transportRecord;

    @ApiModelProperty(value = "运送物资列表，存储格式：物资ID#数量@物资ID#数量")
    private String materialRecord;

    @ApiModelProperty(value = "运送状态：配送中，已接收")
    private String status;

    @ApiModelProperty(value = "接收人信息")
    private Long receiverUserId;

    @ApiModelProperty(value = "运输方式")
    private String transportType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
