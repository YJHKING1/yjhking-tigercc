package org.yjhking.tigercc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yjhking.tigercc.domain.Course;
import org.yjhking.tigercc.domain.CourseMarket;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author YJH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDataOrderVo {
    private List<CourseItemDataOrderVo> courseInfos;
    private BigDecimal totalAmount;
}
