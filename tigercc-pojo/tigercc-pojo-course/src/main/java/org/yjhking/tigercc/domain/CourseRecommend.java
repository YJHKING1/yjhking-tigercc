package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author YJH
 * @since 2022-08-28
 */
@TableName("t_course_recommend")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseRecommend extends Model<CourseRecommend> {
    
    private static final long serialVersionUID = 1L;
    public static final Boolean STATE_ON = true;
    public static final Boolean STATE_OFF = false;
    public static final String STATE = "state";
    private Long id;
    private Boolean state;
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
