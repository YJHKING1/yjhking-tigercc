package org.yjhking.tigercc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YJH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BucketVO {
    private String key;
    private Long docCount;
}
