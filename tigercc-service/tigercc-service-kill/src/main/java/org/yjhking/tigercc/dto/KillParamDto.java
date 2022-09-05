package org.yjhking.tigercc.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author YJH
 */
@Data
public class KillParamDto {
    @NotEmpty(message = "非法请求")
    private String code;
    @NotNull(message = "非法请求")
    private Long killCourseId;
    private Long activityId;
}
