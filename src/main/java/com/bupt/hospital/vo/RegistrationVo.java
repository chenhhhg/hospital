package com.bupt.hospital.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author cgx
 */
@Data
public class RegistrationVo {
    private int uid;
    private String doctorName;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    @Schema(description = "0-早上，1-下午")
    private int time;
    private int count;
    private int id;
    @Schema(description = "0-未审核，1-审核通过")
    private int checked;
    private int availableCount;
    private String hospital;
    private String title;
    private String office;
}
