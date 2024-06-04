package com.bupt.hospital.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 
 * @TableName t_registration
 */
@TableName(value ="t_registration")
@Data
public class Registration implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @Nullable
    private Integer doctorId;

    @NotNull(message = "号源数量不能为空")
    private Integer quantity;

    @Nullable
    private Integer lockedQuantity;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "放号日期不能为空")
    private Date date;

    /**
     * 0-morning,1-afterning
     */
    @NotNull(message = "放号时间段不能为空")
    private Integer daytime;

    /**
     * 0-no,1-yes
     */
    @Nullable
    private Integer authorized;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}