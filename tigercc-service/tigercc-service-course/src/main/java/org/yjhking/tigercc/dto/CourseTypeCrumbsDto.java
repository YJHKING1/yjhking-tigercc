package org.yjhking.tigercc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yjhking.tigercc.domain.CourseType;

import java.util.List;

/**
 * @author YJH
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseTypeCrumbsDto {
    //当前分类
    private CourseType ownerProductType;
    //当前分类的同级分类列表
    private List<CourseType> otherProductTypes;
}
