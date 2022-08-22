package org.yjhking.tigercc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author YJH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationMessage2MQDto {
    private String title;
    private String content;
    private String type;
    private List<Long> userIds;
}
