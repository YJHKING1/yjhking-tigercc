package org.yjhking.tigercc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.yjhking.tigercc.constants.NumberConstants;
import org.yjhking.tigercc.domain.CourseMarket;
import org.yjhking.tigercc.dto.CourseDto;
import org.yjhking.tigercc.enums.GlobalErrorCode;
import org.yjhking.tigercc.mapper.CourseMarketMapper;
import org.yjhking.tigercc.service.ICourseMarketService;
import org.yjhking.tigercc.utils.VerificationUtils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@Service
public class CourseMarketServiceImpl extends ServiceImpl<CourseMarketMapper, CourseMarket> implements ICourseMarketService {
    
    @Override
    public boolean save(CourseDto dto) {
        CourseMarket courseMarket = dto.getCourseMarket();
        if (Objects.equals(courseMarket.getCharge(), NumberConstants.TWO)) {
            VerificationUtils.isNotEmpty(String.valueOf(courseMarket.getPrice()), GlobalErrorCode.COURSE_PRICE_IS_NULL);
            VerificationUtils.isNotEmpty(String.valueOf(courseMarket.getPriceOld())
                    , GlobalErrorCode.COURSE_PRICE_IS_NULL);
        } else {
            courseMarket.setPrice(BigDecimal.valueOf(0));
            courseMarket.setPriceOld(BigDecimal.valueOf(0));
        }
        courseMarket.setId(dto.getCourse().getId());
        return insert(courseMarket);
    }
}
