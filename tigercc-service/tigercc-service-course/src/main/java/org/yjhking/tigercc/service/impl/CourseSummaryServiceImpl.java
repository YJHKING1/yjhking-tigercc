package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.domain.CourseSummary;
import org.yjhking.tigercc.mapper.CourseSummaryMapper;
import org.yjhking.tigercc.service.ICourseSummaryService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseSummaryServiceImpl extends ServiceImpl<CourseSummaryMapper, CourseSummary> implements ICourseSummaryService {
    
    @Override
    public boolean save(Long id) {
        CourseSummary courseSummary = new CourseSummary();
        courseSummary.setId(id);
        courseSummary.setSaleCount(NumberConstants.ZERO);
        courseSummary.setViewCount(NumberConstants.ZERO);
        courseSummary.setCommentCount(NumberConstants.ZERO);
        return insert(courseSummary);
    }
}
