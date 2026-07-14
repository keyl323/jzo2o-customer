package com.jzo2o.customer.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 个人银行账户
 * </p>
 *
 * @author yutsung
 * @since 2026-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bank_account")
@ApiModel(value="BankAccount对象", description="个人银行账户")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户类型")
    private Integer userType;

    @ApiModelProperty(value = "户名")
    private String name;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "市级")
    private String city;

    @ApiModelProperty(value = "区/县")
    private String district;

    @ApiModelProperty(value = "网点")
    private String branch;

    @ApiModelProperty(value = "银行账号")
    private String account;

    @ApiModelProperty(value = "开户证明")
    private String accountCertification;

    @ApiModelProperty(value = "是否已删除，0：未删除，1：已删除")
    private Integer isDeleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;


}
