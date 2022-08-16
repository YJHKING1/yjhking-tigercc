package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@TableName("t_course_detail")
public class CourseDetail extends Model<CourseDetail> {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 详情
     */
    private String description;
    /**
     * 简介
     */
    private String intro;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "CourseDetail{" +
        ", id=" + id +
        ", description=" + description +
        ", intro=" + intro +
        "}";
    }
}
