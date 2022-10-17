package com.ahreumvitsnowflake.graduation.springboot.web.dto;

import com.ahreumvitsnowflake.graduation.springboot.domain.report.ReportType;
import lombok.Data;

@Data
public class ReportRequestDto {
    ReportType type;
}
