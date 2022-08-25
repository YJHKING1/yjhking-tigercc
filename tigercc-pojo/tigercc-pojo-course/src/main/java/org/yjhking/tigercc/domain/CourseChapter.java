package org.yjhking.tigercc.domain;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程章节 ， 一个课程，多个章节，一个章节，多个视频
 * </p>
 *
 * @author YJH
 * @since 2022-08-16
 */
@TableName("t_course_chapter")
@Data
public class CourseChapter extends Model<CourseChapter> {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 章节名称
     */
    private String name;
    /**
     * 第几章
     */
    private Integer number;
    /**
     * 课程ID
     */
    @TableField("course_id")
    private Long courseId;
    /**
     * 课程名
     */
    @TableField("course_name")
    private String courseName;
    @TableField(exist = false)
    private List<MediaFile> mediaFileList = new ArrayList<>();
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
