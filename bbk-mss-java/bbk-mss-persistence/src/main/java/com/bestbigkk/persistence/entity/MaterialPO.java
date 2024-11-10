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
 * @since 2020-04-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_material")
@ApiModel(value="MaterialPO对象", description="")
public class MaterialPO extends Model<MaterialPO> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "物资批次")
    private String materialBatchNo;

    @ApiModelProperty(value = "物资名称")
    private String materialName;

    @ApiModelProperty(value = "分类名称")
    private String materialCategoryCode;

    @ApiModelProperty(value = "库存数量")
    private Integer materialNum;

    @ApiModelProperty(value = "购进来源")
    private String materialFrom;

    @ApiModelProperty(value = "被购方联系人")
    private String materialContactName;

    @ApiModelProperty(value = "被购方联系方式")
    private String materialContactNo;

    @ApiModelProperty(value = "购入合同编号")
    private String materialContractNo;

    @ApiModelProperty(value = "调配策略")
    private Boolean materialEnable;

    @ApiModelProperty(value = "创建日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
